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

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(getPlace().getConfiguration().getPlacementMethod() == PlacementMethod.BUILD) {

                Block block = event.getClickedBlock();
                Player player = event.getPlayer();

                if(player.getItemInHand().getType() == Material.AIR || player.getItemInHand().getType() == null) {
                    return;
                }

                PlacePlayer placePlayer = null;
                if(!getPlace().getPlayers().containsKey(player.getName())) getPlace().getPlayers().put(player.getName(), new PlacePlayer(player));
                placePlayer = getPlace().getPlayers().get(player.getName());

                if(placePlayer.getCurrentCountdown() > 0) {
                    if(!getPlace().getMessageIgnore().isIgnoring(player, "cooldown")) {
                        placePlayer.sendMessage(getPlace().getConfiguration().getMessage("cooldown").replaceAll("%COOLDOWN%", String.valueOf(placePlayer.getCurrentCountdown())));
                        getPlace().getMessageIgnore().put(player, "cooldown");
                    }
                    return;
                }

                if(block.getType() == player.getItemInHand().getType()) {

                    if(!getPlace().getMessageIgnore().isIgnoring(player, "already-typeof")) {
                        placePlayer.sendMessage(getPlace().getConfiguration().getMessage("already-typeof").replaceAll("%MATERIAL%", String.valueOf(player.getItemInHand().getType())));
                        getPlace().getMessageIgnore().put(player, "already-typeof");
                    }

                    return;
                }

                if(!player.getItemInHand().getType().isBlock() || getPlace().getConfiguration().getForbiddenMaterials().contains(player.getItemInHand().getType().getId())) {
                    if(!getPlace().getMessageIgnore().isIgnoring(player, "cant-place-block")) {
                        placePlayer.sendMessage(getPlace().getConfiguration().getMessage("cant-place-block").replaceAll("%MATERIAL%", String.valueOf(player.getItemInHand().getType().toString())));
                        getPlace().getMessageIgnore().put(player, "cant-place-block");
                    }
                    return;
                }

                block.setTypeIdAndData(player.getItemInHand().getTypeId(), player.getItemInHand().getData().getData(), true);
                placePlayer.setBlocksPlaced(placePlayer.getBlocksPlaced() + 1);
                placePlayer.setCurrentCountdown(getPlace().getConfiguration().getCountdown());

                if(!getPlace().getMessageIgnore().isIgnoring(player, "block-placed")) {
                    placePlayer.sendMessage(getPlace().getConfiguration().getMessage("block-placed").replaceAll("%MATERIAL%", String.valueOf(player.getItemInHand().getType().toString())));
                    getPlace().getMessageIgnore().put(player, "block-placed");
                }

            }
        }

        if(!getPlace().getBuild().contains(event.getPlayer().getName()) && !getPlace().getConfiguration().isCanInteract()) {
            event.setCancelled(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK);
        }
    }
}
