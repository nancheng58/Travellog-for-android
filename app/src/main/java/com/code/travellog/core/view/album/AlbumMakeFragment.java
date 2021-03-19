package com.code.travellog.core.view.album;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.library.SuperTextView;
import com.code.travellog.AI.AiBoostManager;
import com.code.travellog.R;
import com.code.travellog.config.Constants;
import com.code.travellog.config.URL;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.album.AlbumPostPojo;
import com.code.travellog.core.data.pojo.album.AlbumResultPojo;
import com.code.travellog.core.data.pojo.album.AlbumWorkPojo;
import com.code.travellog.core.data.source.AlbumRepository;
import com.code.travellog.core.vm.AlbumViewModel;
import com.code.travellog.glide.GlideCacheEngine;
import com.code.travellog.glide.GlideEngine;
import com.code.travellog.network.ApiService;
import com.code.travellog.network.rx.RxSubscriber;
import com.code.travellog.ui.FullyGridLayoutManager;
import com.code.travellog.ui.MakeAlbumActivity;
import com.code.travellog.ui.adapter.GridImageAdapter;
import com.code.travellog.ui.listener.DragListener;
import com.code.travellog.util.JsonUtils;
import com.code.travellog.util.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.style.PictureSelectorUIStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.mvvm.base.AbsLifecycleFragment;
import com.mvvm.base.BaseFragment;
import com.mvvm.event.LiveBus;
import com.mvvm.http.HttpHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @description:
 * @date: 2021/3/7
 */
public class AlbumMakeFragment extends AbsLifecycleFragment<AlbumViewModel> {

