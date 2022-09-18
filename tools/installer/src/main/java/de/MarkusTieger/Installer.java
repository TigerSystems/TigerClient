package de.MarkusTieger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import de.MarkusTieger.Installation.VanillaInstallation;
import de.MarkusTieger.mod.ModFetcher;
import de.MarkusTieger.mod.ModFile;

public class Installer {

    public static final String VERSION = "%mc_version%";
    public static final String FORGE = "%forge_version%";
    public static final String ICON = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAG9UlEQVR4Xu3bMYqsRRQF4IuBYiIPXmBiYCDiDsxN3wMTU92CkanLEGYR7sB1uJ1RjJpvioaDRVXX3/fAl13qr7qno4Gp6nQ6nU6n0+l0Op1Op9Pp/Jffvvvk9R7nOxeLhcv5zsVi4XK+c7FYuJzvXCwWLuc7F4uFy/nOxWLhcr5zsVi4nO8clt+//+L1np+++fwuz+scFguXhcvzOofFwmXh8rzOYbFwWbg8r3NYLFwWLs/rHBYLl4XL8zqHxcJl4fK8zmGxcFm4PK9zWPzDjpzvXCwWLuc7F4uFy/nOxWLhcr5zsVi4nO9cLBYu5zsXi4XL+c7FYuFyvrM4Lz+8e/0/LFT+YUfOp3xPJ4yFpixEFi7nU76nE8ZCUxYiC5fzKd/TCWOhKQuRhcv5lO/phLHQlIXIwuV8yvd0wlhoykJk4XI+5Xs6YSw0ZSGycDmf8j2dMBaashBZuJxP+Z6ni4Xozw/v73r964+7nJeFyvvo9dev7vJ78geRcp/HxYXKhcnC5bwsXN5HFi6/JwtNuc/j4kLlwmThcl4WLu8jC5ffk4Wm3OdxcaFyYbJwOS8Ll/eRhcvvyUJT7vO4uFC5MFm4nJeFy/vIwuX3ZKEp93lcXKhcmCxczsvC5X1k4fJ7stCU+zwuLlQuTBYu52Xh8j6ycPk9WWjKfR4XFyoXJguX87JweR9ZuPyeLDTlPh8uLkwuRC70jZdf7hv8KKbye/K+z/YDsXD5YLmwN1y4LGw2vyfv2z+A/gH0D6B/AP0D6B9A/wD6B9A/gP4B+F5ZaMo+lsfC5YPlwt5w4bKw2fyevG//ALIfwL9HbGVhqb9//vIu5+U+ZOGq3bFw+SDVoJSVLCRl4XJe7kMWrtodC5cPUg1KWclCUhYu5+U+ZOGq3bFw+SDVoJSVLCRl4XJe7kMWrtodC5cPUg1KWclCUhYu5+U+ZOGq3bFw+SDVoJSVLCRl4XJe7kMWrtodC5cPUg1KWclCUhYu5+U+ZOGq3bFw+SDVoJSVLCRl4XJe7kMWrtodC1cNln7LhcmFyXk5L++zmvfRx68/vat2x8JVg0ffsjC5EDkv5+V9VvM+snDV7li4avDoWxYmFyLn5by8z2reRxau2h0LVw0efcvC5ELkvJyX91nN+8jCVbtj4arBo29ZmFyInJfz8j6reR9ZuGp3LFw1ePQtC5MLkfNyXt5nNe8jC1ftjoWrBo++ZWFyIXJezsv7rOZ9ZOGq3bFw1eDRtyxMLkTOy3l5n9W8jyxctTv+YUL+YSNloSnPS3menJeFpixc9rE8Fi4XknLhKc9LeZ6cl4WmLFz2sTwWLheScuEpz0t5npyXhaYsXPaxPBYuF5Jy4SnPS3menJeFpixc9rE8Fi4XknLhKc9LeZ6cl4WmLFz2sTwWLheScuEpz0t5npyXhaYsXPaxPBYuF5Jy4SnPS3menJeFpixc9rE8Fi4XknLhKc9LeZ6cl4WmLFz2sTwWLheiGvxxZCa/JwuV8ykLlfOycNXuWLh8kGpQ2kx+TxYu51MWLudl4ardsXD5INWgtJn8nixczqcsXM7LwlW7Y+HyQapBaTP5PVm4nE9ZuJyXhat2x8Llg1SD0mbye7JwOZ+ycDkvC1ftjoXLB6kGpc3k92Thcj5l4XJeFq7aHQuXD1INSpvJ78nC5XzKwuW8LFy1OxYuH6QalDaT35OFy/mUhct5Wbjq0eMPQj44VYPSH4n31Y/ffnZXnR4LlwtJ1WDpj8T7ysJVp8fC5UJSNVj6I/G+snDV6bFwuZBUDZb+SLyvLFx1eixcLiRVg6U/Eu8rC1edHguXC0nVYOmPxPvKwlWnx8LlQlI1WPoj8b6ycNXpsXC5kFQNlv5IvK8sXHV6LFwvH97d5cJm8w8/Kc+T/ygjC5f7PC4WLguXC53NQlOeJwuXhct9HhcLl4XLhc5moSnPk4XLwuU+j4uFy8LlQmez0JTnycJl4XKfx8XCZeFyobNZaMrzZOGycLnP42LhsnC50NksNOV5snBZuNzncbFwWbhc6GwWmvI8WbgsXO7zuFi4LFwudDYLTXmeLFwWLvd5ufhg+YOQC5cLVw3+eDOT71E9e1yILFwWLgtXDUqbyfeonj0uRBYuC5eFqwalzeR7VM8eFyILl4XLwlWD0mbyPapnjwuRhcvCZeGqQWkz+R7Vs8eFyMJl4bJw1aC0mXyP6tnjQmThsnBZuGpQ2ky+R/XscSGycFm4LFw1KG0m36Pq3I8Lk39Yms3vpXxPJ4wLlYXN5vdSvqcTxoXKwmbzeynf0wnjQmVhs/m9lO/phHGhsrDZ/F7K93TCuFBZ2Gx+L+V7OmFcqCxsNr+X8j2dMC5UFjab30v5nk6n0+l0Op1Op9PpdDqdXfkHX12qTQmy31oAAAAASUVORK5CYII=";

    public static final String BASE_URL = "https://tigersystems.cf/tigerclient/files/";
    
    private static boolean INSTALL_RECOMMENDED = true;
    
