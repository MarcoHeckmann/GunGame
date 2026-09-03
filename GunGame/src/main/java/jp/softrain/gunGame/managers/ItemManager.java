package jp.softrain.gunGame.managers;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class ItemManager {

    private LevelManager instance;
    public ItemManager(LevelManager levelManager){
        this.instance = levelManager;
    }

    public void giveLoadOut(Player player){

        int level = instance.getLevel(player.getUniqueId());

        switch (level){
            case 1 -> {
                instance.switchLevel(player);
                equipPlayerWeapon(player, new ItemStack(Material.WOODEN_SWORD));
                updateInventory(player);
            }
            case 2 -> {
                instance.switchLevel(player);
                equipPlayerWeapon(player, new ItemStack(Material.WOODEN_SWORD));
                equipPlayerArmor(player,
                        new ItemStack(Material.IRON_BOOTS), null, null, null);
                updateInventory(player);
            }
            case 3 -> {
                instance.switchLevel(player);
                equipPlayerWeapon(player, new ItemStack(Material.IRON_SWORD));
                equipPlayerArmor(player,
                        new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.IRON_LEGGINGS), null, null);
                updateInventory(player);
            }
            case 4 -> {
                instance.switchLevel(player);
                equipPlayerWeapon(player, new ItemStack(Material.IRON_SWORD));
                equipPlayerArmor(player,
                        new ItemStack(Material.IRON_BOOTS), Map.of(Enchantment.PROTECTION, 1), new ItemStack(Material.IRON_LEGGINGS), Map.of(), new ItemStack(Material.IRON_CHESTPLATE), Map.of(), null, Map.of());
                updateInventory(player);

            }
            case 5 -> {
                instance.switchLevel(player);
                equipPlayerWeapon(player, new ItemStack(Material.IRON_SWORD), Map.of(Enchantment.SHARPNESS, 32000));
                equipPlayerArmor(player,
                        new ItemStack(Material.IRON_BOOTS), Map.of(Enchantment.PROTECTION, 1), new ItemStack(Material.IRON_LEGGINGS), Map.of(), new ItemStack(Material.IRON_CHESTPLATE), Map.of(Enchantment.KNOCKBACK, 1), new ItemStack(Material.IRON_HELMET), Map.of());
            updateInventory(player);
            }
            default -> {
                instance.iniLevel(player);
                equipPlayerWeapon(player, new ItemStack(Material.WOODEN_AXE));
                updateInventory(player);
            }
        }
    }

    public void updateInventory(Player player){
        player.updateInventory();
    }

    public void equipPlayerWeapon(Player player, ItemStack itemStack){
        equipPlayerWeapon(player, itemStack, Map.of());
    }

    public void equipPlayerWeapon(Player player, ItemStack itemStack, Map<Enchantment, Integer> enchantments){

         enchantStack(itemStack, enchantments, true);
        player.getInventory().addItem(itemStack);
    }

    public void equipPlayerArmor(Player player, ItemStack boots, ItemStack leggings, ItemStack chestPlate, ItemStack helmet){
        equipPlayerArmor(player, boots, Map.of(), leggings, Map.of(), chestPlate, Map.of(), helmet, Map.of());
    }

    public void equipPlayerArmor(Player player,
                                 ItemStack boots, Map<Enchantment, Integer> bootsEnchants,
                                 ItemStack leggings, Map<Enchantment, Integer> leggingsEnchants,
                                 ItemStack chestPlate, Map<Enchantment, Integer> chestPlateEnchants,
                                 ItemStack helmet, Map<Enchantment, Integer> helmetEnchants){

        if (boots != null) enchantStack(boots, bootsEnchants, true);
        if (leggings != null) enchantStack(leggings, leggingsEnchants, true);
        if (chestPlate != null) enchantStack(chestPlate, chestPlateEnchants, true);
        if (helmet != null) enchantStack(helmet, helmetEnchants, true);

        player.getInventory().setBoots(boots);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setChestplate(chestPlate);
        player.getInventory().setHelmet(helmet);
    }

    public ItemStack weaponStack(Material material){
        return new ItemStack(material);
    }

    public ItemStack enchantStack(ItemStack itemStack, Map<Enchantment, Integer> enchantments, boolean unsafe){

        if (unsafe){
            itemStack.addUnsafeEnchantments(enchantments);
        } else {
            itemStack.addEnchantments(enchantments);
        }
        return itemStack;
    }


}
