## 文件管理器
#### 说明：针对android系统数据库的文件更新不及时的问题，我写了这个文件管理器，可以对系统中文件的增加删除和移动进行及时监听。<br />
#### 局限性：
当我们的应用不在栈顶的时候，文件将不再监听。<br />
#### 实现原理：
由于文件改变后会在上一级文件夹上表现出来：即时间的改变。于是我使用了FileObserver进行递归循环对所有文件夹进行监听。
第一次应用启动时会做入库操作，这个时间可能会很长（和手机中的文件数量有关），当应用被杀死后的第二次启动依旧会对已更新的文件夹进行入库更新操作。<br />
#### 使用了第三方库：
```
dependencies {
		'com.jakewharton:butterknife:8.6.0'
		'io.objectbox:objectbox-android:0.9.13'
		'io.reactivex.rxjava2:rxjava:2.x.y'
		'io.reactivex.rxjava2:rxandroid:2.0.1'
		'com.trello.rxlifecycle2:rxlifecycle:2.1.0'
		'com.trello.rxlifecycle2:rxlifecycle-android:2.1.0'
		'com.trello.rxlifecycle2:rxlifecycle-components:2.1.0'
}
```
##### 调用：
```java
public class Application extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        FileUpdatingService.startService(this);
    }
}
```
