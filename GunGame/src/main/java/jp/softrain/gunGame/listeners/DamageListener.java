package jp.softrain.gunGame.listeners;

import jp.softrain.gunGame.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private Main instance;
    public DamageListener(Main instance){
        this.instance = instance;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;

        // Fall 1: Opfer im Spawn -> jeglicher Schaden blockiert.
        if (instance.getSpawnProtectionManager().isInside(victim.getLocation(), instance.getMapChangeManager().getCurrentMap())) {
            event.setCancelled(true);
        }

        if (event instanceof EntityDamageByEntityEvent byEntity){
            if (byEntity.getDamager() instanceof Player attacker){
                if (instance.getSpawnProtectionManager().isInside(attacker.getLocation(), instance.getMapChangeManager().getCurrentMap())){
                    event.setCancelled(true);
                }
            }
        }
    }
}
