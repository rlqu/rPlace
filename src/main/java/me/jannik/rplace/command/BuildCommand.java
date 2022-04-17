package me.jannik.rplace.command;

import lombok.Getter;
import me.jannik.rplace.Place;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {

    @Getter private final Place place;
    public BuildCommand() {
        place = Place.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if(!(commandSender instanceof Player)) {
            System.out.println("You have to be a player to do that.");
            return true;
        }

        Player player = (Player)commandSender;

        if(!player.hasPermission("place.build")) {
            player.sendMessage(getPlace().getConfiguration().getMessage("no-perms"));
            return true;
        }

        if(args.length != 0) {
            player.sendMessage(getPlace().getConfiguration().getMessage("usage").replaceAll("%USAGE%", "build"));
            return true;
        }

        if(getPlace().getBuild().contains(player.getName())) {
            getPlace().getBuild().remove(player.getName());
            player.sendMessage(getPlace().getConfiguration().getMessage("build-left"));
            return true;
        }

        getPlace().getBuild().add(player.getName());
        player.sendMessage(getPlace().getConfiguration().getMessage("build-joined"));

        return true;
    }
}
