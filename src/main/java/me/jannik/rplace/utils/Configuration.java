package me.jannik.rplace.utils;

import lombok.Getter;
import lombok.Setter;
import me.jannik.rplace.Place;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Getter
public class Configuration {

    private final Place place;
    private final File file;
    private final HashMap<String, String> messages;
    private final ArrayList<Integer> forbiddenMaterials;

    @Setter private YamlConfiguration configuration;
    @Setter private Location spawnLocation;
    @Setter private boolean teleportOnJoin;
    @Setter private boolean canDropItems;
    @Setter private boolean canTakeDamage;
    @Setter private boolean canPickUpItems;
    @Setter private boolean canInteract;
    @Setter private boolean shouldSpawnCreatures;
    @Setter private int countdown;
    @Setter private int ignoreMessageCountdown;

    public Configuration(String path, String child) throws IOException {
        place = Place.getInstance();
        file = new File(path, child);
        messages = new HashMap<>();
        forbiddenMaterials = new ArrayList<>();
        configuration = YamlConfiguration.loadConfiguration(getFile());

        Location location = Bukkit.getWorlds().get(0).getSpawnLocation();
        setDefaultValue("spawn-location.x", location.getX());
        setDefaultValue("spawn-location.y", location.getY());
        setDefaultValue("spawn-location.z", location.getZ());
        setDefaultValue("spawn-location.yaw", location.getYaw());
        setDefaultValue("spawn-location.pitch", location.getPitch());
        setDefaultValue("spawn-location.world", location.getWorld().getName());

        setDefaultValue("place-countdown", 20);
        setDefaultValue("place-ignore-message-countdown", 3);
        setDefaultValue("place-teleport-spawn", true);
        setDefaultValue("place-drop-items", false);
        setDefaultValue("place-pick-up-items", false);
        setDefaultValue("place-interactions", false);
        setDefaultValue("place-damage", false);
        setDefaultValue("place-creatures", false);

        setDefaultValue("message.prefix", "§8[§6r/Place§8]§7");
        setDefaultValue("message.no-perms", "%prefix% You are not permitted to do that.");
        setDefaultValue("message.cooldown", "%prefix% You have to wait §6%COOLDOWN% §7seconds before you can place a block again.");
        setDefaultValue("message.already-typeof", "%prefix% This block is already %MATERIAL%.");
        setDefaultValue("message.block-placed", "%prefix% You successfully placed the block %MATERIAL%.");
        setDefaultValue("message.cant-place-block", "%prefix% You can't place %MATERIAL%.");
        setDefaultValue("message.build-joined", "%prefix% You entered the build mode.");
        setDefaultValue("message.build-left", "%prefix% You left the build mode.");
        setDefaultValue("message.how-to", "%prefix% Break a block to place it.");
        setDefaultValue("message.usage", "%prefix% Usage » §c/%USAGE%");
        setDefaultValue("message.setup-spawn-success", "%prefix% The spawn was successfully set.");
        setDefaultValue("message.setup-worldborder-size-success", "%prefix% The world border was set to §6%SIZE%.");
        setDefaultValue("message.setup-worldborder-center-success", "%prefix% The world border was centered to your location.");
        setDefaultValue("message.setup-worldborder-disabled-success", "%prefix% The world border was disabled.");
        setDefaultValue("message.number-format", "%prefix% You have to enter a number. For example: §7§o3.5§8, §7§o1");
        setDefaultValue("message.place-reload-success", "%prefix% Place was reloaded successfully.");
        setDefaultValue("message.join-message", "null");
        setDefaultValue("message.quit-message", "null");

        setDefaultValue("message.scoreboard-title", "§6§lr§8/§6§lPlace");
        setDefaultValue("message.scoreboard-line-0", "§8§m---------------");
        setDefaultValue("message.scoreboard-line-1", "§7Players online");
        setDefaultValue("message.scoreboard-line-2", "  §8● §6%ONLINE_COUNT%");
        setDefaultValue("message.scoreboard-line-3", "§b");
        setDefaultValue("message.scoreboard-line-4", "§7Cool down");
        setDefaultValue("message.scoreboard-line-5", "  §8● §6%COOLDOWN% §7seconds");
        setDefaultValue("message.scoreboard-line-6", "§a");
        setDefaultValue("message.scoreboard-line-7", "§7Blocks placed");
        setDefaultValue("message.scoreboard-line-8", "  §8● §6%BLOCK_COUNTER%");
        setDefaultValue("message.scoreboard-line-9", "null");
        setDefaultValue("message.scoreboard-line-10", "null");
        setDefaultValue("message.scoreboard-line-11", "null");
        setDefaultValue("message.scoreboard-line-12", "null");
        setDefaultValue("message.scoreboard-line-13", "null");
        setDefaultValue("message.scoreboard-line-14", "null");
        setDefaultValue("message.scoreboard-line-15", "null");
        setDefaultValue("message.actionbar", "§6r§8/§6Place §7by §7§ohttps://github.com/rlqu");
        setDefaultForbiddenItems(44, 53, 67, 95, 108, 109, 114, 126, 128, 134, 135, 136, 139, 156, 164, 180, 182, 6, 30,
                                 31, 32, 37, 38, 39, 40, 50, 54, 65, 78, 81, 85, 97, 101, 102, 106, 111, 113, 116, 120,
                                 130, 145, 146, 160, 171, 175, 188, 189, 190, 191, 192, 321, 323, 355, 389, 390, 397,
                                 416, 425, 69, 70, 72, 76, 77, 96, 107, 131, 143, 147, 148, 151, 154, 167, 183, 184,
                                 185, 186, 187, 324, 330, 331, 356, 404, 427, 428, 429, 430, 431, 27, 28, 66, 157, 138,
                                 378, 379, 380, 7, 12, 13, 20);

        spawnLocation = new Location(Bukkit.getWorld(getConfiguration().getString("spawn-location.world")),
                        getConfiguration().getDouble("spawn-location.x"),
                        getConfiguration().getDouble("spawn-location.y"),
                        getConfiguration().getDouble("spawn-location.z"),
                        (float) getConfiguration().getDouble("spawn-location.yaw"),
                        (float) getConfiguration().getDouble("spawn-location.pitch"));

        initializeMessages();
        initializeForbiddenMaterials();

        teleportOnJoin = getConfiguration().getBoolean("place-teleport-spawn");
        countdown = getConfiguration().getInt("place-countdown");
        ignoreMessageCountdown = getConfiguration().getInt("place-ignore-message-countdown") + 1;
        canInteract = getConfiguration().getBoolean("place-interactions");
        canPickUpItems = getConfiguration().getBoolean("place-pick-up-items");
        canDropItems = getConfiguration().getBoolean("place-drop-items");
        shouldSpawnCreatures = getConfiguration().getBoolean("place-creatures");

    }

