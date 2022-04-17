package me.jannik.rplace.listener;

import lombok.Getter;
import lombok.SneakyThrows;
import me.jannik.rplace.Place;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @Getter private final Place place;
    public PlayerQuitListener() {
        place = Place.getInstance();
    }

    @SneakyThrows
    @EventHandler
    public void handlePlayerQuit(PlayerQuitEvent event) {

        String quitMessage = getPlace().getConfiguration().getMessage("quit-message");
        event.setQuitMessage(quitMessage.equalsIgnoreCase("null") ? null : quitMessage);

        if(getPlace().getPlayers().containsKey(event.getPlayer().getName())) {
            getPlace().getPlayers().get(event.getPlayer().getName()).quit();
            getPlace().getPlayers().remove(event.getPlayer().getName());
        }

    }
}
