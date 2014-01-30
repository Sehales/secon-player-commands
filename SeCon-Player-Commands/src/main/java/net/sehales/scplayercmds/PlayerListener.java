
package net.sehales.scplayercmds;

import java.util.Arrays;
import java.util.List;

import net.sehales.secon.SeCon;
import net.sehales.secon.events.PlayerOfflineEvent;
import net.sehales.secon.events.PlayerOnlineEvent;
import net.sehales.secon.player.SCPlayer;
import net.sehales.secon.utils.MiscUtils;
import net.sehales.secon.utils.chat.ChatUtils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    
    // private VirtualChestHandler vch;
    private PlayerCmdCollection pc;
    
    PlayerListener(PlayerCmdCollection pc) {
        // this.vch = pc.getVirtualChestHandler();
        this.pc = pc;
    }
    
    @SuppressWarnings("unchecked")
    @EventHandler(priority = EventPriority.HIGH)
    public void onHighestPlayerChat(AsyncPlayerChatEvent e) {
        SCPlayer scp = SeCon.getInstance().getPlayerManager().getPlayer(e.getPlayer().getName());
        
        List<String> ignoredByPlayers;
        Object obj = null;
        obj = scp.getTransientValue("ignoredByPlayers");
        
        if (obj != null && obj instanceof List) {
            ignoredByPlayers = (List<String>) obj;
        } else {
            return;
        }
        
        if (ignoredByPlayers.size() == 0) {
            return;
        }
        
        for (Object rawPlayer : e.getRecipients().toArray()) {
            Player p = (Player) rawPlayer;
            if (ignoredByPlayers.contains(p.getName())) {
                e.getRecipients().remove(p);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        SCPlayer scp = SeCon.getInstance().getPlayerManager().getPlayer(p.getName());
        
        if (scp.hasData("muted")) {
            e.setCancelled(true);
            ChatUtils.sendFormattedMessage(p, pc.getLanguageNode("mute.muted-info"));
        }
    }
    
    @EventHandler()
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        SCPlayer scp = SeCon.getInstance().getPlayerManager().getPlayer(p.getName());
        
        if (scp.hasData("displayname")) {
            p.setDisplayName(scp.getValue("displayname"));
            scp.removeData("displayname");
        }
        
        if (scp.hasData("listname")) {
            p.setPlayerListName(scp.getValue("listname"));
            scp.removeData("listname");
        }
        
        if (scp.hasData("gamemode")) {
            p.setGameMode(GameMode.valueOf(scp.getValue("gamemode")));
            scp.removeData("gamemode");
        }
        
        if (scp.hasData("flymode")) {
            p.setAllowFlight(Boolean.valueOf(scp.getValue("flymode")));
            scp.removeData("flymode");
        }
        
        if (scp.hasData("flying")) {
            p.setFlying(Boolean.valueOf(scp.getValue("flying")));
            scp.removeData("flying");
        }
        
        if (scp.hasData("flyspeed")) {
            p.setFlySpeed(Float.parseFloat(scp.getValue("flyspeed")));
            scp.removeData("flyspeed");
        }
        
        if (scp.hasData("walkspeed")) {
            p.setWalkSpeed(Float.parseFloat(scp.getValue("walkspeed")));
            scp.removeData("walkspeed");
        }
    }
    
    // @EventHandler()
    // public void onInventoryClose(InventoryCloseEvent e) {
    // Inventory inv = e.getInventory();
    // if (this.vch.isOpen(inv)) {
    // this.vch.remove(this.vch.getOpenInvOwner(inv));
    // this.vch.removeOpenInv(inv);
    // }
    // }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinMonitor(PlayerJoinEvent e) {
        pc.getInvManager().processPlayerJoin(e.getPlayer());
    }
    
    @SuppressWarnings("unchecked")
    @EventHandler()
    public void onPlayerOfflineEvent(PlayerOfflineEvent e) {
        SCPlayer scp = e.getPlayer();
        
        if (scp.hasTransientData("ignoredByPlayers")) {
            List<String> ignoredByPlayers = (List<String>) scp.getTransientValue("ignoredByPlayers");
            StringBuilder sb = new StringBuilder();
            
            for (String s : ignoredByPlayers) {
                sb.append(s).append(",");
            }
            
            scp.putData("ignoredByPlayers", sb.length() > 0 ? sb.substring(0, (sb.length() - 1)) : "");
        }
    }
    
    @EventHandler()
    public void onPlayerOnlineEvent(PlayerOnlineEvent e) {
        SCPlayer scp = e.getPlayer();
        String stringList = null;
        
        if (scp.hasData("ignoredByPlayers") && !((stringList = scp.getValue("ignoredByPlayers")).isEmpty())) {
            List<String> ignoredByPlayers = Arrays.asList(stringList.split(","));
            scp.putTransientData("ignoredByPlayers", ignoredByPlayers);
        }
        
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        SCPlayer scp = SeCon.getInstance().getPlayerManager().getPlayer(p.getName());
        
        if (MiscUtils.hasPermission(p, pc.getConf().getString("permission.remember.gamemode"), false)) {
            scp.putData("gamemode", p.getGameMode().toString());
        }
        
        if (MiscUtils.hasPermission(p, pc.getConf().getString("permission.remember.flymode"), false)) {
            scp.putData("flymode", Boolean.toString(p.getAllowFlight()));
        }
        
        if (MiscUtils.hasPermission(p, pc.getConf().getString("permission.remember.flying"), false)) {
            scp.putData("flying", Boolean.toString(p.isFlying()));
        }
        
        if (MiscUtils.hasPermission(p, pc.getConf().getString("permission.remember.flyspeed"), false)) {
            scp.putData("flyspeed", Float.toString(p.getFlySpeed()));
        }
        
        if (MiscUtils.hasPermission(p, pc.getConf().getString("permission.remember.walkspeed"), false)) {
            scp.putData("walkspeed", Float.toString(p.getWalkSpeed()));
        }
        
        if (MiscUtils.hasPermission(p, pc.getConf().getString("permission.remember.listname"), false)) {
            scp.putData("listname", p.getPlayerListName());
        }
        
        if (MiscUtils.hasPermission(p, pc.getConf().getString("permission.remember.displayname"), false)) {
            scp.putData("displayname", p.getDisplayName());
        }
        
        if (MiscUtils.hasPermission(p, pc.getConf().getString("permission.remember.invisibility"), false) && pc.getInvManager().isHidden(p)) {
            scp.putData("invisible", Boolean.toString(true));
        }
    }
}
