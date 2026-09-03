package jp.softrain.gunGame.managers;

import jp.softrain.gunGame.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public class ForceMapManager {

    private Main instance;
    public ForceMapManager(Main instance){
        this.instance = instance;
    }

    public void ForceMapChange(Player player, String mapName){

        instance.getMapChangeManager().setCurrentMap(mapName);

        Location location = instance.getSpawnManager().getSpawn(instance.getMapChangeManager().getCurrentMap());
        //Dragonland
        //draognland - Keine Map namens "draognland" gefunden.

        if (location == null){return;}

        for (Player players : Bukkit.getOnlinePlayers()){
            players.teleport(location);
            players.sendTitle(
                    instance.getMessagesManager().get("gungame.forcemap.title.map-change.title"),
                    instance.getMessagesManager().get("gungame.forcemap.title.map-change.subtitle", Map.of("%map%", mapName)),
                    instance.getMessagesManager().getInt("gungame.forcemap.title.map-change.fade-in"),
                    instance.getMessagesManager().getInt("gungame.forcemap.title.map-change.stay"),
                    instance.getMessagesManager().getInt("gungame.forcemap.title.map-change.fade-out"));

            players.sendMessage(instance.getMessagesManager().get("gungame.forcemap.changed-by-user", Map.of("%player%", player.getName(), "%map%", mapName)));
        }
        player.sendMessage(instance.getMessagesManager().get("gungame.forcemap.issuer", Map.of("%map%", mapName)));
    }
}


