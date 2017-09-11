package com.example.liujianglong.testapp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by liujianglong on 2017/9/11.
 */

public class ToastUtil {
    private static Toast mToast;
    private static int mGravity = -1;

    private ToastUtil() {
        /*prevent form initial*/
    }

    public static void configGravity(int gravity) {
        mGravity = gravity;
    }

    private static LinearLayout getView(Context context) {
        Context appContext = context.getApplicationContext();
        LinearLayout layout = new LinearLayout(appContext);
        int padding = (int) DisplayUtil.dip2px(12);
        layout.setPadding(padding, padding, padding, padding);
        layout.setBackgroundResource(R.drawable.toast_bg);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        TextView textView = new TextView(appContext);
        ImageView imageView = new ImageView(appContext);
        imageView.setVisibility(View.GONE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, padding, 0);
        imageView.setLayoutParams(lp);
        textView.setTextColor(Color.WHITE);
        layout.addView(imageView);
        layout.addView(textView);
        return layout;
    }

    /**
     * set text ,duration and drawable
     */
    public static void showToastWithDrawable(Context context, String text, int drawableId, int duration) {
        LinearLayout toastView = getView(context);
        TextView textViewCenter = (TextView) toastView.getChildAt(1);
        textViewCenter.setText(text);
        if (drawableId > 0) {
            ImageView image = (ImageView) toastView.getChildAt(0);
            image.setBackgroundResource(drawableId);
            image.setVisibility(View.VISIBLE);
        }
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = new Toast(context.getApplicationContext());

        if (mGravity >= 0) {
            mToast.setGravity(mGravity, 0, 0);
        }
        mToast.setDuration(duration);
        mToast.setView(toastView);
        mToast.show();
    }

    public static void showDrawable(Context context, int resId, String text) {
        showToastWithDrawable(context, text, resId, Toast.LENGTH_SHORT);
    }

    public static void showDrawable(Context context, int resId, int stringResId) {
        showToastWithDrawable(context, context.getString(stringResId), resId, Toast.LENGTH_SHORT);
    }

    /**
     * set text, default LENGTH_SHORT time
     */
    public static void showToast(Context context, String text) {
        showToastWithDrawable(context, text, -1, Toast.LENGTH_SHORT);
    }

    /**
     * set string resource ID, default LENGTH_SHORT time
     */
    public static void showToast(Context context, int stringResId) {
        showToast(context, context.getString(stringResId));
    }
}
