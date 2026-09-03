package jp.softrain.gunGame.util;

import jp.softrain.gunGame.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {

    private Main instance;
    private int secondsleft;

    public Timer(Main instance, int seconds){
        this.instance = instance;
        this.secondsleft = seconds;
    }


    @Override
    public void run() {
        if (secondsleft <= 0){
            instance.getMapChangeManager().ChangeMap();
            this.cancel();
            return;
        }
        secondsleft--;
    }

    public int getSecondsLeft(){
        return secondsleft;
    }

    public String getFormattedTime(){
        int minutes = secondsleft / 60;
        int seconds = secondsleft % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
