package org.pb.android.beatmaker.fragment.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.UiThread;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.data.ContentTickContainer;
import org.pb.android.beatmaker.data.SoundTypeResource;
import org.pb.android.beatmaker.event.Events;
import org.pb.android.beatmaker.fragment.ui.ClickableImageButton;
import org.pb.android.beatmaker.sound.SoundManager;

import java.util.ArrayList;
import java.util.List;

@EView
public class TickSamplesView extends View {

    private static final String TAG = TickSamplesView.class.getSimpleName();

    @Bean
    SoundManager soundManager;

    private List<ContentTickContainer> tickSamplesList = new ArrayList<>();
    private Paint activeColor, inactiveColor, frameColor;

    private int completeWidth = 0, visibleWidth = 0;
    private int scrollXPosition = 0;

    private int samplePlayIndex = -1;

    public TickSamplesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    public void initViews() {
        setWillNotDraw(false);

        activeColor = new Paint();
        activeColor.setColor(getContext().getColor(R.color.blue_cyan));
        activeColor.setStyle(Paint.Style.FILL);

        inactiveColor = new Paint();
        inactiveColor.setColor(getContext().getColor(R.color.blue_light_78));
        inactiveColor.setStyle(Paint.Style.FILL);

        frameColor = new Paint();
        frameColor.setColor(Color.WHITE);
        frameColor.setStyle(Paint.Style.STROKE);
        frameColor.setStrokeWidth(3f);
    }

    @UiThread
    public void refreshUi() {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawSamples(canvas);
        drawScrollFrame(canvas);
        drawSamplePlayingFrame(canvas);
        super.onDraw(canvas);
    }

    public void tickStateChanged(Events.TickStateChangedEvent event) {
        int samplesIndex = event.getSampleIndex();
        SoundTypeResource.SoundType soundType = event.getSoundType();
        boolean tickState = event.getTickState();

        tickSamplesList.get(samplesIndex).getClickableImageButtons().get(soundType.ordinal()).setState(tickState);
        refreshUi();
    }

    public void setupScrollFrame(int completeWidth, int visibleWidth) {
        this.completeWidth = completeWidth;
        this.visibleWidth = visibleWidth;
        refreshUi();
    }

    public void updateScrollPosition(int scrollXPosition) {
        this.scrollXPosition = scrollXPosition;
        refreshUi();
    }

    public void setTickSamplesList(List<ContentTickContainer> tickSamplesList) {
        this.tickSamplesList = tickSamplesList;
        refreshUi();
    }

    public void playTicks(int sampleIndex) {
        samplePlayIndex = sampleIndex;
        refreshUi();

        ContentTickContainer tickContainer = tickSamplesList.get(sampleIndex);
        List<ClickableImageButton> clickableImageButtonList = tickContainer.getClickableImageButtons();

        for (ClickableImageButton clickableImageButton : clickableImageButtonList) {
            if (clickableImageButton.getState()) {
                soundManager.playSound(clickableImageButton.getType());
            }
        }

    }

    private void drawSamples(Canvas canvas) {
        if (tickSamplesList.isEmpty()) {
            return;
        }

        int samples = tickSamplesList.size();
        int ticksPerSample = tickSamplesList.get(0).getClickableImageButtons().size();

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        int space = 10;
        int size = (width - (samples * space)) / samples;

        int offsetCol = (width / 2) - ((size + space) * samples / 2) + (space / 2);
        int offsetRow = (height / 2) - ((size + space) * ticksPerSample / 2) + (space / 2);

        int offsetX = offsetCol;
        int offsetY = offsetRow;

        ContentTickContainer tickContainer;
        ClickableImageButton clickableImageButton;

        for (int row = 1; row <= samples; row++) {

            tickContainer = tickSamplesList.get(row - 1);

            for (int col = 1; col <= ticksPerSample; col++) {

                int x = offsetX + (size * (row - 1));
                int y = offsetY + (size * (col - 1));

                clickableImageButton = tickContainer.getClickableImageButtons().get(col - 1);

                canvas.drawRect(x, y, x + size, y + size, clickableImageButton.getState() ? activeColor : inactiveColor);
                offsetY += space;
            }

            offsetX += space;
            offsetY = offsetRow;
        }
    }

    private void drawScrollFrame(Canvas canvas) {
        if (completeWidth == 0 || visibleWidth == 0) {
            return;
        }

        int frameWidth = visibleWidth * canvas.getWidth() / completeWidth;
        int scrollX = (scrollXPosition * canvas.getWidth() / completeWidth) + 1;

        canvas.drawRect(scrollX, 30, scrollX + frameWidth, canvas.getHeight() - 30, frameColor);
    }

    private void drawSamplePlayingFrame(Canvas canvas) {
        if (samplePlayIndex == -1 || tickSamplesList.isEmpty()) {
            return;
        }

        int offsetX = 10;
        int cellWidth = canvas.getWidth() / tickSamplesList.size();
        int x = offsetX + (samplePlayIndex * cellWidth) + (cellWidth / 2);

        canvas.drawOval(x - 9f, 0f, x + 9f, 18f, activeColor);
    }
}
