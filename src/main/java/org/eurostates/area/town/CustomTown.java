package org.eurostates.area.town;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.eurostates.area.state.CustomState;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.Savable;
import org.eurostates.parser.Serializable;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class CustomTown implements Town, Savable<CustomTown, Map<String, Object>, String> {

    private final UUID uuid;
    private String tag;
    private String name;
    private UUID owner;
    private CustomState state;
    private Block centre;

    public CustomTown(UUID uuid, String tag, String name, UUID owner, CustomState state, Block centre) {
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

    @Deprecated
    public void setOwnerId(UUID owner) {
        this.owner = owner;
    }

    public void setOwner(OfflinePlayer player) {
        this.owner = player.getUniqueId();
    }

    @Override
    public Block getCentre() {
        return this.centre;
    }

    public void setCentre(Block centre) {
        this.centre = centre;
    }

    @Override
    public UUID getStateId() {
        return this.state.getId();
    }

    @Override
    public CustomState getState() {
        return this.state;
    }

    public void setState(CustomState state) {
        this.state = state;
    }

    @Override
    public File getFile() {
        return new File("data/towns/" + this.getId().toString() + ".yml");
    }

    @Override
    public Serializable<CustomTown, Map<String, Object>> getSerializableParser() {
        return null;
    }

    @Override
    public Serializable<CustomTown, String> getIdParser() {
        return Parsers.GETTER_TOWN;
    }

    @Override
    public String getRootNode() {
        return "Town";
    }
}