    @BindView(R.id.tv_delete_text)
    TextView tvDeleteText;
    @BindView(R.id.left_back)
    ImageView leftBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_submit)
    TextView btn_submit;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_updateimg)
    SuperTextView updateimgText;
    @BindView(R.id.tv_albumtitle)
    SuperTextView tvAlbumtitle;
    @BindView(R.id.tv_albumbgm)
    SuperTextView tvAlbumbgm;
    @BindView(R.id.tv_albumpoetry)
    SuperTextView tvAlbumpoetry;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.description)
    EditText description;



    private final static String TAG = AlbumMakeFragment.class.getSimpleName();
    int aspect_ratio_x = 0;
    int aspect_ratio_y = 0;
    private final int maxSelectNum = 30;
    private final List<LocalMedia> selectList = new ArrayList<>();
    private boolean needScaleBig = true;
    private boolean needScaleSmall = true;
    private boolean isUpward;
    private boolean isGif = false;
    private boolean isVoice = true;
    private boolean isCrop = true;
    private boolean isRiginal = true;
    private int themeId;
    private ItemTouchHelper mItemTouchHelper;
    private PictureParameterStyle mPictureParameterStyle;
    private PictureCropParameterStyle mCropParameterStyle;
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    private PictureSelectorUIStyle mSelectorUIStyle;
    private DragListener mDragListener;
    private List<LocalMedia> localMediaList;
    private Unbinder unbinder;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private List<AiBoostManager.Data> mDetectorresult;
    private GridImageAdapter mAdapter;
    private AiBoostManager aiBoostManager ;
    private int workid;
    public static AlbumMakeFragment newInstance() {
        return new AlbumMakeFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_albummake;
    }

    @Override
    protected int getContentResId() {
        return R.id.content_album;
    }

    @Override
    public void initView(Bundle state) {

        unbinder = ButterKnife.bind(this, rootView);
        super.initView(state);
        loadManager.showSuccess();
        updateimgText.setLeftTopTextIsBold(true);
        tvAlbumbgm.setLeftTopTextIsBold(true);
        tvAlbumpoetry.setLeftTopTextIsBold(true);
        tvAlbumtitle.setLeftTopTextIsBold(true);
        aiBoostManager = AiBoostManager.newInstance();
        FullyGridLayoutManager manager = new FullyGridLayoutManager(activity,
                4, GridLayoutManager.VERTICAL, false);
        aiBoostManager.initialize(activity,"mobilenet_quant_v1_224.tflite",1001,"labels_mobilenet_quant_v1_224.txt");
        recycler.setLayoutManager(manager);
        recycler.addItemDecoration(new GridSpacingItemDecoration(4,
                ScreenUtils.dip2px(activity, 8), false));// item 分割和分布样式
        mAdapter = new GridImageAdapter(getContext(), onAddPicClickListener);
        if (state != null && state.getParcelableArrayList("selectorList") != null) {
            mAdapter.setList(state.getParcelableArrayList("selectorList"));
        }
        dataObserver();
        themeId = R.style.picture_white_style;
        getWhiteStyle();
        btn_submit.setOnClickListener(v -> {
            getImageObjectDetector(localMediaList);
            LiveBus.getDefault().subscribe(AiBoostManager.EVENT_KEY_OBJECT,null,List.class).observe(this,list -> {
                mDetectorresult = list;
                postAlbum();
                Log.w(TAG,mDetectorresult.toString());
            });

        });
        mWindowAnimationStyle = new PictureWindowAnimationStyle();
        mWindowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        mAdapter.setSelectMax(maxSelectNum);
        recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((v, position) -> {
            List<LocalMedia> selectList = mAdapter.getData();
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:
                        // 预览视频
                        PictureSelector.create(AlbumMakeFragment.this)
                                .themeStyle(R.style.picture_default_style)
//                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:
                        // 预览音频
                        PictureSelector.create(AlbumMakeFragment.this)
                                .externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                        break;
                    default:
                        // 预览图片 可自定长按保存路径
//                        PictureWindowAnimationStyle animationStyle = new PictureWindowAnimationStyle();
//                        animationStyle.activityPreviewEnterAnimation = R.anim.picture_anim_up_in;
//                        animationStyle.activityPreviewExitAnimation = R.anim.picture_anim_down_out;
                        PictureSelector.create(AlbumMakeFragment.this)
                                .themeStyle(R.style.picture_default_style) // xml设置主题
//                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(position, selectList);
                        break;
                }
            }
        });
        mAdapter.setItemLongClickListener((holder, position, v) -> {
            //如果item不是最后一个，则执行拖拽
            needScaleBig = true;
            needScaleSmall = true;
            int size = mAdapter.getData().size();
            if (size != maxSelectNum) {
                mItemTouchHelper.startDrag(holder);
                return;
            }
            if (holder.getLayoutPosition() != size - 1) {
                mItemTouchHelper.startDrag(holder);
            }
        });
        mDragListener = new DragListener() {
            @Override
            public void deleteState(boolean isDelete) {
                if (isDelete) {
                    tvDeleteText.setText(getString(R.string.app_let_go_drag_delete));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        tvDeleteText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_let_go_delete, 0, 0);
                    }
                } else {
                    tvDeleteText.setText(getString(R.string.app_drag_delete));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        tvDeleteText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.picture_icon_delete, 0, 0);
                    }
                }
            }

            @Override
            public void dragState(boolean isStart) {
                int visibility = tvDeleteText.getVisibility();
                if (isStart) {
                    if (visibility == View.GONE) {
                        tvDeleteText.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
                        tvDeleteText.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (visibility == View.VISIBLE) {
                        tvDeleteText.animate().alpha(0).setDuration(300).setInterpolator(new AccelerateInterpolator());
                        tvDeleteText.setVisibility(View.GONE);
                    }
                }
            }
        };
        // 图片拖动
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    viewHolder.itemView.setAlpha(0.7f);
                }
                return makeMovementFlags(ItemTouchHelper.DOWN | ItemTouchHelper.UP
                        | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //得到item原来的position
                try {
                    int fromPosition = viewHolder.getAdapterPosition();
                    //得到目标position
                    int toPosition = target.getAdapterPosition();
                    int itemViewType = target.getItemViewType();
                    if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                        if (fromPosition < toPosition) {
                            for (int i = fromPosition; i < toPosition; i++) {
                                Collections.swap(mAdapter.getData(), i, i + 1);
                            }
                        } else {
                            for (int i = fromPosition; i > toPosition; i--) {
                                Collections.swap(mAdapter.getData(), i, i - 1);
                            }
                        }
                        mAdapter.notifyItemMoved(fromPosition, toPosition);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            // 自定义动画
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    if (null == mDragListener) {
                        return;
                    }
                    if (needScaleBig) {
                        //如果需要执行放大动画
                        viewHolder.itemView.animate().scaleXBy(0.1f).scaleYBy(0.1f).setDuration(100);
                        //执行完成放大动画,标记改掉
                        needScaleBig = false;
                        //默认不需要执行缩小动画，当执行完成放大 并且松手后才允许执行
                        needScaleSmall = false;
                    }
                    int sh = recyclerView.getHeight() + tvDeleteText.getHeight();
                    int ry = tvDeleteText.getTop() - sh;
                    if (dY >= ry) {
                        //拖到删除处
                        mDragListener.deleteState(true);
                        if (isUpward) {
                            //在删除处放手，则删除item
                            viewHolder.itemView.setVisibility(View.INVISIBLE);
                            mAdapter.delete(viewHolder.getAdapterPosition());
                            resetState();
                            return;
                        }
                    } else {//没有到删除处
                        if (View.INVISIBLE == viewHolder.itemView.getVisibility()) {
                            //如果viewHolder不可见，则表示用户放手，重置删除区域状态
                            mDragListener.dragState(false);
                        }
                        if (needScaleSmall) {//需要松手后才能执行
                            viewHolder.itemView.animate().scaleXBy(1f).scaleYBy(1f).setDuration(100);
                        }
                        mDragListener.deleteState(false);
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                int itemViewType = viewHolder != null ? viewHolder.getItemViewType() : GridImageAdapter.TYPE_CAMERA;
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    if (ItemTouchHelper.ACTION_STATE_DRAG == actionState && mDragListener != null) {
                        mDragListener.dragState(true);
                    }
                    super.onSelectedChanged(viewHolder, actionState);
                }
            }

            @Override
            public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
                needScaleSmall = true;
                isUpward = true;
                return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int itemViewType = viewHolder.getItemViewType();
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    viewHolder.itemView.setAlpha(1.0f);
                    super.clearView(recyclerView, viewHolder);
                    mAdapter.notifyDataSetChanged();
                    resetState();
                }
            }
        });

        // 绑定拖拽事件
        mItemTouchHelper.attachToRecyclerView(recycler);

        // 注册外部预览图片删除按钮回调
        if (getActivity() != null) {
            BroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,
                    BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);
        }
    }

    @Override
    protected void dataObserver() {
//        LiveBus.getDefault().subscribe(Constants.EVENT_KEY_WORK_STATE).observe(this, observer);

        registerSubscriber(AlbumRepository.EVENT_KEY_ALBUMID,AlbumWorkPojo.class).observe(this,albumWorkPojo -> {
            if(albumWorkPojo.code!=200) ToastUtils.showToast(albumWorkPojo.msg);
            else { workid = albumWorkPojo.data.work_id;
                Log.w("workid",workid + "") ;
                upLoadPic();
            }
        });
        registerSubscriber(AlbumRepository.EVENT_KEY_ALBUMPIC, BasePojo.class).observe(this,basePojo -> {
            if(basePojo.code != 200 ) ToastUtils.showToast(basePojo.msg);
            else{
                if(++Turn == localMediaList.size() + 1 ){
                    getResult();
                    Turn = 0;
                }
            }
        });
    }

    public void postAlbum()
    {
        Log.w("getworkid","qwq") ;
        mViewModel.getAlbumId();
    }
    int Turn = 0;

    public void upLoadPic()
    {
        AlbumPostPojo albumPostPojo = new AlbumPostPojo();
        int i = 0 ;
        albumPostPojo.images = new ArrayList<String>(localMediaList.size());
        albumPostPojo.factors = new ArrayList<AlbumPostPojo.Data>(localMediaList.size());
        for(LocalMedia localMedia : localMediaList){
            albumPostPojo.images.add(i,localMedia.getFileName());

            AlbumPostPojo.Data data = new AlbumPostPojo.Data();
            data.types = new ArrayList<>();
            data.values = new ArrayList<>();
            albumPostPojo.factors.add(i,data);
            i ++ ;
        }
        i = 0;int  j = 0 ;
        for (AiBoostManager.Data entry : mDetectorresult){
            albumPostPojo.factors.get(i).types.add(entry.type);
            albumPostPojo.factors.get(i).values.add(entry.value);
            j ++;
            if (j == 3) {
                i++;
                j=0;
            }
        }
        Log.w(TAG, JsonUtils.toJson(albumPostPojo));
        saveJSONDataToFile("info.json",JsonUtils.toJson(albumPostPojo));
        File file =new File(activity.getFilesDir(),"info.json");
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), requestBody).build();
        mViewModel.postPic(workid,multipartBody);
