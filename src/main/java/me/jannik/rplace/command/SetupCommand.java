package me.jannik.rplace.command;

import lombok.Getter;
import lombok.SneakyThrows;
import me.jannik.rplace.Place;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
@Getter
public class SetupCommand implements CommandExecutor {

    private final Place place;
    private Player player;
    public SetupCommand() {
        place = Place.getInstance();
    }

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if(!(commandSender instanceof Player)) {
            System.out.println("You have to be a player to do that.");
            return true;
        }

        player = (Player)commandSender;

        if(!getPlayer().hasPermission("place.setup")) {
            getPlayer().sendMessage(getPlace().getConfiguration().getMessage("no-perms"));
            return true;
        }

        if(args.length != 1 && args.length != 2) {
            sendUsage();
            return true;
        }

        if(args.length == 1) {

            if(!args[0].equalsIgnoreCase("spawn")) {
                sendUsage();
                return true;
            }

            Location location = getPlayer().getLocation();

            getPlace().getConfiguration().getConfiguration().set("spawn-location.x", location.getX());
            getPlace().getConfiguration().getConfiguration().set("spawn-location.y", location.getY());
            getPlace().getConfiguration().getConfiguration().set("spawn-location.z", location.getZ());
            getPlace().getConfiguration().getConfiguration().set("spawn-location.yaw", location.getYaw());
            getPlace().getConfiguration().getConfiguration().set("spawn-location.pitch", location.getPitch());
            getPlace().getConfiguration().getConfiguration().set("spawn-location.world", location.getWorld().getName());
            getPlace().getConfiguration().getConfiguration().save(getPlace().getConfiguration().getFile());

            getPlace().reload();
            getPlayer().sendMessage(getPlace().getConfiguration().getMessage("setup-spawn-success"));

            return true;
        }

        if(!args[0].equalsIgnoreCase("worldborder")) {
            sendUsage();
            return true;
        }

        if(args[1].equalsIgnoreCase("center")) {

            getPlayer().getWorld().getWorldBorder().setCenter(getPlayer().getLocation());
            getPlayer().sendMessage(getPlace().getConfiguration().getMessage("setup-worldborder-center-success"));
            return true;
        }

        if(args[1].equalsIgnoreCase("off")) {

            getPlayer().getWorld().getWorldBorder().reset();
            getPlayer().sendMessage(getPlace().getConfiguration().getMessage("setup-worldborder-disabled-success"));
            return true;
        }

        try {

            double size = Double.parseDouble(args[1]);
            getPlayer().getWorld().getWorldBorder().setSize(size);
            getPlayer().sendMessage(getPlace().getConfiguration().getMessage("setup-worldborder-size-success").replaceAll("%SIZE%", String.valueOf(size)));

        }catch (NumberFormatException numberFormatException) {
            getPlayer().sendMessage(getPlace().getConfiguration().getMessage("number-format"));
        }

        return true;
    }

    private void sendUsage() {
        getPlayer().sendMessage(getPlace().getConfiguration().getMessage("usage").replaceAll("%USAGE%", "setup spawn/ worldborder center/off/<Size>"));
    }
}
