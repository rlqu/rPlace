package me.jannik.rplace.listener;

import lombok.Getter;
import me.jannik.rplace.Place;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@Getter
public class EntityDamageListener implements Listener {

    private final Place place;

    public EntityDamageListener() {
        place = Place.getInstance();
    }

    @EventHandler
    public void handleEntityDamage(EntityDamageEvent event) {
        event.setCancelled(!getPlace().getConfiguration().isCanTakeDamage());
    }
}
