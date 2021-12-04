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
import org.pb.android.beatmaker.sound.SoundManager;

@SuppressLint("NonConstantResourceId")
@EViewGroup(R.layout.button_clickable_imageview)
public class ClickableImageButton extends FrameLayout {

    private static final String TAG = ClickableImageButton.class.getSimpleName();

    public enum TickTypes {
        KICK, SNARE, HIHAT, TONE
    }

    @ViewById
    ImageView ivImage;

    @Bean
    SoundManager soundManager;

    private boolean on;
    private TickTypes type;

    public ClickableImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImage(int resourceId) {
        ivImage.setImageResource(resourceId);
    }

    public void setState(boolean tickState) {
        on = tickState;
    }

    public void setState(TickTypes type) {
        on = !on;

        if (on) {
            soundManager.playSound(type);
        }

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

    public boolean getState() {
        return on;
    }

    public void setType(TickTypes type) {
        this.type = type;
    }

    public TickTypes getType() {
        return type;
    }
}
