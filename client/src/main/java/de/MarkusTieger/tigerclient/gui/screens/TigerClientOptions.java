package de.MarkusTieger.tigerclient.gui.screens;

import de.MarkusTieger.common.gui.screens.options.OptionsScreen;
import de.MarkusTieger.tigerclient.gui.screens.options.ScreenAction;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class TigerClientOptions extends OptionsScreen {

	public TigerClientOptions(Screen parent) {

		super(new TranslatableComponent("screen.options.tigerclient"), parent);
		/*
		 * addOption( new CustomAction("button.option.tigerclient.login." +
		 * SecurityConstants.auth.isLoggedIn(), new Consumer<Button>() {
		 * 
		 * @Override public void accept(Button t) { System.out.println("Login!");
		 * login(t); } }));
		 */
		addOption(new ScreenAction("screen.option.mods", new ModsScreen(this)));
		// addOption(new ScreenAction("screen.option.certs", new CertificatesScreen(this)));
		// addOption(new CustomAction(isTACInstalled() ? "screen.option.uninstall_tac" :
		// "screen.option.install_tac", (btn) -> {

		// })); Translations are not registred. Do it later.

		// addOption(new ScreenAction("screen.option.plugins", new
		// PluginsScreen(this)));

		/*
		 * File plugins = new File(Client.getInstance().getDataDirectory(), "plugins");
		 * if (!plugins.exists()) { plugins.mkdir(); }
		 * 
		 * addOption(new ScreenAction("screen.option.plugins", new PluginsScreen(this,
		 * Client.getInstance().getInstanceRegistry().load(IPluginManager.class),
		 * plugins))).setEnabled(false);
		 */
	}

	/*
	 * public static void login(Button btn) { if
	 * (SecurityConstants.auth.isLoggedIn()) { SecurityConstants.auth.logout(); }
	 * else { SecurityConstants.auth.login(); } }
	 */

}