//        HttpHelper.getInstance().create(ApiService.class).upLoadImg(URL.ALBUM_URL+workid+"/upload",multipartBody)
//                .subscribeOn(Schedulers.io())
//
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new RxSubscriber<BasePojo>() {
//                    @Override
//                    public void onSuccess(BasePojo basePojo) {
//                        if (basePojo.code != 200) {
//                            onFailure(basePojo.msg, basePojo.code);
//                            return;
//                        }
//                        Turn ++ ;
//                        if (Turn == localMediaList.size()){
////                            com.code.travellog.util.ToastUtils.showToast("json上传成功");
//                            getResult();
//                        }
//                    }
//                    @Override
//                    public void onFailure(String msg, int code) {
//                        com.code.travellog.util.ToastUtils.showToast(msg);
//                    }
//                });
        for(LocalMedia localMedia : localMediaList){
            file = new File(localMedia.getAndroidQToPath());
            requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", localMedia.getFileName(), requestBody).build();
            mViewModel.postPic(workid,multipartBody);
//        HttpHelper.getInstance().create(ApiService.class).upLoadImg(URL.ALBUM_URL+workid+"/upload",multipartBody)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new RxSubscriber<BasePojo>() {
//                    @Override
//                    public void onSuccess(BasePojo basePojo) {
//                        if (basePojo.code != 200) {
//                            onFailure(basePojo.msg, basePojo.code);
//                            return;
//                        }
//                        Turn ++ ;
//                        if (Turn == localMediaList.size()){
//                            com.code.travellog.util.ToastUtils.showToast("图片上传成功");
//                            getResult();
//                        }
//                    }
//                    @Override
//                    public void onFailure(String msg, int code) {
//                        com.code.travellog.util.ToastUtils.showToast(msg);
//                    }
//                });
        }
    }
    private void getResult()
    {
//        Bundle bundle = new Bundle();
//        bundle.putInt("workid",workid);
//        this.setArguments(bundle);
        ((MakeAlbumActivity)getActivity()).setWorkid(workid);
        ((MakeAlbumActivity)getActivity()).initFragment(1);

    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (BroadcastAction.ACTION_DELETE_PREVIEW_POSITION.equals(action)) {// 外部预览删除按钮回调
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                    ToastUtils.showToast("delete image index:" + position);
                    mAdapter.remove(position);
                    mAdapter.notifyItemRemoved(position);
                }
            }
        }
    };
    /**
     * 添加图片的点击事件 弹出选择拍照或选择照片
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 进入相册
            PictureSelector.create(AlbumMakeFragment.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
//                    .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                    .isWeChatStyle(false)// 是否开启微信图片选择风格
                    .isUseCustomCamera(false)// 是否使用自定义相机
                    .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                    .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                    .setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
                    .isWithVideoImage(false)// 图片和视频是否可以同选
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .isEnableCrop(isCrop)// 是否裁剪
                    .isMaxSelectEnabledMask(true)
                    .hideBottomControls(false)
                    //.minSelectNum(1)// 最小选择数量
                    //.minVideoSelectNum(1)// 视频最小选择数量，如果没有单独设置的需求则可以不设置，同用minSelectNum字段
                    .maxVideoSelectNum(1) // 视频最大选择数量，如果没有单独设置的需求则可以不设置，同用maxSelectNum字段
                    .imageSpanCount(4)// 每行显示个数
                    .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                    //.isAndroidQTransform(false)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对.isCompress(false); && .isEnableCrop(false);有效,默认处理
                    .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                    .isOriginalImageControl(isRiginal)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                    //.cameraFileName("test.png")    // 重命名拍照文件名、注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                    //.renameCompressFile("test.png")// 重命名压缩文件名、 注意这个不要重复，只适用于单张图压缩使用
                    //.renameCropFileName("test.png")// 重命名裁剪文件名、 注意这个不要重复，只适用于单张图裁剪使用
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
//                        .isSingleDirectReturn(cb_single_back.isChecked())// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                    .isPreviewImage(true)// 是否可预览图片
//                        .isPreviewVideo(cb_preview_video.isChecked())// 是否可预览视频
                    //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                    .isEnablePreviewAudio(isVoice) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
                    //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    .isCompress(true)// 是否压缩
                    .compressQuality(80)// 图片压缩后输出质量 0~ 100
                    .synOrAsy(true)//同步false或异步true 压缩 默认同步
                    //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                    //.compressSavePath(getPath())//压缩图片保存地址
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                    //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
                    .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(isGif)// 是否显示gif图片
                    .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    //.setCircleDimmedColor(ContextCompat.getColor(this, R.color.app_color_white))// 设置圆形裁剪背景色值
                    //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                    //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                    .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .isOpenClickSound(isVoice)// 是否开启点击声音
                    .selectionData(mAdapter.getData())// 是否传入已选图片
                    //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                    //.videoMaxSecond(15)
                    //.videoMinSecond(10)
                    .isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
//                    .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    .rotateEnabled(true) // 裁剪是否可旋转图片
                    .scaleEnabled(true)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                        @Override
                        public void onResult(List<LocalMedia> result) {
                            for (LocalMedia media : result) {
                                Log.i(TAG, "是否压缩:" + media.isCompressed());
                                Log.i(TAG, "压缩:" + media.getCompressPath());
                                Log.i(TAG, "原图:" + media.getPath());
                                Log.i(TAG, "绝对路径:" + media.getRealPath());
                                Log.i(TAG, "是否裁剪:" + media.isCut());
                                Log.i(TAG, "裁剪:" + media.getCutPath());
                                Log.i(TAG, "是否开启原图:" + media.isOriginal());
                                Log.i(TAG, "原图路径:" + media.getOriginalPath());
                                Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());

                                Log.i(TAG, "宽高: " + media.getWidth() + "x" + media.getHeight());
                                Log.i(TAG, "Size: " + media.getSize());
                                // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
                            }
                            if (mAdapter != null) {
                                mAdapter.setList(result);
                                mAdapter.notifyDataSetChanged();
                            }
                            localMediaList = result ;
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
        }
    };
    public void getImageObjectDetector(List<LocalMedia> result)
    {
        aiBoostManager.setTotal(result.size());
        Log.w(TAG,result.size()+"");
        aiBoostManager.setResultEmpty();
        for (LocalMedia media : result){
            try {
                if (null != media.getPath()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(media.getRealPath());
                    Log.w(TAG, bitmap.toString());
                    aiBoostManager.run(bitmap);
                }
            }catch (Exception e){e.printStackTrace();}
        }

    }
    private void getWhiteStyle() {
        // 相册主题
        mPictureParameterStyle = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = true;
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = true;
        // 相册状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#FFFFFF");
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#FFFFFF");
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_orange_arrow_up;
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_orange_arrow_down;
        // 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        // 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back_arrow;
        // 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(getContext(), R.color.app_color_black);
        // 相册右侧取消按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(getContext(), R.color.app_color_black);
        // 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_num_selector;
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(getContext(), R.color.picture_color_fa);
        // 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_fa632d);
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_9b);
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_fa632d);
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_9b);
        // 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_checkbox;
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(getContext(), R.color.app_color_53575e);
        // 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_black_delete;
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
//        // 自定义相册右侧文本内容设置
//        mPictureParameterStyle.pictureRightDefaultText = "";
//        // 自定义相册未完成文本内容
//        mPictureParameterStyle.pictureUnCompleteText = "";
//        // 自定义相册完成文本内容
        mPictureParameterStyle.pictureCompleteText = "确定";
//        // 自定义相册列表不可预览文字
//        mPictureParameterStyle.pictureUnPreviewText = "";
//        // 自定义相册列表预览文字
//        mPictureParameterStyle.picturePreviewText = "";

//        // 自定义相册标题字体大小
//        mPictureParameterStyle.pictureTitleTextSize = 18;
//        // 自定义相册右侧文字大小
//        mPictureParameterStyle.pictureRightTextSize = 14;
//        // 自定义相册预览文字大小
//        mPictureParameterStyle.picturePreviewTextSize = 14;
//        // 自定义相册完成文字大小
//        mPictureParameterStyle.pictureCompleteTextSize = 14;
//        // 自定义原图文字大小
//        mPictureParameterStyle.pictureOriginalTextSize = 14;

        // 裁剪主题
        mCropParameterStyle = new PictureCropParameterStyle(
                ContextCompat.getColor(getContext(), R.color.app_color_white),
                ContextCompat.getColor(getContext(), R.color.app_color_white),
                ContextCompat.getColor(getContext(), R.color.app_color_black),
                mPictureParameterStyle.isChangeStatusBarFontColor);
    }

    /**
     * 返回结果回调
     */
    private static class MyResultCallback implements OnResultCallbackListener<LocalMedia> {
        private WeakReference<GridImageAdapter> mAdapterWeakReference;

        public MyResultCallback(GridImageAdapter adapter) {
            super();
            this.mAdapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        public void onResult(List<LocalMedia> result) {
            for (LocalMedia media : result) {
                Log.i(TAG, "是否压缩:" + media.isCompressed());
                Log.i(TAG, "压缩:" + media.getCompressPath());
                Log.i(TAG, "原图:" + media.getPath());
                Log.i(TAG, "绝对路径:" + media.getRealPath());
                Log.i(TAG, "是否裁剪:" + media.isCut());
                Log.i(TAG, "裁剪:" + media.getCutPath());
                Log.i(TAG, "是否开启原图:" + media.isOriginal());
                Log.i(TAG, "原图路径:" + media.getOriginalPath());
                Log.i(TAG, "Android Q 特有Path:" + media.getAndroidQToPath());

                Log.i(TAG, "宽高: " + media.getWidth() + "x" + media.getHeight());
                Log.i(TAG, "Size: " + media.getSize());
                // TODO 可以通过PictureSelectorExternalUtils.getExifInterface();方法获取一些额外的资源信息，如旋转角度、经纬度等信息
            }
            if (mAdapterWeakReference.get() != null) {
                mAdapterWeakReference.get().setList(result);
                mAdapterWeakReference.get().notifyDataSetChanged();
            }
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "PictureSelector Cancel");
        }
    }

    /**
     * 重置
     */
    private void resetState() {
        if (mDragListener != null) {
            mDragListener.deleteState(false);
            mDragListener.dragState(false);
        }
        isUpward = false;
    }

    /**
     * 清空缓存包括裁剪、压缩、AndroidQToPath所生成的文件，注意调用时机必须是处理完本身的业务逻辑后调用；非强制性
     */
    private void clearCache() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        if (getContext() != null) {
            if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage());
                PictureFileUtils.deleteAllCacheDirFile(getContext());
            } else {
                PermissionChecker.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
            }
        }
    }
    /**
     * 保存JSON数据到文件
     */
    private void saveJSONDataToFile(String fileName, String jsonData) {
        try {
            FileOutputStream fos = activity.openFileOutput(fileName,  Context.MODE_PRIVATE);
            fos.write(jsonData.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onStateRefresh() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}