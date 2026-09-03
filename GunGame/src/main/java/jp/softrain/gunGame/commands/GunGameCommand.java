package jp.softrain.gunGame.commands;

import jp.softrain.gunGame.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class GunGameCommand implements CommandExecutor {

    private Main instance;

    public GunGameCommand(Main instance){
        this.instance = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (args.length == 0){
            player.sendMessage(instance.getMessagesManager().get("errors.wrong-args"));
            return true;
        }
        switch (args[0].toLowerCase()){

            case "pos1" -> {
                if (!player.hasPermission("gungame.setup")){
                    player.sendMessage(instance.getMessagesManager().get("errors.no-permission", Map.of("%permission%","gungame.setup")));
                    return true;
                }

                if (args.length < 2){
                    player.sendMessage(instance.getMessagesManager().get("errors.pos1.wrong-args"));
                    return true;
                }

                String mapName = args[1];

                if (!instance.getMapChangeManager().mapExists(mapName)){
                    player.sendMessage(instance.getMessagesManager().get("errors.pos1.map-dont-exists", Map.of("%map%", mapName)));
                    return true;
                }

                if (instance.getSpawnProtectionManager().isSetup(mapName)){
                    player.sendMessage(instance.getMessagesManager().get("spawn-protection.already-setup"));
                    return true;
                }

                instance.getSpawnProtectionManager().setPos1(player.getLocation(), mapName);
                player.sendMessage(instance.getMessagesManager().get("spawn-protection.pos1.success"));
            }

            case "pos2" -> {
                if (!player.hasPermission("gungame.setup")){
                    player.sendMessage(instance.getMessagesManager().get("errors.no-permission", Map.of("%permission%","gungame.setup")));
                    return true;
                }

                if (args.length < 2){
                    player.sendMessage(instance.getMessagesManager().get("errors.pos2.wrong-args"));
                    return true;
                }

                String mapName = args[1];

                if (!instance.getMapChangeManager().mapExists(mapName)){
                    player.sendMessage(instance.getMessagesManager().get("errors.pos2.map-dont-exists", Map.of("%map%", mapName)));
                    return true;
                }

                if (instance.getSpawnProtectionManager().isSetup(mapName)){
                    player.sendMessage(instance.getMessagesManager().get("spawn-protection.already-setup"));
                    return true;
                }

                instance.getSpawnProtectionManager().setPos2(player.getLocation(), mapName);
                player.sendMessage(instance.getMessagesManager().get("spawn-protection.pos2.success"));
            }

            case "setspawn" -> {
                if (!player.hasPermission("gungame.setspawn")){
                    player.sendMessage(instance.getMessagesManager().get("errors.no-permission", Map.of("%permission%","gungame.setspawn")));
                    return true;
                }

                if (args.length < 2){
                    player.sendMessage(instance.getMessagesManager().get("errors.setspawn.wrong-args"));
                    return true;
                }

                String mapName = args[1];

                if (mapName.contains(".")) {
                    player.sendMessage(instance.getMessagesManager().get("errors.setspawn.contains-dot"));
                    return true;
                }

                boolean success = instance.getSpawnManager().setSpawn(player.getLocation(), mapName);

                if (success) {
                    player.sendMessage(instance.getMessagesManager().get("success.setspawn", Map.of("%map%", mapName)));
                } else {
                    player.sendMessage(instance.getMessagesManager().get("errors.setspawn.already-exists", Map.of("%map%", mapName)));
                }
            }

            case "delspawn" -> {
                if (!player.hasPermission("gungame.delspawn")) {
                    player.sendMessage(instance.getMessagesManager().get("errors.no-permission", Map.of("%permission%", "gungame.delspawn")));
                    return true;
                }

                if (args.length < 2) {
                    player.sendMessage(instance.getMessagesManager().get("errors.delspawn.wrong-args"));
                    return true;
                }

                String mapName = args[1];

                if (!instance.getMapChangeManager().mapExists(mapName)) {
                    player.sendMessage(instance.getMessagesManager().get("errors.delspawn.not-exists", Map.of("%map%", mapName)));
                    return true;
                }

                instance.getSpawnManager().deleteSpawn(mapName);
                player.sendMessage(instance.getMessagesManager().get("success.delspawn", Map.of("%map%", mapName)));
            }

            case "spawn" -> {

                if(args.length < 2){
                    player.sendMessage(instance.getMessagesManager().get("errors.spawn.wrong-args"));
                    return true;
                }

                String mapName = args[1];

                if (!instance.getSpawnManager().isSpawnSet(mapName)){
                    player.sendMessage(instance.getMessagesManager().get("spawn.not-set"));
                    return true;
                }

                Location location = instance.getSpawnManager().getSpawn(mapName);

                if (location == null){
                    player.sendMessage(instance.getMessagesManager().get("spawn.not-set"));
                    return true;
                }

                player.teleport(location);
                player.sendMessage(instance.getMessagesManager().get("spawn.success"));
            }
            case "maps" -> {
                if (!player.hasPermission("gungame.maps")) {
                    player.sendMessage(instance.getMessagesManager().get("errors.no-permission", Map.of("%permission%", "gungame.delspawn")));
                    return true;
                }

                if (instance.getMapChangeManager().getMaps().isEmpty()){
                    player.sendMessage(instance.getMessagesManager().get("gungame.maps.is-empty"));
                    return true;
                }
                player.sendMessage(instance.getMessagesManager().get("gungame.maps.success"));
                for (String map : instance.getMapChangeManager().getMaps()){
                    player.sendMessage(instance.getMessagesManager().get("gungame.maps.entry", Map.of("%map%", map)));
                }
            }

            case "forcemap" -> {
                if (!player.hasPermission("gungame.forcemap")) {
                    player.sendMessage(instance.getMessagesManager().get("errors.no-permission", Map.of("%permission%", "gungame.delspawn")));
                    return true;
                }
                if(instance.getMapChangeManager().getMaps().isEmpty()) {
                    player.sendMessage(instance.getMessagesManager().get("errors.forcemap.no-maps"));
                    return true;
                }

                instance.getForceMapInventoryManager().openForceMapInventory(player);
            }
            case "stats" -> {
                if (args.length < 2){
                    player.sendMessage(instance.getMessagesManager().get("gungame.stats.wrong-args"));
                    return true;
                }

                Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {

                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    boolean exists = instance.getDatabaseManager().existsInDatabase(target.getUniqueId());

                    Bukkit.getScheduler().runTask(instance, () -> {
                        if (!exists){
                            player.sendMessage(instance.getMessagesManager().get("gungame.profile.not-a-player"));
                            return;
                        }

                        // Inventar wird für den AUSFÜHRENDEN Spieler geöffnet, zeigt Ziel-Spieler-Stats
                        instance.getStatsInventoryManager().openStatsInventory(player, target);
                    });
                });
            }

            case "edit" -> {
                if (!player.hasPermission("gungame.editor")) {
                    player.sendMessage(instance.getMessagesManager().get("errors.no-permission", Map.of("%permission%", "gungame.editor")));
                    return true;
                }
                if (args.length == 1) {
                    if (instance.getEditModeManager().isEditor(player.getUniqueId())){
                        instance.getEditModeManager().toggleEditMode(player.getUniqueId());
                        player.sendMessage(instance.getMessagesManager().get("gungame.editor.deactivate"));
                    } else {
                        instance.getEditModeManager().toggleEditMode(player.getUniqueId());
                        player.sendMessage(instance.getMessagesManager().get("gungame.editor.activate"));
                    }
                } else if (args.length == 2){
                    if (!player.hasPermission("gungame.editor.others")){
                        player.sendMessage(instance.getMessagesManager().get("errors.no-permission", Map.of("%permission%", "gungame.editor.others")));
                        return true;
                    }
                    Player target = Bukkit.getPlayerExact(args[1]);

                    if (target == null) {
                        player.sendMessage(instance.getMessagesManager().get("gungame.editor.others.not-online"));
                        return true;
                    }
                    UUID targetUUID = target.getUniqueId();

                    if (instance.getEditModeManager().isEditor(targetUUID)){

                        instance.getEditModeManager().toggleEditMode(targetUUID);
                        player.sendMessage(instance.getMessagesManager().get("gungame.editor.others.deactivate"));
                        target.sendMessage(instance.getMessagesManager().get("gungame.editor.others.deactivate.victim"));
                    } else {
                        instance.getEditModeManager().toggleEditMode(targetUUID);
                        player.sendMessage(instance.getMessagesManager().get("gungame.editor.others.activate"));
                        target.sendMessage(instance.getMessagesManager().get("gungame.editor.others.activate.victim"));
                    }
                }
            }

            case "editors" -> {
                if (!player.hasPermission("gungame.editors")) {
                    player.sendMessage(instance.getMessagesManager().get("errors.no-permission", Map.of("%permission%", "gungame.editors")));
                    return true;
                }
                if (instance.getEditModeManager().getEditors().isEmpty()){
                    player.sendMessage(instance.getMessagesManager().get("gungame.editors.empty"));
                    return true;
                }
                player.sendMessage(instance.getMessagesManager().get("gungame.editors.title"));
                for (UUID editorUUID : instance.getEditModeManager().getEditors()){
                    Player editor = Bukkit.getPlayer(editorUUID);
                    
                    String editorName = "";
                    if (editor != null){
                        editorName = editor.getName();
                    }

                    player.sendMessage(instance.getMessagesManager().get("gungame.editors.list", Map.of("%player%", editorName)));
                }
            }

            default -> {
                player.sendMessage(instance.getMessagesManager().get("errors.wrong-args"));
            }
        }

        return true;
    }
}
