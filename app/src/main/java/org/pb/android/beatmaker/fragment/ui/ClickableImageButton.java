package org.pb.android.beatmaker.fragment.ui;

import android.annotation.SuppressLint;
import android.content.Context;

import android.util.AttributeSet;

import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.data.SoundTypeResource.SoundType;
import org.pb.android.beatmaker.sound.SoundManager;

@SuppressLint("NonConstantResourceId")
@EViewGroup(R.layout.button_clickable_imageview)
public class ClickableImageButton extends FrameLayout {

    private static final String TAG = ClickableImageButton.class.getSimpleName();

    @ViewById
    ImageView ivImage;

    @Bean
    SoundManager soundManager;

    private boolean on;
    private SoundType type;

    public ClickableImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setState(boolean tickState) {
        on = tickState;
        setImage();
    }

    public void setState(SoundType type) {
        on = !on;

        if (on) {
            if (soundManager.isNotSamplesPlaying()) {
                soundManager.playSound(type);
            }
        }

        setImage();
    }

    public boolean getState() {
        return on;
    }

    public void setType(SoundType type) {
        this.type = type;
    }

    public SoundType getType() {
        return type;
    }

    private void setImage() {
        switch (type) {
            case KICK:
                ivImage.setBackgroundResource(on ? R.drawable.rounded_background_green : R.drawable.rounded_background_blue);
                break;
            case SNARE:
                ivImage.setBackgroundResource(on ? R.drawable.rounded_background_red : R.drawable.rounded_background_blue);
                break;
            case HIHAT:
                ivImage.setBackgroundResource(on ? R.drawable.rounded_background_yellow : R.drawable.rounded_background_blue);
                break;
            case TONE:
                break;
        }
    }
}
