package org.eurostates.dynmap;

import org.bukkit.Bukkit;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerSet;
import org.eurostates.EuroStates;
import org.eurostates.area.state.States;
import org.eurostates.area.town.Town;

public class MarkerSetManager {
    public static final String MSET_ID = "eurostates.markerset";
    public static final String MSET_LABEL = "Towns";
    public static final String WORLD = "world";
    public static final String MARKER_NAME = "big house";

    public static void initMarkerSet() {
        Bukkit.getLogger().info("Starting Dynmap MarkerSet init...");
        DynmapCommonAPI dapi = EuroStates.getDynmapAPI();
        MarkerSet markerSet = dapi.getMarkerAPI().createMarkerSet(
                MSET_ID,
                MSET_LABEL,
                dapi.getMarkerAPI().getMarkerIcons(),
                false);
        States.CUSTOM_STATES.forEach(state -> {
            state.getTowns().forEach(town -> {
                Town customTown = (Town) town;
                markerSet.createMarker(
                        customTown.getId().toString(),
                        customTown.getName(),
                        WORLD,
                        customTown.getCentre().getX(),
                        customTown.getCentre().getY(),
                        customTown.getCentre().getZ(),
                        dapi.getMarkerAPI().getMarkerIcon(MARKER_NAME),
                        false
                );
            });
        });
    }

    public static void addTownMarker(Town town) {
        DynmapCommonAPI dapi = EuroStates.getDynmapAPI();
        MarkerSet markerSet = dapi.getMarkerAPI().getMarkerSet(MSET_ID);

        markerSet.createMarker(
                town.getId().toString(),
                town.getName(),
                WORLD,
                town.getCentre().getX(),
                town.getCentre().getY(),
                town.getCentre().getZ(),
                dapi.getMarkerAPI().getMarkerIcon(MARKER_NAME),
                false
        );
    }
    public static void removeTownMarker(Town town) {
        DynmapCommonAPI dapi = EuroStates.getDynmapAPI();
        MarkerSet markerSet = dapi.getMarkerAPI().getMarkerSet(MSET_ID);

        Marker marker = markerSet.findMarker(town.getId().toString());

        marker.deleteMarker();
    }
}

