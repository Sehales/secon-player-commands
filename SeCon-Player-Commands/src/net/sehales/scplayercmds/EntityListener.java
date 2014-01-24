
package net.sehales.scplayercmds;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class EntityListener implements Listener {
    
    private PlayerCmdCollection pc;
    
    EntityListener(PlayerCmdCollection pc) {
        this.pc = pc;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMobTarget(EntityTargetEvent e) {
        if (!(e.getTarget() instanceof Player)) {
            return;
        }
        
        Player p = (Player) e.getTarget();
        if (pc.getInvManager().isHidden(p)) {
            e.setCancelled(true);
        }
    }
}
