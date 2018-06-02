# AndroidClipboardLearn
android Clipboard学习
>最近在看一个android插件系列的文章，里面提到了Clipboard，自己基本没有使用过，所以来学习学习。
参考文章：[Android Clipboard 详解](https://www.jianshu.com/p/213d7062cdbe)<br>
[google官方文档](https://developer.android.com/guide/topics/text/copy-paste.html)

##### 一.涉及到的类
android中剪贴板涉及到的类有ClipboardManager，ClipData，ClipData.Item，ClipDescription。
1.ClipboardManager
ClipboardManager是系统全局的剪贴板对象控制类。
使用：
``` java
  ClipboardManager  clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
```
2.ClipData
即 clip 对象，在系统剪贴板里只存在一个，当另一个 clip 对象进来时，前一个 clip 对象会消失。
3.ClipData.Item
即 data item，它包含了文本、 Uri 或者 Intent 数据，一个 clip 对象可以包含一个或多个 Item 对象。通过 addItem(ClipData.Item item) 可以实现往 clip 对象中添加 Item。
4.ClipDescription
即 clip metadata，它包含了 ClipData 对象的 metadata 信息。可以通过 getMimeType(int index) 获取，MimeType 一般有以下四种类型：
``` java
 public static final String MIMETYPE_TEXT_PLAIN = "text/plain";//普通文本
 public static final String MIMETYPE_TEXT_HTML = "text/html";//html文本
 public static final String MIMETYPE_TEXT_URILIST = "text/uri-list";//uri列表
 public static final String MIMETYPE_TEXT_INTENT = "text/vnd.android.intent";//intent
 ```
 ##### 二.代码说明
 
