package com.liuh.pastemodule;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * 获取粘贴板的内容
 */
public class MainActivity extends AppCompatActivity {

    String pasteData = "";

    Uri pasteUri;

    // Declares a MIME type constant to match against the MIME types offered by the provider
    public static final String MIME_TYPE_CONTACT = "vnd.android.cursor.item/vnd.example.contact";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        // Gets a content resolver instance
        ContentResolver cr = getContentResolver();

        if (clipboardManager.hasPrimaryClip()) {
            //粘贴板上面有数据

            ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);

            pasteData = (String) item.getText();

            pasteUri = item.getUri();

            if (pasteData != null && pasteData.length() > 0) {
                //有文本数据
                Toast.makeText(this, "检测到文本数据：" + pasteData, Toast.LENGTH_SHORT).show();
            }

            if (pasteUri != null) {
                String uriMimeType = cr.getType(pasteUri);

                Log.e("GetPasteData_Uri", "-----------" + uriMimeType);
                if (uriMimeType != null) {
                    if (uriMimeType.equals(MIME_TYPE_CONTACT)) {
                        Cursor pasteCursor = cr.query(pasteUri, null, null, null, null);
                        if (pasteCursor != null) {
                            if (pasteCursor.moveToFirst()) {
//                                pasteCursor.get
                                // get the data from the Cursor here. The code will vary according to the
                                // format of the data model.
                            }
                        }

                        pasteCursor.close();
                    }

                }


            }

        }


    }


}
