package com.liuh.androidclipboardlearn;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A ClipData.Item object contains the text, URI, or Intent data:Text;Uri;Intent
 * <p>
 * 这个Demo的流程是当Clipboard上的数据发生变化时，使用Handler发送一个延时任务，
 * 在这个任务里面使用回调在MainActivity的TextView里面显示ClipData。感觉流程写的还是有点小复杂的。
 */
public class MainActivity extends AppCompatActivity implements ClipboardUtil.CustomOnPrimaryClipChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tv_copy_data)
    TextView tvCopyData;

    ClipboardUtil clipboardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        clipboardUtil = ClipboardUtil.getInstance();
        clipboardUtil.addCustomOnPrimaryClipChangedListener(this);
    }

    @OnClick({R.id.btn_copy_plain_text, R.id.btn_copy_rich_text, R.id.btn_copy_uri, R.id.btn_copy_intent})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_copy_plain_text:
                //复制文本数据
                Log.e(TAG, "复制文本");
                clipboardUtil.clipPlainText("简单文本文字", "Hello,世界");
                break;
            case R.id.btn_copy_rich_text:
                clipboardUtil.clipRichText("HTML拷贝", "我是HTML",
                        "<strong style=\"margin: 0px; padding: 0px; border: 0px; color: rgb(64, 64, 64); font-family: STHeiti, 'Microsoft YaHei', Helvetica, Arial, sans-serif; font-size: 17px; font-style: normal; font-variant: normal; letter-spacing: normal; line-height: 25.920001983642578px; orphans: auto; text-align: justify; text-indent: 34.560001373291016px; text-transform: none; white-space: normal; widows: auto; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(235, 23, 23);\">央视</strong>");
                break;
            case R.id.btn_copy_uri:
//                clipUriData();
                break;
            case R.id.btn_copy_intent:
                clipboardUtil.clipIntent("Intent拷贝", getOpenBrowserIntent());
                break;
        }
    }

    /**
     * 打开浏览器
     *
     * @return
     */
    private Intent getOpenBrowserIntent() {
        Uri uri = Uri.parse("http://www.baidu.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        return intent;
    }

    @Override
    public void onPrimaryClipChanged(ClipboardManager clipboardManager) {
        //粘贴版上的数据发生了改变
        tvCopyData.setText(clipboardManager.getPrimaryClip().toString());

        ClipData clipData = clipboardManager.getPrimaryClip();
        String mimeType = clipboardManager.getPrimaryClipDescription().getMimeType(0);
        Log.e(TAG, "mimeType : " + mimeType);

        if (ClipDescription.MIMETYPE_TEXT_PLAIN.equals(mimeType)) {

        } else if (ClipDescription.MIMETYPE_TEXT_HTML.equals(mimeType)) {

        } else if (ClipDescription.MIMETYPE_TEXT_URILIST.equals(mimeType)) {

        } else if (ClipDescription.MIMETYPE_TEXT_INTENT.equals(mimeType)) {
            startActivity(clipData.getItemAt(0).getIntent());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clipboardUtil.removeCustomOnPrimaryClipChangedListener(this);
    }
}
