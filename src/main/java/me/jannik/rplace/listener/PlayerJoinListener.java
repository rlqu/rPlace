package me.jannik.rplace.listener;

import lombok.Getter;
import me.jannik.rplace.Place;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @Getter private final Place place;
    public PlayerJoinListener() {
        place = Place.getInstance();
    }

    @EventHandler
    public void handlePlayerJoin(PlayerJoinEvent event) {

        String joinMessage = getPlace().getConfiguration().getMessage("join-message").replaceAll("%PLAYER_NAME%", event.getPlayer().getName());
        event.setJoinMessage(joinMessage.equalsIgnoreCase("null") ? null : joinMessage);

        Bukkit.getScheduler().runTaskLater(getPlace(), () -> {
            Player player = event.getPlayer();
            player.setGameMode(GameMode.CREATIVE);
            getPlace().getPlayers().get(player.getName()).setScoreboard();

            if(getPlace().getConfiguration().isTeleportOnJoin()) player.teleport(getPlace().getConfiguration().getSpawnLocation());
        }, 1);

    }

}
