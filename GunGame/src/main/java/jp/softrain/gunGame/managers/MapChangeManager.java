package jp.softrain.gunGame.managers;

import jp.softrain.gunGame.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MapChangeManager {

    private Main instance;
    private String currentMap = null;
    public MapChangeManager(Main instance){
        this.instance = instance;
    }

    public String getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(String mapName){
        this.currentMap = mapName;
    }

    public void setRandomMap(){
        if (getMaps().isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(instance.getMessagesManager().get("gungame.error.mapchange.console-no-maps"));
            return;
        }

        Random random = new Random();

        List<String> maps = getMaps();

        String randomMap = maps.get(random.nextInt(maps.size()));

        setCurrentMap(randomMap);
    }

    public List<String> getMaps(){
        if (instance.getSpawnManager().getConfig().getConfigurationSection("spawn") == null){
            return Collections.emptyList();
        }

        List<String> maps = new ArrayList<>(instance.getSpawnManager().getConfig().getConfigurationSection("spawn").getKeys(false));
        return maps;
    }

    public int getMapCount(){
        return getMaps().size();
    }

    public boolean mapExists(String mapName){
        return instance.getSpawnManager().getConfig().contains("spawn." + mapName);
    }

    public void ChangeMap(){
        if (getMaps().isEmpty()) {
            for (Player players : Bukkit.getOnlinePlayers()){
                players.sendMessage(instance.getMessagesManager().get("gungame.error.mapchange.no-map-exists"));
            }
            return;
        }

        setRandomMap();

        Location location = instance.getSpawnManager().getSpawn(getCurrentMap());

        if (location == null){
            for (Player players : Bukkit.getOnlinePlayers()){
                players.sendMessage(instance.getMessagesManager().get("gungame.error.mapchange.no-map-exists"));
            }
            return;
        }

        for (Player players : Bukkit.getOnlinePlayers()){
            players.teleport(location);
            players.sendTitle(
                    instance.getMessagesManager().get("gungame.title.map-change.title"),
                    instance.getMessagesManager().get("gungame.title.map-change.subtitle", Map.of("%map%", getCurrentMap())),
                    instance.getMessagesManager().getInt("gungame.title.map-change.fade-in"),
                    instance.getMessagesManager().getInt("gungame.title.map-change.stay"),
                    instance.getMessagesManager().getInt("gungame.title.map-change.fade-out")
            );
        }

        instance.setTimer(instance);

    }


}
