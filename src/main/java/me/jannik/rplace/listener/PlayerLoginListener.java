package me.jannik.rplace.listener;

import lombok.Getter;
import me.jannik.rplace.Place;
import me.jannik.rplace.utils.PlacePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    @Getter private final Place place;
    public PlayerLoginListener() {
        place = Place.getInstance();
    }

    @EventHandler
    public void handlePlayerLogin(PlayerLoginEvent event) {

        Player player = event.getPlayer();
        if(!getPlace().getPlayers().containsKey(player.getName())) getPlace().getPlayers().put(player.getName(), new PlacePlayer(player));

    }
}
