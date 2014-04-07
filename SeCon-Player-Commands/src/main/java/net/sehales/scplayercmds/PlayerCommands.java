
package net.sehales.scplayercmds;

import java.util.ArrayList;
import java.util.List;

import net.sehales.secon.SeCon;
import net.sehales.secon.command.CommandType;
import net.sehales.secon.command.MethodCommandHandler;
import net.sehales.secon.command.SeConCommand;
import net.sehales.secon.config.LanguageConfig;
import net.sehales.secon.player.SCPlayer;
import net.sehales.secon.utils.MiscUtils;
import net.sehales.secon.utils.chat.ChatUtils;
import net.sehales.secon.utils.string.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommands {
    
    private PlayerCmdCollection pc;
    private PCUtils             utils;
    private LanguageConfig      lang = SeCon.getInstance().getLang();
    private SeCon               secon;
    
    PlayerCommands(PlayerCmdCollection pc, PCUtils utils) {
        this.pc = pc;
        this.utils = utils;
        this.secon = SeCon.getInstance();
    }
    
    @MethodCommandHandler(name = "chat", description = "<darkaqua>chat as another player", usage = "<darkaqua>/chat [player] Hello!", permission = "secon.command.chat", aliases = { "chatas", "forcechat" })
    public void onChatCommand(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                return;
            }
            p.chat(StringUtils.getStringOfArray(args, 1));
        } else {
            ChatUtils.sendFormattedMessage(sender, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @MethodCommandHandler(name = "checkvisibility", description = "<darkaqua>check the invisiblity state of a player", usage = "<darkaqua>/checkvisibility [player]", permission = "secon.command.checkvisibility", aliases = { "isvisible", "isinvisible", "ishidden" })
    public void onCheckVisibilityCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                return;
            }
            
            if (pc.getInvManager().isHidden(p)) {
                ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("invisiblity.not-visible-msg").replace("<player>", p.getName()));
            } else {
                ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("invisiblity.not-invisible-msg").replace("<player>", p.getName()));
            }
        }
    }
    
    @MethodCommandHandler(name = "closeview", description = "<darkaqua>close the inventory (view) of a player", usage = "<darkaqua>/closeview [player]", permission = "secon.command.closeview", aliases = { "closeinventory" })
    public void onCloseViewCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                return;
            }
            p.closeInventory();
        } else {
            ChatUtils.sendFormattedMessage(sender, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @MethodCommandHandler(name = "enchantmenttable", description = "<darkaqua>open an enchantmenttable for you or for another player", usage = "<darkaqua>/enchantmenttable [player]", additionalPerms = "other:secon.command.enchantmenttable.other", permission = "secon.command.enchantmenttable", aliases = { "et", "etable", "enchanttable" })
    public void onEnchantmentTableCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                    return;
                }
                p.openEnchanting(null, true);
            }
            
        } else if (sender instanceof Player) {
            ((Player) sender).getPlayer().openEnchanting(null, true);
        }
    }
    
    @MethodCommandHandler(name = "enderchest", description = "<darkaqua>open your enderchest or the enderchest of another player", usage = "<darkaqua>/enderchest [player]", additionalPerms = "other:secon.command.enderchest.other", permission = "secon.command.enderchest", aliases = { "ec" })
    public void onEnderchestCmd(Player sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                    return;
                }
                sender.openInventory(p.getEnderChest());
            }
            
        } else {
            sender.openInventory(sender.getEnderChest());
        }
    }
    
    @MethodCommandHandler(name = "feed", description = "<darkaqua>feed yourself or another player", usage = "<darkaqua>/feed [player]", additionalPerms = "other:secon.command.feed.other", permission = "secon.command.feed")
    public void onFeedCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                    return;
                }
                p.setFoodLevel(20);
                ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("feed.fed-msg").replace("<sender>", sender.getName()));
                ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("feed.sender-fed-msg").replace("<player>", p.getName()));
            }
        } else if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            p.setFoodLevel(20);
            ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("feed.fed-msg").replace("<sender>", sender.getName()));
        }
    }
    
    @MethodCommandHandler(name = "flymode", description = "<darkaqua>enable or disable flymode for you or another player", usage = "<darkaqua>/flymode [player]", additionalPerms = "other:secon.command.flymode.other", permission = "secon.command.flymode", aliases = { "fly", "togglefly" })
    public void onFlymodeCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                    return;
                }
                utils.changeFlyMode(sender, p);
            }
        } else if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            utils.changeFlyMode(p, p);
        }
    }
    
    @MethodCommandHandler(name = "flyspeed", description = "<darkaqua>modify your flyspeed or the flyspeed of another player", usage = "<darkaqua>/flyspeed [player] 10", additionalPerms = "other:secon.command.flyspeed.other", permission = "secon.command.flyspeed", aliases = { "fspeed" })
    public void onFlySpeedCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (args.length > 1) {
                if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p == null) {
                        ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                        return;
                    }
                    float f = utils.parseSpeedValue(args[1]);
                    p.setFlySpeed(f);
                    
                    ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("flyspeed.changed").replace("<player>", p.getName()).replace("<value>", args[1]));
                }
            } else if (sender instanceof Player) {
                Player p = ((Player) sender).getPlayer();
                float f = utils.parseSpeedValue(args[0]);
                p.setFlySpeed(f);
                
                ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("flyspeed.changed").replace("<player>", p.getName()).replace("<value>", args[0]));
            }
        } else {
            ChatUtils.sendFormattedMessage(sender, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @MethodCommandHandler(name = "gamemode", description = "<darkaqua>change the gamemode of yourself or another player;<darkaqua>/gamemode [player] [survival|creative|adventure|0|1|2]", usage = "<darkaqua>/gamemode [survival|creative|...]", additionalPerms = "other:secon.command.gamemode.other", permission = "secon.command.gamemode", aliases = { "gm" })
    public void onGameModeCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            Player p;
            String value;
            if (args.length > 1) {
                if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                    p = Bukkit.getPlayer(args[0]);
                    if (p == null) {
                        ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                        return;
                    }
                    value = args[1];
                    try {
                        GameMode mode = utils.parseGameMode(value);
                        utils.setGameMode(mode, sender, p);
                    } catch (IllegalArgumentException e) {
                        ChatUtils.sendFormattedMessage(sender, lang.NOT_ENOUGH_ARGUMENTS);
                    }
                }
            } else if (sender instanceof Player) {
                p = ((Player) sender).getPlayer();
                value = args[0];
                try {
                    GameMode mode = utils.parseGameMode(value);
                    utils.setGameMode(mode, p, p);
                } catch (IllegalArgumentException e) {
                    ChatUtils.sendFormattedMessage(sender, lang.NOT_ENOUGH_ARGUMENTS);
                }
            }
        } else if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            switch (p.getGameMode()) {
                case SURVIVAL: {
                    utils.setGameMode(GameMode.CREATIVE, p, p);
                    break;
                }
                default: {
                    utils.setGameMode(GameMode.SURVIVAL, p, p);
                    break;
                }
            }
        }
    }
    
    @MethodCommandHandler(name = "heal", description = "<darkaqua>heal yourself or another player", usage = "<darkaqua>/heal [player]", additionalPerms = "other:secon.command.heal.other", permission = "secon.command.heal")
    public void onHealCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                    return;
                }
                p.setHealth(p.getMaxHealth());
                ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("heal.healed-msg").replace("<sender>", sender.getName()));
                ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("heal.sender-healed-msg").replace("<player>", p.getName()));
            }
        } else if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            p.setHealth(p.getMaxHealth());
            ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("heal.healed-msg").replace("<sender>", sender.getName()));
        }
    }
    
    @MethodCommandHandler(name = "hide", description = "<darkaqua>make yourself or another player invisible", usage = "<darkaqua>/hide [player]", permission = "secon.command.hide", additionalPerms = "other:secon.command.hide.other")
    public void onHideCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                    return;
                }
                
                if (pc.getInvManager().hide(p)) {
                    ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("invisiblity.invisible-msg").replace("<sender>", sender.getName()));
                    ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("invisiblity.sender-invisible-msg").replace("<player>", sender.getName()));
                } else {
                    ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("invisiblity.not-visible-msg").replace("<player>", p.getName()));
                }
            }
        } else if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            if (pc.getInvManager().hide(p)) {
                ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("invisiblity.invisible-msg").replace("<sender>", sender.getName()));
            } else {
                ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("invisiblity.not-visible-msg").replace("<player>", p.getName()));
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @MethodCommandHandler(name = "ignore", description = "<darkaqua>ignore all messages by another player", usage = "<darkaqua>/ignore [player]", permission = "secon.command.ignore", type = CommandType.PLAYER, additionalPerms = "exempt:secon.command.ignore.exempt")
    public void onIgnoreCmd(Player player, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                ChatUtils.sendFormattedMessage(player, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                return;
            }
            
            if (MiscUtils.hasPermission(p, cmd.getPermission("exempt"), false)) {
                ChatUtils.sendFormattedMessage(player, pc.getLanguageNode("ignore.no-permission").replace("<player>", p.getName()));
                return;
            }
            SCPlayer scp = secon.getPlayerManager().getPlayer(p.getName());
            
            List<String> ignoredByPlayers;
            Object obj = null;
            obj = scp.getTransientValue("ignoredByPlayers");
            
            if (obj != null && obj instanceof List) {
                ignoredByPlayers = (List<String>) obj;
            } else {
                ignoredByPlayers = new ArrayList<String>();
            }
            
            if (!ignoredByPlayers.contains(player.getName())) {
                ignoredByPlayers.add(player.getName());
                ChatUtils.sendFormattedMessage(player, pc.getLanguageNode("ignore.ignore-msg").replace("<player>", p.getName()));
            } else {
                ChatUtils.sendFormattedMessage(player, pc.getLanguageNode("ignore.already-ignored").replace("<player>", p.getName()));
            }
            
            scp.putTransientData("ignoredByPlayers", ignoredByPlayers);
        } else {
            ChatUtils.sendFormattedMessage(player, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @MethodCommandHandler(name = "invisibility", description = "<darkaqua>make yourself or another player invisible", usage = "<darkaqua>/invisibility [player]", aliases = { "inv", "vanish" }, permission = "secon.command.invisibility", additionalPerms = "other:secon.command.invisibility.other")
    public void onInvisibilityCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                    return;
                }
                
                if (pc.getInvManager().toggleInvisibility(p)) {
                    ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("invisiblity.invisible-msg").replace("<sender>", sender.getName()));
                    ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("invisiblity.sender-invisible-msg").replace("<player>", sender.getName()));
                } else {
                    ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("invisiblity.visible-msg").replace("<sender>", sender.getName()));
                    ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("invisiblity.sender-visible-msg").replace("<player>", sender.getName()));
                }
            }
        } else if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            if (pc.getInvManager().toggleInvisibility(p)) {
                ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("invisiblity.invisible-msg").replace("<sender>", sender.getName()));
            } else {
                ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("invisiblity.visible-msg").replace("<sender>", sender.getName()));
            }
        }
    }
    
    @MethodCommandHandler(name = "mute", description = "<darkaqua>mute another player", usage = "<darkaqua>/mute [player]", permission = "secon.command.mute")
    public void onMuteCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                return;
            }
            
            SCPlayer scp = secon.getPlayerManager().getPlayer(args[0]);
            
            if (scp.hasData("muted")) {
                ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("mute.already-muted").replace("<player>", p.getName()));
            } else {
                scp.putData("muted", "true");
                ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("mute.sender-muted-msg").replace("<player>", p.getName()));
                ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("mute.muted-msg").replace("<sender>", sender.getName()));
            }
        }
    }
    
    @MethodCommandHandler(name = "nick", description = "<darkaqua>change your name or the name of another player", usage = "<darkaqua>/nick [player] newName", additionalPerms = "other:secon.command.nick.other", permission = "secon.command.nick", aliases = { "playername" })
    public void onNickCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (args.length > 1) {
                if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p == null) {
                        ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                        return;
                    }
                    String name = ChatUtils.formatMessage(args[1]);
                    if (name.length() > 16) {
                        ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("nick.too-long"));
                        return;
                    }
                    p.setDisplayName(name);
                    p.setPlayerListName(name);
                    
                    SCPlayer scp = secon.getPlayerManager().getPlayer(p.getName());
                    scp.putData("listname", name);
                    scp.putData("displayname", name);
                    
                    ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("nick.sender-changed-msg").replace("<player>", p.getName()).replace("<nick>", p.getDisplayName()));
                    ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("nick.changed-msg").replace("<sender>", sender.getName()).replace("<nick>", p.getDisplayName()));
                }
            } else if (sender instanceof Player) {
                Player p = ((Player) sender).getPlayer();
                String name = ChatUtils.formatMessage(args[0]);
                if (name.length() > 16) {
                    ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("nick.too-long"));
                    return;
                }
                p.setDisplayName(name);
                p.setPlayerListName(name);
                
                SCPlayer scp = secon.getPlayerManager().getPlayer(p.getName());
                scp.putData("listname", name);
                scp.putData("displayname", name);
                
                ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("nick.changed-msg").replace("<sender>", p.getName()).replace("<nick>", p.getDisplayName()));
            }
        } else {
            ChatUtils.sendFormattedMessage(sender, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @MethodCommandHandler(name = "resetnick", description = "<darkaqua>reset your name or the name of another player", usage = "<darkaqua>/resetnick [player]", additionalPerms = "other:secon.command.resetnick.other", permission = "secon.command.resetnick", aliases = { "resetname" })
    public void onResetNickCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                    return;
                }
                String name = p.getName();
                p.setDisplayName(name);
                p.setPlayerListName(name);
                
                ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("nick.sender-reset-msg").replace("<player>", p.getName()));
                ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("nick.reset-msg").replace("<sender>", sender.getName()));
            }
        } else if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            String name = p.getName();
            p.setDisplayName(name);
            p.setPlayerListName(name);
            
            ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("nick.reset-msg").replace("<sender>", p.getName()).replace("<nick>", p.getDisplayName()));
        }
    }
    
    @MethodCommandHandler(name = "show", description = "<darkaqua>make yourself or another player visible", usage = "<darkaqua>/show [player]", permission = "secon.command.show", additionalPerms = "other:secon.command.show.other")
    public void onShowCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                    return;
                }
                
                if (pc.getInvManager().hide(p)) {
                    ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("invisiblity.visible-msg").replace("<sender>", sender.getName()));
                    ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("invisiblity.sender-visible-msg").replace("<player>", sender.getName()));
                } else {
                    ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("invisiblity.not-invisible-msg").replace("<player>", p.getName()));
                }
            }
        } else if (sender instanceof Player) {
            Player p = ((Player) sender).getPlayer();
            if (pc.getInvManager().hide(p)) {
                ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("invisiblity.visible-msg").replace("<sender>", sender.getName()));
            } else {
                ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("invisiblity.not-invisible-msg").replace("<player>", p.getName()));
            }
        }
    }
    
    @MethodCommandHandler(name = "slay", description = "<darkaqua>kill another player or yourself", usage = "<darkaqua>/slay [player]", permission = "secon.command.slay", additionalPerms = "other:secon.command.slay.other", aliases = { "kill" })
    public void onSlayCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                    return;
                }
                p.damage(Integer.MAX_VALUE);
                ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("slay.player-killed-msg").replace("<player>", p.getName()));
            }
            
        } else if (sender instanceof Player) {
            ((Player) sender).getPlayer().damage(Integer.MAX_VALUE);
        }
    }
    
    // TODO:
    @MethodCommandHandler(name = "sudo", description = "<darkaqua>execute a command as another player or as console;", usage = "<darkaqua>/sudo [-u] [player] say Hello!", additionalPerms = "console:secon.command.sudo.console,user:secon.command.sudo.user", permission = "secon.command.sudo", aliases = { "executeas", "exec", "runas", "force" })
    public void onSudoCommand(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("-u") && MiscUtils.hasPermission(sender, cmd.getPermission("user"), true)) {
                if (args.length > 2) {
                    Player p = Bukkit.getPlayer(args[1]);
                    if (p == null) {
                        ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[1]));
                        return;
                    }
                    p.performCommand(StringUtils.getStringOfArray(args, 2));
                    
                } else {
                    ChatUtils.sendFormattedMessage(sender, lang.NOT_ENOUGH_ARGUMENTS);
                }
            } else if (MiscUtils.hasPermission(sender, cmd.getPermission("console"), true)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.getStringOfArray(args, 0));
            }
        } else {
            ChatUtils.sendFormattedMessage(sender, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @SuppressWarnings("unchecked")
    @MethodCommandHandler(name = "unignore", description = "<darkaqua>no longer ignore all messages by another player", usage = "<darkaqua>/unignore [player]", permission = "secon.command.unignore", type = CommandType.PLAYER)
    public void onUnIgnoreCmd(Player player, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                ChatUtils.sendFormattedMessage(player, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                return;
            }
            
            SCPlayer scp = secon.getPlayerManager().getPlayer(p.getName());
            
            List<String> ignoredByPlayers;
            Object obj = null;
            obj = scp.getTransientValue("ignoredByPlayers");
            
            if (obj != null && obj instanceof List) {
                ignoredByPlayers = (List<String>) obj;
            } else {
                ignoredByPlayers = new ArrayList<String>();
            }
            
            if (ignoredByPlayers.contains(player.getName())) {
                ignoredByPlayers.remove(player.getName());
                ChatUtils.sendFormattedMessage(player, pc.getLanguageNode("ignore.ignore-removed-msg").replace("<player>", p.getName()));
            } else {
                ChatUtils.sendFormattedMessage(player, pc.getLanguageNode("ignore.not-ignoring").replace("<player>", p.getName()));
            }
            
            scp.putTransientData("ignoredByPlayers", ignoredByPlayers);
        } else {
            ChatUtils.sendFormattedMessage(player, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @MethodCommandHandler(name = "unmute", description = "<darkaqua>unmute another player", usage = "<darkaqua>/unmute [player]", permission = "secon.command.unmute")
    public void onUnmuteCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {
                ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                return;
            }
            
            SCPlayer scp = secon.getPlayerManager().getPlayer(args[0]);
            
            if (scp.hasData("muted")) {
                scp.removeData("muted");
                ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("mute.sender-unmuted-msg").replace("<player>", p.getName()));
                ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("mute.unmuted-msg").replace("<sender>", sender.getName()));
            } else {
                ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("mute.not-muted").replace("<player>", p.getName()));
            }
        }
    }
    
    // @MethodCommandHandler(name = "virtualchest", help =
    // "<darkaqua>open your virtual double chest or the virtual double chest of another player;<darkaqua>/virtualchest [player]",
    // aliases = "vchest", additionalPerms =
    // "other:secon.command.virtualchest.other", permission =
    // "secon.command.virtualchest", type = CommandType.PLAYER)
    // public void onVirtualChestCmd(Player player, SeConCommand cmd, String[]
    // args) {
    // if (args.length > 0) {
    // if (MiscUtils.hasPermission(player, cmd.getPermission("other"), true)) {
    // String name = args[0];
    // if (name.length() < 16) {
    // Inventory inv = pc.getVirtualChestHandler().getVirtualChest(name);
    // player.openInventory(inv);
    // pc.getVirtualChestHandler().addOpenInv(inv, name);
    // }
    // }
    // } else {
    // player.openInventory(pc.getVirtualChestHandler().getVirtualChest(player.getName()));
    // }
    // }
    
    @MethodCommandHandler(name = "walkspeed", description = "<darkaqua>modify your walkspeed or the walkspeed of another player", usage = "<darkaqua>/walkspeed [player] 10", additionalPerms = "other:secon.command.walkspeed.other", permission = "secon.command.walkspeed", aliases = { "wspeed" })
    public void onWalkSpeedCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (args.length > 1) {
                if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p == null) {
                        ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                        return;
                    }
                    float f = utils.parseSpeedValue(args[1]);
                    p.setWalkSpeed(f);
                    
                    ChatUtils.sendFormattedMessage(sender, pc.getLanguageNode("walkspeed.changed").replace("<player>", p.getName()).replace("<value>", args[1]));
                }
            } else if (sender instanceof Player) {
                Player p = ((Player) sender).getPlayer();
                float f = utils.parseSpeedValue(args[0]);
                p.setWalkSpeed(f);
                
                ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("walkspeed.changed").replace("<player>", p.getName()).replace("<value>", args[0]));
            }
        } else {
            ChatUtils.sendFormattedMessage(sender, lang.NOT_ENOUGH_ARGUMENTS);
        }
    }
    
    @MethodCommandHandler(name = "workbench", description = "<darkaqua>open a workbench for you or another player", usage = "<darkaqua>/workbench [player]", additionalPerms = "other:secon.command.workbench.other", permission = "secon.command.workbench", aliases = { "wb", "openworkbench" })
    public void onWorkbenchCmd(CommandSender sender, SeConCommand cmd, String[] args) {
        if (args.length > 0) {
            if (MiscUtils.hasPermission(sender, cmd.getPermission("other"), true)) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    ChatUtils.sendFormattedMessage(sender, lang.PLAYER_NOT_FOUND.replace("<player>", args[0]));
                    return;
                }
                p.openWorkbench(null, true);
            }
            
        } else if (sender instanceof Player) {
            ((Player) sender).getPlayer().openWorkbench(null, true);
        }
    }
}
