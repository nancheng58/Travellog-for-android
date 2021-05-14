package com.code.travellog.core.view.video;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.adapter.adapter.DelegateAdapter;
import com.adapter.adapter.ItemData;
import com.adapter.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.code.travellog.R;
import com.code.travellog.ai.YoloV5Classifier;
import com.code.travellog.config.Constants;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.album.AlbumPojo;
import com.code.travellog.core.data.pojo.album.AlbumResultPojo;
import com.code.travellog.core.data.pojo.common.TypeVo;
import com.code.travellog.core.data.pojo.plog.PlogPojo;
import com.code.travellog.core.data.pojo.video.VideoListPojo;
import com.code.travellog.core.data.pojo.video.VideoMergePojo;
import com.code.travellog.core.data.pojo.video.VideoPojo;
import com.code.travellog.core.data.repository.VideoRepository;
import com.code.travellog.core.view.base.BaseListFragment;
import com.code.travellog.core.view.common.TypeItemView;
import com.code.travellog.core.view.plog.PlogDetailsActivity;
import com.code.travellog.core.view.video.holder.VideoItemHolder;
import com.code.travellog.core.viewmodel.VideoViewModel;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.util.DisplayUtil;
import com.code.travellog.util.ToastUtils;
import com.mvvm.base.AbsLifecycleActivity;
import com.mvvm.base.BaseActivity;
import com.mvvm.http.HttpHelper;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;


/**
 * @description 视频页面
 * @time 2021/3/24 20:46
 */

public class VideoDetailsActivity extends AbsLifecycleActivity<VideoViewModel> implements OnItemClickListener {
    private StandardGSYVideoPlayer mVideoPlayer;
    private OrientationUtils mOrientationUtils;

    protected RecyclerView mRecyclerView;

    private String albumId;
    private DelegateAdapter adapter;
    private ItemData mItems;
    @Override
    protected void onStateRefresh() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_details;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        albumId = getIntent().getStringExtra(Constants.AlBUM_ID);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DelegateAdapter.Builder<>()
                .bind(TypeVo.class,new TypeItemView(VideoDetailsActivity.this))
                .bind(VideoPojo.class, new VideoItemHolder(VideoDetailsActivity.this)).build();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setFocusable(true);
        mVideoPlayer = findViewById(R.id.video_player);
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
        int widthVideo = DisplayUtil.getScreenWidth(this);
        int heightVideo = widthVideo * 9 / 16;
        mVideoPlayer.getLayoutParams().width = widthVideo;
        mVideoPlayer.getLayoutParams().height = heightVideo;
        mOrientationUtils = new OrientationUtils(this, mVideoPlayer);
        mOrientationUtils.setEnable(false);
        mVideoPlayer.setIsTouchWiget(true);
        mVideoPlayer.setRotateViewAuto(false);
        mVideoPlayer.setLockLand(false);
        mVideoPlayer.setShowFullAnimation(false);
        mVideoPlayer.setNeedLockFull(true);
        mVideoPlayer.setEnlargeImageRes(R.drawable.player_controller_full_screen);
        mVideoPlayer.setShrinkImageRes(R.drawable.player_controller_small_screen);
        mVideoPlayer.setBottomProgressBarDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.local_big));
        mVideoPlayer.getFullscreenButton().setOnClickListener(v -> {
            //直接横屏
            mOrientationUtils.resolveByClick();
            mVideoPlayer.startWindowFullscreen(VideoDetailsActivity.this, true, true);
        });

        mVideoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                mOrientationUtils.setEnable(true);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);

                if (mOrientationUtils != null) {
                    mOrientationUtils.backToProtVideo();
                }
            }
        });
        getNetWorkData();
        dataObserver();
    }

    @Override
    protected void dataObserver(){
        registerSubscriber(VideoRepository.EVENT_KEY_VIDEOLIST, VideoMergePojo.class)
                .observe(this, videoMergePojo -> {
                    if (videoMergePojo != null) {
                        VideoDetailsActivity.this.addItems(videoMergePojo);
                    }

                });
    }

    @SuppressLint("CheckResult")
    private void getNetWorkData() {
        if (TextUtils.isEmpty(albumId)) {
            ToastUtils.showToast("加载出错");
            return;
        }
        String url = URL.ALBUM_URL+albumId+"/status";
        HttpHelper.getInstance().create(ApiService.class).getAlbumResult(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new RxSubscriber<AlbumResultPojo>() {

                    @Override
                    public void onSuccess(AlbumResultPojo albumResultPojo) {
                        if(albumResultPojo.data == null )
                            ToastUtils.showToast(albumResultPojo.msg);
                        else if(albumResultPojo.data.status != 200)
                            ToastUtils.showToast("该影集"+albumResultPojo.data.status_msg);
                        else setUI(albumResultPojo);
//                        getAboutData();
                    }

                    @Override
                    public void onFailure(String msg,int code) {

                    }


                });

            mViewModel.getVideoListData();
    }
    private void setUI(AlbumResultPojo albumResultPojo)
    {
        //                        lessonData = courseDetailVo.data;
//                        fCatalogId = lessonData.f_catalog_id;
//                        sCatalogId = lessonData.s_catalog_id;
//                        teacherId = lessonData.teacheruid;
        ImageView imageView = new ImageView(VideoDetailsActivity.this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //TODO

        Glide.with(VideoDetailsActivity.this).load(URL.IMAGE_URL+albumResultPojo.data.image_urls.get(0)).into(imageView);
        mVideoPlayer.setThumbImageView(imageView);
        mVideoPlayer.setUp(URL.IMAGE_URL+albumResultPojo.data.result_msg, false, albumResultPojo.data.movie_title);
        mVideoPlayer.startPlayLogic();
        loadManager.showSuccess();
    }


//    private void setData(CourseDetailRemVideoVo lessonDetailAboutVideoBean) {
//        ItemData items = new ItemData();
//        DelegateAdapter adapter = new DelegateAdapter.Builder<>()
//        .bind(CourseDetailRemVideoVo.DataBean.CourseListBean.class, new ForumRecommendHolder(VideoDetailsActivity.this)).build();
//        mRecyclerView.setAdapter(adapter);
//        items.addAll(lessonDetailAboutVideoBean.getData().getCourse_list());
////        mRecyclerView.refreshComplete(items, false);
//        adapter.setDatas(items);
//        adapter.notifyDataSetChanged();
//    }

    private void addItems(VideoMergePojo videoMergePojo) {

        if (videoMergePojo.videoListPojo ==null ||
                videoMergePojo.videoListPojo.data ==null
                ||videoMergePojo.videoListPojo.data.movies ==null) return;
        mItems = new ItemData();
        mItems.clear();

        mItems.add(new TypeVo("相关推荐"));
        mItems.addAll(videoMergePojo.videoListPojo.data.movies);
//        mRecyclerView.refreshComplete(items, false);
        adapter.setDatas(mItems);
        adapter.notifyDataSetChanged();

//        mItems.add(videoMergePojo.bannerListVo);


    }

    @Override
    public void onItemClick(View view, int i, Object object) {
        if (object != null) {
            if (object instanceof VideoPojo) {
                Intent intent = new Intent(this, VideoDetailsActivity.class);
                intent.putExtra(Constants.AlBUM_ID, String.valueOf(((VideoPojo) object).work_id));
                startActivity(intent);
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (null!=mVideoPlayer){
            mVideoPlayer.onVideoPause();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null!=mVideoPlayer){
            mVideoPlayer.onVideoResume();
        }
    }
}
