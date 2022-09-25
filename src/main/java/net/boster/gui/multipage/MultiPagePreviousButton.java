package net.boster.gui.multipage;

import org.jetbrains.annotations.NotNull;

public abstract class MultiPagePreviousButton implements MultiPageButton {

    @Override
    public final void performPage(@NotNull MultiPageGUI gui) {
        if(gui.pastPage()) {
            gui.open();
        }
    }
}
