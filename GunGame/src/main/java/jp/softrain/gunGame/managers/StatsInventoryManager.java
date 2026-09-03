package jp.softrain.gunGame.managers;

import com.destroystokyo.paper.profile.PlayerProfile;
import jp.softrain.gunGame.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

public class StatsInventoryManager {

    private final Main instance;

    public StatsInventoryManager(Main instance){
        this.instance = instance;
    }

    public void openStatsInventory(Player player, OfflinePlayer target){

        // Async: DB-Abfragen laufen im separaten Thread
        Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {

            int kills = instance.getDatabaseManager().getKills(target.getUniqueId());
            int deaths = instance.getDatabaseManager().getDeaths(target.getUniqueId());
            double kd = instance.getDatabaseManager().getKD(target.getUniqueId());

            // Zurück in den Main-Thread, weil Inventar-API nur dort sicher ist
            Bukkit.getScheduler().runTask(instance, () -> {

                Inventory inventory = Bukkit.createInventory(null, 27, instance.getMessagesManager().get("guis.stats.title", Map.of("%player%", target.getName())));

                for (int i = 0; i < inventory.getSize(); i++){
                    inventory.setItem(i, createItem(Material.GRAY_STAINED_GLASS_PANE, instance.getMessagesManager().get("guis.stats.item-pane")));
                }

                ItemStack profileHead = createProfileHead(target);
                ItemStack killsHead = createCustomHead("http://textures.minecraft.net/texture/6a5361b52daf4f1c5c5480a39faaa10897595fa5763f757bdda3956588fec678", instance.getMessagesManager().get("guis.stats.kills", Map.of("%kills%", String.valueOf(kills))));
                ItemStack deathsHead = createCustomHead("http://textures.minecraft.net/texture/1b6f0156e28c2ca6c188b2248e72fdab874abaa8d00dba72718e4f8a04e44c27", instance.getMessagesManager().get("guis.stats.deaths", Map.of("%deaths%", String.valueOf(deaths))));
                ItemStack kdHead = createCustomHead("http://textures.minecraft.net/texture/a6b344c726cd93979ff5004909a1c1e52b5a41d56d2f4da51e6f69e42838238a", instance.getMessagesManager().get("guis.stats.kd", Map.of("%kd%", String.valueOf(kd))));

                inventory.setItem(4, profileHead);
                inventory.setItem(11, killsHead);
                inventory.setItem(13, deathsHead);
                inventory.setItem(15, kdHead);

                player.openInventory(inventory);
            });
        });
    }

    private ItemStack createItem(Material material, String displayName){
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack createProfileHead(OfflinePlayer target){
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwningPlayer(target);
        skullMeta.setDisplayName(instance.getMessagesManager().get("guis.stats.profile-head", Map.of("%player%", target.getName())));
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }

    private ItemStack createCustomHead(String URL, String displayName){
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setDisplayName(displayName);
        PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
        PlayerTextures playerTextures = playerProfile.getTextures();

        try{
            playerTextures.setSkin(new URL(URL));
        } catch (MalformedURLException e){
            e.printStackTrace();
            return itemStack;
        }

        playerProfile.setTextures(playerTextures);
        skullMeta.setPlayerProfile(playerProfile);
        itemStack.setItemMeta(skullMeta);
        return itemStack;
    }
}
