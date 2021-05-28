package org.eurostates.area.town;

import org.bukkit.Location;

import java.util.UUID;

public class CustomTown implements Town {

    private final UUID uuid;
    private String tag;
    private String name;
    private UUID owner;
    private UUID state;
    private Location centre;

    public CustomTown(UUID uuid, String tag, String name, UUID owner, UUID state, Location centre) {
        if (tag.length() != 4) {
            throw new IllegalArgumentException("Tag must be greater then 4 characters");
        }
        this.uuid = uuid;
        this.tag = tag;
        this.name = name;
        this.owner = owner;
        this.state = state;
        this.centre = centre;
    }

    public UUID getId() {
        return this.uuid;
    }

    @Override
    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UUID getOwnerId() {
        return this.owner;
    }

    public void setOwnerId(UUID owner) {
        this.owner = owner;
    }

    @Override
    public Location getCentre() {
        return this.centre;
    }

    public void setCentre(Location centre) {
        this.centre = centre;
    }

    @Override
    public UUID getStateId() {
        return this.state;
    }

    public void setStateId(UUID state) {
        this.state = state;
    }
}
