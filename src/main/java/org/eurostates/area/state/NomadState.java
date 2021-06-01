package org.eurostates.area.state;

import org.bukkit.ChatColor;
import org.eurostates.area.ESUser;
import org.eurostates.area.town.Town;
import org.eurostates.technology.Technology;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

public class NomadState implements State {

    NomadState() {
        //so you can't call "new NomadState"
    }

    @Override
    public @NotNull String getTag() {
        return "NOMAD";
    }

    @Override
    public @NotNull String getName() {
        return "NOMAD";
    }

    @Override
    public char getLegacyChatColourCharacter() {
        return ChatColor.WHITE.getChar();
    }

    @Override
    public @NotNull Set<String> getRanks() {
        return Collections.emptySet();
    }

    @Override
    public @NotNull Set<ESUser> getEuroStatesCitizens() {
        return Collections.emptySet();
    }

    @Override
    public @NotNull Set<Town> getTowns() {
        return Collections.emptySet();
    }

    @Override
    public @NotNull Set<Technology> getTechnology() {
        return Collections.emptySet();
    }
}
