package de.MarkusTieger;

import static de.MarkusTieger.obf.ObfuscationConfig.DONT_NOTE;
import static de.MarkusTieger.obf.ObfuscationConfig.DONT_OPTIMIZE;
import static de.MarkusTieger.obf.ObfuscationConfig.DONT_SHRINK;
import static de.MarkusTieger.obf.ObfuscationConfig.DONT_WARN;
import static de.MarkusTieger.obf.ObfuscationConfig.FORCE_PROCESSING;
import static de.MarkusTieger.obf.ObfuscationConfig.IGNORE_WARNINGS;
import static de.MarkusTieger.obf.ObfuscationConfig.KEEP_ALL_ATTRIBUTES;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.MarkusTieger.AbstractConfig.DownloadableFile;
import de.MarkusTieger.AbstractConfig.LoaderConfig;
import de.MarkusTieger.obf.ObfuscationConfig;
import de.MarkusTieger.obf.Obfuscator;

public class Compiler {

    private static final String CERT = "tigerclient";
    @SuppressWarnings("unused")
	private static final String JAVA_PATH = "java";
    private static String PASS = "";
	private static File forgeJar = null, vanillaJar = null;
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final long MAX_FILE_SIZE = ((1024L) * 1024L) * 10L;
    
    public static void main(String[] args) throws Throwable {
    	
        File client = new File("../../client/src/");
        File forge_executor = new File("../../compatiblity/forge/src/");
        File vanilla_executor = new File("../../compatiblity/vanilla/src/");

        File lua_extension = new File("../../extensions/lua/src");
        
        String blank = multi('\n', 20);

        if(args.length > 0) {
        	PASS = args[0];
        } else {
        	Scanner x = new Scanner(System.in);

            System.out.print("PASS: ");

            PASS = x.nextLine();

            x.close();
        }
        
        System.out.println(blank);

        IVersionFetchable fetchable = 
     // Compiler::fetchVersionUsingRunClient; Outdated :(
        
        Compiler::fetchVersionUsingPrinter;
        
        if(client.exists() && forge_executor.exists() && vanilla_executor.exists() && lua_extension.exists()) {
            handleClient(fetchable, client, forge_executor, vanilla_executor);
        } else {
            System.err.println("Not all Sources are present!");
        }

    }

    private static String multi(char c, int count) {
        String str = "";
        for(int i = 0; i < count; i++){
            str += c;
        }
        return str;
    }

