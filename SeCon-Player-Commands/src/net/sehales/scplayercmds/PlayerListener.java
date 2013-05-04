package net.sehales.scplayercmds;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class PlayerListener implements Listener {

	private VirtualChestHandler vch;
	private PCUtils             utils;

	PlayerListener(PlayerCmdCollection pc, PCUtils utils) {
		this.vch = pc.getVirtualChestHandler();
		this.utils = utils;
	}

	@EventHandler()
	public void onInventoryClose(InventoryCloseEvent e) {
		Inventory inv = e.getInventory();
		if (this.vch.isOpen(inv)) {
			this.vch.remove(this.vch.getOpenInvOwner(inv));
			this.vch.removeOpenInv(inv);
		}
	}
}
