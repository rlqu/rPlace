package me.jannik.rplace.utils;

import me.jannik.rplace.Place;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageIgnore {

    private HashMap<String, ArrayList<String>> ignores;

    public MessageIgnore() {
        ignores = new HashMap<>();
    }

    public void put(Player player, String messageType) {
        if(!ignores.containsKey(messageType)) {
            ignores.put(messageType, new ArrayList<>());
        }

        ignores.get(messageType).add(player.getName());
        Bukkit.getScheduler().runTaskLater(Place.getInstance(), () -> ignores.get(messageType).remove(player.getName()), 20L *Place.getInstance().getConfiguration().getIgnoreMessageCountdown());
    }

    public boolean isIgnoring(Player player, String messageType) {
        return ignores.containsKey(messageType) && ignores.get(messageType).contains(player.getName());
    }

}
