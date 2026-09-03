package jp.softrain.gunGame.util;

import jp.softrain.gunGame.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.Map;

public class ScoreboardManager {

    private Main instance;
    public ScoreboardManager(Main instance){
        this.instance = instance;
    }

    public void setScoreboard(Player player){

        Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
           int kills = instance.getDatabaseManager().getKills(player.getUniqueId());
           int deaths = instance.getDatabaseManager().getDeaths(player.getUniqueId());
           double kd = instance.getDatabaseManager().getKD(player.getUniqueId());

           Bukkit.getScheduler().runTaskLater(instance, () -> {
               if (!player.isOnline()) return;
               buildScoreboard(player,kills,deaths,kd);
           },0L);
        });

        //⚔ kills | ☠ deaths | ☮ Streak
        //K/D kd | 123
    }
    public void buildScoreboard(Player player, int kills, int deaths, double kd){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("dummy", Criteria.DUMMY, instance.getMessagesManager().get("scoreboard-title"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        String killsLine = instance.getMessagesManager().get("scoreboard.line-kills", Map.of("%kills%", String.valueOf(kills), "%deaths%", String.valueOf(deaths)));
        String kdLine = instance.getMessagesManager().get("scoreboard.line-kd", Map.of("%kd%", new DecimalFormat("##.##").format(kd)));
        String mapLine = instance.getMessagesManager().get("scoreboard.line-map", Map.of("%map%", instance.getMapChangeManager().getCurrentMap()));

        objective.getScore(instance.getMessagesManager().get("scoreboard.line-top")).setScore(12);
        objective.getScore(instance.getMessagesManager().get("scoreboard.line-spacer-1")).setScore(11);
        objective.getScore(killsLine).setScore(10);
        objective.getScore(instance.getMessagesManager().get("scoreboard.line-spacer-2")).setScore(9);
        objective.getScore(kdLine).setScore(8);
        objective.getScore(instance.getMessagesManager().get("scoreboard.line-spacer-3")).setScore(7);
        objective.getScore(mapLine).setScore(6);
        objective.getScore(instance.getMessagesManager().get("scoreboard.line-spacer-4")).setScore(5);
        objective.getScore(instance.getMessagesManager().get("scoreboard.line-bottom")).setScore(4);

        player.setScoreboard(scoreboard);
    }
}
