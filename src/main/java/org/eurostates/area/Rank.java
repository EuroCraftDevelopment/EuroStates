package org.eurostates.area;

import org.eurostates.area.state.State;

import java.util.UUID;

public class Rank {

    private final UUID id;
    private final State state;
    private String display;

    public Rank(State state, UUID id, String display) {
        this.id = id;
        this.display = display;
        this.state = state;
    }

    public UUID getId() {
        return this.id;
    }

    public State getState(){
        return this.state;
    }

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
