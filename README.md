# 人教数字Android PEPCore文档
### 核心原则
##### 原则一：代码应该简洁易懂，逻辑清晰

> 因为软件是需要人来维护的。这个人在未来很可能不是你。所以首先是为人编写程序，其次才是计算机：不要过分追求技巧，降低程序的可读性。
简洁的代码可以让bug无处藏身。要写出明显没有bug的代码，而不是没有明显bug的代码。

##### 原则二：面向变化编程，而不是面向需求编程。
> 需求是暂时的，只有变化才是永恒的。
本次迭代不能仅仅为了当前的需求，写出扩展性强，易修改的程序才是负责任的做法，对自己负责，对公司负责。

##### 原则三：先保证程序的正确性，防止过度工程
> 过度工程（over-engineering）：在正确可用的代码写出之前就过度地考虑扩展，重用的问题，使得工程过度复杂。


## 项目架构
![](https://images.gitee.com/uploads/images/2019/0927/143246_459f7612_1656776.png)

## 公共库介绍
库  | 描述 |介绍
------------- | ------------- | -------------
library-av  | 音视频库 | 音视频播放基础库
library-ui  |  UI库 | UI通用基础组件
library-base  |  base库 | 基础Base 代码
library-common  | 公共 | 所有组件用到的公共库
library-http  | 网络库 | 基于网络框架的封装
library-image  | 图片库 | 基于图片框架的封装
library-count  | 统计库 | 基于友盟统计的封装-后期可以自己做统计库
library-foxit  | 福昕库 | 福昕的SDK+UI
library-foxit-pep  | pdf 库  | 基于福昕做的人教自有SDK
module-books  | 垂直平台  | 原垂直平台-可以作为库,也可以作为单独app运行

----

### PEP代码使用
## 1.组件化切换module 为 单独APP
在项目跟目中的`gradle.properties` 文件中`isModule`

```java
//表示可以已单独app 运行
isModule=true
//已依赖包的形式给主APP运行
isModule=false

```



## 2.网络请求PEPHttpManager

网络请求底层用的是 `Retrofit` PEP网络库只是在原有框架上进行简单封装.
#### a.初始化
在项目Application中进行初始化操作,并设置基础baseUrl
```java
PEPHttpManager.getInstance().init("https://api.apiopen.top/");
```

#### b.增加拦截器
项目中有多个baseUrl ,可以使用拦截器模式动态替换,下面介绍使用如何添加拦截器

```java
PEPHttpManager.getInstance().addInterceptor();
```

#### c.进行网络请求

**和Retrofit** 请求网络一样,先定义接口类型**GET** 和 **POST** 及 返回的 **Model** 和 **Params**


```java
public interface ApiService {
    @GET("getJoke")
    Call<BaseListModel<JokeModel>> getJoke(@Query("page") int page);
}
```


请求方式式
```java
PEPHttpManager.getInstance().getService(ApiService.class).getJoke(1).enqueue(new Callback<BaseListModel<JokeModel>>() {
            @Override
            public void onResponse(Call<BaseListModel<JokeModel>> call, Response<BaseListModel<JokeModel>> response) {

            }

            @Override
            public void onFailure(Call<BaseListModel<JokeModel>> call, Throwable t) {

            }
        });
```

## 3.网络下载类PEPDownloadManager

#### a.下载方法的使用
```java
PEPDownloadManager.downLoad("url", "path").setOnProgressListener(progress -> {

        }).start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {

            }

            @Override
            public void onError(Error error) {

            }
        });
```
#### b.暂停与恢复下载
```java
PEPDownloadManager.pause(downLoadId);
 PEPDownloadManager.resume(downLoadId);
```
#### c.取消下载

```java
 PEPDownloadManager.cancel(downloadRequest);
```
## 4.图片加载类的使用PEPImageManager
```java
//加载圆图
PEPImageManager.getInstance().loadCircleCrop(this, imageView1, "url");
//加载正常图片
 PEPImageManager.getInstance().load(this, imageView2, "url");

```

## 4.音视频的使用方式
其中 音视频播放 都是基于ExoPlayer 进行二次封装实现
####a.视频播放
通用视频播放器可以调用如下代码即可实现快速播放
```java
PEPVideoFragment pepVideoFragment = new PEPVideoFragment();
pepVideoFragment.setData(VIDEO_TYPE_M3U8, "URL");
pepVideoFragment.show(getSupportFragmentManager(), "PEPVideoFragment");
```


####b.音频播放
音频播放由于没有UI呈现,只做逻辑处理.可以快速的播放音乐及回调
初始化播放器类,一般可以放在成员变量中,方便使用
```java
PEPAudioManager pepAudioManager = new PEPAudioManager(this);
```
设置播放地址及播放音频暂停恢复等操作
```java
//设置播放URL
pepAudioManager.setUrl("url", PEPAvType.VIDEO_TYPE_DEF);

//播放
pepAudioManager.play();

//暂停
pepAudioManager.pause();

//清除资源
pepAudioManager.release();

//设置播放速度
pepAudioManager.speed(4F);

//结合progress 进行设置播放进度
long duration = pepAudioManager.getDuration();
long newPosition = (duration * progress) / 1000L;
pepAudioManager.seekTo((int) newPosition);
```
****!!!注意由于播放处于异步操作,需要在页面关闭时或者停止音乐播放操作时候,要及时进行release.否则资源将不会关闭****

音频播放的时候回调监听在PEPAudioManager 中也可以很方便的进行实现,无需繁琐操作监听代码如下:
```java
pepAudioManager.addPepAudioListener(pepAudioListener);
public interface PEPAudioListener {
    /**
     * On play state. 当前播放状态
     * @param isPlay the is play
     */
    void onPlayState(boolean isPlay);

    /**
     * On buffering.缓冲中
     */
    void onBuffering();

    /**
     * On play end.播放结束
     */
    void onPlayEnd();

    /**
     * On play ready.准备播放
     */
    void onPlayReady();

    /**
     * On player error.播放出错
     * @param error the error
     */
    void onPlayerError(ExoPlaybackException error);

    /**
     * On progress. 实时进度回调
     *
     * @param progress     the progress 当前进度
     * @param bprogress    the bprogress 缓冲的进度
     * @param maxProgress  the max progress 进度最大值 1000
     * @param currentTime  the current time 当前播放的时间戳 00:00格式
     * @param durationTime the duration time 当前资源最大可播放时间 00:00格式
     */
    void onProgress(int progress, int bprogress, int maxProgress, String currentTime, String durationTime);
}
```

#### c.其中音频视频支持的格式包括如下
```java
public class PEPAvType {
    /**
     * The constant VIDEO_TYPE_DEF.
     */
    public  static final int VIDEO_TYPE_DEF = 1;
    /**
     * The constant VIDEO_TYPE_RTMP.
     */
    public  static final int VIDEO_TYPE_RTMP = 2;
    /**
     * The constant VIDEO_TYPE_M3U8.
     */
    public static final int VIDEO_TYPE_M3U8 = 3;
}
```

## 5.UI组件调用方式

####a.菊花弹窗调用
```java
//显示菊花
PEPProgressView.show(this);

//隐藏菊花
PEPProgressView.dismiss();
```

如果你集成的是PEPBaseActivity 的话 调用将更加方便可以直接使用父类中的

```java
//显示菊花
showProgress();

//隐藏菊花
dismissProgress();
```

####b.页面Loading 的调用方式

使用前需要在布局中嵌套自已原有的布局

```xml
<com.pep.core.uibase.PEPLoadingView
            android:id="@+id/pep_loading"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
		<!--你自己原先的布局-->
       <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

            </LinearLayout>
 </com.pep.core.uibase.PEPLoadingView>
```

loadingview 的状态设置

```java
//显示loading
pepLoading.setLoadingType(PEPLoadingView.PEP_LOADING_LOAD);

//结束loading
pepLoading.setLoadingType(PEPLoadingView.PEP_LOADING_FINISH);

//失败时候展示的view
pepLoading.setLoadingType(PEPLoadingView.PEP_LOADING_ERROR)
```

如果网络是被情况下,在点击从新下载的时候可以注册监听从新请求网络

```java
pepLoading.setOnErrorListener(new PEPLoadingView.OnLoadingErrorListener() {
            @Override
            public void onClick() {

            }
});
```

#### c.dialogFragment 支持动画展示(上下左右)

需要现在dialogfragment 中明确 动画执行的方向代码如下
```java
    @Override
    public void onStart() {
        super.onStart();
        PEPAnimate.initWindow(this, Gravity.LEFT);
    }
```

设置要播放动画的view
```java
 @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_left_demo, container, false);
		//主要看这行
		PEPAnimate.leftStartAnimate(contentView);
		Objects.requireNonNull(getDialog()).setOnKeyListener(dialogInterface);
        return contentView;
    }
```

关闭动画比如点击返回键的时候执行移除操作
```java
    /**
     * The Dialog interface.
     */
    private DialogInterface.OnKeyListener dialogInterface = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
			//监听dialog 返回键
            if (KEYCODE_BACK == i && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                //执行关闭动画
				PEPAnimate.leftCloseAnimate(contentView, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        //动画执行完成进行关闭dialogfragment
                        dialogInterface.dismiss();
                    }
                });
                return true;
            }
            return false;
        }
    };

```





