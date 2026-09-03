package jp.softrain.gunGame.managers;

import jp.softrain.gunGame.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;

public class ActionbarManager {

    private Main instance;
    public ActionbarManager(Main instance){
        this.instance = instance;
    }

    public void startActionbar(){
// Einmal beim Start starten (z.B. in onEnable)
        Bukkit.getScheduler().runTaskTimer(instance, () -> {
            if (instance.getTimer() == null) return;

            String map = instance.getMapChangeManager().getCurrentMap();
            String time = instance.getTimer().getFormattedTime();

            for (Player players : Bukkit.getOnlinePlayers()) {
                if (map == null) {
                    players.sendActionBar(instance.getMessagesManager().get("actionbar.no-map"));
                } else {
                    players.sendActionBar(instance.getMessagesManager().get("actionbar.current-map",
                            Map.of("%map%", map, "%time%", time)));
                }
            }
        }, 0L, 20L);
    }
}
