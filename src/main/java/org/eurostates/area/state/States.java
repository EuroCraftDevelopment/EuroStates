package org.eurostates.area.state;

import java.util.HashSet;
import java.util.Set;

public interface States {

    NomadState NOMAD_STATE = new NomadState();
    Set<CustomState> CUSTOM_STATES = new HashSet<>();
}
