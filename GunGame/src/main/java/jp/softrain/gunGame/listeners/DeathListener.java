package jp.softrain.gunGame.listeners;

import jp.softrain.gunGame.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Map;

public class DeathListener implements Listener {

    private Main instance;
    public DeathListener(Main instance){
        this.instance = instance;
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        event.deathMessage(null);
        event.setDroppedExp(0);
        event.setShouldDropExperience(false);
        event.getDrops().clear();

        if (killer != null && !killer.equals(victim)){
            instance.getLevelManager().resetLevel(victim);
            Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {

                instance.getDatabaseManager().addKill(killer.getUniqueId());
                instance.getDatabaseManager().addDeath(victim.getUniqueId());

                Bukkit.getScheduler().runTask(instance, () -> {
                    killer.sendMessage(instance.getMessagesManager().get("stats.killer", Map.of("%victim%", victim.getName())));
                    instance.getLevelManager().addLevel(killer.getUniqueId());
                    instance.getItemManager().giveLoadOut(killer); //killer -> victim

                    victim.sendMessage(instance.getMessagesManager().get("stats.victim", Map.of("%killer%", killer.getName())));
                });
            });
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
                instance.getDatabaseManager().addDeath(victim.getUniqueId());

                Bukkit.getScheduler().runTask(instance, () -> {
                    victim.sendMessage(instance.getMessagesManager().get("stats.no-killer"));
                    instance.getLevelManager().resetLevel(victim);
                });
            });
        }
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            victim.spigot().respawn();
            instance.getItemManager().giveLoadOut(victim);
        },1L);
    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){

        Player player = event.getPlayer();


        Location location = instance.getSpawnManager().getSpawn(instance.getMapChangeManager().getCurrentMap());

        if (location == null){
            player.sendMessage(instance.getMessagesManager().get("not-set"));
            return;
        }

        event.setRespawnLocation(location);
    }
}
