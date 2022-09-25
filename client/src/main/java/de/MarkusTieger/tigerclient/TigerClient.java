package de.MarkusTieger.tigerclient;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.MarkusTieger.VersionPrinter;
import de.MarkusTieger.common.Client;
import de.MarkusTieger.common.ad.IAdManager;
import de.MarkusTieger.common.auth.IAuthicationService;
import de.MarkusTieger.common.certificates.ICertificateManager;
import de.MarkusTieger.common.compatiblity.ClientCompatiblityExecutor;
import de.MarkusTieger.common.config.IConfiguration;
import de.MarkusTieger.common.cosmetics.ICosmeticManager;
import de.MarkusTieger.common.cosmetics.ICosmeticRegistry;
import de.MarkusTieger.common.events.IEventManager;
import de.MarkusTieger.common.instance.IInstanceRegistry;
import de.MarkusTieger.common.language.ILanguageRegistry;
import de.MarkusTieger.common.logger.ILogger;
import de.MarkusTieger.common.logger.LoggingCategory;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.common.modules.IModuleRegistry;
import de.MarkusTieger.common.natives.INativesManager;
import de.MarkusTieger.common.patch.IPatchManager;
import de.MarkusTieger.common.plugins.IPluginInfo;
import de.MarkusTieger.common.plugins.IPluginManager;
import de.MarkusTieger.common.services.IServiceRegistry;
import de.MarkusTieger.common.tac.bridge.ITACBridge;
import de.MarkusTieger.common.utils.CalculatableScreenPosition;
import de.MarkusTieger.common.utils.IDraggable;
import de.MarkusTieger.common.utils.IHighspeedTick;
import de.MarkusTieger.common.utils.IKeyable;
import de.MarkusTieger.common.utils.IMultiDrag;
import de.MarkusTieger.common.utils.ITickable;
import de.MarkusTieger.common.utils.animation.IAnimationRegistry;
import de.MarkusTieger.tac.bridge.exceptions.TACAlreadyEnabledException;
import de.MarkusTieger.tac.bridge.exceptions.TACIOException;
import de.MarkusTieger.tac.bridge.exceptions.TACMultiClientException;
import de.MarkusTieger.tac.bridge.exceptions.TACNotAktiveException;
import de.MarkusTieger.tac.bridge.uninstall.UninstalledTACBridge;
import de.MarkusTieger.tac.manipulators.ConnectManipulator;
import de.MarkusTieger.tac.manipulators.ServerManipulator;
import de.MarkusTieger.tigerclient.api.optifine.OptiFineAPI;
import de.MarkusTieger.tigerclient.auth.DummyAuthicationService;
import de.MarkusTieger.tigerclient.certificates.CertificateManager;
import de.MarkusTieger.tigerclient.config.Configuration;
import de.MarkusTieger.tigerclient.cosmetics.CosmeticManager;
// import de.MarkusTieger.tigerclient.cosmetics.CosmeticManager;
import de.MarkusTieger.tigerclient.events.EventManager;
import de.MarkusTieger.tigerclient.events.impl.client.PostStartEvent;
import de.MarkusTieger.tigerclient.gui.screens.manipulators.MenuManipulator;
import de.MarkusTieger.tigerclient.gui.screens.manipulators.PauseManipulator;
import de.MarkusTieger.tigerclient.instance.InstanceRegistry;
import de.MarkusTieger.tigerclient.language.DefaultLanguage;
import de.MarkusTieger.tigerclient.language.LanguageRegistry;
import de.MarkusTieger.tigerclient.listeners.ScreenListener;
import de.MarkusTieger.tigerclient.logger.DefaultLogger;
import de.MarkusTieger.tigerclient.modules.ModuleRegistry;
import de.MarkusTieger.tigerclient.modules.impl.ArmorStatus;
import de.MarkusTieger.tigerclient.modules.impl.ArrowStatus;
import de.MarkusTieger.tigerclient.modules.impl.CPS;
import de.MarkusTieger.tigerclient.modules.impl.FPS;
import de.MarkusTieger.tigerclient.modules.impl.FourthPerspective;
import de.MarkusTieger.tigerclient.modules.impl.ResourceMonitor;
import de.MarkusTieger.tigerclient.modules.impl.SoupStatus;
import de.MarkusTieger.tigerclient.natives.NativesManager;
import de.MarkusTieger.tigerclient.patch.PatchManager;
import de.MarkusTieger.tigerclient.resources.TigerClientRepo;
import de.MarkusTieger.tigerclient.services.Service;
import de.MarkusTieger.tigerclient.services.ServiceRegistry;
import de.MarkusTieger.tigerclient.services.impl.Debugger;
import de.MarkusTieger.tigerclient.services.impl.ModuleTimer;
import de.MarkusTieger.tigerclient.services.impl.Stopper;
import de.MarkusTieger.tigerclient.utils.animation.AnimationRegistry;
import de.MarkusTieger.tigerclient.utils.animation.AnimationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;

