package me.jannik.rplace.scoreboard;

import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;


public interface ScoreboardText {

    void apply(Team team);

    @Nullable
    String getIdentifier();

}
