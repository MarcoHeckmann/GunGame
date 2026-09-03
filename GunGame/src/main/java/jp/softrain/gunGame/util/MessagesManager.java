package jp.softrain.gunGame.util;

import jp.softrain.gunGame.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;

public class MessagesManager {

    private final Main instance;
    private File file;
    private FileConfiguration config;
    private String PREFIX;

    public MessagesManager(Main instance){
        this.instance = instance;
    }

    public void setup(){
        file = new File(instance.getDataFolder(), "messages.yml");

        if (!file.exists()){
            try{
                instance.saveResource("messages.yml", false);
            } catch (IllegalArgumentException e){
                instance.getLogger().severe(e.getMessage());
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
        PREFIX = config.getString("prefix");
    }
    public void reload(){
        config = YamlConfiguration.loadConfiguration(file);
    }

    public String get(String path){
        return get(path, Map.of());
    }

    public int getInt(String path){
        return config.getInt(path);
    }

    public String get(String path, Map<String, String> replacements){
        if (!config.contains(path)){
            return "§7Message not found";
        }

        String message = config.getString(path, "§cMessage not found: §e" + path);
        message = message.replace("%prefix%", PREFIX);

        for (Map.Entry<String, String> entry : replacements.entrySet()){
            message = message.replace(entry.getKey(), entry.getValue());
        }
        return message;
    }
}
