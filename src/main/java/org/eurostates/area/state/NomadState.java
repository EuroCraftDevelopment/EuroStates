package org.eurostates.area.state;

import org.bukkit.ChatColor;
import org.eurostates.area.ESUser;
import org.eurostates.area.town.Town;

import java.util.Collections;
import java.util.Set;

public class NomadState implements State {

    NomadState() {
        //so you can't call "new NomadState"
    }

    @Override
    public String getTag() {
        return "NOMAD";
    }

    @Override
    public String getName() {
        return "NOMAD";
    }

    @Override
    public char getLegacyChatColourCharacter() {
        return ChatColor.WHITE.getChar();
    }

    @Override
    public Set<String> getRanks() {
        return Collections.emptySet();
    }

    @Override
    public Set<ESUser> getEuroStatesCitizens() {
        return Collections.emptySet();
    }

    @Override
    public Set<Town> getTowns() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getPermissions() {
        return Collections.emptySet();
    }
}
