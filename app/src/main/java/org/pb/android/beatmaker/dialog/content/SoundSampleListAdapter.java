package org.pb.android.beatmaker.dialog.content;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.pb.android.beatmaker.data.sound.SoundSample;
import org.pb.android.beatmaker.data.sound.SoundSampleDao;

import java.util.ArrayList;
import java.util.List;

@EBean
public class SoundSampleListAdapter extends BaseAdapter {

    @RootContext
    Context context;

    @Bean
    SoundSampleDao soundSampleDao;

    private List<SoundSample> soundSampleList = new ArrayList<>();

    @AfterInject
    public void afterInject() {
        soundSampleList = soundSampleDao.getSamples();
    }

    @Override
    public int getCount() {
        return soundSampleList.size();
    }

    @Override
    public SoundSample getItem(int position) {
        return soundSampleList.get(position);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SoundSampleListItemView soundSampleListItemView;

        if (convertView == null) {
            soundSampleListItemView = SoundSampleListItemView_.build(context);
        } else {
            soundSampleListItemView = (SoundSampleListItemView) convertView;
        }

        soundSampleListItemView.bind(getItem(position));
        return soundSampleListItemView;
    }
}
