package com.liuh.androidclipboardlearn;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

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
    private static final long THRESHOLD = 100;//阀值
    private Handler mHandler = new Handler();

    ClipboardManager clipboardManager;

    private static ClipboardUtil instance;

    private ClipboardRunnable mRunnable = new ClipboardRunnable();

    private List<CustomOnPrimaryClipChangedListener> mOnPrimaryClipChangedListeners = new ArrayList<>();

    private ClipboardManager.OnPrimaryClipChangedListener onPrimaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            Log.e("---------", "---" + clipboardManager.getPrimaryClip().toString());
            //按照原作者所说，这里的操作是为了在发生多次回调时，只保留最后一次回调，
            //作者测得多次回调间隔不会超过9ms，而且只有最后一次的数据是完整的
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable, THRESHOLD);
        }
    };

    private ClipboardUtil(Context mContext) {
        this.mContext = mContext;
        clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboardManager.hasPrimaryClip()) {
            //如果剪贴板上面有数据
            Log.e("^^^^^^^^^^^", clipboardManager.getPrimaryClip().toString());
        }
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

    public void copyUri(ContentResolver contentResolver, String label, Uri uri) {
        ClipData clipData = ClipData.newUri(contentResolver, label, uri);
        clipboardManager.setPrimaryClip(clipData);
    }


    /**
     * 判断剪贴板内是否有数据
     *
     * @return
     */
    public boolean hasPrimaryClip() {
        return clipboardManager.hasPrimaryClip();
    }

    public CharSequence coercePrimaryClipToText() {
        if (!hasPrimaryClip()) {
            return null;
        }
        return clipboardManager.getPrimaryClip().getItemAt(0).coerceToText(mContext);
    }

    /**
     * 把多组数据放入剪贴板中，比如选中ListView中多个Item，并将Item的数据一起放进剪贴板
     *
     * @param label
     * @param mimeType mimeType is one of them:{@link android.content.ClipDescription#MIMETYPE_TEXT_PLAIN},
     *                 {@link android.content.ClipDescription#MIMETYPE_TEXT_HTML},
     *                 {@link android.content.ClipDescription#MIMETYPE_TEXT_URILIST},
     *                 {@link android.content.ClipDescription#MIMETYPE_TEXT_INTENT}.
     * @param items    放进剪贴板的数据
     */
    public void copyMultiple(String label, String mimeType, List<ClipData.Item> items) {
        if (items == null) {
            throw new NullPointerException("items is null");
        }

        int size = items.size();

        ClipData clipData = new ClipData(label, new String[]{mimeType}, items.get(0));
        for (int i = 0; i < size; i++) {
            clipData.addItem(items.get(i));
        }
        clipboardManager.setPrimaryClip(clipData);
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
