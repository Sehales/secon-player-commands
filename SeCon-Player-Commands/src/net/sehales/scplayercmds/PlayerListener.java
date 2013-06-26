package net.sehales.scplayercmds;

import java.util.ArrayList;
import java.util.List;

import net.sehales.secon.SeCon;
import net.sehales.secon.exception.DataNotFoundException;
import net.sehales.secon.player.SeConPlayer;
import net.sehales.secon.utils.SeConUtils;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

public class PlayerListener implements Listener {

	private VirtualChestHandler vch;
	private PlayerCmdCollection pc;

	PlayerListener(PlayerCmdCollection pc) {
		this.vch = pc.getVirtualChestHandler();
		this.pc = pc;
	}

	@SuppressWarnings("unchecked")
	@EventHandler(priority = EventPriority.HIGH)
	public void onHighestPlayerChat(AsyncPlayerChatEvent e) {
		SeConPlayer scp = SeCon.getAPI().getPlayerManager().getPlayer(e.getPlayer().getName());

		List<String> ignoredByPlayers;
		Object obj = null;
		try {
			obj = scp.getData("ignoredByPlayers");
		} catch (DataNotFoundException ex) {
		}

		if (obj != null && obj instanceof ArrayList)
			ignoredByPlayers = (List<String>) obj;
		else
			return;

		if (ignoredByPlayers.size() == 0)
			return;

		for (Object rawPlayer : e.getRecipients().toArray()) {
			Player p = (Player) rawPlayer;
			if (ignoredByPlayers.contains(p.getName()))
				e.getRecipients().remove(p);
		}
	}

	@EventHandler()
	public void onInventoryClose(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		if (this.vch.isOpen(inv)) {
			this.vch.remove(this.vch.getOpenInvOwner(inv));
			this.vch.removeOpenInv(inv);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		SeConPlayer scp = SeCon.getAPI().getPlayerManager().getPlayer(p.getName());

		if (scp.hasData("muted")) {
			e.setCancelled(true);
			SeCon.getAPI().getChatUtils().sendFormattedMessage(p, pc.getLanguageInfoNode("mute.muted-info"));
		}
	}

	@EventHandler()
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		SeConPlayer scp = SeCon.getAPI().getPlayerManager().getPlayer(p.getName());

		if (scp.hasData("displayname")) {
			try {
				p.setDisplayName((String) scp.getData("displayname"));
			} catch (DataNotFoundException e1) {
				e1.printStackTrace();
			}
			scp.removeData("displayname");
		}

		if (scp.hasData("listname")) {
			try {
				p.setPlayerListName((String) scp.getData("listname"));
			} catch (DataNotFoundException e1) {
				e1.printStackTrace();
			}
			scp.removeData("listname");
		}

		if (scp.hasData("gamemode")) {
			try {
				p.setGameMode((GameMode) scp.getData("gamemode"));
			} catch (DataNotFoundException e1) {
				e1.printStackTrace();
			}
			scp.removeData("gamemode");
		}

		if (scp.hasData("flymode")) {
			try {
				p.setAllowFlight((Boolean) scp.getData("flymode"));
			} catch (DataNotFoundException e1) {
				e1.printStackTrace();
			}
			scp.removeData("flymode");
		}

		if (scp.hasData("flying")) {
			try {
				p.setFlying((Boolean) scp.getData("flying"));
			} catch (DataNotFoundException e1) {
				e1.printStackTrace();
			}
			scp.removeData("flying");
		}

		if (scp.hasData("flyspeed")) {
			try {
				p.setFlySpeed((Float) scp.getData("flyspeed"));
			} catch (DataNotFoundException e1) {
				e1.printStackTrace();
			}
			scp.removeData("flyspeed");
		}

		if (scp.hasData("walkspeed")) {
			try {
				p.setWalkSpeed((Float) scp.getData("walkspeed"));
			} catch (DataNotFoundException e1) {
				e1.printStackTrace();
			}
			scp.removeData("walkspeed");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoinMonitor(PlayerJoinEvent e) {
		pc.getInvManager().processPlayerJoin(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		SeConPlayer scp = SeCon.getAPI().getPlayerManager().getPlayer(p.getName());
		SeConUtils su = SeCon.getAPI().getSeConUtils();

		if (su.hasPermission(p, pc.getConf().getString("permission.remember.gamemode"), false))
			scp.setData("gamemode", p.getGameMode());

		if (su.hasPermission(p, pc.getConf().getString("permission.remember.flymode"), false))
			scp.setData("flymode", p.getAllowFlight());

		if (su.hasPermission(p, pc.getConf().getString("permission.remember.flying"), false))
			scp.setData("flying", p.isFlying());

		if (su.hasPermission(p, pc.getConf().getString("permission.remember.flyspeed"), false))
			scp.setData("flyspeed", p.getFlySpeed());

		if (su.hasPermission(p, pc.getConf().getString("permission.remember.walkspeed"), false))
			scp.setData("walkspeed", p.getWalkSpeed());

		if (su.hasPermission(p, pc.getConf().getString("permission.remember.listname"), false))
			scp.setData("listname", p.getPlayerListName());

		if (su.hasPermission(p, pc.getConf().getString("permission.remember.displayname"), false))
			scp.setData("displayname", p.getDisplayName());

		if (su.hasPermission(p, pc.getConf().getString("permission.remember.invisibility"), false) && pc.getInvManager().isHidden(p))
			scp.setData("invisible", true);
	}
}
