package jp.softrain.gunGame;

import jp.softrain.gunGame.commands.GunGameCommand;
import jp.softrain.gunGame.database.DatabaseManager;
import jp.softrain.gunGame.listeners.DamageListener;
import jp.softrain.gunGame.listeners.DeathListener;
import jp.softrain.gunGame.listeners.InventoryClickListener;
import jp.softrain.gunGame.listeners.JoinListener;
import jp.softrain.gunGame.managers.*;
import jp.softrain.gunGame.util.MessagesManager;
import jp.softrain.gunGame.util.ScoreboardManager;
import jp.softrain.gunGame.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public final class Main extends JavaPlugin {

    private Main instance;
    private MessagesManager messagesManager;
    private SpawnManager spawnManager;
    private DatabaseManager databaseManager;
    private SpawnProtectionManager spawnProtectionManager;
    private LevelManager levelManager;
    private ItemManager itemManager;
    private ActionbarManager actionbarManager;
    private MapChangeManager mapChangeManager;
    private ForceMapManager forceMapManager;
    private StatsInventoryManager statsInventoryManager;
    private ForceMapInventoryManager forceMapInventoryManager;
    private Timer timer;
    private editModeManager editModeManager;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {

        //INSTANCES:
        instance = this;
        databaseManager = new DatabaseManager(this);
        spawnManager = new SpawnManager(this);
        levelManager = new LevelManager(this);
        itemManager = new ItemManager(levelManager);
        actionbarManager = new ActionbarManager(this);
        messagesManager = new MessagesManager(this);
        forceMapManager = new ForceMapManager(this);
        statsInventoryManager = new StatsInventoryManager(this);
        forceMapInventoryManager = new ForceMapInventoryManager(this);
        editModeManager = new editModeManager(this);
        scoreboardManager = new ScoreboardManager(this);

        //SETUPS:
        spawnManager.setup();
        spawnProtectionManager = new SpawnProtectionManager(spawnManager);
        mapChangeManager = new MapChangeManager(this);
        messagesManager.setup();

        //SETUP BEKANNTE RADNDOMN UNTERMAP MAP:
        mapChangeManager.setRandomMap();

        //COMMANDS:
        getCommand("gungame").setExecutor(new GunGameCommand(this));

        //LISTENERS:
        Bukkit.getPluginManager().registerEvents(new DeathListener(this),this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(this),this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(this),this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(this),this);
        Bukkit.getConsoleSender().sendMessage(messagesManager.get("console.enabled"));
        Bukkit.getConsoleSender().sendMessage(messagesManager.get("console.current-map", Map.of("%map%", mapChangeManager.getCurrentMap())));
        actionbarManager.startActionbar();
        setTimer(instance);

        // Alle 20 Sekunden (20 * 20 = 400 Ticks)
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                getScoreboardManager().setScoreboard(player);
            }
        }, 400L, 400L); // delay, period

    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(messagesManager.get("console.disabled"));
    }

    public Main getInstance() {
        return instance;
    }

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public SpawnProtectionManager getSpawnProtectionManager() {
        return spawnProtectionManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public ActionbarManager getActionbarManager() {
        return actionbarManager;
    }

    public MapChangeManager getMapChangeManager() {
        return mapChangeManager;
    }

    public ForceMapManager getForceMapManager() {
        return forceMapManager;
    }

    public StatsInventoryManager getStatsInventoryManager() {
        return statsInventoryManager;
    }

    public ForceMapInventoryManager getForceMapInventoryManager() {
        return forceMapInventoryManager;
    }

    public Timer getTimer() {
        return timer;
    }
    public void setTimer(Main instance){
        timer = new Timer(this, 60);
        timer.runTaskTimer(this, 0L, 20L);
    }

    public editModeManager getEditModeManager() {
        return editModeManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}

