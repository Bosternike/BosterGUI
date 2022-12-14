package net.boster.gui.multipage;

public enum MultiPageButtonAppearance {

    NEEDED {
        @Override
        public boolean shouldAppear(MultiPageGUI gui, boolean next) {
            if(next) {
                return gui.getPageNumber() < gui.getPages();
            } else {
                return gui.getPageNumber() > 1;
            }
        }
    },
    ALWAYS {
        @Override
        public boolean shouldAppear(MultiPageGUI gui, boolean next) {
            return true;
        }
    };

    public abstract boolean shouldAppear(MultiPageGUI gui, boolean next);
}
