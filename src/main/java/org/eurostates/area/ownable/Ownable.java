package org.eurostates.area.ownable;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Ownable {

    @NotNull UUID getOwnerId();
}
