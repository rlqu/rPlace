package me.jannik.rplace.listener;

import lombok.Getter;
import me.jannik.rplace.Place;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

@Getter
public class PlayerPickupItemListener implements Listener {

    private final Place place;

    public PlayerPickupItemListener() {
        place = Place.getInstance();
    }

    @EventHandler
    public void handlePlayerPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(!getPlace().getBuild().contains(event.getPlayer().getName()) && !getPlace().getConfiguration().isCanPickUpItems());
    }
}
