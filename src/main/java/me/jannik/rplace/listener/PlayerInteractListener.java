package me.jannik.rplace.listener;

import lombok.Getter;
import me.jannik.rplace.Place;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@Getter
public class PlayerInteractListener implements Listener {

    private final Place place;

    public PlayerInteractListener() {
        place = Place.getInstance();
    }

    @EventHandler
    public void handlePlayerInteract(PlayerInteractEvent event) {
        if(!getPlace().getBuild().contains(event.getPlayer().getName()) && !getPlace().getConfiguration().isCanInteract()) {
            event.setCancelled(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK);
        }
    }
}
