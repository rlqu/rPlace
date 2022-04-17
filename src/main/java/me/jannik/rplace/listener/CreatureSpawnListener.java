package me.jannik.rplace.listener;

import lombok.Getter;
import me.jannik.rplace.Place;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener implements Listener {

    @Getter
    private final Place place;
    public CreatureSpawnListener() {
        place = Place.getInstance();
    }

    @EventHandler
    public void handleCreatureSpawn(CreatureSpawnEvent event) {

        if(!getPlace().getConfiguration().isShouldSpawnCreatures()) {
            if(event.getEntity().getType() != EntityType.PLAYER && event.getEntity().getType() != EntityType.ARMOR_STAND) {
                event.getEntity().remove();
                event.setCancelled(true);
            }
        }

    }
}