    public static void main(String[] args) {

    	
    	System.out.println("Initializing Look and Feel...");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        // Test-Code
        
        runTests();
        
        System.out.println("Starting Installer...");

        Consumer<Runnable> executor = (run) -> new Thread(run).start();

        JFrame frame = new JFrame("TigerClient v2 Installer");
        frame.setSize(400, 250);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.setVisible(true);
        frame.setVisible(false);

        int width = frame.getContentPane().getWidth();
        int height = frame.getContentPane().getHeight();

        JCheckBox install_recommends = new JCheckBox("Install Recommended Mods (if available)");
        install_recommends.setBounds(25, 125, 300, 25);
        install_recommends.setFocusPainted(false);
        install_recommends.setSelected(true);
        install_recommends.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				INSTALL_RECOMMENDED = install_recommends.isSelected();
			}
		});
        frame.add(install_recommends);
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(25, 25, width - 50, 100);
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Loader-Type"));

        JRadioButton forge = new JRadioButton("Forge");
        JRadioButton vanilla = new JRadioButton("Vanilla");

        forge.setBounds(25, 25, width - 50, 25);
        forge.setFocusPainted(false);
        panel.add(forge);

        vanilla.setBounds(25, 50, frame.getContentPane().getWidth() - 50, 25);
        vanilla.setFocusPainted(false);
        // vanilla.setEnabled(false);
        panel.add(vanilla);

        addRadioMechanic(forge, vanilla);

        frame.add(panel);

        JButton next = new JButton("Next");
        next.setBounds(width - 125, height - 50, 100, 25);
        next.setFocusPainted(false);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                executor.accept(() -> selectDirectory(executor, (forge.isSelected() ? 0 : (vanilla.isSelected() ? 1 : -1))));
            }
        });
        frame.add(next);

        JButton extract_multimc = new JButton("MultiMC");
        extract_multimc.setBounds(width - 250, height - 50, 100, 25);
        extract_multimc.setFocusPainted(false);
        extract_multimc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                
                FileNameExtensionFilter filefilter = new FileNameExtensionFilter("Zip Archieve", "zip");
                chooser.addChoosableFileFilter(filefilter);
                chooser.setFileFilter(filefilter);
                chooser.setFileHidingEnabled(false);
                chooser.showOpenDialog(null);
                if(chooser.getSelectedFile() != null) {
                	frame.setVisible(false);
                	executor.accept(() -> extract_multimc(executor, (forge.isSelected() ? 0 : (vanilla.isSelected() ? 1 : -1)), chooser.getSelectedFile()));
                }
            }
        });
        frame.add(extract_multimc);
        
        JButton exit = new JButton("Exit");
        exit.setBounds(width - 375, height - 50, 100, 25);
        exit.setFocusPainted(false);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            }
        });
        frame.add(exit);

        frame.setVisible(true);
    }
    
    private static void runTests() {
    	@SuppressWarnings("unused")
		String __commit = """
    			
    			It's just for me to execute here code.
    			
    			""";
    	
    	
    	// Exec
	}

	private static final String[] PACK_IGNORE = new String[] {
    	
    		".minecraft/TigerClient",
    		".minecraft/config",
    		".minecraft/coremods",
    		".minecraft/defaultconfigs",
    		".minecraft/logs",
    		".minecraft/options.txt",
    		".minecraft/optionsof.txt",
    		".minecraft/resourcepacks",
    		".minecraft/saves",
    		".minecraft/screenshots",
    		".minecraft/server-resource-packs",
    		".minecraft/shaderpacks",
    		".minecraft/texturepacks"
    		
    };
    
    private static void extract_multimc(Consumer<Runnable> executor, int id, File file) {
    	if(id == -1) System.exit(id);
    	if(id == 1) {
    		JOptionPane.showMessageDialog(null, "The Vanilla version has no MultiMC support at this time.");
    		return;
    	}
    	
    	File temp = new File("tcv2_temp");
    	
    	install(executor, id, null, temp, (inst) -> {}, () -> {
    		
    		File ignore = new File(temp, ".packignore");
    		File instance = new File(temp, "instance.cfg");
    		File mmc = new File(temp, "mmc-pack.json");
    		File icon = new File(temp, "icon.png");
    		
    		try {
    			if(!ignore.exists()) ignore.createNewFile();
    			
    			String data = "";
        		for(String ig : PACK_IGNORE) {
        			data += ig;
        			data += '\n';
        		}
        		if(data.endsWith("\n")) {
        			data = data.substring(0, data.length() - 1);
        		}
    			
        		try(FileOutputStream out = new FileOutputStream(ignore)) {
        			out.write(data.getBytes(StandardCharsets.UTF_8));
    				out.flush();
        		}
    			
    			Properties properties = new Properties();
    			properties.setProperty("AutoCloseConsole", "false");
    			properties.setProperty("ForgeVersion", "");
    			properties.setProperty("InstanceType", "OneSix");
    			properties.setProperty("IntendedVersion", "");
    			properties.setProperty("JavaPath", "/usr/lib/jvm/zulu-fx-17-amd64/bin/java");
    			properties.setProperty("JoinServerOnLaunch", "");
    			properties.setProperty("JoinServerOnLaunchAddress", "");
    			properties.setProperty("JvmArgs", "");
    			properties.setProperty("LWJGLVersion", "");
    			properties.setProperty("LaunchMaximized", "");
    			properties.setProperty("LiteloaderVersion", "");
    			properties.setProperty("LogPrePostOutput", "");
    			properties.setProperty("MCLaunchMethod", "LauncherPart");
    			properties.setProperty("MaxMemAlloc", "8192");
    			properties.setProperty("MinMemAlloc", "8192");
    			properties.setProperty("MinecraftWinHeight", "480");
    			properties.setProperty("MinecraftWinWidth", "854");
    			properties.setProperty("OverrideCommands", "false");
    			properties.setProperty("OverrideConsole", "false");
    			properties.setProperty("OverrideGameTime", "false");
    			properties.setProperty("OverrideJava", "false");
    			properties.setProperty("OverrideJavaArgs", "false");
    			properties.setProperty("OverrideJavaLocation", "false");
    			properties.setProperty("OverrideMCLaunchMethod", "false");
    			properties.setProperty("OverrideMemory", "false");
    			properties.setProperty("PermGen", "128");
    			properties.setProperty("PostExitCommand", "");
    			properties.setProperty("PreLaunchCommand", "");
    			properties.setProperty("RecordGameTime", "true");
    			properties.setProperty("ShowConsole", "false");
    			properties.setProperty("ShowConsoleOnError", "true");
    			properties.setProperty("ShowGameTime", "true");
    			properties.setProperty("UseNativeGLFW", "false");
    			properties.setProperty("UseNativeOpenAL", "false");
    			properties.setProperty("WrapperCommand", "");
    			properties.setProperty("iconKey", "tigerclient");
    			properties.setProperty("lastLaunchTime", System.currentTimeMillis() + "");
    			properties.setProperty("lastTimePlayed", "0");
    			properties.setProperty("name", "TigerClient-v2");
    			properties.setProperty("notes", "");
    			properties.setProperty("totalTimePlayed", "0");
    			
    			if(!instance.exists()) instance.createNewFile();
    			
    			try(FileOutputStream out = new FileOutputStream(instance)) {
    				properties.store(out, "Created by Original TigerClient-v2 Installer");
    				out.flush();
    			}
    			
    			if(!mmc.exists()) mmc.createNewFile();
    			
    			JsonObject obj = new JsonObject();
    			
    			JsonArray components = new JsonArray();
    			
    			JsonObject lwjgl = new JsonObject();
    			lwjgl.addProperty("cachedName", "LWJGL 3");
    			lwjgl.addProperty("cachedVersion", "3.2.2");
    			lwjgl.addProperty("cachedVolatile", Boolean.TRUE);
    			lwjgl.addProperty("dependencyOnly", Boolean.TRUE);
    			lwjgl.addProperty("uid", "org.lwjgl3");
    			lwjgl.addProperty("version", "3.2.2");
    			components.add(lwjgl);
    			
    			JsonObject mc = new JsonObject();
    			mc.addProperty("cachedName", "Minecraft");
    			
    			JsonArray requires = new JsonArray();
    			
    			lwjgl = new JsonObject();
    			lwjgl.addProperty("equals", "3.2.2");
    			lwjgl.addProperty("suggests", "3.2.2");
    			lwjgl.addProperty("uid", "org.lwjgl3");
    			requires.add(lwjgl);
    			
    			mc.add("cachedRequires", requires);
    			mc.addProperty("cachedVersion", VERSION);
    			mc.addProperty("important", Boolean.TRUE);
    			mc.addProperty("uid", "net.minecraft");
    			mc.addProperty("version", VERSION);
    			components.add(mc);
    			
    			
    			if(id == 0) {
    				JsonObject forge = new JsonObject();
    				forge.addProperty("cachedName", "Forge");
    				
    				requires = new JsonArray();
        			
        			mc = new JsonObject();
        			mc.addProperty("equals", VERSION);
        			mc.addProperty("uid", "net.minecraft");
        			requires.add(mc);
        			
        			forge.add("cachedRequires", requires);
        			
        			forge.addProperty("cachedVersion", FORGE);
        			forge.addProperty("uid", "net.minecraftforge");
        			forge.addProperty("version", FORGE);
        			
        			components.add(forge);
    			}
    			
    			obj.add("components", components);
    			obj.addProperty("formatVersion", 1);
    			
    			try(FileOutputStream out = new FileOutputStream(mmc)) {
    				out.write(GSON.toJson(obj).getBytes(StandardCharsets.UTF_8));
    				out.flush();
    			}
    			
    			if(!icon.exists()) icon.createNewFile();
    			
    			String base64 = ICON.substring(22);
    			
    			try (FileOutputStream out = new FileOutputStream(icon)) {
    				out.write(
    						Base64.getDecoder().decode(base64)
    						);
    				out.flush();
    			}
    			
    			
    			
    			
    			
    			
    			
    			if(!file.exists()) file.createNewFile();
    			
    			try (ZipOutputStream out = new ZipOutputStream(
    						new FileOutputStream(file)
    					)) {
    				
    				String prefix = "TigerClient-v2/";
    				
    				ZipEntry entry = new ZipEntry(prefix);
    				out.putNextEntry(entry);
    				out.closeEntry();
    				
    				entry = new ZipEntry(prefix + ignore.getName());
    				
    				out.putNextEntry(entry);
    				
    				copy(ignore, out);
    				
    				out.closeEntry();
    				
    				
    				entry = new ZipEntry(prefix + instance.getName());
    				
    				out.putNextEntry(entry);
    				
    				copy(instance, out);
    				
    				out.closeEntry();
    				
    				
    				entry = new ZipEntry(prefix + mmc.getName());
    				
    				out.putNextEntry(entry);
    				
    				copy(mmc, out);
    				
    				out.closeEntry();
    				
    				
    				entry = new ZipEntry(prefix + "tigerclient.png");
    				
    				out.putNextEntry(entry);
    				
    				copy(icon, out);
    				
    				out.closeEntry();
    				
    				entry = new ZipEntry(prefix + ".minecraft/");
    				
    				out.putNextEntry(entry);
    				out.closeEntry();
    				
    				
    				prefix += ".minecraft/";
    				
    				entry = new ZipEntry(prefix + "mods/");
    				out.putNextEntry(entry);
    				out.closeEntry();
    				
    				
    				File key = new File(temp, "TigerClient" + File.pathSeparatorChar + "key.tc2-beta-secret");
    				if(key.exists()) {
    					entry = new ZipEntry(prefix + "TigerClient/key.tc2-beta-secret");
    					out.putNextEntry(entry);
    					
    					copy(key, out);
    					
    					out.closeEntry();
    				}
    				
    				
    				File mods = new File(temp, "mods");
    				
    				if(mods.exists()) {
    					prefix += mods.getName() + "/";
    					
    					for(File f : mods.listFiles()) {
    						entry = new ZipEntry(prefix + f.getName());
    						out.putNextEntry(entry);
    						
    						copy(f, out);
    						
    						out.closeEntry();
    					}
    					
    				}
    				
    				
    				
    				out.flush();
    				out.finish();
    				
    				delete(temp);
    				
    				JOptionPane.showMessageDialog(null, "TigerClient Successfully packed into Zip Archieve for MultiMC.");
                	System.exit(0);
    			}
    			
    		} catch(Throwable e) {
    			e.printStackTrace();
    			try {
					delete(temp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                JOptionPane.showMessageDialog(null, "TigerClient can't Installed! -> Console");
                return;
    		}
    		
    	});
    	
    }

    private static void copy(File file, OutputStream out) throws IOException {
        try (FileInputStream in = new FileInputStream(file)) {
        	int len;
        	byte[] buffer = new byte[1024];
        	while((len = in.read(buffer)) > 0){
            	out.write(buffer, 0, len);
        	}
        	out.flush();
        }
	}

	private static void selectDirectory(Consumer<Runnable> executor, int id) {

        if(id == -1) System.exit(id);

        JFrame frame = new JFrame("TigerClient v2 Installer");
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.setVisible(true);
        frame.setVisible(false);

        int width = frame.getContentPane().getWidth();
        int height = frame.getContentPane().getHeight();


        File target;
        String mcdir = ".minecraft";
        String home = System.getProperty("user.home", ".");
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

        if(os.contains("win") && System.getenv("APPDATA") != null) {
            target = new File(System.getenv("APPDATA"), mcdir);
        } else if(os.contains("mac")) {
            target = new File(new File(new File(home, "Library"), "Application Support"), "minecraft");
        } else {
            target = new File(home, mcdir);
        }


        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 25, width, 75);
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Launcher"));

        JTextField launcher = new JTextField();
        launcher.setBounds(25, 25, width - 175, 25);
        panel.add(launcher);

        JButton launcher_choose = new JButton("Choose");
        launcher_choose.setBounds(width - 125, 25, 100, 25);
        launcher_choose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                if(new File(launcher.getText()).exists()) chooser.setCurrentDirectory(new File(launcher.getText()));
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setFileHidingEnabled(false);
                chooser.showOpenDialog(null);
                if(chooser.getSelectedFile() != null){
                    launcher.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        panel.add(launcher_choose);

        frame.add(panel);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 125, width, 75);
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Game"));

        JTextField game = new JTextField();
        game.setBounds(25, 25, width - 175, 25);
        panel.add(game);

        JButton game_choose = new JButton("Choose");
        game_choose.setBounds(width - 125, 25, 100, 25);
        game_choose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                if(new File(launcher.getText()).exists()) chooser.setCurrentDirectory(new File(launcher.getText()));
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setFileHidingEnabled(false);
                chooser.showOpenDialog(null);
                if(chooser.getSelectedFile() != null){
                    game.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        panel.add(game_choose);

        frame.add(panel);

        launcher.setText(target.getAbsolutePath());
        game.setText(target.getAbsolutePath());

        JButton next = new JButton("Next");
        next.setBounds(width - 125, height - 50, 100, 25);
        next.setFocusPainted(false);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);

                File l = new File(launcher.getText());
                File g = new File(game.getText());

                if(!l.exists() || !l.isDirectory()){
                    executor.accept(() -> JOptionPane.showMessageDialog(null, "The Launcher Directory is Invalid!"));
                    frame.setVisible(true);
                    return;
                }

                if(!g.exists() || !g.isDirectory()){
                    executor.accept(() -> JOptionPane.showMessageDialog(null, "The Game Directory is Invalid!"));
                    frame.setVisible(true);
                    return;
                }

                File def = new File(l, "launcher_profiles.json");
                File micro = new File(l, "launcher_profiles_microsoft_store.json");
                if(!def.exists() && !micro.exists()) {
                    executor.accept(() -> JOptionPane.showMessageDialog(null, "No Launcher was found! Install the Default Launcher or the Launcher of the Microsoft Store to continue!"));
                    frame.setVisible(true);
                    return;
                }

                if(def.exists() && micro.exists()){
                    executor.accept(() -> selectLauncher(executor, id, def, micro, l, g));
                } else if(def.exists()){
                    executor.accept(() -> selectProfile(executor, id, def, l, g));
                } else if(micro.exists()){
                    executor.accept(() -> selectProfile(executor, id, micro, l, g));
                }

                // executor.accept(() -> selectProfile(executor, id, width, height));
            }
        });
        frame.add(next);

        JButton exit = new JButton("Back");
        exit.setBounds(width - 250, height - 50, 100, 25);
        exit.setFocusPainted(false);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                executor.accept(() -> Installer.main(new String[0]));
            }
        });
        frame.add(exit);

        frame.setVisible(true);

    }

    private static void selectLauncher(Consumer<Runnable> executor, int id, File defProfiles, File microProfiles, File launcherDir, File gameDir) {
        JFrame frame = new JFrame("TigerClient v2 Installer");
        frame.setSize(400, 250);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.setVisible(true);
        frame.setVisible(false);

        int width = frame.getContentPane().getWidth();
        int height = frame.getContentPane().getHeight();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 25, width, 100);
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Launcher"));

        JRadioButton mojang = new JRadioButton("Mojang");
        JRadioButton microsoft = new JRadioButton("Microsoft");

        mojang.setBounds(25, 25, width - 50, 25);
        mojang.setFocusPainted(false);
        panel.add(mojang);

        microsoft.setBounds(25, 50, frame.getContentPane().getWidth() - 50, 25);
        microsoft.setFocusPainted(false);
        microsoft.setEnabled(false);
        panel.add(microsoft);

        addRadioMechanic(mojang, microsoft);

        frame.add(panel);

        JButton next = new JButton("Next");
        next.setBounds(width - 125, height - 50, 100, 25);
        next.setFocusPainted(false);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);

                executor.accept(() -> selectProfile(executor, id, mojang.isSelected() ? defProfiles : microProfiles, launcherDir, gameDir));

            }
        });
        frame.add(next);

        JButton exit = new JButton("Back");
        exit.setBounds(width - 250, height - 50, 100, 25);
        exit.setFocusPainted(false);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                executor.accept(() -> selectDirectory(executor, id));
            }
        });
        frame.add(exit);

        frame.setVisible(true);
    }

    private static void addRadioMechanic(JRadioButton def, JRadioButton... btns) {

        def.setSelected(true);
        for(JRadioButton btn : btns) btn.setSelected(false);

        ArrayList<JRadioButton> array = new ArrayList<>();
        array.add(def);
        Collections.addAll(array, btns);

        for(JRadioButton btn : array){
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if(btn.isSelected()){
                        for(JRadioButton b : array) {
                            if(b != btn) b.setSelected(false);
                        }
                    } else {
                        boolean selected = false;
                        for(JRadioButton b : array){
                            if(b.isSelected()) selected = true;
                        }
                        if(!selected) btn.setSelected(true);
                    }

                }
            });
        }

    }

    private static void selectProfile(Consumer<Runnable> executor, int id, File profilesFile, File launcherDir, File gameDir) {

        JFrame frame = new JFrame("TigerClient v2 Installer");
        frame.setSize(400, 250);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.setVisible(true);
        frame.setVisible(false);

        int width = frame.getContentPane().getWidth();
        int height = frame.getContentPane().getHeight();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 25, width, 100);
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Profiles"));

        JRadioButton select = new JRadioButton(id == 0 ? "Select Installation" : "Create Profile");
        JRadioButton no = new JRadioButton("Skip Profiles");

        select.setBounds(25, 25, width - 50, 25);
        select.setFocusPainted(false);
        panel.add(select);

        no.setBounds(25, 50, frame.getContentPane().getWidth() - 50, 25);
        no.setFocusPainted(false);
        panel.add(no);

        addRadioMechanic(select, no);

        frame.add(panel);

        JButton next = new JButton("Next");
        next.setBounds(width - 125, height - 50, 100, 25);
        next.setFocusPainted(false);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);

                if(select.isSelected()){
                    executor.accept(() -> configureProfile(executor, id, profilesFile, launcherDir, gameDir));
                }

                if(no.isSelected()){
                    executor.accept(() -> install(executor, id, launcherDir, gameDir, (inst) -> {}, () -> {
                    	JOptionPane.showMessageDialog(null, "TigerClient Successfully Installed!");
                    	System.exit(0);
                    }));
                }

            }
        });
        frame.add(next);

        JButton exit = new JButton("Back");
        exit.setBounds(width - 250, height - 50, 100, 25);
        exit.setFocusPainted(false);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                executor.accept(() -> selectDirectory(executor, id));
            }
        });
        frame.add(exit);

        frame.setVisible(true);

    }

    private static void configureProfile(Consumer<Runnable> executor, int id, File profilesFile, File launcherDir, File gameDir) {

    	File versions = new File(launcherDir, "versions");
    	
        if(id == 1) {
            install(executor, id, launcherDir, gameDir, (inst) -> profile(executor, profilesFile, versions, gameDir, inst, "-Vanilla"),
            		() -> {
            	JOptionPane.showMessageDialog(null, "TigerClient Successfully Installed!");
            	System.exit(0);
            });
            return;
        }

        JFrame frame = new JFrame("TigerClient v2 Installer");
        frame.setSize(400, 250);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.setVisible(true);
        frame.setVisible(false);

        int width = frame.getContentPane().getWidth();
        int height = frame.getContentPane().getHeight();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 25, width, 75);
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Installations"));

        JComboBox<Installation> combo = new JComboBox<Installation>();
        combo.setBounds(25, 25, width - 50, 25);
        panel.add(combo);

        
        if(versions.exists()){
            ArrayList<Installation> inst = new ArrayList<Installation>();
            for(File ver : versions.listFiles()){
                File js = new File(ver, ver.getName() + ".json");
                if(!js.exists()) continue;
                if(ver.getName().toLowerCase().startsWith((VERSION + "-forge").toLowerCase())){
                    inst.add(new Installation(ver, js));
                }
            }
            if(inst.size() == 0){
                combo.setEnabled(false);
            } else {
                for(Installation in : inst){
                    combo.addItem(in);
                    if(in.getDir().getName().equalsIgnoreCase(VERSION + "-forge-" + FORGE)) {
                    	combo.setSelectedItem(in);
                    }
                }
            }
        } else {
            combo.setEnabled(false);
        }

        frame.add(panel);

        JButton next = new JButton("Next");
        next.setBounds(width - 125, height - 50, 100, 25);
        next.setFocusPainted(false);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                executor.accept(() -> install(executor, id, launcherDir, gameDir, (inst) -> profile(executor, profilesFile, versions, gameDir, ((Installation)combo.getSelectedItem()), "-Forge"), () -> {
                	JOptionPane.showMessageDialog(null, "TigerClient Successfully Installed!");
                System.exit(0); }));
            }
        });
        next.setEnabled(combo.isEnabled());
        frame.add(next);

        JButton exit = new JButton("Back");
        exit.setBounds(width - 250, height - 50, 100, 25);
        exit.setFocusPainted(false);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                executor.accept(() -> selectProfile(executor, id, profilesFile, launcherDir, gameDir));
            }
        });
        frame.add(exit);

        frame.setVisible(true);

    }
    
    private static String state = "Waiting... (Wait what)";
    private static double percend = 0D;

    private static void installWindow(Consumer<Runnable> executor, String target) {
    	
    	JFrame frame = new JFrame("Installing TigerClient on " + target + " ...");
    	frame.setSize(350, 125);
    	frame.setLayout(null);
    	frame.setResizable(false);
    	frame.setLocationRelativeTo(null);
    	
    	JProgressBar bar = new JProgressBar();
    	bar.setBounds(25, 25, 300, 25);
    	bar.setMinimum(0);
    	bar.setMaximum(10000);
    	bar.setValue(0);
    	frame.add(bar);
    	
    	JLabel label = new JLabel("<html>" + state + "</html>");
    	label.setBounds(25, 50, 300, 30);
    	frame.add(label);
    	
    	frame.setVisible(true);
    	
    	executor.accept(() -> {
    		
    		while(percend < 1D) {
        		bar.setValue((int) (percend * ((double)bar.getMaximum())));
        		label.setText("<html>" + state + "</html>");
        		try {
    				Thread.sleep(0L, 100);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
        	}
        	frame.setVisible(false);
    		
    	});
    }
    
    private static final Predicate<Object> NOT_NULL = (obj) -> obj != null;
    
    private static boolean verify(JarEntry je) {
    	boolean equal = false;

        Certificate[] certs = je.getCertificates();
        
        if(certs == null) {
        	return false;
        }
        
        String cert_str = "";
        try {
        	InputStream in = Installer.class.getResourceAsStream("/cert");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while((len = in.read(buffer)) > 0){
                out.write(buffer, 0, len);
            }
            in.close();
            
            cert_str = new String(out.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
        	e.printStackTrace();
        	return false;
        }
        
        System.out.println("Cert: " + cert_str);
        
        for(Certificate cert : certs) {
            try {
            	
            	if(!(cert instanceof X509Certificate x509cert)) continue;
            	
            	x509cert.checkValidity();
            	
                String data = DatatypeConverter.printHexBinary(
                        MessageDigest.getInstance("SHA-256").digest(
                                cert.getEncoded())).toLowerCase();

           
                
                if(data.equalsIgnoreCase(cert_str)) equal = true;
            } catch(Throwable e){
                e.printStackTrace();
            }
        }
        
        
        return equal;
    }
    
    private static void install(Consumer<Runnable> executor, int id, File launcherDir, File gameDir, Consumer<VanillaInstallation> profileSet, Runnable exit) {
        String install_target_tmp = "";
        if(id == 0) install_target_tmp = "Forge";
        if(id == 1) install_target_tmp = "Vanilla";
        final String install_target = install_target_tmp;
        
        executor.accept(() -> installWindow(executor, install_target));
        
        state = "Require Beta-Secret";
        
        final String extension = "tc2-beta-secret";
        
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("TigerClient Beta Secret File", extension));
    	chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	chooser.setFileHidingEnabled(false);
    	chooser.setDialogTitle("TigerClient Beta Secret File Chooser");
        chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
        chooser.showOpenDialog(null);
        
        byte[] secret = null;
        if(chooser.getSelectedFile() == null) {
        	JOptionPane.showMessageDialog(null, "No Beta-Secret Selected. Continuing without, but the TigerClient will not load without the key.");
        } else {
        	File f = chooser.getSelectedFile();
        	try {
				InputStream in = new FileInputStream(f);
				secret = in.readAllBytes();
				in.close();
				
				JarInputStream jis = new JarInputStream(new ByteArrayInputStream(secret), true);
				
				JarEntry target = null,
						je = null;
				
				while((je = jis.getNextJarEntry()) != null) {
					
					if(je.getName().equalsIgnoreCase("allowed_users")) {
						target = je;
					}
					
				}
				
				
				
				if(verify(target)) {
					// All good
				} else {
					
					JOptionPane.showMessageDialog(null, "The Beta-Secret can't verified. Maybe there is an error while verifing it. When it is invalid, the TigerClient will not load.");
				}
				
				jis.close();
				
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "The Beta-Secret can't readed. Continuing without, but the TigerClient will not load without the key.");
			}
        }
        
        copy_key: {
        	
        	if(secret == null) break copy_key;
        	
        	File tc_dir = new File(gameDir, "TigerClient");
            if(!tc_dir.exists()) tc_dir.mkdirs();
            
            try {
            	File secret_file = new File(tc_dir, "key." + extension);
                if(!secret_file.exists()) secret_file.createNewFile();
                
                FileOutputStream out = new FileOutputStream(secret_file);
                out.write(secret);
                out.flush();
                out.close();
            } catch(IOException ex) {
            	ex.printStackTrace();
            }
        	
        }
        
    	if(id == 0) {

            File mods = new File(gameDir, "mods");
            if(!mods.exists()) mods.mkdirs();
            
            state = "Installing TigerClient...";
            percend = 0D;

            try {
                HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL + "loaders/forge.jar").openConnection();
                con.setRequestProperty("User-Agent", "TigerSystems");
                
                double content = ((double)con.getContentLength());
                
                InputStream in = con.getInputStream();
                int len;
                byte[] buffer = new byte[1024];
                File file = new File(mods, "tigerclient.jar");
                if(!file.exists()) file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                
                double readed = 0D;
                while((len = in.read(buffer)) > 0){
                    out.write(buffer, 0, len);
                    readed += len;
                    
                    double data = (readed * 0.25D);
                    data /= content;
                    percend = data;
                }
                out.flush();
                out.close();
                in.close();
            } catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "TigerClient can't Installed! -> Console");
                return;
            }
            
            System.out.println("TigerClient installed.");
            
            state = "Fetching Additional Modifications...";
            percend = 0.25D;
            
            List<ModFile> mod_files = new ArrayList<>();
            
            if (INSTALL_RECOMMENDED) {
            	try {
                	mod_files = ModFetcher.fetchMods((d) -> {
                		
                		percend = 0.25D + (d / 4);
                		
                	});
                } catch (Exception e) {
    				e.printStackTrace();
    				JOptionPane.showMessageDialog(null, "The Recommend Mods can't Installed! -> Console");
    			}
			}
            
            
            
            mod_files = mod_files.stream().filter(
            		NOT_NULL
            		).toList(); // Filter
            
            state = "Downloading Additional Modifications...";
            percend = 0.50D;
            
            double pos = 0D;
            for(ModFile mod_file : mod_files) {
            	try {
            		HttpURLConnection con = (HttpURLConnection) new URL(mod_file.file_download()).openConnection();
                    con.setRequestProperty("User-Agent", "TigerSystems");
                    InputStream in = con.getInputStream();
                    int len;
                    byte[] buffer = new byte[1024];
                    File file = new File(mods, mod_file.file_name());
                    if(!file.exists()) file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    while((len = in.read(buffer)) > 0){
                        out.write(buffer, 0, len);
                    }
                    out.flush();
                    out.close();
                    in.close();
                    
                    System.out.println(mod_file.name() + " installed.");
                    
                    pos++;
                    
                    double d = (pos * 0.5D);
                    d /= ((double)mod_files.size());
                    
                    percend = 0.5D + d;
                    
            	} catch(Exception e) {
            		e.printStackTrace();
            		JOptionPane.showMessageDialog(null, "The Recommend Mod \"" + mod_file.name() + "\" can't Installed! -> Console (will be ignored)");
            	}
            }

            state = "Finish";
            percend = 1.00D;
            
            profileSet.accept(null);
            exit.run();
            return;
        }

        if(id == 1) {
        	
        	final int minSectors = 1;
        	
        	String target_id = "TigerClient-v2-Vanilla";
        	
        	File versions = new File(launcherDir, "versions");
        	if(!versions.exists()) versions.mkdirs();
        	
        	File ver = new File(versions, target_id);
        	if(!ver.exists()) ver.mkdirs();
        	
        	int len;
        	byte[] buffer = new byte[1024];
        	
        	state = "Downloading Sectors...";
        	
        	percend = 0;
        	
        	int pos = 0;
        	while(true) {
        		
        		state = "Downloading Sectors... (" + pos + ")";
        		
        		try {
            		HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL + "loaders/vanilla/sector" + pos + ".zip").openConnection();
                    con.setRequestProperty("User-Agent", "TigerSystems");
                    InputStream in = con.getInputStream();
                    File file = new File(ver, "sector" + pos + ".zip");
                    if(!file.exists()) file.createNewFile();
                    FileOutputStream out = new FileOutputStream(file);
                    while((len = in.read(buffer)) > 0){
                        out.write(buffer, 0, len);
                    }
                    out.flush();
                    out.close();
                    in.close();
                    
                } catch(Exception e){
                    e.printStackTrace();
                    if(pos < minSectors) {
                    	System.out.println("Sectors missing.");
                    	JOptionPane.showMessageDialog(null, "TigerClient can't Installed! -> Console");
                    	return;
                    }
                    break;
                }
        		
        		pos++;
        	}
        	
        	percend = 0.40;
        	
        	state = "Downloading Signature...";
        	
        	try {
        		HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL + "loaders/vanilla/signature.zip").openConnection();
                con.setRequestProperty("User-Agent", "TigerSystems");
                int length = con.getContentLength();
                InputStream in = con.getInputStream();
                File file = new File(ver, "signature.zip");
                if(!file.exists()) file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                
                double readed = 0D;
                while((len = in.read(buffer)) > 0){
                    out.write(buffer, 0, len);
                    readed += len;
                    
                    double data = readed * 0.1D;
                    data /= ((double)length);
                    percend = 0.4D + data;
                }
                out.flush();
                out.close();
                in.close();
                
            } catch(Exception e) {
            	e.printStackTrace();
                System.out.println("Signature missing.");
            }
        	
        	percend = 0.50;
        	state = "Building Jar...";
        	
        	try {
        		
        		List<String> ignore = new ArrayList<>();
        		
        		File file = new File(ver, target_id + ".jar");
        		if(!file.exists()) file.createNewFile();
        		FileOutputStream out = new FileOutputStream(file);
        		ZipOutputStream zos = new ZipOutputStream(out);
        		
        		ZipEntry ze = null;
        		
        		for(int i = 0; i < pos; i++) {
        			
        			state = "Building Jar... ( " + i + " / " + (pos - 1) + " )";
        			
        			
        			FileInputStream in = new FileInputStream(new File(ver, "sector" + i + ".zip"));
        			ZipInputStream zis = new ZipInputStream(in);
        			
        			while((ze = zis.getNextEntry()) != null) {
        				
        				state = "Building Jar... ( " + i + " / " + (pos - 1) + " )<br/>" + ze.getName();
        				
        				if(ze.getName().toLowerCase().startsWith("meta-inf")) continue;
        				
        				if(ignore.contains(ze.getName())) continue;
        				ignore.add(ze.getName());
        				
        				if(ze.isDirectory()) {
        					zos.putNextEntry(new ZipEntry(ze));
        					zos.closeEntry();
        				} else {
        					zos.putNextEntry(new ZipEntry(ze));
        					buffer_copy(zis, zos, buffer);
        					zos.closeEntry();
        				}
        				
        				zis.closeEntry();
        				
        			}
        			
        			zis.close();
        			
        			double data = ((double)i) * 0.40D;
        			data /= ((double)pos);
        			percend = 0.50D + data;
        			
        		}
        		
        		
        		
        		File f = new File(ver, "signature.zip");
        		if(f.exists()) {
        			
        			state = "Building Jar (Signature)...";
        			
        			FileInputStream in = new FileInputStream(f);
        			ZipInputStream zis = new ZipInputStream(in);
        			
        			while((ze = zis.getNextEntry()) != null) {
        				
        				state = "Building Jar (Signature)...<br/>" + ze.getName();
        				
        				if(ignore.contains(ze.getName())) continue;
        				ignore.add(ze.getName());
        				
        				if(ze.isDirectory()) {
        					zos.putNextEntry(new ZipEntry(ze));
        					zos.closeEntry();
        				} else {
        					zos.putNextEntry(new ZipEntry(ze));
        					buffer_copy(zis, zos, buffer);
        					zos.closeEntry();
        				}
        				
        				zis.closeEntry();
        				
        			}
        			
        			zis.close();
        			
        			percend = 0.95D;
        			
        		}
        		
        		zos.finish();
        		zos.flush();
        		zos.close();
        		
        		
        		final File jar = file;
        		
        		
        		HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL + "config/launcher.json").openConnection();
                con.setRequestProperty("User-Agent", "TigerSystems");
                InputStream in = con.getInputStream();
                file = new File(ver, target_id + ".json");
                if(!file.exists()) file.createNewFile();
                out = new FileOutputStream(file);
                while((len = in.read(buffer)) > 0){
                    out.write(buffer, 0, len);
                }
                out.flush();
                out.close();
                in.close();
                
                
                
                VanillaInstallation inst = new VanillaInstallation(ver, file);
                
                modify(inst, jar);
                
                percend = 1.00D;
                state = "Finish";
                
                profileSet.accept(inst);
                
            } catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "TigerClient can't Installed! -> Console");
                return;
            }
        	
        	
        	
            exit.run();
            return;
        	
        }
        
        JOptionPane.showMessageDialog(null, "Something went wrong...");
        throw new RuntimeException("This Should never reached, ID: " + id);
    }

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
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
    
    private static void modify(VanillaInstallation inst, File jar) throws IOException, JsonSyntaxException, JsonParseException {
    	FileInputStream in = new FileInputStream(inst.getJs());
    	byte[] data = in.readAllBytes();
    	in.close();
    	JsonObject obj = GSON.fromJson(new String(data, StandardCharsets.UTF_8), JsonObject.class);
    	
    	obj.remove("id");
    	obj.addProperty("id", inst.getDir().getName());
    	
    	JsonObject downloads = obj.getAsJsonObject("downloads");
    	obj.remove("downloads");
    	
    	JsonObject client = downloads.getAsJsonObject("client");
    	downloads.remove("client");
    	
    	client.remove("sha1");
    	client.remove("size");
    	
    	client.addProperty("size", jar.length());
    	client.addProperty("sha1", hash(jar));
    	
    	downloads.add("client", client);
    	
    	obj.add("downloads", downloads);
    	
    	FileOutputStream out = new FileOutputStream(inst.getJs());
    	out.write(GSON.toJson(obj).getBytes(StandardCharsets.UTF_8));
    	out.flush();
    	out.close();
    }

	private static void buffer_copy(InputStream in, OutputStream out, byte[] buffer) throws IOException {
    	int len;
    	while((len = in.read(buffer)) > 0) {
    		out.write(buffer, 0, len);
    	}
	}

    @SuppressWarnings("unused")
	private static void profile(Consumer<Runnable> executor, File profilesFile, File versions, File gameDir, Installation selectedItem, String suffix) {
        try {
        	
        	final String target_id = "TigerClient-v2" + suffix;
        	
        	
        	File javaExecutable = null;
        	// File javaExecutable = searchJavaExecutable();
        	
        	if(javaExecutable == null) {
        		System.out.println("Using Default-Java-Executable");
        		
        		// JOptionPane.showMessageDialog(null, "Warning no Java " + javaVersion + " Installation was found. Using Default Java Installation of Minecraft Launcher. Please change the Java version to an version over or equal 18.");
        		
        	} else System.out.println("Using Java-Executable: " + javaExecutable.getAbsolutePath());
        	
        	
        	
        	
            if(!(selectedItem instanceof VanillaInstallation)) {
            	File target = new File(versions, target_id);
                delete(target);
                target.mkdirs();
                copy(selectedItem.getDir(), target);
                
                File json = selectedItem.getJs();
                if(!json.exists()) return;
                FileReader reader = new FileReader(json);
                JsonObject obj = GSON.fromJson(reader, JsonObject.class);
                reader.close();
                
                
                

                obj.remove("id");
                obj.addProperty("id", target_id);
                
                
                JsonObject javaVersion = new JsonObject();
                javaVersion.addProperty("component", "java-runtime-beta");
                javaVersion.addProperty("majorVersion", Installer.javaVersion);
                
                
                obj.remove("javaVersion");
                obj.add("javaVersion", javaVersion);
            	
            	if(!json.exists()) json.createNewFile();
            	
            	PrintWriter writer = new PrintWriter(json);
            	writer.write(GSON.toJson(obj));
            	writer.flush();
            	writer.close();
            }
            
            if(!profilesFile.exists()) return;

            FileReader reader = new FileReader(profilesFile);
            JsonObject obj = GSON.fromJson(reader, JsonObject.class);
            reader.close();

            JsonObject profiles = new JsonObject();
            if(obj.has("profiles")){
                profiles = obj.get("profiles").getAsJsonObject();
                obj.remove("profiles");
            }

            JsonObject profile = new JsonObject();

            if(profiles.has(target_id)) {
                profile = profiles.get(target_id).getAsJsonObject();
                profiles.remove(target_id);
            }

            if(!profile.has("gameDir")) profile.remove("gameDir");
            if(profile.has("name")) profile.remove("name");
            if(profile.has("type")) profile.remove("type");
            if(profile.has("icon")) profile.remove("icon");
            if(profile.has("lastVersionId")) profile.remove("lastVersionId");

            profile.addProperty("name", target_id);
            profile.addProperty("type", "custom");
            profile.addProperty("icon", ICON);
            profile.addProperty("lastVersionId", target_id);
            profile.addProperty("gameDir", gameDir.getAbsolutePath());
            if(javaExecutable != null) profile.addProperty("javaDir", javaExecutable.getAbsolutePath());

            profiles.add(target_id, profile);

            obj.add("profiles", profiles);

            PrintWriter writer = new PrintWriter(profilesFile);
            writer.write(GSON.toJson(obj));
            writer.flush();
            writer.close();

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    

	@SuppressWarnings("unused")
	private static File searchJavaExecutable() {
		
    	String os = System.getProperty("os.name").toLowerCase();    	
    	if(os.contains("win")) {
    	
    		search_x64: {
    			File searchDir = new File("C:\\Program Files\\Java");
        		if(!searchDir.exists()) break search_x64;
        		
        		File exec = searchJavaExecutable(searchDir, ".exe");
        		if(exec != null) return exec;
    		}
    		
    		search_x86: {
    			File searchDir = new File("C:\\Program Files (x86)\\Java");
        		if(!searchDir.exists()) break search_x86;
        		
        		File exec = searchJavaExecutable(searchDir, ".exe");
        		if(exec != null) return exec;
    		}
    		
    	} else search_unix: {
    		File searchDir = new File("/usr/lib/jvm");
    		if(!searchDir.exists()) break search_unix;
    		
    		File exec = searchJavaExecutable(searchDir, "");
    		if(exec != null) return exec;
    	}
    		
		return null;
	}

	private static File searchJavaExecutable(File searchDir, String suffix) {
		for(File f : searchDir.listFiles()) {
			
			if(!f.isDirectory()) continue;
			
			File bin = new File(f, "bin");
			if(!bin.exists()) continue;
			
			File exec = new File(bin, "java" + suffix);
			if(!exec.exists()) continue;
			
			File wrapper_exec = new File(bin, "javaw" + suffix);
			if(!wrapper_exec.exists()) wrapper_exec = exec;
			
			try {
				if(checkJavaVersion(exec)) return wrapper_exec;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
	
	private static final int javaVersion = 17;

	private static boolean checkJavaVersion(File executable) throws IOException {
		
		if(!executable.getAbsolutePath().contains(javaVersion + "")) return false;
		
		ProcessBuilder builder = new ProcessBuilder(executable.getAbsolutePath(), "--version");
		Process p = builder.start();
		InputStream in = p.getInputStream();
		byte[] result = new byte[8192];
		int len = in.read(result);
		String str = new String(result, 0, len);
		
		if(str.contains(javaVersion + ".")) {
			return true;
		}
		
		return false;
	}

	private static void copy(File dir, File target) throws IOException {
        if(!dir.exists()) return;
        if(dir.isDirectory()){
            if(!target.exists()) target.mkdirs();
            for(File f : dir.listFiles()){
                String name = f.getName();
                name = name.replaceAll(dir.getName(), target.getName());
                File tar = new File(target, name);
                copy(f, tar);
            }
        }
        if(dir.isFile()){
            if(!target.exists()) target.createNewFile();
            FileInputStream in = new FileInputStream(dir);
            FileOutputStream out = new FileOutputStream(target);
            int len;
            byte[] buffer = new byte[1024];
            while((len = in.read(buffer)) > 0){
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            in.close();
        }
    }
    @SuppressWarnings("unused")
	private static void copy(InputStream in, File target) throws IOException {
        if(!target.exists()) target.createNewFile();
        FileOutputStream out = new FileOutputStream(target);
        int len;
        byte[] buffer = new byte[1024];
        while((len = in.read(buffer)) > 0){
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
    }


    private static void delete(File file) throws IOException {
    	if(!file.exists()) return;
    	
        if(file.isDirectory()){
            for(File f : file.listFiles()){
                delete(f);
            }
        }
        Files.delete(file.toPath());
    }

}
