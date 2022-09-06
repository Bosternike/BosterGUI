package net.boster.gui.multipage;

public abstract class MultiPagePreviousButton implements MultiPageButton {

    @Override
    public final void performPage(MultiPageGUI gui) {
        if(gui.pastPage()) {
            gui.open();
        }
    }
}
