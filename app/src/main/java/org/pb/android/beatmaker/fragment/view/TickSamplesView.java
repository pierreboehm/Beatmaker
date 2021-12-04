package org.pb.android.beatmaker.fragment.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EView;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.data.ContentTickContainer;
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
    private Paint inactiveColor;

    public TickSamplesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    public void initViews() {
        setWillNotDraw(false);

        inactiveColor = new Paint();
        inactiveColor.setColor(getContext().getColor(R.color.blue_cyan));
        inactiveColor.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawSamples(canvas);
        super.onDraw(canvas);
    }

    public void setTickSamplesList(List<ContentTickContainer> tickSamplesList) {
        this.tickSamplesList = tickSamplesList;
    }

    private void drawSamples(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        int space = 10;
        int size = (width - (32 * space)) / 32;

        int offsetCol = (width / 2) - ((size + space) * 32 / 2) + (space / 2);
        int offsetRow = (height / 2) - ((size + space) * 4 / 2) + (space / 2);

        int offsetX = offsetCol;
        int offsetY = offsetRow;

        for (int row = 1; row <= 32; row++) {

            for (int col = 1; col <= 4; col++) {

                int x = offsetX + (size * (row - 1));
                int y = offsetY + (size * (col - 1));

                canvas.drawRect(x, y, x + size, y + size, inactiveColor);
                offsetY += space;
            }

            offsetX += space;
            offsetY = offsetRow;
        }
    }

    private void playSamples(int tickIndex) {
        for (ContentTickContainer tickContainer : tickSamplesList) {

            List<ClickableImageButton> clickableImageButtonList = tickContainer.getClickableImageButtons();

            for (ClickableImageButton clickableImageButton : clickableImageButtonList) {
                if (clickableImageButton.getState()) {
                    soundManager.playSound(clickableImageButton.getType());
                }
            }
        }
    }
}
