# AndroidClipboardLearn
android Clipboard学习
>最近在看一个android插件系列的文章，里面提到了Clipboard，自己基本没有使用过，所以来学习学习。
参考文章：[Android Clipboard 详解](https://www.jianshu.com/p/213d7062cdbe)<br>
[google官方文档](https://developer.android.com/guide/topics/text/copy-paste.html)

一.涉及到的类
android中剪贴板涉及到的类有ClipboardManager，ClipData，ClipData.Item，ClipDescription。
'''java
  ClipboardManager  clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
'''
