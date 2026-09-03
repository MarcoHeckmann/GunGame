package jp.softrain.gunGame.listeners;

import jp.softrain.gunGame.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryClickListener implements Listener {

    private Main instance;
    public InventoryClickListener(Main instance){
        this.instance = instance;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){

        //Abfrage vom Namen des Inventars
        if (!event.getView().getTitle().equals(instance.getMessagesManager().get("guis.force-map.title"))) return;
        event.setCancelled(true);

        //Ob Itemstack = null
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) return;

        //Ob es ein Grassblock ist
        if (itemStack.getType() != Material.GRASS_BLOCK) return;

        //Ob es eine ItemMeta gibt.
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        String mapName = ChatColor.stripColor(itemMeta.getDisplayName());

        if (!instance.getMapChangeManager().mapExists(mapName)) return;

        Player player = (Player) event.getWhoClicked();
        instance.getForceMapManager().ForceMapChange(player, mapName);
        player.closeInventory();
    }
}
