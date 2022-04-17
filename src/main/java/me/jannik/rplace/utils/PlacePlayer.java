package me.jannik.rplace.utils;

import com.mysql.jdbc.UpdatableResultSet;
import lombok.Getter;
import lombok.Setter;
import me.jannik.rplace.Place;
import me.jannik.rplace.scoreboard.PlayerScoreboard;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;

@Getter
public class PlacePlayer {

    private final Place place;
    private final Player bukkitPlayer;
    @Setter private int currentCountdown;
    @Setter private int blocksPlaced;

    public PlacePlayer(Player bukkitPlayer) {

        this.bukkitPlayer = bukkitPlayer;
        place = Place.getInstance();

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(getPlace().getConfiguration().getFile());
        currentCountdown = yamlConfiguration.getInt("count-down." + getBukkitPlayer().getUniqueId().toString());
        blocksPlaced = yamlConfiguration.getInt("blocks-placed." + getBukkitPlayer().getUniqueId().toString());

    }

    public void sendMessage(String message) {
        getBukkitPlayer().sendMessage(message);
    }
    public void quit() throws IOException {

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(getPlace().getConfiguration().getFile());
        yamlConfiguration.set("blocks-placed." + getBukkitPlayer().getUniqueId().toString(), getBlocksPlaced());
        yamlConfiguration.set("count-down." + getBukkitPlayer().getUniqueId().toString(), getCurrentCountdown());

        yamlConfiguration.save(getPlace().getConfiguration().getFile());
    }

    public void setScoreboard() {
        PlayerScoreboard playerScoreboard = new PlayerScoreboard(getPlace().getConfiguration().getMessage("scoreboard-title"));

        setEntries(playerScoreboard);

        playerScoreboard.update();
        playerScoreboard.setScoreboard(getBukkitPlayer());
        getPlace().getScoreboards().put(getBukkitPlayer().getName(), playerScoreboard);
    }

    public void updateScoreboard() {

        if(getPlace().getScoreboards().get(getBukkitPlayer().getName()) == null) {
            setScoreboard();
        }

        PlayerScoreboard playerScoreboard = getPlace().getScoreboards().get(getBukkitPlayer().getName());

        playerScoreboard.clearEntries();
        setEntries(playerScoreboard);
        playerScoreboard.setScoreboard(getBukkitPlayer());

        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(getPlace().getConfiguration().getMessage("actionbar")), (byte)2);
        ((CraftPlayer)getBukkitPlayer()).getHandle().playerConnection.sendPacket(packet);
    }

    private void setEntries(PlayerScoreboard playerScoreboard) {
        for (int i = 0; i < 15; i++) {

            String line = getPlace().getConfiguration().getMessage("scoreboard-line-" + i);

            if(!line.equalsIgnoreCase("null")) {
                playerScoreboard.addEntry(line.replaceAll("%ONLINE_COUNT%", String.valueOf(Bukkit.getOnlinePlayers().size())).replaceAll("%COOLDOWN%", String.valueOf(getCurrentCountdown())).replaceAll("%BLOCK_COUNTER%", String.valueOf(getBlocksPlaced())));
            }
        }

        playerScoreboard.update();
    }
}
