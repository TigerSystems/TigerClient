package de.MarkusTieger.common;

import java.io.File;
import java.util.UUID;
import java.util.function.BiConsumer;

import de.MarkusTieger.annotations.NoObfuscation;
import de.MarkusTieger.common.compatiblity.ClientCompatiblityExecutor;
import de.MarkusTieger.common.cosmetics.ICosmeticRegistry;
import de.MarkusTieger.common.events.IEventManager;
import de.MarkusTieger.common.instance.IInstanceRegistry;
import de.MarkusTieger.common.logger.ILogger;
import de.MarkusTieger.common.modules.IModule;
import de.MarkusTieger.common.modules.IModuleRegistry;
import de.MarkusTieger.common.services.IServiceRegistry;
import de.MarkusTieger.common.tac.bridge.ITACBridge;
import de.MarkusTieger.common.utils.IHighspeedTick;
import de.MarkusTieger.common.utils.ITickable;
import de.MarkusTieger.common.utils.animation.IAnimationRegistry;
import de.MarkusTieger.tigerclient.TigerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;

@NoObfuscation
public abstract class Client implements IModule<Throwable> {

	private static Client instance;

	public static Client getInstance() {
		return instance;
	}

	public static Minecraft getMinecraft() {
		return Minecraft.getInstance();
	}

	public abstract String getModId();

	public abstract String getName();

	public abstract String getVersion();

	public abstract String getCleanVersion();

	public abstract String getVersionType();

	public abstract String getBuild();

	public abstract int getPluginVersion();

	public abstract void start(BiConsumer<String, Float> progress);

	public abstract void postStart();

	public abstract void stop();

	public abstract ClientCompatiblityExecutor getCompatiblityExecutor();

	public abstract ICosmeticRegistry getCosmeticRegistry();

	public abstract IInstanceRegistry getInstanceRegistry();

	public abstract IModuleRegistry getModuleRegistry();

	public abstract IServiceRegistry getServiceRegistry();

	public abstract IAnimationRegistry getAnimationRegistry();

	public abstract IEventManager getEventManager();

	public abstract boolean hasAccepted();

	public abstract String getAcceptURL();

	public abstract File getDataDirectory();

	public abstract ILogger getLogger();

	public abstract void accept();

	public abstract ITACBridge getTAC();

	public abstract void addTickable(ITickable<?> tick);

	public abstract void removeTickable(ITickable<?> tick);

	public abstract void addHighTickable(IHighspeedTick<?> tick);

	public abstract void removeHighTickable(IHighspeedTick<?> tick);

	public abstract UUID getUUID();

	public static Client newInstance(ClientCompatiblityExecutor executor) {
		return new TigerClient(executor);
	}

	public abstract User getMCUser();

	public abstract boolean isDev();
}
