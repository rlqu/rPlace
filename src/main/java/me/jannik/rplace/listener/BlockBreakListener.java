package me.jannik.rplace.listener;

import lombok.Getter;
import me.jannik.rplace.Place;
import me.jannik.rplace.utils.PlacePlayer;
import me.jannik.rplace.utils.PlacementMethod;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class BlockBreakListener implements Listener {

    @Getter private final Place place;
    public BlockBreakListener() {
        place = Place.getInstance();
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if(getPlace().getBuild().contains(player.getName())) {
            return;
        }

        event.setCancelled(true);

        if(getPlace().getConfiguration().getPlacementMethod() != PlacementMethod.BREAK) {
            if(!place.getMessageIgnore().isIgnoring(player, "how-to")) {
                player.sendMessage(getPlace().getConfiguration().getMessage("how-to"));
                place.getMessageIgnore().put(player, "how-to");
            }
            return;
        }

        if(player.getItemInHand().getType() == Material.AIR || player.getItemInHand().getType() == null) {
            return;
        }

        PlacePlayer placePlayer = null;
        if(!getPlace().getPlayers().containsKey(player.getName())) getPlace().getPlayers().put(player.getName(), new PlacePlayer(player));
        placePlayer = getPlace().getPlayers().get(player.getName());

        if(placePlayer.getCurrentCountdown() > 0) {
            if(!place.getMessageIgnore().isIgnoring(player, "cooldown")) {
                placePlayer.sendMessage(getPlace().getConfiguration().getMessage("cooldown").replaceAll("%COOLDOWN%", String.valueOf(placePlayer.getCurrentCountdown())));
                place.getMessageIgnore().put(player, "cooldown");
            }
            return;
        }

        if(event.getBlock().getType() == player.getItemInHand().getType()) {

            if(!place.getMessageIgnore().isIgnoring(player, "already-typeof")) {
                placePlayer.sendMessage(getPlace().getConfiguration().getMessage("already-typeof").replaceAll("%MATERIAL%", String.valueOf(player.getItemInHand().getType())));
                place.getMessageIgnore().put(player, "already-typeof");
            }

            return;
        }

        if(!player.getItemInHand().getType().isBlock() || getPlace().getConfiguration().getForbiddenMaterials().contains(player.getItemInHand().getType().getId())) {
            if(!place.getMessageIgnore().isIgnoring(player, "cant-place-block")) {
                placePlayer.sendMessage(getPlace().getConfiguration().getMessage("cant-place-block").replaceAll("%MATERIAL%", String.valueOf(player.getItemInHand().getType().toString())));
                place.getMessageIgnore().put(player, "cant-place-block");
            }
            return;
        }

        event.getBlock().setTypeIdAndData(player.getItemInHand().getTypeId(), player.getItemInHand().getData().getData(), true);
        placePlayer.setBlocksPlaced(placePlayer.getBlocksPlaced() + 1);
        placePlayer.setCurrentCountdown(getPlace().getConfiguration().getCountdown());

        if(!place.getMessageIgnore().isIgnoring(player, "block-placed")) {
            placePlayer.sendMessage(getPlace().getConfiguration().getMessage("block-placed").replaceAll("%MATERIAL%", String.valueOf(player.getItemInHand().getType().toString())));
            place.getMessageIgnore().put(player, "block-placed");
        }

    }
}
