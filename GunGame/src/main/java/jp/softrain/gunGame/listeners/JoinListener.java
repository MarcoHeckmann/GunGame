package jp.softrain.gunGame.listeners;

import jp.softrain.gunGame.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    private Main instance;
    public JoinListener(Main instance){
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        Location location = instance.getSpawnManager().getSpawn(instance.getMapChangeManager().getCurrentMap());

        if (location == null){
            player.sendMessage(instance.getMessagesManager().get("not-set"));
            return;
        }
        instance.getScoreboardManager().setScoreboard(player);
        player.teleport(location);
        instance.getItemManager().giveLoadOut(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        instance.getLevelManager().deletePlayer(player.getUniqueId());
    }
}
