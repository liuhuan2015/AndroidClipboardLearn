package com.liuh.androidclipboardlearn;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    private static final String MIME_CONTACT = "vnd.android.cursor.dir/person";

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

    @OnClick({R.id.btn_copy_plain_text, R.id.btn_copy_rich_text, R.id.btn_copy_uri, R.id.btn_copy_intent,
            R.id.btn_copy_multiple})
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
                clipboardUtil.copyUri(getContentResolver(), "Uri拷贝", Uri.parse("content://contacts/people"));
                break;
            case R.id.btn_copy_intent:
                clipboardUtil.clipIntent("Intent拷贝", getOpenBrowserIntent());
                break;
            case R.id.btn_copy_multiple:
                //复制多组数据
                copyMultiple();
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

    private void copyMultiple() {
        //ClipData目前只能设置单个MimeType
        //所以ClipData.Item的类型必须和MimeType设置的相符
        //比如都是文字，都是Uri，或者都是Intent，而不是混合各种模式
        List<ClipData.Item> items = new ArrayList<>();

        ClipData.Item item1 = new ClipData.Item("text1");
        ClipData.Item item2 = new ClipData.Item("text2");
        ClipData.Item item3 = new ClipData.Item("text3");
        ClipData.Item item4 = new ClipData.Item("text4");
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        clipboardUtil.copyMultiple("Multiple Copy", ClipDescription.MIMETYPE_TEXT_PLAIN, items);

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
            //当uri=content://media/external时，copyUri会进入此if-else语句

        } else if (ClipDescription.MIMETYPE_TEXT_INTENT.equals(mimeType)) {
            startActivity(clipData.getItemAt(0).getIntent());
        } else if (MIME_CONTACT.equals(mimeType)) {
            //当uri=content://contacts/people时，copyUri会进入此if-else语句
            Log.e(TAG, clipboardUtil.coercePrimaryClipToText().toString());
            StringBuilder sb = new StringBuilder(tvCopyData.getText() + "\n\n");
            int index = 1;
            Uri uri = clipData.getItemAt(0).getUri();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            while (cursor.moveToNext()) {
                //打印所有联系人姓名
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                sb.append("联系人 " + index + " : " + name + "\n");
            }
            tvCopyData.setText(sb.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clipboardUtil.removeCustomOnPrimaryClipChangedListener(this);
    }
}
