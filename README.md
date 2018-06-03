# AndroidClipboardLearn
android Clipboard学习
>最近在看一个android插件系列的文章，里面提到了Clipboard，自己基本没有使用过，所以来学习学习。
参考文章：[Android Clipboard 详解](https://www.jianshu.com/p/213d7062cdbe)<br>
[google官方文档](https://developer.android.com/guide/topics/text/copy-paste.html)

##### 一.涉及到的类
android中剪贴板涉及到的类有ClipboardManager，ClipData，ClipData.Item，ClipDescription。<br>
1.ClipboardManager<br>
ClipboardManager是系统全局的剪贴板对象控制类。<br>
使用：
``` java
  ClipboardManager  clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
```
2.ClipData<br>
即 clip 对象，在系统剪贴板里只存在一个，当另一个 clip 对象进来时，前一个 clip 对象会消失。<br>
3.ClipData.Item<br>
即 data item，它包含了文本、 Uri 或者 Intent 数据，一个 clip 对象可以包含一个或多个 Item 对象。通过 addItem(ClipData.Item item) 可以实现往 clip 对象中添加 Item。<br>
4.ClipDescription<br>
即 clip metadata，它包含了 ClipData 对象的 metadata 信息。可以通过 getMimeType(int index) 获取，MimeType 一般有以下四种类型：
``` java
 public static final String MIMETYPE_TEXT_PLAIN = "text/plain";//普通文本
 public static final String MIMETYPE_TEXT_HTML = "text/html";//html文本
 public static final String MIMETYPE_TEXT_URILIST = "text/uri-list";//uri列表
 public static final String MIMETYPE_TEXT_INTENT = "text/vnd.android.intent";//intent
 ```
 ##### 二.代码说明
使用剪贴板可以做出支付宝，淘宝的吱口令等效果。<br>
本项目只做了简单的剪贴板内容监测流程实现，并没有具体到某一个比较好的实用效果上去。<br>
因为原作者在使用ClipboardManager的过程中，ClipboardManager.OnPrimaryClipChangedListener会收到多次回调，所以他使用了Handler来进行回调过滤，只保留最后一次回调。<br>
当Clipboard上的数据发生变化时，使用Handler发送一个延时任务，用于回调的过滤，回调事件的处理，考虑到事件过滤和内存泄漏问题，项目流程写的还是有一些复杂的<br>
代码片段：
```java
private ClipboardManager.OnPrimaryClipChangedListener onPrimaryClipChangedListener 
      = new ClipboardManager.OnPrimaryClipChangedListener() {
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
```



