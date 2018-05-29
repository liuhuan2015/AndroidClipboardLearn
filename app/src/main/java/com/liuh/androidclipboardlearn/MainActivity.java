package com.liuh.androidclipboardlearn;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * A ClipData.Item object contains the text, URI, or Intent data:Text;Uri;Intent
 */
public class MainActivity extends AppCompatActivity {

    // Declares the base URI string
    private static final String CONTACTS = "content://com.example.contacts";

    // Declares a path string for URIs that you use to copy data
    private static final String COPY_PATH = "/copy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        //粘贴文本文字
//        ClipPlainText(clipboardManager);
        //粘贴Uri类型数据
        clipUriData(clipboardManager);

    }

    private void clipUriData(ClipboardManager clipboardManager) {
        // Declares the Uri to paste to the clipboard
        Uri copyUri = Uri.parse(CONTACTS + COPY_PATH + "/" + "liuh");
        ClipData clipData = ClipData.newUri(getContentResolver(), "URI", copyUri);

        clipboardManager.setPrimaryClip(clipData);
    }

    private void ClipPlainText(ClipboardManager clipboardManager) {
        //1.Text
        ClipData clipData = ClipData.newPlainText("简单文本文字", "Hello,世界");

        //把粘贴数据放进粘贴板
        clipboardManager.setPrimaryClip(clipData);
    }
}
