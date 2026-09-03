package jp.softrain.gunGame.managers;

import jp.softrain.gunGame.Main;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class editModeManager {

    private Main instance;
    public editModeManager(Main instance){
        this.instance = instance;
    }

    public Set<UUID> editors = new HashSet<>();

    public void toggleEditMode(UUID uuid){
        if (isEditor(uuid)){
            editors.remove(uuid);
        } else {
         editors.add(uuid);
        }
    }

    public boolean isEditor(UUID uuid){
        return editors.contains(uuid);
    }

    public Set<UUID> getEditors(){
        return Set.copyOf(editors);
    }
}