    private static void handleClient(IVersionFetchable fetchable, File... searchDirectories) throws IOException, InterruptedException, NoSuchAlgorithmException {

    	List<File> signs = new ArrayList<>();
    	AbstractConfig.LoaderConfig.RootLoaderConfig loader_config = new AbstractConfig.LoaderConfig.RootLoaderConfig();
    	
    	System.out.println("Getting Minecraft and Forge version...");
    	
    	Pair<String, String> mc_version = fetchMCVersion();
    	
    	
        // File root = new File();

    	File output = new File("../../data/compiled");
        if(!output.exists()) output.mkdirs();
    	
        File keystore = new File(output, "../keystore/cert");
        
        

        File client_dir = new File(output, "client");
        if(!client_dir.exists()) client_dir.mkdir();
        
        System.out.println("Searching Files...");

        HashMap<String, File> files = new HashMap<String, File>();

        for(File f : searchDirectories) {
        	
        	File main = new File(f, "main");
        	if(main.exists() && main.isDirectory()) {
        		
        		for(File fl : main.listFiles()) {
        			if(!fl.isDirectory()) continue;
        			search(fl, "", files);
        		}
        		
        	} else search(f, "", files);
        	
        }

        System.out.println("Extracting Files...");

        File lang = new File(client_dir, "lang.zip");
        if(!lang.exists()) lang.createNewFile();
        FileOutputStream lout = new FileOutputStream(lang);
        ZipOutputStream lzos = new ZipOutputStream(lout);
        mkdir(lzos, "assets/");
        mkdir(lzos, "assets/tigerclient/");
        loader_config.all.add(new DownloadableFile(output, lang));

        /*File root = new File(output, "root.zip");
        if(!root.exists()) root.createNewFile();
        FileOutputStream rout = new FileOutputStream(root);
        ZipOutputStream rzos = new ZipOutputStream(rout);*/

        File cosmetics = new File("temp_cosmetics.zip");
        if(!cosmetics.exists()) cosmetics.createNewFile();
        FileOutputStream cout = new FileOutputStream(cosmetics);
        ZipOutputStream czos = new ZipOutputStream(cout);
        mkdir(czos, "assets/");
        mkdir(czos, "assets/tigerclient/");

        File textures = new File(client_dir, "textures.zip");
        if(!textures.exists()) textures.createNewFile();
        FileOutputStream tout = new FileOutputStream(textures);
        ZipOutputStream tzos = new ZipOutputStream(tout);
        mkdir(tzos, "assets/");
        mkdir(tzos, "assets/tigerclient/");
        loader_config.all.add(new DownloadableFile(output, textures));
        
        File fonts = new File(client_dir, "fonts.zip");
        if(!fonts.exists()) textures.createNewFile();
        FileOutputStream fout = new FileOutputStream(fonts);
        ZipOutputStream fzos = new ZipOutputStream(fout);
        mkdir(fzos, "assets/");
        mkdir(fzos, "assets/tigerclient/");
        loader_config.all.add(new DownloadableFile(output, textures));

        /*File common = new File("source_common.jar");
        if(!common.exists()) common.createNewFile();
        FileOutputStream coout = new FileOutputStream(common);
        ZipOutputStream cozos = new ZipOutputStream(coout);
        mkdir(cozos, "de/");
        mkdir(cozos, "de/MarkusTieger/");

        File tac = new File("source_tac.jar");
        if(!tac.exists()) tac.createNewFile();
        FileOutputStream tacout = new FileOutputStream(tac);
        ZipOutputStream taczos = new ZipOutputStream(tacout);
        mkdir(taczos, "de/");
        mkdir(taczos, "de/MarkusTieger/");*/

        File client = new File("source_client.jar");
        if(!client.exists()) client.createNewFile();
        FileOutputStream clout = new FileOutputStream(client);
        ZipOutputStream clzos = new ZipOutputStream(clout);
        mkdir(clzos, "de/");

        /*File annotations = new File("source_annotations.jar");
        if(!annotations.exists()) annotations.createNewFile();
        FileOutputStream anout = new FileOutputStream(annotations);
        ZipOutputStream anzos = new ZipOutputStream(anout);
        mkdir(anzos, "de/");
        mkdir(anzos, "de/MarkusTieger/");*/

        File forge_executor = new File("source_forge_executor.jar");
        if(!forge_executor.exists()) forge_executor.createNewFile();
        FileOutputStream feout = new FileOutputStream(forge_executor);
        ZipOutputStream fezos = new ZipOutputStream(feout);
        mkdir(fezos, "de/");
        mkdir(fezos, "de/MarkusTieger/");
        mkdir(fezos, "de/MarkusTieger/tigerclient/");
        
        File vanilla_executor = new File("source_vanilla_executor.jar");
        if(!vanilla_executor.exists()) vanilla_executor.createNewFile();
        FileOutputStream veout = new FileOutputStream(vanilla_executor);
        ZipOutputStream vezos = new ZipOutputStream(veout);
        mkdir(vezos, "de/");
        mkdir(vezos, "de/MarkusTieger/");
        mkdir(vezos, "de/MarkusTieger/tigerclient/");
        
        File lua_extension = new File("source_lua_extension.jar");
        if(!lua_extension.exists()) lua_extension.createNewFile();
        FileOutputStream leout = new FileOutputStream(lua_extension);
        ZipOutputStream lezos = new ZipOutputStream(leout);
        mkdir(lezos, "de/");
        mkdir(lezos, "de/MarkusTieger/");
        mkdir(lezos, "de/MarkusTieger/tigerclient/");
        mkdir(lezos, "de/MarkusTieger/tigerclient/plugins/");
        mkdir(lezos, "de/MarkusTieger/tigerclient/events/");
        mkdir(lezos, "de/MarkusTieger/common/");

        for(Map.Entry<String, File> e : files.entrySet()){
            String name = e.getKey().toLowerCase();
            if(name.startsWith("/")){
                name = name.substring(1);
            }

            if(name.startsWith("assets/tigerclient/lang")){
                copy(e, lzos);
            } else if(name.startsWith("assets/tigerclient/textures/cosmetics")){
                copy(e, czos);
            } else if(name.startsWith("assets/tigerclient/textures")) {
                copy(e, tzos);
            } else if(name.startsWith("assets/tigerclient/fonts")) {
                copy(e, fzos);
            } else if(name.startsWith("de/markustieger/tigerclient/forge")) {
                copy(e, fezos);
            } else if(name.startsWith("de/markustieger/tigerclient/vanilla")) {
                copy(e, vezos);
                
                // Lua Extension
            } else if(name.startsWith("de/markustieger/common/lua")) {
            	copy(e, lezos);
            } else if(name.startsWith("de/markustieger/tigerclient/lua")) {
            	copy(e, lezos);
            } else if(name.startsWith("de/markustieger/tigerclient/plugins/lua")) {
            	copy(e, lezos);
            } else if(name.startsWith("de/markustieger/tigerclient/events/lua")) {
            	copy(e, lezos);
            	
            /*} else if(name.startsWith("de/markustieger/common")) {
                copy(e, cozos);
            } else if(name.startsWith("de/markustieger/annotations")){
                copy(e, anzos);
            } else if(name.startsWith("de/markustieger/tac")){
                copy(e, taczos);*/
            } else if(name.startsWith("de/markustieger")){
                copy(e, clzos);
            } else if(name.startsWith("javax/annotation")){
                copy(e, clzos);
            /*} else if(!name.contains("/")) {
                copy(e, rzos);*/
            } else {
                System.out.println("Skipped: " + name);
            }
        }

        /*rzos.finish();
        rzos.flush();
        rzos.close();

        anzos.finish();
        anzos.flush();
        anzos.close();*/

        fezos.finish();
        fezos.flush();
        fezos.close();
        
        vezos.finish();
        vezos.flush();
        vezos.close();
        
        lezos.finish();
        lezos.flush();
        lezos.close();

        lzos.finish();
        lzos.flush();
        lzos.close();

        czos.finish();
        czos.flush();
        czos.close();

        tzos.finish();
        tzos.flush();
        tzos.close();

        /*cozos.finish();
        cozos.flush();
        cozos.close();

        taczos.finish();
        taczos.flush();
        taczos.close();*/

        clzos.finish();
        clzos.flush();
        clzos.close();

        System.out.println("Files extracted!");
        
        System.out.println("Sectoring Files...");

        File[] sectored_files = sector(new File(client_dir,"cosmetics"), cosmetics, MAX_FILE_SIZE, true);

        for (File f : sectored_files) loader_config.all.add(new DownloadableFile(output, f));
        
        System.out.println("Sectoring Complete!");

        System.out.println("Compiling...");

        File defJar = new File("temp_default.jar");
        File obfJar = new File("temp_obfuscated.jar");

        compile(
        		mc_version,
        		new File(output, "../mdk"),
        		defJar, obfJar,
        		client,
        		forge_executor, vanilla_executor,
        		lua_extension);

        // obfuscate(new File(root, "proguard\\bin\\proguard.bat"), outputDef, defJar, libFiles, clientFiles);

        delete(client);
        delete(forge_executor);
        delete(lua_extension);

        System.out.println("Filtering Files...");
        
        File client_dir_obf = new File(client_dir, "obf");
        if(!client_dir_obf.exists()) client_dir_obf.mkdirs();
        File client_dir_obf_extensions = new File(client_dir_obf, "extensions");
        if(!client_dir_obf_extensions.exists()) client_dir_obf_extensions.mkdirs();
        
        File client_dir_obf_vanilla = new File(client_dir, "obf_vanilla");
        if(!client_dir_obf.exists()) client_dir_obf.mkdirs();
        File client_dir_obf_vanilla_extensions = new File(client_dir_obf_vanilla, "extensions");
        if(!client_dir_obf_vanilla_extensions.exists()) client_dir_obf_vanilla_extensions.mkdirs();
        
        File client_dir_def = new File(client_dir, "def");
        if(!client_dir_def.exists()) client_dir_def.mkdirs();
        File client_dir_def_extensions = new File(client_dir_def, "extensions");
        if(!client_dir_def_extensions.exists()) client_dir_def_extensions.mkdirs();
        
        File def_client = new File(client_dir_def, "client.jar");
        File def_tac = new File(client_dir_def, "tac.jar");
        File def_common = new File(client_dir_def, "common.jar");
        File def_annotations = new File(client_dir_def, "annotations.jar");
        File def_forge_executor = new File(client_dir_def, "forge_executor.jar");
        File def_vanilla_executor = new File(client_dir_def, "vanilla_executor.jar");
        File def_extension_lua = new File(client_dir_def_extensions, "lua.jar");
        
        File obf_client = new File(client_dir_obf, "client.jar");
        File obf_tac = new File(client_dir_obf, "tac.jar");
        File obf_common = new File(client_dir_obf, "common.jar");
        File obf_annotations = new File(client_dir_obf, "annotations.jar");
        File obf_forge_executor = new File(client_dir_obf, "forge_executor.jar");
        File obf_vanilla_executor = new File(client_dir_obf, "vanilla_executor.jar");
        File obf_extension_lua = new File(client_dir_obf_extensions, "lua.jar");
        
        File obf_client_vanilla = new File(client_dir_obf_vanilla, "client.jar");
        File obf_tac_vanilla = new File(client_dir_obf_vanilla, "tac.jar");
        File obf_common_vanilla = new File(client_dir_obf_vanilla, "common.jar");
        File obf_annotations_vanilla = new File(client_dir_obf_vanilla, "annotations.jar");
        // File obf_forge_executor = new File(output, "obf_forge_executor.jar");
        File obf_vanilla_executor_vanilla = new File(client_dir_obf_vanilla, "vanilla_executor.jar");
        File obf_extension_lua_vanilla = new File(client_dir_obf_vanilla_extensions, "lua.jar");
        
        LoaderConfig lua = new LoaderConfig();
        loader_config.extensions.put("lua", lua);
        
        loader_config.def.add(new DownloadableFile(output, def_client));
        loader_config.def.add(new DownloadableFile(output, def_tac));
        loader_config.def.add(new DownloadableFile(output, def_common));
        loader_config.def.add(new DownloadableFile(output, def_annotations));
        lua.def.add(new DownloadableFile(output, def_extension_lua));
        
        loader_config.forge.def.add(new DownloadableFile(output, def_forge_executor));
        loader_config.vanilla.def.add(new DownloadableFile(output, def_vanilla_executor));
        
        loader_config.forge.obf.add(new DownloadableFile(output, obf_client));
        loader_config.forge.obf.add(new DownloadableFile(output, obf_tac));
        loader_config.forge.obf.add(new DownloadableFile(output, obf_common));
        loader_config.forge.obf.add(new DownloadableFile(output, obf_annotations));
        loader_config.forge.obf.add(new DownloadableFile(output, obf_forge_executor));
        lua.forge.obf.add(new DownloadableFile(output, obf_extension_lua));
        // loader_config.vanilla.def.add(new DownloadableFile(output, obf_vanilla_executor));
        
        loader_config.vanilla.obf.add(new DownloadableFile(output, obf_client_vanilla));
        loader_config.vanilla.obf.add(new DownloadableFile(output, obf_tac_vanilla));
        loader_config.vanilla.obf.add(new DownloadableFile(output, obf_common_vanilla));
        loader_config.vanilla.obf.add(new DownloadableFile(output, obf_annotations_vanilla));
        loader_config.vanilla.obf.add(new DownloadableFile(output, obf_vanilla_executor_vanilla));
        lua.vanilla.obf.add(new DownloadableFile(output, obf_extension_lua_vanilla));
        
        signs.add(def_client);
        signs.add(def_tac);
        signs.add(def_common);
        signs.add(def_annotations);
        signs.add(def_forge_executor);
        signs.add(def_vanilla_executor);
        signs.add(def_extension_lua);
        
        signs.add(obf_client);
        signs.add(obf_tac);
        signs.add(obf_common);
        signs.add(obf_annotations);
        signs.add(obf_forge_executor);
        signs.add(obf_vanilla_executor);
        signs.add(obf_extension_lua);
        
        signs.add(obf_client_vanilla);
        signs.add(obf_tac_vanilla);
        signs.add(obf_common_vanilla);
        signs.add(obf_annotations_vanilla);
        signs.add(obf_vanilla_executor_vanilla);
        signs.add(obf_extension_lua_vanilla);
        
        System.out.println("Loading Minecraft-Version...");
        
        File versions = new File("../../data/gradle/caches/versions");
        if(!versions.exists()) versions.mkdirs();
        File verjson = new File(versions, mc_version.getValue() + ".json");
        if(!verjson.exists()) {
        	
        	URL url = new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        	HttpURLConnection con = (HttpURLConnection) url.openConnection();
        	con.setRequestProperty("User-Agent", "TigerSystems");
        	byte[] data = null;
        	try (InputStream in = con.getInputStream()) {
        		data = in.readAllBytes();
        	}
        	JsonObject obj = GSON.fromJson(new String(data, StandardCharsets.UTF_8), JsonObject.class);
        	JsonArray vers = obj.getAsJsonArray("versions");
        	for(int i = 0; i < vers.size(); i++) {
        		JsonObject o = vers.get(i).getAsJsonObject();
        		if(o.get("id").getAsString().equalsIgnoreCase(mc_version.getValue())) {
        			url = new URL(o.get("url").getAsString());
        			con = (HttpURLConnection) url.openConnection();
                	con.setRequestProperty("User-Agent", "TigerSystems");
                	
                	int len;
                	byte[] buffer = new byte[1024];
                	
                	try (InputStream in = con.getInputStream()) {
                		if(!verjson.exists()) verjson.createNewFile();
                		try (FileOutputStream out = new FileOutputStream(verjson)) {
                			while((len = in.read(buffer)) > 0) {
                				out.write(buffer, 0, len);
                			}
                			out.flush();
                		}
                	}
        			break;
        		}
        	}
        	if(!verjson.exists()) throw new RuntimeException("Version \"" + mc_version.getKey() + "\" not found."); 
        }
        
        byte[] data = null;
        
        try (FileInputStream fis = new FileInputStream(verjson)) {
        	data = fis.readAllBytes();
        }
        
        JsonObject verobj = GSON.fromJson(new String(data, StandardCharsets.UTF_8), JsonObject.class);
        
        File apply_mapping = new File("../../data/gradle/caches/apply_mapping_" + mc_version.getValue() + ".txt");
        
        if(!apply_mapping.exists()) {
        	JsonObject client_mappings = verobj.getAsJsonObject("downloads").getAsJsonObject("client_mappings");
        	String url = client_mappings.get("url").getAsString();
        	fromLib(apply_mapping, new URL(url));
        }
        
        File print_mapping = new File(output, "mapping.txt");
        
        filter(defJar, def_client, def_tac, def_common, def_annotations, def_forge_executor, def_vanilla_executor, def_extension_lua);
        filter(obfJar, obf_client, obf_tac, obf_common, obf_annotations, obf_forge_executor, obf_vanilla_executor, obf_extension_lua);
        
        File loader_dir = new File(output, "loaders");
        if(!loader_dir.exists()) loader_dir.mkdir();
        
        Compiler.forgeJar = new File(loader_dir, "forge.jar");
        signs.add(forgeJar);
        
        // compileCommonLoader();
        
        compileForgeLoader(forgeJar);
        
        Compiler.vanillaJar = new File("temp_def_vanilla.jar");
        
        compileVanillaLoader(mc_version, vanillaJar);
        
        File obf_vanilla = new File("temp_obf_vanilla.jar");
        
        File javaHome = new File(System.getProperty("java.home", "."));
        File jmods = new File(javaHome, "jmods");
        
        
        ObfuscationConfig config = new ObfuscationConfig();        
        
        config.setMappingLoad(apply_mapping);
        config.setMappingSave(print_mapping);
        
        
        config.getJars().put(vanillaJar, obf_vanilla);
        config.getJars().put(def_common, obf_common_vanilla);
        config.getJars().put(def_tac, obf_tac_vanilla);
        config.getJars().put(def_client, obf_client_vanilla);
        config.getJars().put(def_annotations, obf_annotations_vanilla);
        config.getJars().put(def_vanilla_executor, obf_vanilla_executor_vanilla);
        config.getJars().put(def_extension_lua, obf_extension_lua_vanilla);
        
        
        if(!jmods.exists()) throw new IllegalStateException("JMods not found for obfuscation.");
        
        
        
        System.out.println("Copying Libraries...");

        File out_libs = new File(output, "libs");
        if(!out_libs.exists()) out_libs.mkdir();
        
        Map<URL, Pair<String, ArrayList<DownloadableFile>>> libraries = new HashMap<>();
        
        libraries.put(new URL("https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.39.3.0/sqlite-jdbc-3.39.3.0.jar"), new Pair<>("sqlite-jdbc_3-39-3-0.jar", loader_config.all));
        libraries.put(new URL("https://repo1.maven.org/maven2/com/github/oshi/oshi-core/5.8.6/oshi-core-5.8.6.jar"), new Pair<>("oshi-core_5-8-6.jar", loader_config.all));
        
        libraries.put(new URL("https://repo1.maven.org/maven2/org/luaj/luaj-jse/3.0.1/luaj-jse-3.0.1.jar"), new Pair<>("luaj-jse_3-0-1.jar", lua.all));
        
        for (Map.Entry<URL, Pair<String, ArrayList<DownloadableFile>>> e : libraries.entrySet()) {
        	File target = new File(out_libs, e.getValue().getKey());
        	
        	fromLib(target, e.getKey());
        	
        	e.getValue().getValue().add(new DownloadableFile(output, target));
        	signs.add(target);
        }
        
        System.out.println("Checking and Downloading Minecraft Libraries...");
        
        File mc_libs = new File("../../data/gradle/caches/mc_libs");
        if(!mc_libs.exists()) mc_libs.mkdirs();
        
        
        JsonArray verlibraries = verobj.getAsJsonArray("libraries");
        for(int i = 0; i < verlibraries.size(); i++) {
        	
        	JsonObject artifact = verlibraries.get(i).getAsJsonObject().getAsJsonObject("downloads").getAsJsonObject("artifact");
        	
        	File target = new File(mc_libs, verlibraries.get(i).getAsJsonObject().get("name").getAsString().replace(':', '.'));
        	if(target.exists()) {
        		System.out.println("Library: " + verlibraries.get(i).getAsJsonObject().get("name") + " present.");
        		continue;
        	}
        	System.out.println("Library: " + verlibraries.get(i).getAsJsonObject().get("name") + " downloading...");
        	
        	fromLib(target, new URL(artifact.get("url").getAsString()));
        	
        }
        
        System.out.println("Obfuscating...");
        
        addRekursivly(mc_libs, (f) -> {
        	if(f.getName().toLowerCase().endsWith(".jar")) config.getLibs().add(f);
        });
        
        addRekursivly(out_libs, (f) -> {
        	if(f.getName().toLowerCase().endsWith(".jar")) config.getLibs().add(f);
        });
        
        Arrays.stream(jmods.listFiles()).forEach(config.getLibs()::add);
        
        
        config.getCustomLines().add(FORCE_PROCESSING);
        config.getCustomLines().add(DONT_SHRINK);
        config.getCustomLines().add(DONT_OPTIMIZE);
        // config.getCustomLines().add(OVERLOAD_AGGRESIVLY);
        config.getCustomLines().add(KEEP_ALL_ATTRIBUTES);
        config.getCustomLines().add(DONT_NOTE);
        config.getCustomLines().add(DONT_WARN);
        config.getCustomLines().add(IGNORE_WARNINGS);
        
        config.getCustomLines().add("-keeppackagenames net.minecraft.client.main");
        config.getCustomLines().add("-keeppackagenames de.MarkusTieger");
        config.getCustomLines().add("-keepclassmembers class * {\r\n"
        		+ "    @net.minecraft.obfuscate.DontObfuscate\r\n"
        		+ "    public <fields>;\r\n"
        		+ "    @net.minecraft.obfuscate.DontObfuscate\r\n"
        		+ "    public <methods>;\r\n"
        		+ "}");
        
        
        config.getCustomLines().add("-keepclasseswithmembers,includedescriptorclasses,includecode class net.minecraft.client.main.**");
        /*config.getCustomLines().add("-keepclasseswithmembers,includedescriptorclasses,includecode class de.MarkusTieger.** {\r\n"
        		+ "    *** **;\r\n"
        		+ "    *** **(...);\r\n"
        		+ "}");*/
        config.getCustomLines().add("-keepclasseswithmembers,includedescriptorclasses,includecode class de.MarkusTieger.**");
        
        // config.getCustomLines().add("-keepclasseswithmembers,includedescriptorclasses,includecode class net.minecraft.client.renderer.entity.layers.RenderLayer");
        
        config.getCustomLines().add("-keep class net.minecraft.obfuscate.DontObfuscate { *; }");
        config.getCustomLines().add("-keep class de.MarkusTieger.annotations.NoObfuscation { *; }");
        // config.getCustomLines().add("-keep class net.minecraftforge.api.distmarker.OnlyIn");
        // config.getCustomLines().add("-keep class net.minecraftforge.api.distmarker.Dist");
        
        config.getCustomLines().add("-keepclasseswithmembers public class * {\r\n"
        		+ "    public static void main(java.lang.String[]);\r\n"
        		+ "}");
        config.getCustomLines().add("-keepclassmembers enum  * {\r\n"
        		+ "    public static **[] values();\r\n"
        		+ "    public static ** valueOf(java.lang.String);\r\n"
        		+ "}");
        
        config.getCustomLines().add("-keep class de.MarkusTieger.VersionPrinter { *; }");
        config.getCustomLines().add("-keep class de.MarkusTieger.tigerclient.loader.AbstractConfig { *; }");
        config.getCustomLines().add("-keep class de.MarkusTieger.tigerclient.loader.AbstractConfig$LoaderConfig { *; }");
        config.getCustomLines().add("-keep class de.MarkusTieger.tigerclient.loader.AbstractConfig$DownloadableFile { *; }");
        config.getCustomLines().add("-keep @de.MarkusTieger.annotations.NoObfuscation class * { *; }");
        
        config.getCustomLines().add("-keep class * extends java.sql.Driver");
        config.getCustomLines().add("-keep class * extends javax.swing.plaf.ComponentUI {\r\n"
        		+ "    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent);\r\n"
        		+ "}");
        config.getCustomLines().add("-keepclasseswithmembers,includedescriptorclasses,allowshrinking class * {\r\n"
        		+ "    native <methods>;\r\n"
        		+ "}");
        /*config.getCustomLines().add("-keepclassmembers,includedescriptorclasses,includecode class * {\r\n"
        		+ "    <methods>;\r\n"
        		+ "}");*/
        // config.getCustomLines().add("-keep public class de.MarkusTieger.**");
        
        Obfuscator obf = new Obfuscator();
        obf.run(config);
        
        System.out.println("Cleaning...");
        
        delete(defJar);
        delete(obfJar);
        
        System.out.println("Sectoring Vanilla-Jar...");
        
        File vanilla = new File(loader_dir, "vanilla");
        if(!vanilla.exists()) vanilla.mkdirs();
        
        sectored_files = sector(vanilla, obf_vanilla, MAX_FILE_SIZE, false);
        Arrays.stream(sectored_files).forEach(signs::add);
        
        signs.add(vanillaJar);
        
        System.out.println("Signing Jars...");

        /* sign(def_common, CERT, PASS, keystore);
        sign(def_tac, CERT, PASS, keystore);
        sign(def_client, CERT, PASS, keystore);
        sign(def_forge_executor, CERT, PASS, keystore);
        sign(def_vanilla_executor, CERT, PASS, keystore);
        sign(def_annotations, CERT, PASS, keystore);

        sign(obf_common_vanilla, CERT, PASS, keystore);
        sign(obf_tac_vanilla, CERT, PASS, keystore);
        sign(obf_client_vanilla, CERT, PASS, keystore);
        sign(obf_vanilla_executor_vanilla, CERT, PASS, keystore);
        sign(obf_annotations_vanilla, CERT, PASS, keystore); */
        
        for(File f : signs) sign(f, CERT, PASS, keystore);

        System.out.println("Generating Launcher-Config (Vanilla) ...");
        
        
        
        
        File launcher_config = verjson;
        if(!launcher_config.exists()) throw new RuntimeException("Launcher config not found.");
        
        FileInputStream fis = new FileInputStream(launcher_config);
        byte[] d = fis.readAllBytes();
        fis.close();
        
        JsonObject obj = GSON.fromJson(new String(d, StandardCharsets.UTF_8), JsonObject.class);
        
        String id = "TigerClient-v2-Vanilla";
        
        obj.remove("id");
        obj.addProperty("id", id);
        
        JsonObject downloadsobj = obj.getAsJsonObject("downloads");
        JsonObject clientobj = downloadsobj.getAsJsonObject("client");
        
        clientobj.remove("sha1");
        clientobj.remove("size");
        clientobj.remove("url");
        
        clientobj.addProperty("size", obf_vanilla.length());
        clientobj.addProperty("sha1", hash(obf_vanilla));
        clientobj.addProperty("url", "https://tigersystems.cf/try/reinstalling/the/TigerClient/LG/MarkusTieger");
        
        downloadsobj.remove("client");
        downloadsobj.add("client", clientobj);
        
        obj.remove("downloads");
        obj.add("downloads", downloadsobj);
        
        File config_dir = new File(output, "config");
        if(!config_dir.exists()) config_dir.mkdirs();
        
        launcher_config = new File(config_dir, "launcher.json");
        
        if(!launcher_config.exists()) launcher_config.createNewFile();
        
        FileOutputStream fos = new FileOutputStream(launcher_config);
        fos.write(GSON.toJson(obj).getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
        
        /*System.out.println("Copying Vanilla-Loader for Testing...");
        
        File version_dir = new File(System.getProperty("user.home"), ".minecraft/versions/" + id);
        if(!version_dir.exists()) version_dir.mkdirs();
        
        copy(obf_vanilla, new File(version_dir, id + ".jar"));
        copy(launcher_config, new File(version_dir, id + ".json"));*/
        
        System.out.println("Generating Loader Configuration...");
        
        File loader_config_file = new File(config_dir, "loader.json");
        if(!loader_config_file.exists()) loader_config_file.createNewFile();
        
        fos = new FileOutputStream(loader_config_file);
        fos.write(GSON.toJson(loader_config).getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
        
        ClientVersion versionPair = fetchable.fetch();
        
        loader_config.version = versionPair.client();
        
        fos = new FileOutputStream(loader_config_file);
        fos.write(GSON.toJson(loader_config).getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
        
        System.out.println("Generating Forge Configuration...");
        
        obj = new JsonObject();
        
        obj.addProperty("homepage", "https://tigersystems.cf");
        
        JsonObject promos = new JsonObject();
        
        promos.addProperty(mc_version.getValue() + "-latest", versionPair.loader_versions().getOrDefault("forge", "0.0.0"));
        promos.addProperty(mc_version.getValue() + "-recommended", versionPair.loader_versions().getOrDefault("forge", "0.0.0"));
        
        obj.add("promos", promos);
        
        File forge_config_file = new File(config_dir, "forge.json");
        if(!forge_config_file.exists()) forge_config_file.createNewFile();
        
        fos = new FileOutputStream(forge_config_file);
        fos.write(GSON.toJson(obj).getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
        
        /*System.out.println("Copying Forge-Loader for Testing...");
        
        copy(forgeJar, new File(System.getProperty("user.home"), ".minecraft/mods/tigerclient.jar"));*/
        
        System.out.println("Writing Recommendations...");
        
        obj = new JsonObject();
        
        JsonArray recommnendations = new JsonArray();
        
        // Not CurseForge mods
        recommnendations.add("optifine://" + mc_version.getValue() + "/latest"); // OptiFine
        
        // JEI (CurseForge) Mods
        recommnendations.add("curseforge://238222/" + mc_version.getValue() + "/latest"); // Just enough items
        recommnendations.add("curseforge://240630/" + mc_version.getValue() + "/latest"); // Just enough resources
        
        // CurseForge Mods
        recommnendations.add("curseforge://380401/" + getMajorVersion(mc_version.getValue()) + "/latest"); // WorldEdit-CUI Forge Edition 3
        recommnendations.add("curseforge://416089/" + mc_version.getValue() + "/forge-latest"); // Simple Voice Chat
        recommnendations.add("curseforge://248787/" + mc_version.getValue() + "/latest"); // Apple Skin
        recommnendations.add("curseforge://250398/" + mc_version.getValue() + "/latest"); // Controlling
        recommnendations.add("curseforge://250419/" + mc_version.getValue() + "/latest"); // Enchantment Descriptions
        recommnendations.add("curseforge://280294/" + mc_version.getValue() + "/forge-latest"); // FPS Reducer
        recommnendations.add("curseforge://325492/" + mc_version.getValue() + "/forge-latest"); // Light Overlay
        
        // CurseForge Dependencies
        recommnendations.add("curseforge://228525/" + mc_version.getValue() + "/latest"); // Bookshelf
        recommnendations.add("curseforge://419699/" + mc_version.getValue() + "/forge-latest"); // Architectury
        recommnendations.add("curseforge://348521/" + mc_version.getValue() + "/forge-latest"); // Cloth-Config
        
        obj.add("recommendations", recommnendations);
        
        File recommend_config_file = new File(config_dir, "recommends.json");
        if(!recommend_config_file.exists()) recommend_config_file.createNewFile();
        
        fos = new FileOutputStream(recommend_config_file);
        fos.write(GSON.toJson(obj).getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
        
        System.out.println("Compiling Installer...");
        
        compileInstaller(output);
        
        System.out.println("Creating Hashes...");
        
        hashRecursivly(output);
        
        System.out.println("Finish!\n");
        
        
        System.out.println("TigerClient: " + versionPair.client());
        
        for(Map.Entry<String, String> e : versionPair.loader_versions().entrySet()) {
        	
        	String k = e.getKey();
        	char[] chars = k.toCharArray();
        	chars[0] = (chars[0] + "").toUpperCase().toCharArray()[0];
        	k = new String(chars);
        	
        	System.out.println("TigerClient-Loader-" + k + ": " + e.getValue());
        	
        }
        System.out.println("TigerClient-Loader-Common: " + versionPair.loader_common());
        
        System.out.println("Forge: " + mc_version.getKey());
        System.out.println("Minecraft: " + mc_version.getValue());
        System.out.println("Java: " + System.getProperty("java.version", "Unknown"));
    }
    
    
    
    private static void hashRecursivly(File output) throws IOException, NoSuchAlgorithmException {
    	MessageDigest digest = MessageDigest.getInstance("SHA-256");
        
    	
		for(File f : output.listFiles()) {
			if(f.isDirectory()) hashRecursivly(f);
			if(f.isFile()) {
				File sha = new File(output, f.getName() + ".sha256");
				
				try (FileInputStream fis = new FileInputStream(f)) {
					byte[] hash = digest.digest(fis.readAllBytes());
					
					if(!sha.exists()) sha.createNewFile();
					try (FileOutputStream fos = new FileOutputStream(sha)) {
						fos.write(hash);
						fos.flush();
					}
				}
				
			}
		}
	}

	private static void compileInstaller(File output) throws InterruptedException, IOException {
		File tools = new File(output, "tools");
		if(!tools.exists()) tools.mkdirs();
		
		File installer = new File("../installer");
		if(!installer.exists()) installer.mkdirs();
		
		ProcessBuilder builder = new ProcessBuilder("./gradlew", "-Dgradle.user.home=../../data/gradle", "build", "shadowJar");
		builder.directory(installer);
		builder.start().waitFor();
		
		File target = new File(installer, "build/libs/Installer-all.jar");
		if(!target.exists()) throw new RuntimeException("Installer Shadow Jar does not exists.");
		
		target.renameTo(new File(tools, "Installer.jar"));
	}

	private static Pair<String, String> fetchMCVersion() {
		
    	ProcessBuilder builder = new ProcessBuilder("./gradlew", "-Dgradle.user.home=../../data/gradle", "build");
    	builder.directory(new File("."));
    	
    	String loader_ver = null;
		String client_ver = null;
    	
    	try {
    		Process p = builder.start();
        	
        	BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
    		
    		final String first_loader = "--::forge_loader=";
    		final String last_loader = "=forge_loader::--";
    		
    		final String first_client = "--::mc_version=";
    		final String last_client = "=mc_version::--";
    		
    		
    		
    		String str = null;
    		while((str = reader.readLine()) != null) {
    			
    			loader: {
    				if(!str.contains(first_loader)) break loader;
    				if(!str.contains(last_loader)) break loader;
    			
    				int index1 = str.indexOf(first_loader) + first_loader.length();
    				int index2 = str.indexOf(last_loader);
    			
    				loader_ver = str.substring(index1, index2);
    			}
    			
    			client: {
    				if(!str.contains(first_client)) break client;
    				if(!str.contains(last_client)) break client;
    			
    				int index1 = str.indexOf(first_client) + first_client.length();
    				int index2 = str.indexOf(last_client);
    			
    				client_ver = str.substring(index1, index2);
    			}
    			
    		}
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
		
    	return new Pair<>(loader_ver, client_ver);
	}

	/*private static void compileCommonLoader() {
    	File baseDir = new File("/media/markustieger/Samsung-2TB/Eclipse/Projects/internal/new/Java/16.05.22/TigerClient v2 - Loader Common");
    	
    	ProcessBuilder builder = new ProcessBuilder("./gradlew", "build", "publishToMavenLocal");
    	builder.directory(baseDir);
    	
    	try {
			Process p = builder.start();
			p.waitFor();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}*/

	private static void fromLib(File target, URL key) throws IOException {
		int len;
		byte[] buffer = new byte[1024];
		
		HttpURLConnection con = (HttpURLConnection) key.openConnection();
		con.setRequestProperty("User-Agent", "TigerSystems");
		try (InputStream in = con.getInputStream()) {
			if(!target.exists()) target.createNewFile();
			try (FileOutputStream out = new FileOutputStream(target)) {
				while((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				out.flush();
			}
		}
	}

	private static String getMajorVersion(String value) {
		String[] args = value.split("\\.");
		if(args.length == 2) return value;
		return args[0] + '.' + args[1];
	}

	private static ClientVersion fetchVersionUsingPrinter() throws IOException {
		
		final String printer_class = "de.MarkusTieger.VersionPrinter";
		
		ClientVersion forge = null;
		ClientVersion vanilla = null;
		
		final PrintStream out = System.out;
		ClientVersionPrinter sysout = new ClientVersionPrinter(new ByteArrayOutputStream());
		
		System.setOut(sysout);
		
		try (URLClassLoader loader = new URLClassLoader(new URL[] {forgeJar.toURI().toURL()})) {
			
			Class<?> clazz = Class.forName(printer_class, true, loader);
			
			Method m = clazz.getDeclaredMethod("recursivly");
			m.invoke(null);
			
			forge = sysout.toVersion("forge");
		} catch (Throwable e) {
			e.printStackTrace(System.err);
		}
		sysout = new ClientVersionPrinter(new ByteArrayOutputStream());
		
		System.setOut(sysout);
		
		try (URLClassLoader loader = new URLClassLoader(new URL[] {vanillaJar.toURI().toURL()})) {
			
			Class<?> clazz = Class.forName(printer_class, true, loader);
			
			Method m = clazz.getDeclaredMethod("recursivly");
			m.invoke(null);
			
			vanilla = sysout.toVersion("vanilla");
		} catch (Throwable e) {
			e.printStackTrace(System.err);
		}
		System.setOut(out);
		
		System.out.println();
		
		return combine(forge, vanilla);
	}
	
	private static ClientVersion combine(ClientVersion ver1, ClientVersion ver2) {
		String clientVer = null;
		String commonVer = null;
		Map<String, String> loader_versions = new HashMap<>();
		
		
		if(ver1 != null) {
			clientVer = ver1.client();
			commonVer = ver1.loader_common();
			loader_versions.putAll(ver1.loader_versions());
		}
		
		if(ver2 != null) {
			if(ver1 == null) {
				clientVer = ver2.client();
				commonVer = ver2.loader_common();
				loader_versions.putAll(ver2.loader_versions());
			} else {
				if(!clientVer.equalsIgnoreCase(ver2.client())) throw new RuntimeException("Difference between Forge and Vanilla Client-Version: "+ clientVer + " != " + ver2.client());
				if(!commonVer.equalsIgnoreCase(ver2.loader_common())) throw new RuntimeException("Difference between Forge and Vanilla Common-Version: "+ commonVer + " != " + ver2.loader_common());
				for(Map.Entry<String, String> e : ver2.loader_versions().entrySet()) {
					if(loader_versions.containsKey(e.getKey())) throw new RuntimeException("Value's Duplicated: " + e.getKey());
					loader_versions.put(e.getKey(), e.getValue());
				}
			}
		}
		
		return new ClientVersion(clientVer, loader_versions, commonVer);
	}
	
	private static ClientVersion fetchVersionUsingRunClient() throws InterruptedException, IOException {
    	
    	File baseDir = new File("../../loader/forge");
    	
    	ProcessBuilder builder = new ProcessBuilder("chmod", "+x", "./gradlew");
        builder.directory(baseDir);
        builder.start().waitFor();
    	
    	builder = new ProcessBuilder("./gradlew", "-Dgradle.user.home=../../data/gradle", "runClient");
    	builder.directory(baseDir);
    	
    	String loader_common_ver = null;
    	String loader_forge_ver = null;
		String client_ver = null;
    	
    	try {
    		Process p = builder.start();
    		
    		return ClientVersionPrinter.toVersion("forge", p.getInputStream(), p::destroyForcibly);
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    	Map<String, String> loaders = new HashMap<>();
		loaders.put("forge", loader_forge_ver);
		
		return new ClientVersion(client_ver, loaders, loader_common_ver);
	}

	private static String hash(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		byte[] data = in.readAllBytes();
		in.close();
		
		try {
			data = MessageDigest.getInstance("SHA-1").digest(data);
		} catch (NoSuchAlgorithmException e) {
			throw new IOException(e);
		}
		
		
		
		return bytesToHex(data);
	}
    
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

	private static void addRekursivly(File file, Consumer<File> c) {
		for(File f : file.listFiles()) {
			if(f.isDirectory()) addRekursivly(f, c);
			if(f.isFile()) c.accept(f);
		}
	}

	/*private static void obfuscate(File batchFile, File output, File inputBinary, File[] libs, File... otherStuff) throws IOException {

        InputStream in = Compiler.class.getResourceAsStream("/exampleconfig");
        String data_ = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        in.close();

        String data = "# Begin Automatic Config values\n";

        data += "-injars \'" + inputBinary.getAbsolutePath() + "\'\n";
        data += "-outjars \'" + output.getAbsolutePath() + "\'\n";

        for(File lib : libs){
            data += "-libraryjars \'" + lib.getAbsolutePath() + "\'\n";
        }

        for(File other : otherStuff){
            data += "-libraryjars \'" + other.getAbsolutePath() + "\'\n";
        }

        data = data + "\n\n\n\n\n\n" + data_;

        File tempConfig = new File("temp_config.cfg");
        if(!tempConfig.exists()) tempConfig.createNewFile();
        FileOutputStream out = new FileOutputStream(tempConfig);
        out.write(data.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();

        ProcessBuilder builder = new ProcessBuilder("cmd", "/c", batchFile.getAbsolutePath(), "@temp_config.cfg");
        builder.redirectOutput(new File("pro_out"));
        builder.redirectError(new File("pro_err"));
        Process p = builder.start();
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // delete(tempConfig);

    }*/

    private static void compileVanillaLoader(Pair<String, String> mc_version, File vanillaJar) throws IOException, InterruptedException {
		
    	File baseDir = new File("../../loader/vanilla");
    	
    	ProcessBuilder builder = new ProcessBuilder("git", "clone", "--branch", mc_version.getValue() + "-official", "https://github.com/Hexeption/MCP-Reborn.git");
    	builder.directory(baseDir).start().waitFor();
    	
    	File reborn = new File(baseDir, "MCP-Reborn");
    	if(!reborn.exists()) throw new RuntimeException("MCP-Reborn directory does not exists.");
    	
    	builder = new ProcessBuilder("rm", "-rf", ".git/");
    	builder.directory(reborn).start().waitFor();
    	
    	System.out.println("Setting up MCP-Reborn...");
    	
    	builder = new ProcessBuilder("./gradlew", "-Dgradle.user.home=../../../data/gradle", "setup");
    	builder.inheritIO().directory(reborn).start().waitFor();
    	
    	copy(new File(baseDir, "build.gradle"), new File(reborn, "build.gradle"));
    	copy(new File(baseDir, "settings.gradle"), new File(reborn, "settings.gradle"));
    	
    	builder = new ProcessBuilder("git", "init");
    	builder.inheritIO().directory(reborn).start().waitFor();
    	
    	System.out.println("Applying Patch...");
    	
    	builder = new ProcessBuilder("rm", "-rf", ".gitignore");
    	builder.directory(reborn).start().waitFor();
    	
    	builder = new ProcessBuilder("git", "apply", "../0002-Loader.patch");
    	builder.inheritIO().directory(reborn).start().waitFor();
    	
    	File src = new File(reborn, "src");
    	if(!src.exists()) src.mkdirs();
    	
    	System.out.println("Copying Common Loader...");
    	
    	copyDirectory(new File("../../loader/common/src/main"), new File(src, "common"));
    	
    	System.out.println("Building...");
    	
    	builder = new ProcessBuilder("./gradlew", "-Dgradle.user.home=../../../data/gradle", "build");
    	builder.inheritIO().directory(reborn).start().waitFor();
    	
    	// copyCommonLoader(baseDir);
    	
    	File f = new File(reborn, "build/libs/");
    	if(!f.exists() || !f.isDirectory()) throw new RuntimeException("Vanilla Client (Deobf) Jar-Directory does not exists or is not a directory.");
    	File[] list = f.listFiles();
    	if(list.length == 0) throw new RuntimeException("Libs directory is empty.");
    	File target = list[0];
    	if(!target.isFile()) throw new RuntimeException("File \"" + target.getName() + "\" is not a File.");
    	
    	target.renameTo(vanillaJar);
    	
    	builder = new ProcessBuilder("rm", "-rf", reborn.getName() + "/");
    	builder.inheritIO().directory(baseDir).start().waitFor();
    	
	}
    
    private static void compileForgeLoader(File vanillaJar) throws IOException, InterruptedException {
		
    	File baseDir = new File("../../loader/forge");
    	
    	ProcessBuilder builder = new ProcessBuilder("chmod", "+x", "./gradlew");
        builder.directory(baseDir);
        builder.start().waitFor();
    	
    	builder = new ProcessBuilder("./gradlew", "-Dgradle.user.home=../../data/gradle", "build");
    	builder.directory(baseDir);
		
		// copyCommonLoader(baseDir);
		
		Process p = null;
    	try {
    		p = builder.start();
    		
    		
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	File f = new File(baseDir, "build/libs/Loader-v2.jar");
    	if(!f.exists()) throw new RuntimeException("Forge Client (Deobf) Jar does not exists.");
    	
    	f.renameTo(vanillaJar);
    	
    	// return new Pair<String, String>(loader_ver, client_ver);
	}

	/*private static void copyCommonLoader(File baseDir) throws IOException {
		
		File common = new File("/media/markustieger/Samsung-2TB/Eclipse/Projects/internal/new/Java/16.05.22/TigerClient v2 - Loader Common");
		
		copyDirectory(new File(common, "src/main/java"), new File(baseDir, "src/common/java"));
		copyDirectory(new File(common, "src/main/resources"), new File(baseDir, "src/common/resources"));
	}*/
    
    
    

	private static void copyDirectory(File from, File to) throws IOException {
		if(!to.exists()) to.mkdirs();
		for(File f : from.listFiles()) {
			
			if(f.isDirectory()) {
				File target = new File(to, f.getName());
				if(!target.exists()) target.mkdirs();
				
				// System.out.println("Directory: " + f.getAbsolutePath() + " TO: " + target.getAbsolutePath() + " Exists: " + target.exists());
				
				copyDirectory(f, target);
			}
			if(f.isFile()) {
				// System.out.println("From: " + f.getAbsolutePath() + " To: " + to.getAbsolutePath() + " / " + f.getName());
				copy(f, new File(to, f.getName()));
			}
			
		}
	}

	@SuppressWarnings("resource")
	private static void copy(File in, File target) throws IOException {
        if(!target.exists()) target.createNewFile();
        FileInputStream input = new FileInputStream(in);
        FileOutputStream output = new FileOutputStream(target);
        int len;
        byte[] buffer = new byte[1024];
        while((len = input.read(buffer)) > 0){
            output.write(buffer, 0, len);
        }
        output.flush();
        output.close();
    }

    private static void sign(File target, String cert, String pass, File keystore) throws IOException, InterruptedException {

        ProcessBuilder builder = new ProcessBuilder("jarsigner", "-keystore", keystore.getAbsolutePath(), target.getAbsolutePath(), cert);
        builder.redirectOutput(new File("sign.txt"));
        Process p = builder.start();

        PrintStream stream = new PrintStream(p.getOutputStream());
        stream.println(pass);
        stream.flush();

        p.waitFor();

    }

    private static void filter(File input, File client, File tac, File common, File annotations, File forge_executor, File vanilla_executor, File extension_lua) throws IOException {

        if(!client.exists()) client.createNewFile();
        if(!tac.exists()) tac.createNewFile();
        if(!common.exists()) common.createNewFile();
        if(!annotations.exists()) annotations.createNewFile();
        if(!forge_executor.exists()) forge_executor.createNewFile();
        if(!vanilla_executor.exists()) vanilla_executor.createNewFile();

        FileInputStream in = new FileInputStream(input);
        ZipInputStream zis = new ZipInputStream(in);

        FileOutputStream coout = new FileOutputStream(common);
        ZipOutputStream cozos = new ZipOutputStream(coout);
        mkdir(cozos, "de/");
        mkdir(cozos, "de/MarkusTieger/");

        FileOutputStream anout = new FileOutputStream(annotations);
        ZipOutputStream anzos = new ZipOutputStream(anout);
        mkdir(anzos, "de/");
        mkdir(anzos, "de/MarkusTieger/");

        FileOutputStream tacout = new FileOutputStream(tac);
        ZipOutputStream taczos = new ZipOutputStream(tacout);
        mkdir(taczos, "de/");
        mkdir(taczos, "de/MarkusTieger/");

        FileOutputStream clout = new FileOutputStream(client);
        ZipOutputStream clzos = new ZipOutputStream(clout);
        mkdir(clzos, "de/");

        FileOutputStream feout = new FileOutputStream(forge_executor);
        ZipOutputStream fezos = new ZipOutputStream(feout);
        mkdir(fezos, "de/");
        mkdir(fezos, "de/MarkusTieger/");
        mkdir(fezos, "de/MarkusTieger/tigerclient/");
        
        FileOutputStream veout = new FileOutputStream(vanilla_executor);
        ZipOutputStream vezos = new ZipOutputStream(veout);
        mkdir(vezos, "de/");
        mkdir(vezos, "de/MarkusTieger/");
        mkdir(vezos, "de/MarkusTieger/tigerclient/");
        
        FileOutputStream leout = new FileOutputStream(extension_lua);
        ZipOutputStream lezos = new ZipOutputStream(leout);
        mkdir(lezos, "de/");
        mkdir(lezos, "de/MarkusTieger/");
        mkdir(lezos, "de/MarkusTieger/tigerclient/");
        mkdir(lezos, "de/MarkusTieger/tigerclient/events/");
        mkdir(lezos, "de/MarkusTieger/tigerclient/plugins/");
        mkdir(lezos, "de/MarkusTieger/common/");

        ZipEntry ze = null;
        while((ze = zis.getNextEntry()) != null){

            String name = ze.getName().toLowerCase();
            if(name.startsWith("/")){
                name = name.substring(1);
            }

            if(name.startsWith("de/markustieger/common/lua")) {
            	copy(ze, zis, lezos);
            } else if(name.startsWith("de/markustieger/tigerclient/lua")) {
                copy(ze, zis, lezos);
            } else if(name.startsWith("de/markustieger/tigerclient/events/lua")) {
                copy(ze, zis, lezos);
            } else if(name.startsWith("de/markustieger/tigerclient/plugins/lua")) {
                copy(ze, zis, lezos);
            } else if(name.startsWith("de/markustieger/common")) {
                copy(ze, zis, cozos);
            } else if(name.startsWith("de/markustieger/annotations")){
                copy(ze, zis, anzos);
            } else if(name.startsWith("javax/annotation")){
                copy(ze, zis, anzos);
            } else if(name.startsWith("de/markustieger/tac")){
                copy(ze, zis, taczos);
            } else if(name.startsWith("de/markustieger/tigerclient/forge")) {
                copy(ze, zis, fezos);
            } else if(name.startsWith("de/markustieger/tigerclient/vanilla")) {
                copy(ze, zis, vezos);
            } else if(name.startsWith("de/markustieger")) {
                copy(ze, zis, clzos);
            } else {
                System.out.println("Skipped: " + name);
            }

            zis.closeEntry();

        }
        zis.close();

        cozos.finish();
        cozos.flush();
        cozos.close();

        anzos.finish();
        anzos.flush();
        anzos.close();

        taczos.finish();
        taczos.flush();
        taczos.close();

        clzos.finish();
        clzos.flush();
        clzos.close();

        fezos.finish();
        fezos.flush();
        fezos.close();
        
        vezos.finish();
        vezos.flush();
        vezos.close();
        
        lezos.finish();
        lezos.flush();
        lezos.close();

    }

    private static long copy(Map.Entry<String, File> e, ZipOutputStream zos) throws IOException {

        zos.putNextEntry(new ZipEntry(e.getKey() + (e.getValue().isDirectory() ? "/" : "")));

        long writed = 0L;

        if(!e.getValue().isDirectory()){
            FileInputStream zis = new FileInputStream(e.getValue());
            int len;
            byte[] buffer = new byte[1024];
            while((len = zis.read(buffer)) > 0){
                writed += len;
                zos.write(buffer, 0, len);
            }
        }

        zos.closeEntry();
        return writed;

    }

    private static void search(File input, String path, HashMap<String, File> files) {
        if(!path.isEmpty()) files.put(path, input);
        for(File f : input.listFiles()){
            if(f.isFile()){
                files.put(path + f.getName(), f);
            }
            if(f.isDirectory()){
                search(f, path + f.getName() + "/", files);
            }
        }
    }

    private static void compile(Pair<String, String> pair, File mdk, File defJar, File obfJar, File... files) throws IOException, InterruptedException {
    	File check_file = new File(mdk, pair.getValue() + "-" + pair.getKey() + ".mdk");
        if(!check_file.exists()) {
        	System.out.println("Cleaning MDK...");
        	deleteChildren(mdk);
        	
        	System.out.println("Downloading MDK...");
        	
        	int len;
        	byte[] buffer = new byte[1024];
        	
        	File tmp_mdk = new File("tmp_mdk.zip");
        	
        	URL url = new URL("https://maven.minecraftforge.net/net/minecraftforge/forge/" + pair.getValue() + "-" + pair.getKey() + "/forge-" + pair.getValue() + "-" + pair.getKey() + "-mdk.zip");
        	HttpURLConnection con = (HttpURLConnection) url.openConnection();
        	con.setRequestProperty("User-Agent", "TigerSystems");
        	try (InputStream in = con.getInputStream()) {
        		if(!tmp_mdk.exists()) tmp_mdk.createNewFile();
        		
        		try (FileOutputStream out = new FileOutputStream(tmp_mdk)) {
        			while((len = in.read(buffer)) > 0) {
        				out.write(buffer, 0, len);
        			}
        			out.flush();
        		}
        	}
        	
        	if(!mdk.exists()) mdk.mkdirs();
        	
        	try (ZipInputStream zis = new ZipInputStream(new FileInputStream(tmp_mdk))) {
        		ZipEntry ze = null;
        		
        		while((ze = zis.getNextEntry()) != null) {
        			File target = new File(mdk, ze.getName());
        			
        			if(ze.isDirectory()) {
        				if(!target.exists()) target.mkdirs();
        			} else {
        				if(!target.exists()) target.createNewFile();
        				try (FileOutputStream fos = new FileOutputStream(target)) {
        					while((len = zis.read(buffer)) > 0) {
        						fos.write(buffer, 0, len);
            				}
        					fos.flush();
        				}
        			}
        			zis.closeEntry();
        		}
        	}
             
             if(!check_file.exists()) check_file.createNewFile();
        }
        
        ProcessBuilder builder = new ProcessBuilder("chmod", "+x", "./gradlew");
        builder.directory(mdk);
        builder.start().waitFor();
        
        System.out.println("Cleaning...");

        File java = new File(mdk, "src/main/java");
        File resources = new File(mdk, "src/main/resources");

        delete(new File(mdk, "build"));
        deleteChildren(java);
        deleteChildren(resources);
        deleteChildren(new File(mdk, "src/test/java"));
        deleteChildren(new File(mdk, "src/test/resources"));
        delete(new File(mdk, ".gradle"));

        System.out.println("Copying Source-Code...");

        for(File f : files){

            FileInputStream in = new FileInputStream(f);
            ZipInputStream zis = new ZipInputStream(in);

            ZipEntry ze = null;
            while((ze = zis.getNextEntry()) != null) {


                if(ze.isDirectory()){
                    new File(java, ze.getName()).mkdirs();
                    new File(resources, ze.getName()).mkdirs();
                } else {

                    File target = ze.getName().toLowerCase().endsWith(".java") ? new File(java, ze.getName()) : new File(resources, ze.getName());
                    if(!target.exists()) {
                        target.getParentFile().mkdirs();
                        target.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(target);

                    int len;
                    byte[] buffer = new byte[1024];
                    while((len = zis.read(buffer)) > 0){
                        out.write(buffer, 0, len);
                        out.flush();
                    }

                    out.close();

                }


                zis.closeEntry();
            }


            zis.close();

        }

        System.out.println("Starting Gradle...");

        builder = new ProcessBuilder("./gradlew", "-Dgradle.user.home=../gradle", "build");
        builder.directory(mdk);
        Process p = builder.start();

        p.waitFor();

        if(!defJar.exists()) {
            defJar.createNewFile();
        }

        FileOutputStream out = new FileOutputStream(defJar);
        ZipOutputStream zos = new ZipOutputStream(out);

        HashMap<String, File> files_ = new HashMap<String, File>();
        search(new File(mdk, "build/classes/java/main"), "", files_);

        for(Map.Entry<String, File> entry : files_.entrySet()){
            copy(entry, zos);
        }

        zos.finish();
        zos.flush();
        zos.close();


        File obf = new File(mdk, "build/libs/modid-1.0.jar");

        if(!obf.exists()) throw new IOException("Obfuscated Jar not found!");

        obf.renameTo(obfJar);
    }

    private static void deleteChildren(File file) throws IOException {
        if(!file.isDirectory()) return;
        for(File f : file.listFiles()){
            delete(f);
        }
    }

    private static File[] sector(File odir, File in, long MAX_SIZE, boolean deleteAfterSector) throws IOException {
        FileInputStream cin = new FileInputStream(in);
        ZipInputStream czis = new ZipInputStream(cin);

        if(odir.exists()) {
            delete(odir);
        }
        odir.mkdirs();

        List<File> files = new ArrayList<>();
        
        File csfile = new File(odir, "sector0.zip");
        if(!csfile.exists()) csfile.createNewFile();

        FileOutputStream csout = new FileOutputStream(csfile);
        ZipOutputStream cszos = new ZipOutputStream(csout);

        long writed = 0L;
        int count = 0;

        ZipEntry ze = null;

        while((ze = czis.getNextEntry()) != null){

            if((writed + ze.getSize()) > MAX_SIZE){
                cszos.finish();
                cszos.flush();
                cszos.close();
                files.add(csfile);
                count++;
                csfile = new File(odir, "sector" + count + ".zip");
                if(!csfile.exists()) csfile.createNewFile();
                csout = new FileOutputStream(csfile);
                cszos = new ZipOutputStream(csout);
                writed = 0L;
            }

            writed += 1024L;
            writed += copy(ze, czis, cszos);
        }

        czis.close();

        cszos.finish();
        cszos.flush();
        cszos.close();
        files.add(csfile);

        if(deleteAfterSector) Files.delete(in.toPath());
        return files.toArray(new File[0]);
    }

    private static void delete(File cdir) throws IOException {
        if(!cdir.exists()) return;
        if(cdir.isDirectory()) for(File file : cdir.listFiles()){
            if(file.isDirectory()){
                delete(file);
            }
            if(file.isFile()){
                Files.delete(file.toPath());
            }
        }
        Files.delete(cdir.toPath());
    }

    private static void mkdir(ZipOutputStream lzos, String s) throws IOException {
        lzos.putNextEntry(new ZipEntry(s));
        lzos.closeEntry();
    }

    private static long copy(ZipEntry ze, ZipInputStream zis, ZipOutputStream zos) throws IOException {
        zos.putNextEntry(new ZipEntry(ze.getName()));

        long writed = 0L;

        if(!ze.isDirectory()){
            int len;
            byte[] buffer = new byte[1024];
            while((len = zis.read(buffer)) > 0){
                writed += len;
                zos.write(buffer, 0, len);
            }
        }

        zos.closeEntry();
        return writed;
    }

}
