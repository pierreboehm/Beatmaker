package org.pb.android.beatmaker.data;

import org.pb.android.beatmaker.R;

public class SoundTypeResource {

    public enum SoundType {
        KICK(new int[] {R.raw.bass_drum_1, R.raw.bass_drum_2, R.raw.bass_drum_3, R.raw.boom_kick}),
        SNARE(new int[] {R.raw.hip_hop_snare_1}),
        HIHAT(new int[] {R.raw.closed_hihat_1}),
        TONE(new int[] {});

        private final int[] resourceIds;

        SoundType(int[] resourceIds) {
            this.resourceIds = resourceIds;
        }

        public int[] getResourceIdsOf(SoundType soundType) {
            switch (soundType) {
                case KICK:
                    return KICK.resourceIds;
                case SNARE:
                    return SNARE.resourceIds;
                case HIHAT:
                    return HIHAT.resourceIds;
                case TONE:
                    return TONE.resourceIds;
                default:
                    return null;
            }
        }
    }

}
