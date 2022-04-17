package me.jannik.rplace.listener;

import lombok.Getter;
import me.jannik.rplace.Place;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

@Getter
public class BlockPlaceListener implements Listener {

    private final Place place;
    private final ArrayList<String> ignoreMessages;

    public BlockPlaceListener() {
        place = Place.getInstance();
        ignoreMessages = new ArrayList<>();
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();

        if(!getPlace().getBuild().contains(player.getName())) {
            event.setCancelled(true);
            if(!getIgnoreMessages().contains(player.getName())) {
                player.sendMessage(getPlace().getConfiguration().getMessage("how-to"));

                getIgnoreMessages().add(player.getName());
                Bukkit.getScheduler().runTaskLater(getPlace(), () -> getIgnoreMessages().remove(player.getName()), 20L * getPlace().getConfiguration().getIgnoreMessageCountdown());
            }
        }
    }
}
