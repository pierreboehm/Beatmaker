package org.pb.android.beatmaker.event;

import org.pb.android.beatmaker.data.SoundTypeResource.SoundType;

public class Events {

    public static class TickStateChangedEvent {
        private final boolean tickState;
        private final int sampleIndex;
        private final SoundType soundType;

        public TickStateChangedEvent(int sampleIndex, SoundType soundType, boolean tickState) {
            this.sampleIndex = sampleIndex;
            this.soundType = soundType;
            this.tickState = tickState;
        }

        public boolean getTickState() {
            return tickState;
        }

        public SoundType getSoundType() {
            return soundType;
        }

        public int getSampleIndex() {
            return sampleIndex;
        }
    }

    public static class SamplePlayEvent {
        private final int sampleIndex;

        public SamplePlayEvent(int sampleIndex) {
            this.sampleIndex = sampleIndex;
        }

        public int getSampleIndex() {
            return sampleIndex;
        }
    }

    public static class GraficalSoundEvent {
        private final SoundType soundType;

        public GraficalSoundEvent(SoundType soundType) {
            this.soundType = soundType;
        }

        public SoundType getSoundType() {
            return soundType;
        }
    }

}
