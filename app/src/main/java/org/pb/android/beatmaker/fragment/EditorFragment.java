package org.pb.android.beatmaker.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;

import androidx.fragment.app.Fragment;

import org.androidannotations.annotations.AfterViews;
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
