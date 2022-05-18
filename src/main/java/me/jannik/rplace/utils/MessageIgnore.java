package me.jannik.rplace.utils;

import lombok.Getter;
import me.jannik.rplace.Place;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageIgnore {

    @Getter private final HashMap<String, ArrayList<String>> ignores;

    public MessageIgnore() {
        ignores = new HashMap<>();
    }

    public void put(Player player, String messageType) {
        if(!getIgnores().containsKey(messageType)) {
            getIgnores().put(messageType, new ArrayList<>());
        }

        getIgnores().get(messageType).add(player.getName());
        Bukkit.getScheduler().runTaskLater(Place.getInstance(), () -> getIgnores().get(messageType).remove(player.getName()), 20L * Place.getInstance().getConfiguration().getIgnoreMessageCountdown());
    }

    public boolean isIgnoring(Player player, String messageType) {
        return getIgnores().containsKey(messageType) && getIgnores().get(messageType).contains(player.getName());
    }

}
