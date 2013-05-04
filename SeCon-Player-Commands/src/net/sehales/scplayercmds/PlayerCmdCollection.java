package net.sehales.scplayercmds;

import net.sehales.secon.SeCon;
import net.sehales.secon.addon.SeConAddon;
import net.sehales.secon.annotations.SeConAddonHandler;
import net.sehales.secon.exception.DatabaseException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

@SeConAddonHandler()
public class PlayerCmdCollection extends SeConAddon {

	private PCUtils             pcu;
	private VirtualChestHandler vch;

	void addConfigNode(String path, Object value) {
		if (!configContains(path))
			getConfig().set(path, value);
	}

	boolean configContains(String path) {
		return getConfig().contains(path);
	}

	FileConfiguration getConf() {
		return getConfig();
	}

	public VirtualChestHandler getVirtualChestHandler() {
		return this.vch;
	}

	private void initConfig() {
		addConfigNode("virtualchest-name", "<gold>Virtual Chest");
		saveConfig();
	}

	private void initLanguage() {
		addLanguageInfoNode("flymode.enabled", "<green><sender> <gold>has <red>enabled <gold>your flymode");
		addLanguageInfoNode("flymode.disabled", "<green><sender> <gold>has <red>disabled <gold>your flymode");
		addLanguageInfoNode("flymode.sender-msg-disabled", "<gold>You have disable the flymode of <green><player>");
		addLanguageInfoNode("flymode.sender-msg-enabled", "<gold>You have enable the flymode of <green><player>");
		addLanguageInfoNode("gamemode.sender-msg-survival", "<gold>You have set the gamemode of <green><player> <gold>to <red>survival-mode");
		addLanguageInfoNode("gamemode.sender-msg-creative", "<gold>You have set the gamemode of <green><player> <gold>to <red>creative-mode");
		addLanguageInfoNode("gamemode.sender-msg-adventure", "<gold>You have set the gamemode of <green><player> <gold>to <red>adventure-mode");
		addLanguageInfoNode("gamemode.survival", "<green><sender> <gold>has set your gamemode to <red>survival-mode");
		addLanguageInfoNode("gamemode.creative", "<green><sender> <gold>has set your gamemode to <red>creative-mode");
		addLanguageInfoNode("gamemode.adventure", "<green><sender> <gold>has set your gamemode to <red>adventure-mode");
		addLanguageInfoNode("flyspeed.changed", "<gold>You have set the flyspeed of <green><player> <gold>to <red><value>");
		addLanguageInfoNode("walkspeed.changed", "<gold>You have set the walkspeed of <green><player> <gold>to <red><value>");
		addLanguageInfoNode("heal.healed-msg", "<gold>You have been healed by <green><sender>");
		addLanguageInfoNode("heal.sender-healed-msg", "<gold>You have healed <green><player>");
		addLanguageInfoNode("feed.feeded-msg", "<gold>You have been fed by <green><sender>");
		addLanguageInfoNode("feed.sender-feeded-msg", "<gold>You have fed <green><player>");
		addLanguageInfoNode("nick.changed-msg", "<green><sender> <gold>has changed your nick to: <green><nick>");
		addLanguageInfoNode("nick.sender-changed-msg", "<gold>You have changed the nick of <green><player> <gold>to <green><nick>");
		addLanguageInfoNode("nick.too-long", "<red>Your name can't be longer than 16 characters");
		addLanguageInfoNode("nick.reset-msg", "<green><sender> <gold>has resetted your display name");
		addLanguageInfoNode("nick.sender-reset-msg", "<gold>You have resetted the display name of <green><player>");
		addLanguageInfoNode("virtualchest.longer-than-16", "<red>The name can't be longer than 16 characters!");
		addLanguageInfoNode("slay.player-killed-msg", "<gold>You have killed <player>");
	}

	@Override
	protected void onDisable() {

	}

	@Override
	protected boolean onEnable(Plugin secon) {
		if (!SeCon.getAPI().isOnlineMySQL()) {
			SeCon.getAPI().getLogger().warning("Player-Cmd-Collection", "MySQL isn't available, enable mysql and try again");
			return false;
		} else {
			initConfig();
			initLanguage();
			this.pcu = new PCUtils(this);
			this.vch = new VirtualChestHandler(this);
			try {
				this.vch.init();
			} catch (DatabaseException e) {
				SeCon.getAPI().getLogger().warning("Player-Cmd-Collection", "DatabaseException occured: " + e.getMessage());
				e.printStackTrace();
			}
			registerCommands(new PlayerCommands(this, this.pcu));
			registerListener(new PlayerListener(this, this.pcu));
			return true;
		}
	}

	void reloadConf() {
		reloadConfig();
	}

	void saveConf() {
		saveConfig();
	}

	void setConfigNode(String path, Object value) {
		getConfig().set(path, value);
	}

}