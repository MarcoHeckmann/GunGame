package jp.softrain.gunGame.managers;

import jp.softrain.gunGame.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SpawnManager {

    private final Main instance;
    private File file;
    private FileConfiguration config;


    public SpawnManager(Main instance){
        this.instance = instance;
    }

    public void setup(){
        file = new File(instance.getDataFolder(), "spawns.yml");

        if(!file.exists()){
            try{
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                instance.getLogger().severe(e.getMessage());
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }
    public void save(){
        try{
            config.save(file);
        }catch (IOException e){
            instance.getLogger().severe(e.getMessage());
        }
    }
    public void reload(){
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig(){
        return config;
    }

    public boolean setSpawn(Location location, String mapName){
        String path = "spawn." + mapName;

        if (isSpawnSet(mapName)) return false;


        config.set(path, location);
        save();
        return true;
    }

    public boolean deleteSpawn(String mapName){
        String path = "spawn." + mapName;

        if (!isSpawnSet(mapName)) return false;


        config.set(path, null);
        save();
        return true;
    }

    public boolean isSpawnSet(String mapName){
        String path = "spawn." + mapName;

        return config.contains(path);
    }

    public Location getSpawn(String mapName){
        String path = "spawn." + mapName;
        return config.getLocation(path);
    }
}
