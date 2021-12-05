package org.pb.android.beatmaker.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.data.ContentTickContainer;
import org.pb.android.beatmaker.data.ContentTickContainer_;
import org.pb.android.beatmaker.event.Events;
import org.pb.android.beatmaker.fragment.view.TickSamplesView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NonConstantResourceId")
@EFragment(R.layout.fragment_editor)
public class EditorFragment extends Fragment {

    public static final String TAG = EditorFragment.class.getSimpleName();

    @ViewById(R.id.contentSamplesScrollContainer)
    HorizontalScrollView contentSamplesScrollContainer;

    @ViewById(R.id.contentSamplesContainer)
    ViewGroup contentSamplesContainer;

    @ViewById(R.id.tickSamplesView)
    TickSamplesView tickSamplesView;

    @ViewById(R.id.valueVolume)
    TextView valueVolume;

    private List<ContentTickContainer> contentTickContainerList = new ArrayList<>();

    @AfterViews
    public void initViews() {
        setupContentTickHolders();

        contentSamplesScrollContainer.setOnScrollChangeListener(ScrollContainerListener);
        contentSamplesScrollContainer.getViewTreeObserver().addOnGlobalLayoutListener(ScrollContainerLayoutListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Click(R.id.btnDecrementVolume)
    public void onDecrementVolumeClick() {
        int volumeValue = Integer.parseInt(valueVolume.getText().toString());
        int volumeValueNew = Math.max(volumeValue - 1, 0);
        valueVolume.setText(String.format("%s", volumeValueNew));
    }

    @Click(R.id.btnIncrementVolume)
    public void onIncrementVolumeClick() {
        int volumeValue = Integer.parseInt(valueVolume.getText().toString());
        int volumeValueNew = Math.min(volumeValue + 1, 300);
        valueVolume.setText(String.format("%s", volumeValueNew));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Events.TickStateChangedEvent event) {
        tickSamplesView.tickStateChanged(event);
    }

    private void setupContentTickHolders() {
        for (int index = 0; index <= 31; index++) {
            ContentTickContainer tickContainer = ContentTickContainer_.build(getContext(), index);
            contentSamplesContainer.addView(tickContainer);
            contentTickContainerList.add(tickContainer);
        }

        tickSamplesView.setTickSamplesList(contentTickContainerList);
    }

    private ViewTreeObserver.OnGlobalLayoutListener ScrollContainerLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            tickSamplesView.setupScrollFrame(contentSamplesContainer.getWidth(), contentSamplesScrollContainer.getWidth());
        }
    };

    private View.OnScrollChangeListener ScrollContainerListener = new View.OnScrollChangeListener() {
        @Override
        public void onScrollChange(View view, int scrollX, int i1, int i2, int i3) {
            tickSamplesView.updateScrollPosition(scrollX);
        }
    };
}
