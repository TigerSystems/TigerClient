package de.MarkusTieger.tigerclient.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.commons.lang3.function.TriFunction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.FourConsumer;
import de.MarkusTieger.common.FourFunction;
import de.MarkusTieger.common.ILoader;
import de.MarkusTieger.tigerclient.CanceledScreen;
import de.MarkusTieger.tigerclient.loader.AbstractConfig.LoaderConfig;
import de.MarkusTieger.tigerclient.loader.plmgr.PluginManagerInjector;
import de.MarkusTieger.tigerclient.recovery.RecoveryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;

public class ClientLoader implements ILoader {

	public static final String VERSION = "1.0.0";
	
	static {
		
		final String first = "--::tc_common=";
		final String last = "=tc_common::--";
		System.out.println(first + VERSION + last);
		
	}
	
    private File directory = null;
    private Runnable start = () -> {},
                    postStart = () -> {},
                    started = () -> {};
    private BiFunction<Screen, Boolean, Screen> screen = (sc, canceled) -> {
    	if(canceled) return CanceledScreen.INSTANCE;
    	return sc;
    };

    private TriFunction<Integer, Integer, Boolean, Boolean> mouse = (button, action, canceled) -> canceled;
    private BiConsumer<Integer, Integer> key = (key, action) -> {};
    private Consumer<PoseStack> render = (event) -> {};

    private FourFunction<Screen, List<GuiEventListener>, Consumer<GuiEventListener>, Consumer<GuiEventListener>, Boolean> screenPre = (sc, list, add, remove) -> false;
    private FourConsumer<Screen, List<GuiEventListener>, Consumer<GuiEventListener>, Consumer<GuiEventListener>> screenPost = (sc, list, add, remove) -> {};

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private VersionInformation info = null;
    private BiConsumer<String, Float> progress;

    private ClientClassLoader loader = null;
    
