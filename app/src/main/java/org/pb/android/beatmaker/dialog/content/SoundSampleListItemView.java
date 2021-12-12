package org.pb.android.beatmaker.dialog.content;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.data.sound.SoundSample;

@EViewGroup(R.layout.view_sound_sample_list_item)
public class SoundSampleListItemView extends RelativeLayout {

    @ViewById(R.id.tvName)
    TextView sampleName;

    public SoundSampleListItemView(Context context) {
        super(context);
    }

    public void bind(SoundSample soundSample) {
        sampleName.setText(soundSample.getName());
    }
}
