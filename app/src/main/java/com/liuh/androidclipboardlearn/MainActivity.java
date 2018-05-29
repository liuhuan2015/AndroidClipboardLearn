package com.liuh.androidclipboardlearn;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
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
 */
public class MainActivity extends AppCompatActivity implements ClipboardManager.OnPrimaryClipChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.tv_copy_data)
    TextView tvCopyData;

    // Declares the base URI string
    private static final String CONTACTS = "content://com.example.contacts";

    // Declares a path string for URIs that you use to copy data
    private static final String COPY_PATH = "/copy";

    ClipboardManager clipboardManager;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    @OnClick({R.id.btn_copy_plain_text, R.id.btn_copy_rich_text, R.id.btn_copy_uri, R.id.btn_copy_intent})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_copy_plain_text:
                //复制文本数据
                Log.e(TAG, "复制文本");
                clipPlainText();
                break;
            case R.id.btn_copy_rich_text:
                break;
            case R.id.btn_copy_uri:
                clipUriData();
                break;
            case R.id.btn_copy_intent:
                break;

        }


    }


    private void clipUriData() {
        // Declares the Uri to paste to the clipboard
        Uri copyUri = Uri.parse(CONTACTS + COPY_PATH + "/" + "liuh");
        ClipData clipData = ClipData.newUri(getContentResolver(), "URI", copyUri);

        clipboardManager.setPrimaryClip(clipData);
    }

    private void clipPlainText() {
        //1.Text
        ClipData clipData = ClipData.newPlainText("简单文本文字", "Hello,世界");

        //把粘贴数据放进粘贴板
        clipboardManager.setPrimaryClip(clipData);
    }

    @Override
    public void onPrimaryClipChanged() {
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

    class MyonPrimaryClipChangedListener implements ClipboardManager.OnPrimaryClipChangedListener {
        @Override
        public void onPrimaryClipChanged() {


        }
    }

}
