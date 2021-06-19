package org.eurostates.mosecommands;

import org.eurostates.mosecommands.commands.state.admin.StateCreateCommand;
import org.eurostates.mosecommands.commands.state.admin.StateDeleteCommand;
import org.eurostates.mosecommands.commands.state.admin.StateForceEditCommand;
import org.eurostates.mosecommands.commands.state.admin.StateForceHandoverCommand;
import org.eurostates.mosecommands.commands.state.global.StateInviteAcceptCommand;
import org.eurostates.mosecommands.commands.state.global.StateViewCommand;
import org.eurostates.mosecommands.commands.state.leader.*;
import org.eurostates.mosecommands.commands.technology.admin.*;
import org.eurostates.mosecommands.commands.town.admin.TownCreateCommand;
import org.eurostates.mosecommands.commands.town.admin.TownDeleteCommand;
import org.eurostates.mosecommands.commands.town.global.TownViewCommand;

import javax.swing.undo.StateEdit;

public interface ArgumentCommands {

    TownViewCommand TOWN_VIEW = new TownViewCommand();
    StateViewCommand STATE_VIEW = new StateViewCommand();
    @Deprecated
    StateEditCommand STATE_EDIT = new StateEditCommand();
    StateEditNameCommand STATE_EDIT_NAME = new StateEditNameCommand();
    StateEditLegacyColourCommand STATE_EDIT_LEGACY_COLOUR = new StateEditLegacyColourCommand();
    StateEditTagCommand STATE_EDIT_TAG = new StateEditTagCommand();
    StateEditPrefixCommand STATE_EDIT_PREFIX = new StateEditPrefixCommand();
    StateEditCurrencyCommand STATE_EDIT_CURRENCY = new StateEditCurrencyCommand();
    StateCreateCommand STATE_CREATE = new StateCreateCommand();
    StateDeleteCommand STATE_DELETE = new StateDeleteCommand();
    @Deprecated
    StateForceEditCommand STATE_FORCE_EDIT = new StateForceEditCommand();
    StateForceHandoverCommand STATE_FORCE_HANDOVER = new StateForceHandoverCommand();
    StateInviteCommand STATE_INVITE_COMMAND = new StateInviteCommand();
    StateInviteAcceptCommand STATE_INVITE_ACCEPT_COMMAND = new StateInviteAcceptCommand();

    TownCreateCommand TOWN_CREATE = new TownCreateCommand();
    TownDeleteCommand TOWN_DELETE = new TownDeleteCommand();

    TechnologyCreateCommand TECHNOLOGY_CREATE = new TechnologyCreateCommand();
    TechnologyRequirementAddCommand TECHNOLOGY_REQUIREMENT_ADD = new TechnologyRequirementAddCommand();
    TechnologyRequirementRemoveCommand TECHNOLOGY_REQUIREMENT_REMOVE = new TechnologyRequirementRemoveCommand();
    TechnologyPermissionAddCommand TECHNOLOGY_PERMISSION_ADD = new TechnologyPermissionAddCommand();
    TechnologyPermissionRemoveCommand TECHNOLOGY_PERMISSION_REMOVE = new TechnologyPermissionRemoveCommand();
    TechnologyAssignCommand TECHNOLOGY_ASSIGN = new TechnologyAssignCommand();

    //this needs to be registered. Still need to do that
    static ArgumentCommand[] getCommands() {
        return new ArgumentCommand[]{
                TOWN_VIEW
        };
    }

}
