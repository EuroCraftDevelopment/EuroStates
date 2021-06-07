package org.eurostates.area.state;

import java.util.HashSet;
import java.util.Set;

/**
 * A class containing everything to do with multiple states
 */
public interface States {

    /**
     * The Nomad State.
     */
    NomadState NOMAD_STATE = new NomadState();

    /**
     * All known custom states in the server. These are registered on plugin {@link org.eurostates.EuroStates#onEnable() onEnable}
     */
    Set<CustomState> CUSTOM_STATES = new HashSet<>();
}
