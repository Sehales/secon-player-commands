package net.sehales.scplayercmds;

import java.util.ArrayList;
import java.util.List;

import net.sehales.secon.SeCon;
import net.sehales.secon.player.SeConPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InvisibilityManager {

	private List<String>        invPlayerNames = new ArrayList<String>();
	private PlayerCmdCollection pc;

	public InvisibilityManager(PlayerCmdCollection pc) {
		this.pc = pc;
	}

	public boolean canSeeOthers(Player player) {
		return SeCon.getAPI().getSeConUtils().hasPermission(player, pc.getConf().getString("permission.invisibility.see"), false);
	}

	public boolean hide(Player player) {
		if (!isHidden(player)) {
			invPlayerNames.add(player.getName());

			for (Player p : Bukkit.getOnlinePlayers())
				if (SeCon.getAPI().getSeConUtils().hasPermission(p, pc.getConf().getString("permission.invisibility.see"), false))
					p.hidePlayer(player);

			return true;
		}
		return false;
	}

	public boolean isHidden(Player player) {
		return invPlayerNames.contains(player.getName());
	}

	public void processPlayerJoin(Player player) {
		SeConPlayer scp = SeCon.getAPI().getPlayerManager().getPlayer(player.getName());
		if (scp.hasData("invisible")) {
			hide(player);
			scp.removeData("invisible");
		}

		if (!SeCon.getAPI().getSeConUtils().hasPermission(player, pc.getConf().getString("permission.invisibility.see"), false))
			for (String name : invPlayerNames.toArray(new String[0])) {
				Player p = Bukkit.getPlayer(name);
				if (p != null)
					player.hidePlayer(p);
			}
	}

	public boolean show(Player player) {
		if (isHidden(player)) {
			invPlayerNames.remove(player.getName());

			for (Player p : Bukkit.getOnlinePlayers())
				p.showPlayer(player);

			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param player
	 * @return true if the player is hidden now, false otherwise
	 */
	public boolean toggleInvisibility(Player player) {
		if (isHidden(player)) {
			show(player);
			return false;
		} else {
			hide(player);
			return true;
		}
	}

}