package me.jannik.rplace.command;

import lombok.Getter;
import lombok.SneakyThrows;
import me.jannik.rplace.Place;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadPlaceCommand implements CommandExecutor {

    @Getter
    private final Place place;
    public ReloadPlaceCommand() {
        place = Place.getInstance();
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if(!(commandSender instanceof Player)) {

            commandSender.sendMessage("[PLACE] Place was reloaded");
            getPlace().reload();
            return true;
        }

        Player player = (Player)commandSender;

        if(!player.hasPermission("place.reload")) {
            player.sendMessage(getPlace().getConfiguration().getMessage("no-perms"));
            return true;
        }

        if(args.length != 0) {
            player.sendMessage(getPlace().getConfiguration().getMessage("usage").replaceAll("%USAGE%", "reloadplace"));
            return true;
        }

        getPlace().reload();
        player.sendMessage(getPlace().getConfiguration().getMessage("place-reload-success"));

        return true;
    }
}
