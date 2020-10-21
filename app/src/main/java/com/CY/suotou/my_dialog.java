package com.CY.suotou;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.CY.suotou.R;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import static android.content.Context.MODE_PRIVATE;

public class my_dialog {
    public MaterialDialog.Builder dialog_about(final Context context)
    {
        final MaterialDialog.Builder materialDialog=new MaterialDialog.Builder(context);
        materialDialog.title("关于软件");
        materialDialog.content(context.getResources().getString(R.string.myabout));
        materialDialog.checkBoxPromptRes(R.string.about_select, false, null);

        materialDialog.positiveText("同意");
        materialDialog.neutralText("不同意");
        materialDialog.onNeutral(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                System.exit(0);
            }
        });
        materialDialog.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (dialog.isPromptCheckBoxChecked())
                {
                    SharedPreferences.Editor share=context.getSharedPreferences("data",MODE_PRIVATE).edit();
                    share.putBoolean("first_use",false);
                    share.apply();
                }else
                {
                    Toast.makeText(context,"请勾选同意以上内容后继续",Toast.LENGTH_LONG).show();
                    dialog_about(context);
                }
            }
        });
    materialDialog.show() ;
        return materialDialog;
    }
    public MaterialDialog dialog_itemattr(final Context context)
    {
        final MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .customView(R.layout.layout_dialog_itemedit, false)
                .show();
        Window window = materialDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.background_dialog_item);
            window.getDecorView().setPadding(0, 0, 0, 20);
            //getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            window.getAttributes().height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setGravity(Gravity.BOTTOM);
        }
        return materialDialog;
    }
}
