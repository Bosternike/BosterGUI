package net.boster.gui.multipage;

import org.jetbrains.annotations.NotNull;

public interface MultiPageButton extends MultiPageEntry {

    default void performPage(MultiPageGUI gui) {
        throw new UnsupportedOperationException();
    }

    int getSlot();

    @NotNull MultiPageButtonAppearance getAppearance();
}