public class TigerClient extends Client
		implements IModule<Throwable>, ITickable<Throwable>, IKeyable<Throwable>, IHighspeedTick<Throwable> {

	private static final Logger APACHE_LOGGER = LogManager.getLogger();

	public static final String versionType = "Beta";
	public static final String clVersion = "2.10.0";
	public static final String build = "0023";
	public static final String version_format = "%s-%s";
	private static final String version = String.format(version_format, clVersion, build);
	private static final int versionNumber = 2;
	private static final String currentAccept = "1";
	private final ClientCompatiblityExecutor executor;

	private final IInstanceRegistry instance = new InstanceRegistry();

	private final File directory;

	private boolean accepted = false;

	public static final boolean DEV;

	static {
		DEV = System.getProperty("development", "false").equalsIgnoreCase("true");

		if (DEV) {
			VersionPrinter.__internal__();
		}
	}

	@SuppressWarnings("resource")
	public TigerClient(ClientCompatiblityExecutor executor) {
		this.executor = executor;

		directory = new File(Minecraft.getInstance().gameDirectory, "TigerClient");
		if (!directory.exists()) {
			directory.mkdir();
		}

		ILogger LOGGER = new DefaultLogger(APACHE_LOGGER, new File(directory, "logs"));

		instance.register(ILogger.class, LOGGER);

	}

	private UUID uuid;

	public void updateAccount() {
		uuid = getMCUser().getGameProfile().getId();

		ILogger LOGGER = getLogger();

		ICosmeticManager cosmetic = getInstanceRegistry().load(ICosmeticManager.class);

		try {
			if (cosmetic != null)
				cosmetic.initPlayer(getMCUser().getGameProfile());
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.warn(LoggingCategory.MAIN, "Can't initialize Player's Cosmetics.");
		}
	}

	@Override
	public String getModId() {
		return getCompatiblityExecutor().getModId();
	}

	@Override
	public String getName() {
		return TigerClient.class.getSimpleName();
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getCleanVersion() {
		return clVersion;
	}

	@Override
	public String getVersionType() {
		return versionType;
	}

	@Override
	public String getBuild() {
		return build;
	}

	@Override
	public int getPluginVersion() {
		return versionNumber;
	}

	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public void start(BiConsumer<String, Float> progress) {

		float one = 1F / 24F;

		final ILogger LOGGER = getLogger();

		LOGGER.info(LoggingCategory.MAIN, "Starting " + getName() + "...");

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Initializing Instances..."), one);

		IInstanceRegistry instance = getInstanceRegistry();

		IConfiguration config = new Configuration(new File(directory, "config.json"));

		ICosmeticManager cosmetic = null;
		try {
			cosmetic = new CosmeticManager();
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.warn(LoggingCategory.MAIN, "Cosmetic-Manager can't loaded.");
		}
		instance.register(ICosmeticManager.class, cosmetic);

		INativesManager natives = new NativesManager();
		IEventManager events = new EventManager();
		IAdManager ad = null;

		instance.register(IConfiguration.class, config);
		instance.register(ICosmeticRegistry.class, cosmetic == null ? null : cosmetic.getRegistery());
		instance.register(IModuleRegistry.class, new ModuleRegistry());
		instance.register(INativesManager.class, natives);
		instance.register(IAnimationRegistry.class, new AnimationRegistry());
		instance.register(IServiceRegistry.class, new ServiceRegistry());
		instance.register(IAdManager.class, ad);
		instance.register(IEventManager.class, new EventManager());
		instance.register(IAuthicationService.class, new DummyAuthicationService());
		instance.register(ILanguageRegistry.class, new LanguageRegistry());
		instance.register(IPatchManager.class, new PatchManager());
		instance.register(ICertificateManager.class, new CertificateManager());

		instance.register(TigerClientRepo.class, new TigerClientRepo(instance.load(ILanguageRegistry.class)));

		progress.accept(null, Float.NaN);

		IPluginManager plugins = instance.load(IPluginManager.class);

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Loading Configuration..."), one * 2F);

		config.load();

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Loading Language..."), one * 3F);

		instance.load(ILanguageRegistry.class).register(new DefaultLanguage("en_us", "US", "English"));

		progress.accept(null, one * 4F);
		accepted = config.getOrDefault("accepted", "unknown").equalsIgnoreCase(currentAccept);

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Loading Natives..."), one * 5F);

		natives.load();

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Injecting Cosmetics..."), one * 6F);

		try {
			if (cosmetic != null)
				cosmetic.inject();
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.warn(LoggingCategory.MAIN, "Cosmetics can't injected.");
		}

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Loading Animations..."), one * 7F);

		getAnimationRegistry().register("cape_tigerclient",
				AnimationUtils.aniFrames("cosmetics/capes/tigerclient/", 120));
		getAnimationRegistry().register("cape_demoneye", AnimationUtils.frame("cosmetics/capes/demoneye/demoneye"));
		getAnimationRegistry().register("elytra_demoneye", AnimationUtils.frame("cosmetics/elytras/demoneye/demoneye"));

		if (plugins == null) {
			progress.accept(null, one * 8F);
		} else {

			progress.accept(LOGGER.info(LoggingCategory.MAIN, "Loading Plugins..."), one * 8F);

			plugins.loadPluginsSilently(directory);

			float pos = 0;
			LOGGER.info(LoggingCategory.MAIN, "Enabling Plugins...");
			for (IPluginInfo info : plugins.getLoadedPlugins()) {

				pos++;
				float v = ((pos * one) / (plugins.getLoadedPlugins().size()));
				progress.accept(LOGGER.info(LoggingCategory.MAIN, "Enabling Plugin \"" + info.getId() + "\" ..."),
						(one * 8F) + v);

				boolean en = config.getOrDefault("plugins#" + info.getId() + ":" + info.getVersion(), false);
				if (en) {
					try {
						plugins.enablePlugin(info);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Loading Modules..."), one * 9F);

		getModuleRegistry().register(this);
		getModuleRegistry().register(getAnimationRegistry());
		getModuleRegistry().register(new FPS());
		getModuleRegistry().register(new CPS());
		getModuleRegistry().register(new ArmorStatus());
		getModuleRegistry().register(new ArrowStatus());
		getModuleRegistry().register(new SoupStatus());
		getModuleRegistry().register(new ResourceMonitor());
		getModuleRegistry().register(new FourthPerspective());
		// getModuleRegistry().register(new StreamOverlay());
		// modules.add(new TAC());

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Configuring Modules..."), one * 10F);

		float pos = 0F;

		for (IModule<?> mod : getModuleRegistry().toArray()) {

			pos++;
			float v = ((pos * one) / (getModuleRegistry().size()));
			progress.accept(LOGGER.info(LoggingCategory.MAIN, "Configuring Module \"" + mod.getId() + "\" ..."),
					(one * 10F) + v);

			try {
				mod.setEnabled(config.getOrDefault("modules#" + mod.getId() + "#enabled", mod.isEnabled()));
			} catch (Throwable ex) {
				Client.getInstance().getLogger().warn(LoggingCategory.MODULES,
						"Module \"" + mod.getId() + "\" throwed an Exception on Enable-Configuring. Will be ignored.",
						ex);
			}

			if (mod instanceof IDraggable<?> drag) {
				try {
					drag.position_set(CalculatableScreenPosition.fromConfiguration((d, def) -> config.getOrDefault("modules#" + mod.getId() + "#" + d, def)));
				} catch (Throwable ex) {
					Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "Draggable-Module \"" + mod.getId()
							+ "\" throwed an Exception on Screen-Position-Configuring. Will be ignored.", ex);
				}
			}

			if (mod instanceof IMultiDrag) {
				try {
					for (IDraggable<?> drag : ((IMultiDrag<?>) mod).getDraggables()) {
						try {
							drag.position_set(CalculatableScreenPosition.fromConfiguration((d, def) -> config.getOrDefault("modules#" + drag.getId() + "#" + d, def)));
						} catch (Throwable ex) {
							Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "Draggable-Module \""
									+ drag.getId()
									+ "\" throwed an Exception on Screen-Position-Configuring. Will be ignored.", ex);
						}
					}
				} catch (Throwable ex) {
					Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "MultiDrag-Module \"" + mod.getId()
							+ "\" throwed an Exception on Screen-Position-Configuring. Will be ignored.", ex);
				}
			}
		}

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Initializing TAC..."), one * 12F);

		File tac = new File(directory, "tac_target.jar");

		if (tac.exists()) {

			try {

				URL url = tac.toURL();

				injectPackage(url);

				Class<?> clazz = Class.forName("de.MarkusTieger.tac.bridge.TargetBridge");

				Object obj = clazz.newInstance();

				ITACBridge bridge = (ITACBridge) obj;

				getInstanceRegistry().register(ITACBridge.class, bridge);
				LOGGER.info(LoggingCategory.MAIN, "TAC successfully initialized.");

			} catch (Throwable e) {
				e.printStackTrace();
				getInstanceRegistry().register(ITACBridge.class, new UninstalledTACBridge());
				LOGGER.info(LoggingCategory.MAIN, "Can't Inject TAC. TAC can't loaded. Ignore...");
			}

		} else {
			getInstanceRegistry().register(ITACBridge.class, new UninstalledTACBridge());
			LOGGER.info(LoggingCategory.MAIN, "TAC not installed! Ignore...");
		}

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Initializing Services..."), one * 13F);

		Service css = new Service("Client-Stopper", new Stopper());
		Service mts = new Service("Module-Timer", new ModuleTimer());
		Service dbg = new Service("Debugger", new Debugger());

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Loading Services..."), one * 14F);

		getServiceRegistry().register(css);
		getServiceRegistry().register(mts);
		getServiceRegistry().register(dbg);
		// getServiceRegistry().register(dcs);
		// getServiceRegistry().register(ads);

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Starting Services..."), one * 15F);

		css.start();
		mts.start();
		dbg.start();

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Patching Minecraft..."), one * 16F);

		patch();

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Registring Listeners..."), one * 17F);

		ScreenListener sl = null;
		try {
			sl = new ScreenListener();
			sl.getManipulators().add(new MenuManipulator());
			sl.getManipulators().add(new PauseManipulator());
			getEventManager().register(sl);
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.warn(LoggingCategory.MAIN, "ScreenListener can't initialized.");
		}

		try {
			ServerManipulator sm = new ServerManipulator();
			synchronized (keys) {
				keys.add(sm);
			}
			if (sl != null)
				sl.getManipulators().add(sm);
			getEventManager().register(sm);
		} catch (Throwable e) {
			e.printStackTrace();
			LOGGER.warn(LoggingCategory.MAIN, "ServerManipulator can't initialized.");
		}

		getEventManager().register(new ConnectManipulator());

		progress.accept(LOGGER.info(LoggingCategory.TAC, "Enabling TAC..."), one * 18F);

		try {
			getTAC().enable();
			LOGGER.info(LoggingCategory.TAC, "Successfully Enabled TAC!");
		} catch (TACAlreadyEnabledException e) {
			LOGGER.warn(LoggingCategory.TAC, "Internal Error!", e);
		} catch (TACNotAktiveException e) {
			LOGGER.info(LoggingCategory.TAC, "TAC not ready!");
		} catch (TACIOException e) {
			LOGGER.warn(LoggingCategory.TAC, "Internal Error!", e);
		} catch (TACMultiClientException e) {
			LOGGER.info(LoggingCategory.TAC, "MultiClient Error!");
		}

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Updating Registries..."), one * 19F);

		getModuleRegistry().update();

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Updating (X509-) Certificates..."), one * 20F);

		File cert_dir = new File(directory, "certs");
		if (!cert_dir.exists())
			cert_dir.mkdirs();

		getInstanceRegistry().load(ICertificateManager.class).loadCertificates(cert_dir);

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Updating Account..."), one * 21F);

		updateAccount();

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Loading Integrations..."), one * 22F);

		loadIntegrations();

		progress.accept(LOGGER.info(LoggingCategory.RESOURCE, "Injecting Resources..."), one * 23F);

		// Nothing to inject

		progress.accept(LOGGER.info(LoggingCategory.MAIN, "Finished"), 1F);

	}

	private void injectPackage(URL url) throws Throwable {
		ClassLoader loader = TigerClient.class.getClassLoader();

		Class<?> clazz = Class.forName("de.MarkusTieger.tigerclient.loader.ClientClassLoader");

		Method m = clazz.getDeclaredMethod("addURL", URL.class);
		m.invoke(loader, url);
	}

	private void patch() {
		ILogger LOGGER = getLogger();

		IPatchManager patch = getInstanceRegistry().load(IPatchManager.class);

		Optional<Throwable> patched = patch.checkPatchable();

		if (patched == null) {
			LOGGER.info(LoggingCategory.PATCH, "Patch status is fine.");

		} else if (!patched.isEmpty()) {
			LOGGER.warn(LoggingCategory.PATCH, "Patch status is not patched.", patched.get());
		} else {
			LOGGER.warn(LoggingCategory.PATCH, "Patch status is not patched.");
		}
	}

	private void loadIntegrations() {
		ILogger LOGGER = getLogger();

		OptiFineAPI of = OptiFineAPI.create();
		if (of != null) {
			getInstanceRegistry().register(OptiFineAPI.class, of);
			LOGGER.info(LoggingCategory.MAIN, "Found OptiFine as integration.");
		}
	}

	private String getDisplayNameAsString() {
		return getName();
	}

	@Override
	public void postStart() {
		final ILogger LOGGER = getLogger();

		LOGGER.info(LoggingCategory.MAIN, "Loading Resources...");

		PackRepository repo = Minecraft.getInstance().getResourcePackRepository();

		for (Field f : PackRepository.class.getDeclaredFields()) {
			if (f.getType().equals(Set.class)) {
				try {
					f.setAccessible(true);
					@SuppressWarnings("unchecked")
					Set<RepositorySource> sources = (Set<RepositorySource>) f.get(repo);
					sources.add(getInstanceRegistry().load(TigerClientRepo.class));
					LOGGER.debug(LoggingCategory.MAIN, "Repository-Source injected!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		repo.reload();

		getEventManager().call(new PostStartEvent());
	}

	@Override
	public void stop() {
		final ILogger LOGGER = getLogger();

		LOGGER.info(LoggingCategory.MAIN, "Stopping " + getName() + "...");

		LOGGER.info(LoggingCategory.MAIN, "Saving Config...");

		IConfiguration config = getInstanceRegistry().load(IConfiguration.class);

		for (IModule<?> mod : getModuleRegistry().toArray()) {
			try {
				config.set("modules#" + mod.getId() + "#enabled", mod.isEnabled());
			} catch (Throwable e) {
				Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "Module \"" + mod.getId()
						+ "\" throwed an Exception on Enable-Saving. Will be saved as disabled.", e);
				config.set("modules#" + mod.getId() + "#enabled", false);
			}

			if (mod instanceof IDraggable<?> drag) {

				try {
					drag.position().storeConfiguration((d, v) -> config.set("modules#" + mod.getId() + "#" + d, v));
				} catch (Throwable ex) {
					Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "Draggable-Module \"" + mod.getId()
							+ "\" throwed an Exception on Screen-Position-Saving. Data loss...", ex);
					continue;
				}
			}

			if (mod instanceof IMultiDrag) {
				try {
					for (IDraggable<?> drag : ((IMultiDrag<?>) mod).getDraggables()) {
						try {
							drag.position().storeConfiguration((d, v) -> config.set("modules#" + drag.getId() + "#" + d, v));
						} catch (Throwable ex) {
							Client.getInstance().getLogger()
									.warn(LoggingCategory.MODULES,
											"Draggable-Module \"" + drag.getId()
													+ "\" throwed an Exception on Screen-Position-Saving. Data loss...",
											ex);
							continue;
						}
					}
				} catch (Throwable ex) {
					Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "MultiDrag-Module \"" + mod.getId()
							+ "\" throwed an Exception on Screen-Position-Saving. Data loss...", ex);
					continue;
				}
			}

		}

		IPluginManager plugins = getInstanceRegistry().load(IPluginManager.class);

		if(plugins != null) {
			
			for (IPluginInfo info : plugins.getLoadedPlugins()) {
				config.set("plugins#" + info.getId() + "#" + info.getVersion(), false);
			}
			for (IPluginInfo info : plugins.getEnabledPlugins()) {
				config.set("plugins#" + info.getId() + "#" + info.getVersion(), true);

			}

			plugins.getEnabledPlugins().stream().forEach(plugins::disablePlugin);
			
		}

		config.save();
	}

	@Override
	public ClientCompatiblityExecutor getCompatiblityExecutor() {
		return executor;
	}

	@Override
	public ICosmeticRegistry getCosmeticRegistry() {
		return getInstanceRegistry().load(ICosmeticRegistry.class);
	}

	@Override
	public IInstanceRegistry getInstanceRegistry() {
		return instance;
	}

	@Override
	public IModuleRegistry getModuleRegistry() {
		return getInstanceRegistry().load(IModuleRegistry.class);
	}

	@Override
	public IServiceRegistry getServiceRegistry() {
		return getInstanceRegistry().load(IServiceRegistry.class);
	}

	@Override
	public IAnimationRegistry getAnimationRegistry() {
		return getInstanceRegistry().load(IAnimationRegistry.class);
	}

	@Override
	public IEventManager getEventManager() {
		return getInstanceRegistry().load(IEventManager.class);
	}

	@Override
	public boolean hasAccepted() {
		return accepted;
	}

	@Override
	public String getAcceptURL() {
		return "https://cdn.discordapp.com/attachments/807610774210084864/845677408194920479/Nutzungsbedingungen.pdf";
	}

	@Override
	public File getDataDirectory() {
		return directory;
	}

	@Override
	public ILogger getLogger() {
		return getInstanceRegistry().load(ILogger.class);
	}

	@Override
	public void accept() {
		accepted = true;
		getInstanceRegistry().load(IConfiguration.class).set("accepted", currentAccept);
		getInstanceRegistry().load(IConfiguration.class).save();
	}

	@Override
	public ITACBridge getTAC() {
		return getInstanceRegistry().load(ITACBridge.class);
	}

	private final List<IKeyable<?>> keys = Collections.synchronizedList(new ArrayList<>());
	private final List<ITickable<?>> ticks = Collections.synchronizedList(new ArrayList<>());
	private final List<IHighspeedTick<?>> hticks = Collections.synchronizedList(new ArrayList<>());

	@Override
	public void addTickable(ITickable<?> tick) {
		synchronized (ticks) {
			ticks.add(tick);
		}
	}

	@Override
	public void removeTickable(ITickable<?> tick) {
		synchronized (ticks) {
			ticks.remove(tick);
		}
	}

	@Override
	public void addHighTickable(IHighspeedTick<?> tick) {
		synchronized (hticks) {
			hticks.add(tick);
		}
	}

	@Override
	public void removeHighTickable(IHighspeedTick<?> tick) {
		synchronized (hticks) {
			hticks.remove(tick);
		}
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public void bindIcon() {
	}

	@Override
	public Component getDescription() {
		return new TextComponent("A Minecraft Client");
	}

	@Override
	public Component getDisplayName() {
		return new TextComponent(getDisplayNameAsString());
	}

	@Override
	public String getId() {
		return getClass().getSimpleName();
	}

	@Override
	public ResourceLocation getIcon() {
		return new ResourceLocation(getModId(), "textures/gui/icon.png");
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void setEnabled(boolean bool) {
		if (!bool) {
			System.exit(0);
		}
	}

	@Override
	public void onTick() {
		synchronized (ticks) {
			for (ITickable<?> tick : ticks) {
				try {
					tick.onTick();
				} catch (Throwable ex) {
					Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "Tickable \""
							+ tick.getClass().getName() + "\" throwed an Exception on Tick-Event. Will be ignored.",
							ex);
				}
			}
		}
	}

	@Override
	public boolean onKey(int keyCode, int flag) {
		boolean cancel = false;
		synchronized (keys) {
			for (IKeyable<?> key : keys) {
				try {
					if (key.onKey(keyCode, flag))
						cancel = true;
				} catch (Throwable ex) {
					Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "Keyable \""
							+ key.getClass().getName() + "\" throwed an Exception on Key-Event. Will be ignored.", ex);
				}
			}
		}
		return cancel;
	}

	@Override
	public void onHighTick() {
		synchronized (hticks) {
			for (IHighspeedTick<?> htick : hticks) {
				try {
					htick.onHighTick();
				} catch (Throwable ex) {
					Client.getInstance().getLogger().warn(LoggingCategory.MODULES, "High-Tickable \""
							+ htick.getClass().getName() + "\" throwed an Exception on H-Tick-Event. Will be ignored.",
							ex);
				}
			}
		}
	}

	@Override
	public User getMCUser() {
		return Minecraft.getInstance().getUser();
	}

	@Override
	public boolean isDev() {
		return DEV;
	}
}
