package jp.softrain.gunGame.managers;

import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public class SpawnProtectionManager {

    private final SpawnManager spawnManager;

    public SpawnProtectionManager(SpawnManager spawnManager){
        this.spawnManager = spawnManager;
    }

    public void setPos1(Location location, String mapName){
        spawnManager.getConfig().set("spawnprotection.pos1." + mapName, location);
        spawnManager.save();
    }
    public void setPos2(Location location, String mapName){
        spawnManager.getConfig().set("spawnprotection.pos2." + mapName, location);
        spawnManager.save();
    }

    public boolean isSetup(String mapName){
        return getPos1(mapName) != null && getPos2(mapName) != null;
    }

    public Location getPos1(String mapName){
        return spawnManager.getConfig().getLocation("spawnprotection.pos1." + mapName);
    }
    public Location getPos2(String mapName){
        return spawnManager.getConfig().getLocation("spawnprotection.pos2." + mapName);
    }

    public boolean isInside(Location location, String mapName){
        if (!isSetup(mapName)) return false;

        Location pos1 = getPos1(mapName);
        Location pos2 = getPos2(mapName);

        BoundingBox boundingBox = BoundingBox.of(pos1,pos2);
        return boundingBox.contains(location.toVector());
    }
}
