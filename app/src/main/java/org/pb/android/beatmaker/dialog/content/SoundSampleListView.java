package org.pb.android.beatmaker.dialog.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.data.sound.SoundSample;

@SuppressLint("NonConstantResourceId")
@EViewGroup(R.layout.view_sound_sample_list)
public class SoundSampleListView extends LinearLayout {

    private static final String TAG = SoundSampleListView.class.getSimpleName();

    @ViewById(R.id.lvItemContainer)
    ListView itemContainer;

    @Bean
    SoundSampleListAdapter soundSampleListAdapter;

    public SoundSampleListView(Context context) {
        super(context);
    }

    @AfterViews
    public void initView() {
        itemContainer.setDivider(null);
        itemContainer.setAdapter(soundSampleListAdapter);
    }

    @ItemClick(R.id.lvItemContainer)
    public void onItemClick(SoundSample soundSample) {
        Log.d(TAG, "selected item: " + soundSample.getName());
    }
}
