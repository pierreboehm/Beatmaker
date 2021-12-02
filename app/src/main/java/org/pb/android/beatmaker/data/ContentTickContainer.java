package org.pb.android.beatmaker.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.fragment.ui.ClickableImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("NonConstantResourceId")
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

    private int index;

    public ContentTickContainer(Context context) {
        super(context);
    }

    public ContentTickContainer(Context context, int index) {
        super(context);
        this.index = index;
    }

    public ContentTickContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentTickContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Click(R.id.button_1)
    public void onButton1Click() {
        Log.d(TAG, "#" + index + " onButton1Click");
    }

    @Click(R.id.button_2)
    public void onButton2Click() {
        Log.d(TAG, "#" + index + " onButton2Click");
    }

    @Click(R.id.button_3)
    public void onButton3Click() {
        Log.d(TAG, "#" + index + " onButton3Click");
    }

    @Click(R.id.button_4)
    public void onButton4Click() {
        Log.d(TAG, "#" + index + " onButton4Click");
    }

    public List<ClickableImageButton> getClickableImageButtons() {
        return new ArrayList<>(Arrays.asList(button1, button2, button3, button4));
    }


}
