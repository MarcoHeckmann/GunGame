package jp.softrain.gunGame.managers;

import jp.softrain.gunGame.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class ForceMapInventoryManager {

    private Main instance;
    public ForceMapInventoryManager(Main instance){
        this.instance = instance;
    }
    public void openForceMapInventory(Player player){

        int inventorySize;
        if (instance.getMapChangeManager().getMapCount() <= 9){
            inventorySize = 9;
        } else if (instance.getMapChangeManager().getMapCount() <= 18){
            inventorySize = 18;
        } else if (instance.getMapChangeManager().getMapCount() <= 27){
            inventorySize = 27;
        } else if (instance.getMapChangeManager().getMapCount() <= 36){
            inventorySize = 36;
        } else if (instance.getMapChangeManager().getMapCount() <= 45){
            inventorySize = 45;
        } else {
            inventorySize = 54; // Bukkit-Inventare erlauben max. 54 Slots (6 Reihen)
        }

        Inventory inventory = Bukkit.createInventory(null, inventorySize, instance.getMessagesManager().get("guis.force-map.title"));

        for (int i = 0; i < inventorySize; i++){
            inventory.setItem(i, createItem(Material.GRAY_STAINED_GLASS_PANE, instance.getMessagesManager().get("guis.force-map.item-pane")));
        }

       int i = 0;
            for (String map : instance.getMapChangeManager().getMaps()){
                inventory.setItem(i++, createItem(Material.GRASS_BLOCK, instance.getMessagesManager().get("guis.force-map.item-map", Map.of("%map%", map))));
        }

        player.openInventory(inventory);
    }
    private ItemStack createItem(Material material, String displayName){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
