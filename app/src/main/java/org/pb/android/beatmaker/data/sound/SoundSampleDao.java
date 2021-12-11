package org.pb.android.beatmaker.data.sound;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.pb.android.beatmaker.data.ContentTickContainer;

import java.util.ArrayList;
import java.util.List;

@EBean
public class SoundSampleDao {

    private static final String TAG = SoundSampleDao.class.getSimpleName();

    @RootContext
    Context context;

    @AfterInject
    void init() {
        FlowManager.init(new FlowConfig.Builder(context).build());
    }

    @Nullable
    public SoundSample getSample(String name) {
        return SQLite.select().from(SoundSample.class).where(SoundSample_Table.name.eq(name)).querySingle();
    }

    public void deleteSample(String name) {
        SoundSample soundSample = getSample(name);

        if(soundSample != null) {
            soundSample.delete();
        }
    }

    public void saveSampleConfiguration(String name, List<ContentTickContainer> tickSamplesList) {
        SoundSample soundSample = getSample(name);

        if (soundSample == null) {
            soundSample = new SoundSample();
            soundSample.setName(name);
        }

        soundSample.setTickSamples(tickSamplesList);
        soundSample.save();
    }

    public List<ContentTickContainer> loadSampleConfiguration(String name) {
        SoundSample soundSample = getSample(name);

        if (soundSample == null) {
            return new ArrayList<>();
        }

        return soundSample.getTickSamplesList(context);
    }
}
