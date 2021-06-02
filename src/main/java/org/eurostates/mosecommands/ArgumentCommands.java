package org.eurostates.mosecommands;

import org.eurostates.mosecommands.commands.state.admin.StateCreateCommand;
import org.eurostates.mosecommands.commands.state.admin.StateDeleteCommand;
import org.eurostates.mosecommands.commands.state.admin.StateForceEditCommand;
import org.eurostates.mosecommands.commands.state.admin.StateForceHandoverCommand;
import org.eurostates.mosecommands.commands.state.global.StateViewCommand;
import org.eurostates.mosecommands.commands.state.leader.StateEditCommand;
import org.eurostates.mosecommands.commands.town.global.TownViewCommand;

public interface ArgumentCommands {

    TownViewCommand TOWN_VIEW = new TownViewCommand();
    StateViewCommand STATE_VIEW = new StateViewCommand();
    StateEditCommand STATE_EDIT = new StateEditCommand();
    StateCreateCommand STATE_CREATE = new StateCreateCommand();
    StateDeleteCommand STATE_DELETE = new StateDeleteCommand();
    StateForceEditCommand STATE_FORCE_EDIT = new StateForceEditCommand();
    StateForceHandoverCommand STATE_FORCE_HANDOVER = new StateForceHandoverCommand();

    //this needs to be registered. Still need to do that
    static ArgumentCommand[] getCommands() {
        return new ArgumentCommand[]{
                TOWN_VIEW
        };
    }

}
