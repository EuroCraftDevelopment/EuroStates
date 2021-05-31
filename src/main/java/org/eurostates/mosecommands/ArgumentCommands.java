package org.eurostates.mosecommands;

import org.eurostates.mosecommands.commands.town.global.TownViewCommand;

public interface ArgumentCommands {

    TownViewCommand TOWN_VIEW = new TownViewCommand();

    //this needs to be registered. Still need to do that
    static ArgumentCommand[] getCommands() {
        return new ArgumentCommand[]{
                TOWN_VIEW
        };
    }

}
