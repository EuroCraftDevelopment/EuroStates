package org.eurostates.area.town;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.eurostates.area.state.CustomState;
import org.eurostates.parser.Parsers;
import org.eurostates.parser.Savable;
import org.eurostates.parser.area.town.GetterTownParser;
import org.eurostates.parser.area.town.LoadableTownParser;
import org.eurostates.relationship.Relationship;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CustomTown implements Town, Savable<CustomTown, Map<String, Object>, String> {

    private final @NotNull UUID uuid;
    private @NotNull String tag;
    private @NotNull String name;
    private @NotNull UUID owner;
    private @NotNull CustomState state;
    private @NotNull Block centre;

    public CustomTown(
            @NotNull UUID uuid,
            @NotNull String tag,
            @NotNull String name,
            @NotNull UUID owner,
            @NotNull CustomState state,
            @NotNull Block centre) {
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

    public @NotNull UUID getId() {
        return this.uuid;
    }

    @Override
    public @NotNull String getTag() {
        return this.tag;
    }

    public void setTag(@NotNull String tag) {
        this.tag = tag;
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull UUID getOwnerId() {
        return this.owner;
    }

    @Deprecated
    public void setOwnerId(@NotNull UUID owner) {
        this.owner = owner;
    }

    public void setOwner(@NotNull OfflinePlayer player) {
        this.owner = player.getUniqueId();
    }

    @Override
    public @NotNull Block getCentre() {
        return this.centre;
    }

    public void setCentre(@NotNull Block centre) {
        this.centre = centre;
    }

    @Override
    public @NotNull UUID getStateId() {
        return this.state.getId();
    }

    @Override
    public @NotNull CustomState getState() {
        return this.state;
    }

    public void setState(@NotNull CustomState state) {
        this.state = state;
    }

    @Override
    public @NotNull File getFile() {
        return new File("data/towns/" + this.getId().toString() + ".yml");
    }

    @Override
    public @NotNull LoadableTownParser getSerializableParser() {
        return Parsers.LOADABLE_TOWN;
    }

    @Override
    public @NotNull GetterTownParser getIdParser() {
        return Parsers.GETTER_TOWN;
    }

    @Override
    public @NotNull String getRootNode() {
        return "Town";
    }
}
