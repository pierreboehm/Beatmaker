package org.pb.android.beatmaker.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.data.SoundTypeResource.SoundType;
import org.pb.android.beatmaker.event.Events;
import org.pb.android.beatmaker.fragment.ui.ClickableImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint({"NonConstantResourceId", "ViewConstructor"})
@EViewGroup(R.layout.content_tick_container)
public class ContentTickContainer extends LinearLayout {

    private static final String TAG = ContentTickContainer.class.getSimpleName();

    @ViewById(R.id.button_1)
    ClickableImageButton button1;

    @ViewById(R.id.button_2)
    ClickableImageButton button2;

    @ViewById(R.id.button_3)
    ClickableImageButton button3;

    @ViewById(R.id.button_4)
    ClickableImageButton button4;

    private final int index;

    public ContentTickContainer(Context context, int index) {
        super(context);
        this.index = index;
    }

    @AfterViews
    public void initViews() {
        button1.setType(SoundType.KICK);
        button2.setType(SoundType.SNARE);
        button3.setType(SoundType.HIHAT);
        button4.setType(SoundType.TONE);
    }

    @Click(R.id.button_1)
    public void onButton1Click() {
        button1.setState(SoundType.KICK);
        EventBus.getDefault().post(new Events.TickStateChangedEvent(index, SoundType.KICK, button1.getState()));
    }

    @Click(R.id.button_2)
    public void onButton2Click() {
        button2.setState(SoundType.SNARE);
        EventBus.getDefault().post(new Events.TickStateChangedEvent(index, SoundType.SNARE, button2.getState()));
    }

    @Click(R.id.button_3)
    public void onButton3Click() {
        button3.setState(SoundType.HIHAT);
        EventBus.getDefault().post(new Events.TickStateChangedEvent(index, SoundType.HIHAT, button3.getState()));
    }

    @Click(R.id.button_4)
    public void onButton4Click() {
        button4.setState(SoundType.TONE);
        EventBus.getDefault().post(new Events.TickStateChangedEvent(index, SoundType.TONE, button4.getState()));
    }

    public void setButtonStates(int[] buttonStates) {
        button1.setState(buttonStates[0] == 1);
        button2.setState(buttonStates[1] == 1);
        button3.setState(buttonStates[2] == 1);
        button4.setState(buttonStates[3] == 1);
    }

    public int[] getButtonStates() {
        int kick = button1.getState() ? 1 : 0;
        int snare = button2.getState() ? 1 : 0;
        int hiHat = button3.getState() ? 1 : 0;
        int tone = button4.getState() ? 1 : 0;

        return new int[] {kick, snare, hiHat, tone};
    }

    public List<ClickableImageButton> getClickableImageButtons() {
        return new ArrayList<>(Arrays.asList(button1, button2, button3, button4));
    }

}
