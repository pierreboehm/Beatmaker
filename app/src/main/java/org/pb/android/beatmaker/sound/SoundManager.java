package org.pb.android.beatmaker.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.greenrobot.eventbus.EventBus;
import org.pb.android.beatmaker.data.ContentTickContainer;
import org.pb.android.beatmaker.data.SoundTypeResource.SoundType;
import org.pb.android.beatmaker.event.Events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@EBean(scope = EBean.Scope.Singleton)
public class SoundManager {

    private static final String TAG = SoundManager.class.getSimpleName();
    private static final float DEFAULT_VOLUME = .5f;

    @RootContext
    Context context;

    private Timer tickTimer;
    private SoundPool soundPool;
    private int kickSoundIndex, snareSoundIndex, hihatSoundIndex, toneSoundIndex;
    private int playerSampleIndex = 0;

    private Map<SoundType, Integer> soundBank = new HashMap<SoundType, Integer>()
    {{
        put(SoundType.KICK, SoundType.KICK.getResourceById(0));
        put(SoundType.SNARE, SoundType.SNARE.getResourceById(0));
        put(SoundType.HIHAT, SoundType.HIHAT.getResourceById(0));
        put(SoundType.TONE, SoundType.TONE.getResourceById(0));
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

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d(TAG, "sampleId=" + sampleId + ", status=" + status + " (0=success)");
            }
        });

        kickSoundIndex = soundPool.load(context, soundBank.get(SoundType.KICK), 1);
        snareSoundIndex = soundPool.load(context, soundBank.get(SoundType.SNARE), 1);
        hihatSoundIndex = soundPool.load(context, soundBank.get(SoundType.HIHAT), 1);
        toneSoundIndex = soundPool.load(context, soundBank.get(SoundType.TONE), 1);
    }

    public void reloadSoundBank(Integer kickResourceId, Integer snareResourceId, Integer hiHatResourceId, Integer toneResourceId) {
        soundBank.replace(SoundType.KICK, kickResourceId);
        soundBank.replace(SoundType.SNARE, snareResourceId);
        soundBank.replace(SoundType.HIHAT, hiHatResourceId);
        soundBank.replace(SoundType.TONE, toneResourceId);

        initSoundManager();
    }

    public void playSound(SoundType type) {
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

    public boolean isNotSamplesPlaying() {
        return tickTimer == null;
    }

    private void startPlaying(final int startIndex, final int endIndex, int bpmValue) {
        if (tickTimer == null) {
            tickTimer = new Timer();
        }

        playerSampleIndex = startIndex;
        int tickValue = (int) (60f / (float) bpmValue * 2f * 100f);     // FIXME: is that correct? no!
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
