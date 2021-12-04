package org.pb.android.beatmaker.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.fragment.ui.ClickableImageButton;

import java.util.HashMap;
import java.util.Map;

@EBean(scope = EBean.Scope.Singleton)
public class SoundManager {

    private static final float DEFAULT_VOLUME = .5f;
    @RootContext
    Context context;

    private SoundPool soundPool;
    private int kickSoundIndex, snareSoundIndex, hihatSoundIndex, toneSoundIndex;

    private Map<ClickableImageButton.TickTypes, Integer> soundBank = new HashMap<ClickableImageButton.TickTypes, Integer>()
    {{
        put(ClickableImageButton.TickTypes.KICK, R.raw.boom_kick);
        put(ClickableImageButton.TickTypes.SNARE, R.raw.hip_hop_snare_1);
        put(ClickableImageButton.TickTypes.HIHAT, R.raw.closed_hihat_1);
        put(ClickableImageButton.TickTypes.TONE, R.raw.hi_tom_1);
    }};

    @AfterInject
    public void initSoundManager() {
        // init soundpool here
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();

        kickSoundIndex = soundPool.load(context, soundBank.get(ClickableImageButton.TickTypes.KICK), 1);
        snareSoundIndex = soundPool.load(context, soundBank.get(ClickableImageButton.TickTypes.SNARE), 1);
        hihatSoundIndex = soundPool.load(context, soundBank.get(ClickableImageButton.TickTypes.HIHAT), 1);
        toneSoundIndex = soundPool.load(context, soundBank.get(ClickableImageButton.TickTypes.TONE), 1);
    }

    public void playSound(ClickableImageButton.TickTypes type) {
        switch (type) {
            case KICK:
                soundPool.play(kickSoundIndex, DEFAULT_VOLUME, DEFAULT_VOLUME, 1, 0, 1f);
                break;
            case SNARE:
                soundPool.play(snareSoundIndex, DEFAULT_VOLUME, DEFAULT_VOLUME, 1, 0, 1f);
                break;
            case HIHAT:
                soundPool.play(hihatSoundIndex, DEFAULT_VOLUME, DEFAULT_VOLUME, 1, 0, 1f);
                break;
            case TONE:
                soundPool.play(toneSoundIndex, DEFAULT_VOLUME, DEFAULT_VOLUME, 1, 0, 1f);
                break;
        }
    }

}
