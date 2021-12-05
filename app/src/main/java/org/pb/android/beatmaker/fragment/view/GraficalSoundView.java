package org.pb.android.beatmaker.fragment.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.UiThread;
import org.pb.android.beatmaker.R;
import org.pb.android.beatmaker.event.Events;

@EView
public class GraficalSoundView extends View {

    private Paint kickColor, snareColor, hiHatColor;
    private int alphaKick, alphaSnare, alphaHiHat;
    private boolean shouldDrawKick, shouldDrawSnare, shouldDrawHiHat;

    public GraficalSoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    public void initViews() {
        setWillNotDraw(false);

        kickColor = new Paint();
        kickColor.setColor(getContext().getColor(R.color.purple_700));
        kickColor.setAlpha(80);
        kickColor.setStyle(Paint.Style.STROKE);
        kickColor.setStrokeWidth(80f);

        snareColor = new Paint();
        snareColor.setColor(getContext().getColor(R.color.purple_500));
        snareColor.setAlpha(80);
        snareColor.setStyle(Paint.Style.STROKE);
        snareColor.setStrokeWidth(40f);

        hiHatColor = new Paint();
        hiHatColor.setColor(getContext().getColor(R.color.purple_200));
        hiHatColor.setAlpha(80);
        hiHatColor.setStyle(Paint.Style.STROKE);
        hiHatColor.setStrokeWidth(20f);

        refreshUi();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawKickOval(canvas);
        drawSnareOval(canvas);
        drawHiHatOval(canvas);

        super.onDraw(canvas);

        checkForLoop();
    }

    @UiThread
    public void refreshUi() {
        invalidate();
    }

    public void handleSoundEvent(Events.GraficalSoundEvent event) {
        switch (event.getTickType()) {
            case KICK:
                drawKick();
                break;
            case SNARE:
                drawSnare();
                break;
            case HIHAT:
                drawHiHat();
                break;
        }
    }

    private void drawKick() {
        alphaKick = 255;
    }

    private void drawSnare() {
        alphaSnare = 255;
    }

    private void drawHiHat() {
        alphaHiHat = 255;
    }

    private void checkForLoop() {
        // if any alpha can be still decreased call refreshUi()
        boolean shouldRefresh = false;

        if (alphaKick > 80) {
            alphaKick -= 10;
            shouldRefresh = true;
        } else {
            alphaKick = 80;
        }

        if (alphaSnare > 80) {
            alphaSnare -= 10f;
            shouldRefresh = true;
        } else {
            alphaSnare = 80;
        }

        if (alphaHiHat > 80) {
            alphaHiHat -= 10;
            shouldRefresh = true;
        } else {
            alphaHiHat = 80;
        }

        if (shouldRefresh) {
            refreshUi();
        }
    }

    private void drawKickOval(Canvas canvas) {
        kickColor.setAlpha(alphaKick);
        canvas.drawOval(canvas.getWidth() / 2f - 80f, canvas.getHeight() / 2f + 80f, canvas.getWidth() / 2f + 80f, canvas.getHeight() / 2f - 80f, kickColor);
        refreshUi();
    }

    private void drawSnareOval(Canvas canvas) {
        snareColor.setAlpha(alphaSnare);
        canvas.drawOval(canvas.getWidth() / 2f - 160f, canvas.getHeight() / 2f + 160f, canvas.getWidth() / 2f + 160f, canvas.getHeight() / 2f - 160f, snareColor);
        refreshUi();
    }

    private void drawHiHatOval(Canvas canvas) {
        hiHatColor.setAlpha(alphaHiHat);
        canvas.drawOval(canvas.getWidth() / 2f - 240f, canvas.getHeight() / 2f + 240f, canvas.getWidth() / 2f + 240f, canvas.getHeight() / 2f - 240f, hiHatColor);
    }
}
