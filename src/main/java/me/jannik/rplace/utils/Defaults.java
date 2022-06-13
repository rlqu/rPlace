package me.jannik.rplace.utils;

import lombok.Getter;

import java.util.HashMap;

public class Defaults {

    @Getter private HashMap<String, String> defaultMessages;

    public Defaults() {
        defaultMessages = new HashMap<>();

        getDefaultMessages().put("prefix", "§8[§6r/Place§8]§7");
        getDefaultMessages().put("no-perms", "%prefix% You are not permitted to do that.");
        getDefaultMessages().put("cooldown", "%prefix% You have to wait §6%COOLDOWN% §7seconds before you can place a block again.");
        getDefaultMessages().put("already-typeof", "%prefix% This block is already %MATERIAL%.");
        getDefaultMessages().put("block-placed", "%prefix% You successfully placed the block %MATERIAL%.");
        getDefaultMessages().put("cant-place-block", "%prefix% You can't place %MATERIAL%.");
        getDefaultMessages().put("build-joined", "%prefix% You entered the build mode.");
        getDefaultMessages().put("build-left", "%prefix% You left the build mode.");
        getDefaultMessages().put("how-to", "%prefix% Break a block to place it.");
        getDefaultMessages().put("usage", "%prefix% Usage » §c/%USAGE%");
        getDefaultMessages().put("setup-spawn-success", "%prefix% The spawn was successfully set.");
        getDefaultMessages().put("setup-worldborder-size-success", "%prefix% The world border was set to §6%SIZE%.");
        getDefaultMessages().put("setup-worldborder-center-success", "%prefix% The world border was centered to your location.");
        getDefaultMessages().put("setup-worldborder-disabled-success", "%prefix% The world border was disabled.");
        getDefaultMessages().put("number-format", "%prefix% You have to enter a number. For example: §7§o3.5§8, §7§o1");
        getDefaultMessages().put("place-reload-success", "%prefix% Place was reloaded successfully.");
        getDefaultMessages().put("join-message", "null");
        getDefaultMessages().put("quit-message", "null");

        getDefaultMessages().put("scoreboard-title", "§6§lr§8/§6§lPlace");
        getDefaultMessages().put("scoreboard-line-0", "§8§m---------------");
        getDefaultMessages().put("scoreboard-line-1", "§7Players online");
        getDefaultMessages().put("scoreboard-line-2", "  §8● §6%ONLINE_COUNT%");
        getDefaultMessages().put("scoreboard-line-3", "§b");
        getDefaultMessages().put("scoreboard-line-4", "§7Cool down");
        getDefaultMessages().put("scoreboard-line-5", "  §8● §6%COOLDOWN% §7seconds");
        getDefaultMessages().put("scoreboard-line-6", "§a");
        getDefaultMessages().put("scoreboard-line-7", "§7Blocks placed");
        getDefaultMessages().put("scoreboard-line-8", "  §8● §6%BLOCK_COUNTER%");
        getDefaultMessages().put("scoreboard-line-9", "null");
        getDefaultMessages().put("scoreboard-line-10", "null");
        getDefaultMessages().put("scoreboard-line-11", "null");
        getDefaultMessages().put("scoreboard-line-12", "null");
        getDefaultMessages().put("scoreboard-line-13", "null");
        getDefaultMessages().put("scoreboard-line-14", "null");
        getDefaultMessages().put("scoreboard-line-15", "null");
        getDefaultMessages().put("actionbar", "§6r§8/§6Place §7by §ohttps://github.com/xwhs");

    }

    public String getMessage(String key) {
        System.out.println("[Place] Pulled a message from defaults. Message key: " + key);
        System.out.println("[Place] Simply add the message to the config file with this key.");
        return getDefaultMessages().get(key);
    }
}
