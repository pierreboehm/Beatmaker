package org.pb.android.beatmaker.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.greenrobot.eventbus.EventBus;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.data.ContentTickContainer;
import org.pb.android.beatmaker.data.SoundTypeResource;
import org.pb.android.beatmaker.data.SoundTypeResource.SoundType;
import org.pb.android.beatmaker.event.Events;
import org.pb.android.beatmaker.fragment.ui.ClickableImageButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@EBean(scope = EBean.Scope.Singleton)
public class SoundManager {

    private static final float DEFAULT_VOLUME = .5f;
    @RootContext
    Context context;

    private Timer tickTimer;
    private SoundPool soundPool;
    private int kickSoundIndex, snareSoundIndex, hihatSoundIndex, toneSoundIndex;
    private int playerSampleIndex = 0;

    private Map<SoundType, Integer> soundBank = new HashMap<SoundType, Integer>()
    {{
        put(SoundType.KICK, R.raw.boom_kick);
        put(SoundType.SNARE, R.raw.hip_hop_snare_1);
        put(SoundType.HIHAT, R.raw.closed_hihat_1);
        put(SoundType.TONE, R.raw.hi_tom_1);
    }};

    @AfterInject
    public void initSoundManager() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();

        kickSoundIndex = soundPool.load(context, soundBank.get(SoundType.KICK), 1);
        snareSoundIndex = soundPool.load(context, soundBank.get(SoundType.SNARE), 1);
        hihatSoundIndex = soundPool.load(context, soundBank.get(SoundType.HIHAT), 1);
        toneSoundIndex = soundPool.load(context, soundBank.get(SoundType.TONE), 1);
    }

    public void playSound(SoundType type) {

        soundPool.stop(kickSoundIndex);
        soundPool.stop(snareSoundIndex);
        soundPool.stop(hihatSoundIndex);
        soundPool.stop(toneSoundIndex);

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

        EventBus.getDefault().post(new Events.GraficalSoundEvent(type));
    }

    public void playSamples(List<ContentTickContainer> tickSamplesList, int bpmValue) {
        if (isPlaying()) {
            stopPlaying();
        } else {
            startPlaying(0, tickSamplesList.size() - 1, bpmValue);
        }
    }

    private void startPlaying(final int startIndex, final int endIndex, int bpmValue) {
        if (tickTimer == null) {
            tickTimer = new Timer();
        }

        playerSampleIndex = startIndex;
        int tickValue = (int) (60f / (float) bpmValue * 2f * 100f);     // FIXME: is that correct?
        // 82,5bpm here is as 103bpm
        //int tickValue = 582;

        tickTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                EventBus.getDefault().post(new Events.SamplePlayEvent(playerSampleIndex));

                playerSampleIndex++;

                if (playerSampleIndex > endIndex) {
                    playerSampleIndex = startIndex;
                }
            }
        }, 0, tickValue);
    }

    private boolean isPlaying() {
        return tickTimer != null;
    }

    private void stopPlaying() {
        if (tickTimer != null) {
            tickTimer.cancel();
        }
        tickTimer = null;
    }

}
