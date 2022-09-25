package net.boster.gui.multipage;

import org.jetbrains.annotations.NotNull;

public abstract class MultiPageNextButton implements MultiPageButton {

    @Override
    public final void performPage(@NotNull MultiPageGUI gui) {
        if(gui.newPage()) {
            gui.open();
        }
    }
}
