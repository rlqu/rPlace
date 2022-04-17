package me.jannik.rplace;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.jannik.rplace.command.BuildCommand;
import me.jannik.rplace.command.ReloadPlaceCommand;
import me.jannik.rplace.command.SetupCommand;
import me.jannik.rplace.scoreboard.PlayerScoreboard;
import me.jannik.rplace.utils.Configuration;
import me.jannik.rplace.utils.Defaults;
import me.jannik.rplace.utils.MessageIgnore;
import me.jannik.rplace.utils.PlacePlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Getter
public class Place extends JavaPlugin {

    @Getter private static Place instance;
    @Setter private Configuration configuration;

    private MessageIgnore messageIgnore;
    private Defaults defaults;
    private HashMap<String, PlacePlayer> players;
    private HashMap<String, PlayerScoreboard> scoreboards;
    private ArrayList<String> build;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;
        configuration = new Configuration("plugins/rPlace", "config.yml");
        players = new HashMap<>();
        build = new ArrayList<>();
        scoreboards = new HashMap<>();
        defaults = new Defaults();
        messageIgnore = new MessageIgnore();

        register();
        getCommand("build").setExecutor(new BuildCommand());
        getCommand("setup").setExecutor(new SetupCommand());
        getCommand("reloadplace").setExecutor(new ReloadPlaceCommand());

        Bukkit.getConsoleSender().sendMessage("Place was activated successfully.");

        Bukkit.getScheduler().runTaskTimer(getInstance(), () -> {
            if(!getPlayers().isEmpty()) {
                getPlayers().forEach((s, placePlayer) -> {
                    if(placePlayer.getCurrentCountdown() > 0) placePlayer.setCurrentCountdown(placePlayer.getCurrentCountdown() - 1);
                    placePlayer.updateScoreboard();
                });
            }
        }, 0L, 20L);

        Bukkit.getScheduler().runTaskLater(getInstance(), () -> {

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.setGameMode(GameMode.CREATIVE);
                if(!getPlayers().containsKey(player.getName())) getPlayers().put(player.getName(), new PlacePlayer(player));
            });

            if(!getConfiguration().isShouldSpawnCreatures()) {
                Bukkit.getWorld(getConfiguration().getSpawnLocation().getWorld().getName()).getEntities().forEach(entity -> {
                    if(entity.getType() != EntityType.ARMOR_STAND && entity.getType() != EntityType.PLAYER) {
                        entity.remove();
                    }
                });
            }

        }, 3);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("Place was deactivated successfully.");
    }

    private void register() {
        Reflections reflections = new Reflections("me.jannik.rplace.listener", new SubTypesScanner(false));

        new HashSet<>(reflections.getSubTypesOf(Object.class)).forEach(aClass -> {
            if(Listener.class.isAssignableFrom(aClass)) {

                Object clazz = null;
                try {
                    clazz = aClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                Bukkit.getPluginManager().registerEvents((Listener) clazz, getInstance());
            }
        });
    }
    public void reload() throws IOException {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(getConfiguration().getFile());

        getConfiguration().setCanDropItems(yamlConfiguration.getBoolean("place-drop-items"));
        getConfiguration().setCanInteract(yamlConfiguration.getBoolean("place-interactions"));
        getConfiguration().setCanInteract(yamlConfiguration.getBoolean("place-pick-up-items"));
        getConfiguration().setCanTakeDamage(yamlConfiguration.getBoolean("place-damage"));
        getConfiguration().setCountdown(yamlConfiguration.getInt("place-countdown"));
        getConfiguration().setIgnoreMessageCountdown(yamlConfiguration.getInt("place-ignore-message-countdown"));
        getConfiguration().setTeleportOnJoin(yamlConfiguration.getBoolean("place-teleport-spawn"));
        getConfiguration().setSpawnLocation(new Location(Bukkit.getWorld(yamlConfiguration.getString("spawn-location.world")), yamlConfiguration.getDouble("spawn-location.x"), yamlConfiguration.getDouble("spawn-location.y"), yamlConfiguration.getDouble("spawn-location.z"), (float) yamlConfiguration.getDouble("spawn-location.yaw"), (float) yamlConfiguration.getDouble("spawn-location.pitch")));
        getConfiguration().initializeMessages();
        getConfiguration().initializeForbiddenMaterials();
    }

}
