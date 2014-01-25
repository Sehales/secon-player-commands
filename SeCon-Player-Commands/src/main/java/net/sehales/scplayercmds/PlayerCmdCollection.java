
package net.sehales.scplayercmds;

import net.sehales.secon.addon.Addon;

import org.bukkit.configuration.file.FileConfiguration;

public class PlayerCmdCollection extends Addon {
    
    private PCUtils             pcu;
    // private VirtualChestHandler vch;
    private InvisibilityManager invManager;
    
    void addConfigNode(String path, Object value) {
        if (!configContains(path)) {
            getConfig().set(path, value);
        }
    }
    
    boolean configContains(String path) {
        return getConfig().contains(path);
    }
    
    FileConfiguration getConf() {
        return getConfig();
    }
    
    public InvisibilityManager getInvManager() {
        return invManager;
    }
    
    // public VirtualChestHandler getVirtualChestHandler() {
    // return this.vch;
    // }
    
    private void initConfig() {
        
        addConfigNode("virtualchest-name", "<gold>Virtual Chest");
        addConfigNode("permission.remember.flymode", "secon.remember.flymode");
        addConfigNode("permission.remember.gamemode", "secon.remember.gamemode");
        addConfigNode("permission.remember.flying", "secon.remember.flying");
        addConfigNode("permission.remember.walkspeed", "secon.remember.walkspeed");
        addConfigNode("permission.remember.flyspeed", "secon.remember.flyspeed");
        addConfigNode("permission.remember.listname", "secon.remember.listname");
        addConfigNode("permission.remember.displayname", "secon.remember.displayname");
        addConfigNode("permission.remember.invisibility", "secon.remember.invisibility");
        addConfigNode("permission.invisibility.see", "secon.command.invisibility.see");
        
        saveConfig();
    }
    
    private void initLanguage() {
        addLanguageNode("flymode.enabled", "<green><sender> <gold>has <red>enabled <gold>your flymode");
        addLanguageNode("flymode.disabled", "<green><sender> <gold>has <red>disabled <gold>your flymode");
        addLanguageNode("flymode.sender-msg-disabled", "<gold>You have disable the flymode of <green><player>");
        addLanguageNode("flymode.sender-msg-enabled", "<gold>You have enable the flymode of <green><player>");
        addLanguageNode("gamemode.sender-msg-survival", "<gold>You have set the gamemode of <green><player> <gold>to <red>survival-mode");
        addLanguageNode("gamemode.sender-msg-creative", "<gold>You have set the gamemode of <green><player> <gold>to <red>creative-mode");
        addLanguageNode("gamemode.sender-msg-adventure", "<gold>You have set the gamemode of <green><player> <gold>to <red>adventure-mode");
        addLanguageNode("gamemode.survival", "<green><sender> <gold>has set your gamemode to <red>survival-mode");
        addLanguageNode("gamemode.creative", "<green><sender> <gold>has set your gamemode to <red>creative-mode");
        addLanguageNode("gamemode.adventure", "<green><sender> <gold>has set your gamemode to <red>adventure-mode");
        addLanguageNode("flyspeed.changed", "<gold>You have set the flyspeed of <green><player> <gold>to <red><value>");
        addLanguageNode("walkspeed.changed", "<gold>You have set the walkspeed of <green><player> <gold>to <red><value>");
        addLanguageNode("heal.healed-msg", "<gold>You have been healed by <green><sender>");
        addLanguageNode("heal.sender-healed-msg", "<gold>You have healed <green><player>");
        addLanguageNode("feed.fed-msg", "<gold>You have been fed by <green><sender>");
        addLanguageNode("feed.sender-fed-msg", "<gold>You have fed <green><player>");
        addLanguageNode("nick.changed-msg", "<green><sender> <gold>has changed your nick to: <green><nick>");
        addLanguageNode("nick.sender-changed-msg", "<gold>You have changed the nick of <green><player> <gold>to <green><nick>");
        addLanguageNode("nick.too-long", "<red>Your name can't be longer than 16 characters");
        addLanguageNode("nick.reset-msg", "<green><sender> <gold>has resetted your display name");
        addLanguageNode("nick.sender-reset-msg", "<gold>You have resetted the display name of <green><player>");
        addLanguageNode("virtualchest.longer-than-16", "<red>The name can't be longer than 16 characters!");
        addLanguageNode("slay.player-killed-msg", "<gold>You have killed <player>");
        addLanguageNode("mute.muted-msg", "<red>You have been muted by <green><sender>");
        addLanguageNode("mute.sender-muted-msg", "<gold>You have muted <green><player>");
        addLanguageNode("mute.unmuted-msg", "<gold>You have been unmuted by <green><sender>");
        addLanguageNode("mute.sender-unmuted-msg", "<gold>You have unmuted <green><player>");
        addLanguageNode("mute.already-muted", "<green><player> <gold>is already muted");
        addLanguageNode("mute.muted-info", "<red>You are muted!");
        addLanguageNode("mute.not-muted", "<green><player> <gold>is not muted");
        addLanguageNode("invisiblity.sender-visible-msg", "<gold>You have made <green><player> <gold>visible");
        addLanguageNode("invisiblity.visible-msg", "<green><sender> <gold>has made you visible");
        addLanguageNode("invisiblity.sender-invisible-msg", "<gold>You have made <green><player> <gold>invisible");
        addLanguageNode("invisiblity.invisible-msg", "<green><sender> <gold>has made you invisible");
        addLanguageNode("invisiblity.not-invisible-msg", "<green><player> <gold>is not invisible");
        addLanguageNode("invisiblity.not-visible-msg", "<green><player> <gold>is not visible");
        addLanguageNode("ignore.ignore-msg", "<gold>You are now ignoring <green><player>");
        addLanguageNode("ignore.already-ignored", "<red>You already ignore <green><player>");
        addLanguageNode("ignore.ignore-removed-msg", "<gold>You are no longer ignoring <green><player>");
        addLanguageNode("ignore.no-permission", "<red>You can't ignore <green><player>");
        addLanguageNode("ignore.not-ignoring", "<red>You don't ignore <green><player>");
    }
    
    @Override
    protected void onDisable() {
        
    }
    
    @Override
    protected boolean onEnable() {
        // SeCon secon = SeCon.getInstance();
        // if (!(secon.getSQLDB() != null)) {
        // secon.log().warning("Player-Cmd-Collection",
        // "(My)SQL isn't available, enable mysql and try again");
        // return false;
        // } else {
        initConfig();
        initLanguage();
        this.pcu = new PCUtils(this);
        // this.vch = new VirtualChestHandler(this);
        // try {
        // this.vch.init();
        // } catch (DatabaseException e) {
        // SeCon.getAPI().getLogger().warning("Player-Cmd-Collection",
        // "DatabaseException occured: " + e.getMessage());
        // e.printStackTrace();
        // }
        invManager = new InvisibilityManager(this);
        
        registerCommandsFromObject(new PlayerCommands(this, this.pcu));
        registerListener(new PlayerListener(this));
        registerListener(new EntityListener(this));
        return true;
        // }
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
