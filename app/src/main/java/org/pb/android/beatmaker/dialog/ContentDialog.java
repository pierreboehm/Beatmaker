package org.pb.android.beatmaker.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.pb.android.beatmaker.R;

@SuppressLint("NonConstantResourceId")
@EViewGroup(R.layout.view_content_dialog)
public class ContentDialog extends LinearLayout {

    @ViewById(R.id.dialogContentContainer)
    ViewGroup dialogContentContainer;

    private Dialog dialog;

    public ContentDialog(Context context) {
        super(context);
    }

    public void show() {
        if (dialog == null) {

            dialog = new AlertDialog.Builder(getContext()).setView(this).create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            final Window window = dialog.getWindow();
            if (window != null) {
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        // without onShowListener this line will crash on Android 6 devices
                        window.getDecorView().setBackgroundResource(android.R.color.transparent);
                    }
                });
            }

            dialog.setCanceledOnTouchOutside(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dismiss();
                }
            });
        }

        dialog.show();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static class Builder {
        private ContentDialog contentDialog;

        public Builder(Context context) {
            contentDialog = ContentDialog_.build(context);
        }

        public ContentDialog build() {
            return contentDialog;
        }

        public ContentDialog.Builder setContent(View content) {
            contentDialog.dialogContentContainer.removeAllViews();
            contentDialog.dialogContentContainer.addView(content);
            return this;
        }
    }

}