    private void setDefaultForbiddenItems(Integer... toForbid) throws IOException {
        setDefaultValue("forbidden-materials", Arrays.asList(toForbid));
    }
    private void setDefaultValue(String key, Object value) throws IOException {
        if(getConfiguration().get(key) == null) getConfiguration().set(key, value);
        getConfiguration().save(getFile());
    }
    public void initializeMessages() {

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(getFile());
        getMessages().clear();

        yamlConfiguration.getConfigurationSection("message").getKeys(true).forEach(key -> {
            getMessages().put(key, yamlConfiguration.getString("message." + key));
        });
    }
    public void initializeForbiddenMaterials() {

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(getFile());
        getForbiddenMaterials().clear();

        yamlConfiguration.getIntegerList("forbidden-materials").forEach(material -> {
            try {
                getForbiddenMaterials().add(material);
            }catch(Exception exception) {
                Bukkit.getConsoleSender().sendMessage(getMessage("prefix") + "§cConfig error: ID '" + material + "' is not a material.");
            }
        });
    }
    public String getMessage(String key) {

        return getMessages().containsKey(key) ? getMessages().get(key).replaceAll("%prefix%", getMessages().get("prefix")) : getPlace().getDefaults().getMessage(key).replaceAll("%prefix%", getPlace().getDefaults().getDefaultMessages().get("prefix"));
    }

}
