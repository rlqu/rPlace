package me.jannik.rplace.listener;

import lombok.Getter;
import me.jannik.rplace.Place;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

@Getter
public class PlayerDropItemListener implements Listener {

    private final Place place;
    private final ItemStack airItemStack;

    public PlayerDropItemListener() {
        place = Place.getInstance();
        airItemStack = new ItemStack(Material.AIR);
    }

    @EventHandler
    public void handlePlayerDropItem(PlayerDropItemEvent event) {
        if(!getPlace().getBuild().contains(event.getPlayer().getName()) && !getPlace().getConfiguration().isCanDropItems()) {
            event.getItemDrop().remove();
        }
    }
}