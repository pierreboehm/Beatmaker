package org.pb.android.beatmaker.event;

import org.pb.android.beatmaker.fragment.ui.ClickableImageButton.TickTypes;

public class Events {

    public static class TickStateChangedEvent {
        private final boolean tickState;
        private final int sampleIndex;
        private final TickTypes tickType;

        public TickStateChangedEvent(int sampleIndex, TickTypes tickType, boolean tickState) {
            this.sampleIndex = sampleIndex;
            this.tickType = tickType;
            this.tickState = tickState;
        }

        public boolean getTickState() {
            return tickState;
        }

        public TickTypes getTickType() {
            return tickType;
        }

        public int getSampleIndex() {
            return sampleIndex;
        }
    }

}
