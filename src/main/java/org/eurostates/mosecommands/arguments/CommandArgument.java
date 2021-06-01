package org.eurostates.mosecommands.arguments;

import org.jetbrains.annotations.NotNull;

public interface CommandArgument<T> extends ParseCommandArgument<T>, SuggestCommandArgument<T> {

    @NotNull String getId();

    default @NotNull String getUsage() {
        return "<" + this.getId() + ">";
    }
}
