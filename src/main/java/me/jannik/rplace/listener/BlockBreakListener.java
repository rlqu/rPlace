package me.jannik.rplace.listener;

import lombok.Getter;
import me.jannik.rplace.Place;
import me.jannik.rplace.utils.PlacePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class BlockBreakListener implements Listener {

    @Getter private final Place place;
    @Getter private final ArrayList<String> ignoreMessages;
    public BlockBreakListener() {
        place = Place.getInstance();
        ignoreMessages = new ArrayList<>();
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {


        Player player = event.getPlayer();

        if(getPlace().getBuild().contains(player.getName())) {
            return;
        }

        event.setCancelled(true);

        if(player.getItemInHand().getType() == Material.AIR || player.getItemInHand().getType() == null) {
            return;
        }

        PlacePlayer placePlayer = null;
        if(!getPlace().getPlayers().containsKey(player.getName())) getPlace().getPlayers().put(player.getName(), new PlacePlayer(player));
        placePlayer = getPlace().getPlayers().get(player.getName());

        if(placePlayer.getCurrentCountdown() > 0) {
            if(!getIgnoreMessages().contains(player.getName())) {
                placePlayer.sendMessage(getPlace().getConfiguration().getMessage("cooldown").replaceAll("%COOLDOWN%", String.valueOf(placePlayer.getCurrentCountdown())));
                getIgnoreMessages().add(player.getName());
                Bukkit.getScheduler().runTaskLater(getPlace(), () -> getIgnoreMessages().remove(player.getName()), 20L * getPlace().getConfiguration().getIgnoreMessageCountdown());
            }
            return;
        }

        if(event.getBlock().getType() == player.getItemInHand().getType()) {
            placePlayer.sendMessage(getPlace().getConfiguration().getMessage("already-typeof").replaceAll("%MATERIAL%", String.valueOf(player.getItemInHand().getType())));
            return;
        }

        if(!player.getItemInHand().getType().isBlock() || getPlace().getConfiguration().getForbiddenMaterials().contains(player.getItemInHand().getType().getId())) {
            placePlayer.sendMessage(getPlace().getConfiguration().getMessage("cant-place-block").replaceAll("%MATERIAL%", String.valueOf(player.getItemInHand().getType().toString())));
            return;
        }

        event.getBlock().setTypeIdAndData(player.getItemInHand().getTypeId(), player.getItemInHand().getData().getData(), true);
        placePlayer.setBlocksPlaced(placePlayer.getBlocksPlaced() + 1);
        placePlayer.setCurrentCountdown(getPlace().getConfiguration().getCountdown());
        placePlayer.sendMessage(getPlace().getConfiguration().getMessage("block-placed").replaceAll("%MATERIAL%", String.valueOf(player.getItemInHand().getType().toString())));

    }
}
