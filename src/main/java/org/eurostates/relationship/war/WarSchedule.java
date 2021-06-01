package org.eurostates.relationship.war;

import org.eurostates.relationship.WarRelationship;

public class WarSchedule implements Runnable {

    private final WarRelationship relationship;

    public WarSchedule(WarRelationship relationship) {
        this.relationship = relationship;
    }

    @Override
    public void run() {
        //TODO
    }
}
