package com.liuh.androidclipboardlearn;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huan on 2018/5/30.
 */

public class ClipboardUtil {

    // Declares the base URI string
    private static final String CONTACTS = "content://com.example.contacts";

    // Declares a path string for URIs that you use to copy data
    private static final String COPY_PATH = "/copy";

    private Context mContext;
    private static final long THRESHOLD = 100;
    private Handler mHandler = new Handler();

    ClipboardManager clipboardManager;

    private static ClipboardUtil instance;

    private ClipboardRunnable mRunnable = new ClipboardRunnable();

    private List<CustomOnPrimaryClipChangedListener> mOnPrimaryClipChangedListeners = new ArrayList<>();

    private ClipboardManager.OnPrimaryClipChangedListener onPrimaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable, THRESHOLD);
        }
    };

    private ClipboardUtil(Context mContext) {
        this.mContext = mContext;
        clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(onPrimaryClipChangedListener);
    }

    /**
     * 采用单例模式
     * 为了避免内存泄露，建议这个方法在Application的onCreate方法里面调用（原作者注释）
     *
     * @param context
     * @return
     */
    public static void init(Context context) {
        if (instance == null) {
            instance = new ClipboardUtil(context);
        }
    }

    /**
     * 获取ClipboardUtil实例，获取前要先初始化
     *
     * @return
     */
    public static ClipboardUtil getInstance() {
        return instance;
    }

    public void clipPlainText(String label, String text) {
        //1.Text
        ClipData clipData = ClipData.newPlainText(label, text);

        //把粘贴数据放进粘贴板
        clipboardManager.setPrimaryClip(clipData);
    }

    public void clipRichText(String label, String text, String htmlText) {
        ClipData clipData = ClipData.newHtmlText(label, text, htmlText);
        clipboardManager.setPrimaryClip(clipData);

    }

    public void clipIntent(String text, Intent intent) {
        ClipData clipData = ClipData.newIntent(text, intent);
        clipboardManager.setPrimaryClip(clipData);
    }

    public void clipUriData() {
        // Declares the Uri to paste to the clipboard
        Uri copyUri = Uri.parse(CONTACTS + COPY_PATH + "/" + "liuh");
//        ClipData clipData = ClipData.newUri(getContentResolver(), "URI", copyUri);
//
//        clipboardManager.setPrimaryClip(clipData);
    }

    public void addCustomOnPrimaryClipChangedListener(CustomOnPrimaryClipChangedListener listener) {
        mOnPrimaryClipChangedListeners.add(listener);
    }

    public void removeCustomOnPrimaryClipChangedListener(CustomOnPrimaryClipChangedListener listener) {
        mOnPrimaryClipChangedListeners.remove(listener);
    }


    private class ClipboardRunnable implements Runnable {

        @Override
        public void run() {
            for (CustomOnPrimaryClipChangedListener listener : mOnPrimaryClipChangedListeners) {
                listener.onPrimaryClipChanged(clipboardManager);
            }
        }
    }

    /**
     * 自定义的OnPrimaryClipChangedListener
     */
    public interface CustomOnPrimaryClipChangedListener {
        void onPrimaryClipChanged(ClipboardManager clipboardManager);
    }

}
