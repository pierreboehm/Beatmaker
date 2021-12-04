package org.pb.android.beatmaker.fragment;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.data.ContentTickContainer;
import org.pb.android.beatmaker.data.ContentTickContainer_;
import org.pb.android.beatmaker.fragment.view.TickSamplesView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NonConstantResourceId")
@EFragment(R.layout.fragment_editor)
public class EditorFragment extends Fragment {

    public static final String TAG = EditorFragment.class.getSimpleName();

    @ViewById(R.id.contentSamplesContainer)
    ViewGroup contentSamplesContainer;

    @ViewById(R.id.tickSamplesView)
    TickSamplesView tickSamplesView;

    private List<ContentTickContainer> contentTickContainerList = new ArrayList<>();

    // TODO
    @AfterViews
    public void initViews() {
        setupContentTickHolders();
    }

    private void setupContentTickHolders() {
        for (int index = 0; index <= 31; index++) {
            ContentTickContainer tickContainer = ContentTickContainer_.build(getContext(), index);
            contentSamplesContainer.addView(tickContainer);
            contentTickContainerList.add(tickContainer);
        }

        tickSamplesView.setTickSamplesList(contentTickContainerList);
    }
}
