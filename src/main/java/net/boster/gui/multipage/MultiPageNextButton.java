package net.boster.gui.multipage;

public abstract class MultiPageNextButton implements MultiPageButton {

    @Override
    public final void performPage(MultiPageGUI gui) {
        if(gui.newPage()) {
            gui.open();
        }
    }
}
