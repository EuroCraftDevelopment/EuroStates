package org.eurostates.area;

import org.bukkit.ChatColor;
import org.eurostates.area.town.Town;
import org.eurostates.area.relationship.Relationship;
import org.eurostates.area.relationship.war.WarRelationship;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

/**
 * The base class for all area based class
 */
public interface Area {

    /**
     * gets the tag string of the area. This is typically between 3-4 characters.
     * @return A string of the tag
     */
    @NotNull String getTag();

    /**
     * Gets the name of the area
     * @return The name of the area
     */
    @NotNull String getName();

    /**
     * Gets the legacy minecraft chat colour char that belongs to the area
     * @return The character of the colour
     */
    char getLegacyChatColourCharacter();

    /**
     * Gets the relationships that this area has with other states.
     * Note that the neutral relationships will not be contained within this set as no relationship implies that the relationship is neutral
     * @return A set of the relationships for this area
     */
    Set<Relationship> getRelationships();

    /**
     * If the area is at war with the provided Town then the exact War relationship will be returned, however if a war is not between this area and the provided town then {@link Optional#empty()} will be returned
     * @param town The town to check with at war
     * @return A Optional of the war relationship between this area and the provided town
     */
    Optional<WarRelationship> getWarWith(Town town);

    /**
     * Gets the Bukkit object for chat colours of the areas chat colour, see {@link #getLegacyChatColour()} for more info
     * @return The Bukkit chat colour for this area
     * @throws IllegalStateException If the character provided to the area is not valid
     */
    default @NotNull ChatColor getLegacyChatColour() {
        ChatColor colour = ChatColor.getByChar(this.getLegacyChatColourCharacter());
        if (colour == null) {
            throw new IllegalStateException("Attached legacy chat colour does not have a valid colour.");
        }
        return colour;
    }
}
