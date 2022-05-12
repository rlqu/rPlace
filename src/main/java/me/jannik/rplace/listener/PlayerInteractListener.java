package me.jannik.rplace.listener;

import lombok.Getter;
import me.jannik.rplace.Place;
import me.jannik.rplace.utils.PlacePlayer;
import me.jannik.rplace.utils.PlacementMethod;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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

        Player player = event.getPlayer();

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(place.getConfiguration().getPlacementMethod() == PlacementMethod.BUILD) {

                event.setCancelled(true);
                Block block = event.getClickedBlock();

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

                if(block.getType() == player.getItemInHand().getType()) {

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

                block.setTypeIdAndData(player.getItemInHand().getTypeId(), player.getItemInHand().getData().getData(), true);
                placePlayer.setBlocksPlaced(placePlayer.getBlocksPlaced() + 1);
                placePlayer.setCurrentCountdown(getPlace().getConfiguration().getCountdown());

                if(!place.getMessageIgnore().isIgnoring(player, "block-placed")) {
                    placePlayer.sendMessage(getPlace().getConfiguration().getMessage("block-placed").replaceAll("%MATERIAL%", String.valueOf(player.getItemInHand().getType().toString())));
                    place.getMessageIgnore().put(player, "block-placed");
                }

                return;
            }

            if(!place.getMessageIgnore().isIgnoring(player, "how-to")) {
                player.sendMessage(getPlace().getConfiguration().getMessage("how-to"));
                place.getMessageIgnore().put(player, "how-to");
            }
        }

        if(!getPlace().getBuild().contains(event.getPlayer().getName()) && !getPlace().getConfiguration().isCanInteract()) {
            event.setCancelled(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK);
        }
    }
}
