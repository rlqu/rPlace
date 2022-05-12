package me.jannik.rplace.listener;

import lombok.Getter;
import me.jannik.rplace.Place;
import me.jannik.rplace.utils.PlacementMethod;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

@Getter
public class BlockPlaceListener implements Listener {

    private final Place place;

    public BlockPlaceListener() {
        place = Place.getInstance();
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();

        if(!getPlace().getBuild().contains(player.getName())) {
            event.setCancelled(true);
        }
    }
}