    @SuppressWarnings("resource")
	@Override
    public void initialize(String modid, BiConsumer<String, Float> consumer, ClientType type) {

        progress = consumer
        		// .andThen((str, f) -> System.out.println("Current: " + str))
        		;

        directory = new File(Minecraft.getInstance().gameDirectory, "TigerClient");

        if(!directory.exists()) directory.mkdirs();

        progress.accept("Checking for Updates...", 0F);

        boolean download = true;

        File loadercfg = new File(directory, "loader.json");
        if(loadercfg.exists()){

            loadConfig(loadercfg);

            download = checkVersion(loadercfg);

        }

        if(new File(directory, "debug").exists()) download = true;

        if(download) {
            try {
                loadConnection("config/loader", "json");
                saveConfig(loadercfg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Configuration found: " + cfg);

        File extensions = new File(directory, "extensions.json");
        Predicate<Map.Entry<String, LoaderConfig>> extension_filter = loadExtensionFilter(extensions);
        
        
        progress.accept("Building File-List...", 0.03125F);

        try {

            ArrayList<AbstractConfig.DownloadableFile> files_ = new ArrayList<>();

            if(cfg == null) throw new NullPointerException("Config = null!");

            boolean obf = true;
            try {

            	String get_instance = "yttInstance";

            	char[] chars = get_instance.toCharArray();

            	chars[0] = 'g';
            	chars[1] = 'e';

            	get_instance = new String(chars);

                Minecraft.class.getDeclaredMethod(get_instance);

                if(!Minecraft.class.getSimpleName().toLowerCase().startsWith("m")) throw new IOException("It's Obfuscated :)");

                obf = false;
            } catch(Exception e){}

            files_.addAll(cfg.all);
            files_.addAll(obf ? cfg.obf : cfg.def);

            if(type == ClientType.FORGE){
                files_.addAll(cfg.forge.all);
                files_.addAll(obf ? cfg.forge.obf : cfg.forge.def);
            } else if(type == ClientType.VANILLA) {
                files_.addAll(cfg.vanilla.all);
                files_.addAll(obf ? cfg.vanilla.obf : cfg.vanilla.def);
            }
            
            for(LoaderConfig enabled_extensions : cfg.extensions.entrySet().stream().filter(extension_filter).map(Map.Entry::getValue).toList()) {
            	files_.addAll(enabled_extensions.all);
                files_.addAll(obf ? enabled_extensions.obf : enabled_extensions.def);

                if(type == ClientType.FORGE){
                    files_.addAll(enabled_extensions.forge.all);
                    files_.addAll(obf ? enabled_extensions.forge.obf : enabled_extensions.forge.def);
                } else if(type == ClientType.VANILLA) {
                    files_.addAll(enabled_extensions.vanilla.all);
                    files_.addAll(obf ? enabled_extensions.vanilla.obf : enabled_extensions.vanilla.def);
                }
            }

            progress.accept("Checking Files...", 0.03125F * 2F);

            float pos = 0;
            for(AbstractConfig.DownloadableFile f : files_) {
            	pos++;
                float p = ((pos * 0.0625F) / (files_.size()));
                progress.accept("Checking File \"" + f.name + "." + f.type + "\" ...", (0.0625F * 2F) + p);

                File file = new File(directory, f.name + "." + f.type);
                if(!file.exists()) download = true;
            }



            pos = 0F;

            if(download) {

            	progress.accept("Downloading Files...", (0.03125F * 3F));

                for(AbstractConfig.DownloadableFile f : files_){

                    // - client / + f.name

                    File file = new File(directory,f.name + "." + f.type);

                    file.getParentFile().mkdirs();

                    System.out.println(file.getAbsolutePath());

                    if(!file.exists()) file.createNewFile();

                    float max = 0.03125F * 12F;
                    float one = (1F * max)  / (files_.size());
                    float already = ((pos) * one);
                    pos++;

                    progress.accept("Downloading File \"" + f.name + "." + f.type + "\" ...", -1F);

                    download(f.name, f.type, file, (0.03125F * 3F) + already, one);

                    progress.accept(null, (0.03125F * 3F) + ((pos) * one));

                }
            } else {

            	progress.accept(null, (0.03125F * 3F));

            }
            progress.accept("Building URL-List...", 0.03125F * 12F);

            pos = 0F;

            ArrayList<URL> sources = new ArrayList<>();

            for(AbstractConfig.DownloadableFile f : files_){

                // - client / + f.name

            	pos++;
                float p = ((pos * 0.03125F) / (files_.size()));
                progress.accept("Building URL \"" + f.name + "." + f.type + "\" ...", (0.03125F * 12F) + p);

                File file = new File(directory, f.name + "." + f.type);

                sources.add(file.toURI().toURL());



            }

            InputStream in = ClientLoader.class.getResourceAsStream("/cert");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while((len = in.read(buffer)) > 0){
                out.write(buffer, 0, len);
            }
            in.close();

            ClientVerification verify = new ClientVerification(new String(out.toByteArray(), StandardCharsets.UTF_8));

            progress.accept("Initializing Client...", (0.03125F * 14F));

            loader = new ClientClassLoader(verify, sources.toArray(new URL[0]));

            Class<?> comp = Class.forName(type.getExecutor(), true, loader);
            Constructor<?> compConstruct = comp.getDeclaredConstructor(String.class, Runnable.class);

            Object compatiblity = compConstruct.newInstance(modid, (Runnable) RecoveryManager::enableRecovery);

            Class<?> main = Class.forName("de.MarkusTieger.common.Client", true, loader);

            Method get_name = main.getDeclaredMethod("getName");
            Method get_vertype = main.getDeclaredMethod("getVersionType");
            Method get_version = main.getDeclaredMethod("getVersion");
            Method get_clVersion = main.getDeclaredMethod("getCleanVersion");
            Method get_build = main.getDeclaredMethod("getBuild");

            // Class<?> main_client = Class.forName("de.MarkusTieger.tigerclient.TigerClient", true, loader);
            // Constructor<?> main_client_construct = main_client.getDeclaredConstructor(Class.forName("de.MarkusTieger.common.compatiblity.ClientCompatiblityExecutor", true, loader));

            Method main_client_construct = main.getDeclaredMethod("newInstance", Class.forName("de.MarkusTieger.common.compatiblity.ClientCompatiblityExecutor", true, loader));

            Field instance = main.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, main_client_construct.invoke(null, compatiblity));

            Method method = main.getDeclaredMethod("getInstance");
            Object obj = method.invoke(null);
            System.out.println("Client-Class: " + obj.getClass().getName());

            Method method1 = main.getDeclaredMethod("start", BiConsumer.class);
            start = () -> {
                try {
                    // Making Manipulated Consumer
                    BiConsumer<String, Float> c = (str, f) -> {
                    	if(f == Float.NaN && str == null) {
                    		PluginManagerInjector.injectPluginManager(loader);
                    		return;
                    	}
                    	if(f != -1F) f = 0.5F + (f / 2F);
                        progress.accept(str, f);
                    };
                    method1.invoke(obj, c);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            };

            Method method2 = main.getDeclaredMethod("postStart");

            postStart = () -> {

                try {
                    method2.invoke(obj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            };

            Class<?> sce = Class.forName("de.MarkusTieger.tigerclient.events.impl.screen.ScreenChangeEvent", true, loader);
            Class<?> em = Class.forName("de.MarkusTieger.common.events.IEventManager", true, loader);

            Constructor<?> scec = sce.getConstructor(Screen.class, boolean.class);
            Method emec = em.getDeclaredMethod("call", Class.forName("de.MarkusTieger.common.events.IEvent", true, loader));
            Method scegs = sce.getDeclaredMethod("getScreen");
            Method sceic = sce.getDeclaredMethod("isCancelled");
            Method gem = main.getDeclaredMethod("getEventManager");

            Class<?> preEv = Class.forName("de.MarkusTieger.tigerclient.events.impl.screen.PreScreenInitEvent", true, loader);
            Class<?> postEv = Class.forName("de.MarkusTieger.tigerclient.events.impl.screen.PostScreenInitEvent", true, loader);

            Constructor<?> preEvConstruct = preEv.getDeclaredConstructor(Screen.class, List.class, Consumer.class, Consumer.class);
            Constructor<?> postEvConstruct = postEv.getDeclaredConstructor(Screen.class, List.class, Consumer.class, Consumer.class);

            Class<?> imr = Class.forName("de.MarkusTieger.common.modules.IModuleRegistry", true, loader);

            Method gmr = main.getDeclaredMethod("getModuleRegistry");

            Method oio = imr.getDeclaredMethod("onIngameOverlay", PoseStack.class);
            Method ok = imr.getDeclaredMethod("onKey", int.class, int.class);
            Method om = imr.getDeclaredMethod("onMouse", int.class, int.class, boolean.class);

            started = () -> {

              try {
                  Object emobj = gem.invoke(obj);

                  screen = (sc, canceled) -> {
                      try {
                          Object event = scec.newInstance(sc, canceled);
                          emec.invoke(emobj, event);

                          Boolean b = (Boolean) sceic.invoke(event);

                          if(b) {
                        	  return CanceledScreen.INSTANCE;
                          }


                          return (Screen) scegs.invoke(event);
                      } catch (InstantiationException ex) {
                          ex.printStackTrace();
                      } catch (IllegalAccessException ex) {
                          ex.printStackTrace();
                      } catch (InvocationTargetException ex) {
                          ex.printStackTrace();
                      }

                      if(canceled) return CanceledScreen.INSTANCE;

                      return sc;
                  };

                  Object mr = gmr.invoke(obj);

                  render = (stack) -> {
                      try {
                          oio.invoke(mr, stack);
                      } catch (IllegalAccessException ex) {
                          ex.printStackTrace();
                      } catch (InvocationTargetException ex) {
                          ex.printStackTrace();
                      }
                  };

                  mouse = (button, action, canceled) -> {
                      try {
                          Object result = om.invoke(mr, button, action, canceled);
                          return (Boolean) result;
                      } catch (IllegalAccessException ex) {
                          ex.printStackTrace();
                      } catch (InvocationTargetException ex) {
                          ex.printStackTrace();
                      }
                      return canceled;
                  };

                  key = (k, a) -> {
                      try {
                          ok.invoke(mr, k, a);
                      } catch (IllegalAccessException ex) {
                          ex.printStackTrace();
                      } catch (InvocationTargetException ex) {
                          ex.printStackTrace();
                      }
                  };

                  screenPre = (sc, children, add, remove) -> {
                      try {

                          Object pe = preEvConstruct.newInstance(sc, children, add, remove);
                          emec.invoke(emobj, pe);
                      } catch (InstantiationException ex) {
                          ex.printStackTrace();
                      } catch (IllegalAccessException ex) {
                          ex.printStackTrace();
                      } catch (InvocationTargetException ex) {
                          ex.printStackTrace();
                      }
                      return false;
                  };

                  screenPost = (sc, children, add, remove) -> {
                      try {
                          Object pe = postEvConstruct.newInstance(sc, children, add, remove);
                          emec.invoke(emobj, pe);
                      } catch (InstantiationException ex) {
                          ex.printStackTrace();
                      } catch (IllegalAccessException ex) {
                          ex.printStackTrace();
                      } catch (InvocationTargetException ex) {
                          ex.printStackTrace();
                      }
                  };
              } catch(Exception e){
                  e.printStackTrace();
              }

            };

            progress.accept("Building Version-Information...", (0.03125F * 15F));

            info = new VersionInformation(
                    get_name.invoke(obj) + "",
                    get_vertype.invoke(obj) + "",
                    get_version.invoke(obj) + "",
                    get_clVersion.invoke(obj) + "",
                    get_build.invoke(obj) + ""
            );

            progress.accept("Finished Client Initialisation!", 0.5F);

        } catch(Exception ex){
            ex.printStackTrace();
            info = new VersionInformation(
                    "Unknown",
                    "Unknown",
                    "0.0.0-0000",
                    "0.0.0",
                    "0000"
            );
            try {
                Files.delete(loadercfg.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    

	private Predicate<Entry<String, LoaderConfig>> loadExtensionFilter(File extensions) {
    	Predicate<Map.Entry<String, LoaderConfig>> extension_filter = (e) -> false;
    	
    	if(!extensions.exists()) return extension_filter;
    	if(extensions.length() > 8192) return extension_filter;
    	
    	try (FileInputStream in = new FileInputStream(extensions)) {
    		byte[] data = in.readAllBytes();
    		
    		JsonArray array = GSON.fromJson(new String(data, StandardCharsets.UTF_8), JsonArray.class);
    		
    		List<JsonElement> ext0 = new ArrayList<>();
    		array.forEach(ext0::add);
    		
    		List<String> ext = ext0.stream().filter(JsonElement::isJsonPrimitive).map((e) -> (JsonPrimitive)e).filter(JsonPrimitive::isString).map(JsonPrimitive::getAsString).toList();
    		
    		extension_filter = (e) -> ext.stream().anyMatch(e.getKey()::equalsIgnoreCase);
    		
    	} catch (Throwable ex) {
    		ex.printStackTrace();
    	}
    	
		return extension_filter;
	}

	private void saveConfig(File loadercfg) {
        try {
            if(!loadercfg.exists()) loadercfg.createNewFile();
            FileOutputStream out = new FileOutputStream(loadercfg);
            out.write(GSON.toJson(cfg).getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void download(String name, String type, File target, float current, float max) throws IOException {
        URLConnection con = getConnection(name, type);

        if(!target.exists()) target.createNewFile();

        InputStream in = con.getInputStream();
        FileOutputStream out = new FileOutputStream(target);

        long length = con.getContentLength();
        long readed = 0L;
        int len;
        byte[] buffer = new byte[1024];
        while((len = in.read(buffer)) > 0){
            out.write(buffer, 0, len);
            out.flush();
            readed += len;
            progress.accept(null, current + ((((readed) * max) / (length))));
        }
        out.close();
        in.close();
    }

    @SuppressWarnings("deprecation")
	private URLConnection getConnection(String name, String type) {
        if(new File(directory, "debug").exists()) {
            try {
                URLConnection con = new File("/media/markustieger/Samsung-2TB/Eclipse/Projects/internal/new/Java/16.05.22/TigerClient_Target/data", name.toLowerCase() + "." + type).toURL().openConnection();
                return con;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            try {
                HttpURLConnection con = (HttpURLConnection) new URL("https://tigersystems.cf/tigerclient/files/" + name.toLowerCase() + "." + type).openConnection();
                con.setRequestProperty("User-Agent", "TigerClient-v2");
                return con;
            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        /*try {
            URLConnection con = new File("U:\\IntelliJ\\Projects\\internal\\10.10.21\\TigerClient v2 - Compiler\\data", name.toLowerCase() + "." + type).toURL().openConnection();
            return con;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;*/
    }

    private AbstractConfig.RootLoaderConfig cfg = null;

    private void loadConfig(File loadercfg) {
        try {
            FileInputStream in = new FileInputStream(loadercfg);
            loadConfig(in);
            in.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void loadConnection(String name, String type) throws IOException {
        URLConnection con = getConnection(name, type);
        InputStream in = con.getInputStream();
        loadConfig(in);
        in.close();
    }

    private void loadConfig(InputStream in){
        try {
            cfg = GSON.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), AbstractConfig.RootLoaderConfig.class);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkVersion(File loadercfg) {
        try {
            String ver = getVersion();
            if((ver == null) || (cfg == null) || (cfg.version == null)) return true;
            if(cfg.version.equalsIgnoreCase(ver)) return false;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    private String version = null;

    private String getVersion() throws IOException {
        return version == null ? (version = getVersion0()) : version;
    }

    private String getVersion0() throws IOException {
        URLConnection con = getConnection("config/client", "json");
        InputStream in = con.getInputStream();
        JsonObject obj = GSON.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), JsonObject.class);
        in.close();
        if(obj.has("version")){
            return obj.get("version").getAsString();
        }
        return null;
    }

    @Override
    public void start(){
        start.run();

        started.run();

    }

    @Override
    public void postStart() { postStart.run(); }

    @Override
    public Screen onScreen(Screen screen, boolean canceled){
        return this.screen.apply(screen, canceled);
    }

    @Override
    public void onKey(int key, int action){
        this.key.accept(key, action);
    }

    @Override
    public boolean onMouse(int button, int action, boolean canceled) {
        return mouse.apply(button, action, canceled);
    }

    @Override
    public void onIngameRender(PoseStack stack) {
        render.accept(stack);
    }

    @Override
    public boolean onScreenPre(Screen sc, List<GuiEventListener> children, Consumer<GuiEventListener> add, Consumer<GuiEventListener> remove){
        return screenPre.apply(sc, children, add, remove);
    }

    @Override
    public void onScreenPost(Screen sc, List<GuiEventListener> children, Consumer<GuiEventListener> add, Consumer<GuiEventListener> remove){
        screenPost.accept(sc, children, add, remove);
    }

    @Override
    public VersionInformation getVersionInformation() {
        return info;
    }
}
