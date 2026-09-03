package jp.softrain.gunGame.managers;

import jp.softrain.gunGame.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class LevelManager {

    private Main instance;

    public LevelManager(Main instance){
        this.instance = instance;
    }

    public HashMap<UUID, Integer> level = new HashMap<>();
    int maxLevel = 5;

    public void addLevel(UUID uuid){
        int currentLevel = getLevel(uuid);

        if (currentLevel >= maxLevel) return;

        level.put(uuid, currentLevel + 1);
    }

    public int getMaxLevel(){
        return maxLevel;
    }

    public void resetLevel(Player player){
        level.put(player.getUniqueId(), 0);
        player.setLevel(0);
    }

    public int getLevel(UUID uuid){
        return level.getOrDefault(uuid, 0);
    }
    public void deletePlayer(UUID uuid){
        level.remove(uuid);
    }

    public void switchLevel(Player player){
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1,1);
        player.setLevel(getLevel(player.getUniqueId()));
    }
    public void iniLevel(Player player){
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[]{null, null, null, null});
        player.setLevel(getLevel(player.getUniqueId()));
    }
}
