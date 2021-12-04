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
import org.pb.android.beatmaker.event.Events;
import org.pb.android.beatmaker.fragment.ui.ClickableImageButton;
import org.pb.android.beatmaker.fragment.ui.ClickableImageButton.TickTypes;

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
        button1.setType(TickTypes.KICK);
        button2.setType(TickTypes.SNARE);
        button3.setType(TickTypes.HIHAT);
        button4.setType(TickTypes.TONE);
    }

    @Click(R.id.button_1)
    public void onButton1Click() {
        button1.setState(TickTypes.KICK);
        EventBus.getDefault().post(new Events.TickStateChangedEvent(index, TickTypes.KICK, button1.getState()));
    }

    @Click(R.id.button_2)
    public void onButton2Click() {
        button2.setState(TickTypes.SNARE);
        EventBus.getDefault().post(new Events.TickStateChangedEvent(index, TickTypes.SNARE, button2.getState()));
    }

    @Click(R.id.button_3)
    public void onButton3Click() {
        button3.setState(TickTypes.HIHAT);
        EventBus.getDefault().post(new Events.TickStateChangedEvent(index, TickTypes.HIHAT, button3.getState()));
    }

    @Click(R.id.button_4)
    public void onButton4Click() {
        button4.setState(TickTypes.TONE);
        EventBus.getDefault().post(new Events.TickStateChangedEvent(index, TickTypes.TONE, button4.getState()));
    }

    public List<ClickableImageButton> getClickableImageButtons() {
        return new ArrayList<>(Arrays.asList(button1, button2, button3, button4));
    }

}
