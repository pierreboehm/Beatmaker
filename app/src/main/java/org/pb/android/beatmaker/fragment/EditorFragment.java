package org.pb.android.beatmaker.fragment;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.pb.android.beatmaker.AppPreferences_;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.data.ContentTickContainer;
import org.pb.android.beatmaker.data.ContentTickContainer_;
import org.pb.android.beatmaker.data.sound.SoundSample;
import org.pb.android.beatmaker.data.sound.SoundSampleDao;
import org.pb.android.beatmaker.dialog.ContentDialog;
import org.pb.android.beatmaker.dialog.content.SoundSampleListView;
import org.pb.android.beatmaker.dialog.content.SoundSampleListView_;
import org.pb.android.beatmaker.event.Events;
import org.pb.android.beatmaker.fragment.view.GraficalSoundView;
import org.pb.android.beatmaker.fragment.view.TickSamplesView;
import org.pb.android.beatmaker.sound.SoundManager;

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

    @ViewById(R.id.graficalSoundView)
    GraficalSoundView graficalSoundView;

    @ViewById(R.id.bpmValue)
    TextView bpmTextValue;

    @Bean
    SoundManager soundManager;

    @Bean
    SoundSampleDao soundSampleDao;

    @Pref
    AppPreferences_ preferences;

    private ContentDialog contentDialog = null;
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

    @Click(R.id.graficalSoundView)
    public void onGraficalSoundViewClick() {
        if (contentDialog != null) {
            contentDialog.dismiss();
        }

        SoundSampleListView soundSampleListView = SoundSampleListView_.build(getContext());
        contentDialog = new ContentDialog.Builder(getContext())
                .setContent(soundSampleListView)
                .build();

        contentDialog.show();

    }

    @Click(R.id.btnDecrementVolume)
    public void onDecrementVolumeClick() {
        int bpmValue = Integer.parseInt(bpmTextValue.getText().toString());
        int bpmValueNew = Math.max(bpmValue - 1, 0);
        bpmTextValue.setText(String.format("%s", bpmValueNew));
    }

    @Click(R.id.btnIncrementVolume)
    public void onIncrementVolumeClick() {
        int bpmValue = Integer.parseInt(bpmTextValue.getText().toString());
        int bpmValueNew = Math.min(bpmValue + 1, 300);
        bpmTextValue.setText(String.format("%s", bpmValueNew));
    }

    @Click(R.id.tickSamplesView)
    public void onTickSamplesViewClick() {
        int bpmValue = Integer.parseInt(bpmTextValue.getText().toString());
        soundManager.playSamples(contentTickContainerList, bpmValue);
    }

    @Click(R.id.ivKick)
    public void onKickClicked() {
        int bpmValue = Integer.parseInt(bpmTextValue.getText().toString());
        soundSampleDao.saveSampleConfiguration("test", bpmValue, contentTickContainerList);
        Toast.makeText(getContext(), "sample test saved", Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.ivSnare)
    public void onSnareClicked() {
        soundSampleDao.deleteSample("test");
        Toast.makeText(getContext(), "sample test deleted", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Events.TickStateChangedEvent event) {
        tickSamplesView.tickStateChanged(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Events.SamplePlayEvent event) {
        tickSamplesView.playTicks(event.getSampleIndex());
        Log.d(TAG, "sample # " + event.getSampleIndex());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Events.SampleSelectEvent event) {
        // TODO: re-init with selected sound sample
        contentDialog.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Events.GraficalSoundEvent event) {
        graficalSoundView.handleSoundEvent(event);
    }

    @SuppressLint("SetTextI18n")
    private void setupContentTickHolders() {

        SoundSample soundSample = soundSampleDao.getSample("test");
        List<ContentTickContainer> storedTickSamplesList = new ArrayList<>();

        if (soundSample != null) {
            int bpmValue = soundSample.getBpmValue();
            storedTickSamplesList = soundSample.getTickSamplesList(getContext());

            bpmTextValue.setText(Integer.toString(bpmValue));
            /*soundManager.reloadSoundBank(
                    soundSample.getKickResourceId(), soundSample.getSnareResourceId(),
                    soundSample.getHiHatResourceId(), soundSample.getToneResourceId()
            );*/
        }

        contentSamplesContainer.removeAllViews();
        contentTickContainerList.clear();

        if (storedTickSamplesList.isEmpty()) {
            for (int index = 0; index <= 31; index++) {
                ContentTickContainer tickContainer = ContentTickContainer_.build(getContext(), index);
                contentSamplesContainer.addView(tickContainer);
                contentTickContainerList.add(tickContainer);
            }
        } else {
            for (ContentTickContainer tickContainer : storedTickSamplesList) {
                contentSamplesContainer.addView(tickContainer);
                contentTickContainerList.add(tickContainer);
            }
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
