package com.code.travellog.core.view.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.adapter.adapter.DelegateAdapter;
import com.adapter.adapter.ItemData;
import com.adapter.listener.OnItemClickListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.code.travellog.R;
import com.code.travellog.cluster.ClusterClickListener;
import com.code.travellog.cluster.ClusterItem;
import com.code.travellog.cluster.ClusterOverlay;
import com.code.travellog.cluster.ClusterRender;
import com.code.travellog.cluster.RegionItem;
import com.code.travellog.core.data.pojo.geo.CityListPojo;
import com.code.travellog.core.data.pojo.geo.CityPojo;
import com.code.travellog.core.data.pojo.geo.GeoPojo;
import com.code.travellog.core.data.repository.PictureRepository;
import com.code.travellog.core.view.picture.PictureShowActivity;
import com.code.travellog.core.vm.PictureViewModel;
import com.code.travellog.core.view.AdapterPool;
import com.code.travellog.util.ScreenUtil;
import com.mvvm.base.AbsLifecycleActivity;
import com.yinglan.scrolllayout.ScrollLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @date: 2021/3/17
 */
public class MapActivity extends AbsLifecycleActivity<PictureViewModel> implements ClusterRender,
        AMap.OnMapLoadedListener, ClusterClickListener, GeocodeSearch.OnGeocodeSearchListener,OnItemClickListener {
    @BindView(R.id.map)
    MapView map;
    AMap aMap;

    private GeoPojo geoPojo ;
    private int clusterRadius = 100;

    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<Integer, Drawable>();

    private ClusterOverlay mClusterOverlay;

    private List<CityPojo> cityListPojo = null;
    private GeocodeSearch geocoderSearch;
    @Override
    public int getLayoutId() {
        return R.layout.activity_map;
    }

    protected DelegateAdapter adapter;

    protected ItemData mItems;

    @Override
    public void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        showLoading();
        mScrollLayout = (ScrollLayout) findViewById(R.id.scroll_down_layout);
        text_foot = (TextView) findViewById(R.id.text_foot);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_view);
        adapter = createAdapter() ;
        mItems = new ItemData();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        getData();
        dataObserver();
        setBottomBar();
    }
    protected void setUiData(Collection<?> data) {
        mItems.clear();
        mItems.addAll(data);
        setData();
    }
    protected void setData() {
        adapter.setDatas(mItems);
        adapter.notifyDataSetChanged();

    }
    protected DelegateAdapter createAdapter() {
        return AdapterPool.newInstance().getMapAdapter(this)
                .setOnItemClickListener(this)
                .build();
    }
    @Override
    protected void dataObserver() {
        registerSubscriber(PictureRepository.EVENT_KEY_PICEXIF, ArrayList.class).observe(this,arrayList -> {
            mViewModel.getGeoExif(arrayList);

        });
        registerSubscriber(PictureRepository.ENTER_KEY_GEO,GeoPojo.class).observe(this,list -> {
            geoPojo = list ;
            init();
            mViewModel.getCityList(geoPojo);
        });
        registerSubscriber(PictureRepository.ENTER_KEY_CITYLIST, CityListPojo.class).observe(this, cityListPojo -> {
            this.cityListPojo =cityListPojo.cityPojos ;
            setUiData(cityListPojo.cityPojos);

        });
    }

    private ScrollLayout mScrollLayout;
    private TextView text_foot;

    private void getData() {
        //申请读写SD卡的权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else{
            try {
                mViewModel.getGalleryExif(getContentResolver());
            }catch (IOException e) {e.printStackTrace();}
        }
    }
    private void setBottomBar()
    {
        /**设置 setting*/
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.root_layout);
        mScrollLayout.setMinOffset((int) (ScreenUtil.getScreenHeight(this) * 0.1));
        mScrollLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight(this) * 0.5));
        mScrollLayout.setExitOffset(ScreenUtil.dip2px(this, 15));
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(false);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToExit();
        mScrollLayout.getBackground().setAlpha(0);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.scrollToExit();
            }
        });
        text_foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.setToOpen();
            }
        });
    }

    private final ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                mScrollLayout.getBackground().setAlpha(255 - (int) precent);
            }
            if (text_foot.getVisibility() == View.VISIBLE)
                text_foot.setVisibility(View.GONE);
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {
                text_foot.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onChildScroll(int top) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        // 设置了一个可视范围的初始化位置
        // CameraPosition 第一个参数： 目标位置的屏幕中心点经纬度坐标。
        // CameraPosition 第二个参数： 目标可视区域的缩放级别
        // CameraPosition 第三个参数： 目标可视区域的倾斜度，以角度为单位。
        // CameraPosition 第四个参数： 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0度到360度
//        mapOptions.camera(new CameraPosition(centerBJPoint, 10f, 0, 0));
        // 定义一个 MapView 对象，构造方法中传入 mapOptions 参数类
//        map= new MapView(this, mapOptions);
        // 调用 onCreate方法 对 MapView LayoutParams 设置
        map.onCreate(savedInstanceState);


    }
    private void init() {
        if (aMap == null) {
            // 初始化地图
            aMap = map.getMap();
//            aMap.setOnMapLoadedListener(this);
            //这个类就是设置地图移动的参数，CameraPosition，参数1---要移动到的经纬度，
            //参数2---地图的放缩级别zoom，参数3---地图倾斜度，参数4---地图的旋转角度
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(new LatLng(38.67, 116.50), 6, 0, 0));
            //带动画的移动，aMap添加动画监听时，会有动画效果。不添加不会开启动画
            geocoderSearch = new GeocodeSearch(MapActivity.this);
            geocoderSearch.setOnGeocodeSearchListener(this);
            aMap.animateCamera(mCameraUpdate, 5000, new AMap.CancelableCallback() {
                @Override
                public void onFinish() {
                }

                @Override
                public void onCancel() {
                }
            });
            aMap.getUiSettings().setRotateGesturesEnabled(false);//旋转
            aMap.getUiSettings().setTiltGesturesEnabled(false);//倾斜
            onMapLoaded();
            //地图样式
//            aMap.setCustomMapStyle(
//                    new com.amap.api.maps.model.CustomMapStyleOptions()
//                            .setEnable(true)
//                            .setStyleData(AssetsUtil.getAssetsStyle(MapActivity.this,"style.data"))
//                            .setStyleExtraData(AssetsUtil.getAssetsStyle(MapActivity.this,"style_extra.data"))
//            );
//            //点击可以动态添加点
//            aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng latLng) {
//                    double lat = Math.random() + 39.474923;
//                    double lon = Math.random() + 116.027116;
//
//                    LatLng latLng1 = new LatLng(lat, lon, false);
//                    RegionItem regionItem = new RegionItem(latLng1,
//                            "test");
//                    mClusterOverlay.addClusterItem(regionItem);
//
//                }
//            });
        }
    }


    @Override
    public void onMapLoaded() {
        //添加数据
        new Thread(() -> {

            List<ClusterItem> items = new ArrayList<ClusterItem>();
            HashMap<Long, GeoPojo.DataBean> dataBeanHashMap = geoPojo.geo;
            for (Long id : dataBeanHashMap.keySet()) {

                GeoPojo.DataBean dataBean = dataBeanHashMap.get(id);
                double lat = dataBean.lan;
                double lon = dataBean.lng;
                LatLng latLng = new LatLng(lat, lon, false);
                Log.e("onMapLoaded",String.valueOf(lat));
                RegionItem regionItem = new RegionItem(latLng,
                        id.toString());
                items.add(regionItem);

            }
            mClusterOverlay = new ClusterOverlay(aMap, items,
                    dp2px(getApplicationContext(), clusterRadius),
                    getApplicationContext());
            mClusterOverlay.setClusterRenderer(MapActivity.this);
            mClusterOverlay.setOnClusterClickListener(MapActivity.this);

            loadManager.showSuccess();

        }).start();
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        if(map != null) map.onDestroy();
        if(mClusterOverlay !=null) mClusterOverlay.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        map.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        map.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        map.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {

        if(clusterItems.size() > 1 ){
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (ClusterItem clusterItem : clusterItems) {
                builder.include(clusterItem.getPosition());
            }

            LatLngBounds latLngBounds = builder.build();
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0));
        }
        else {
            LatLng latLng =clusterItems.get(0).getPosition();

            CityPojo cityPojo1 = null ;
            for(CityPojo cityPojo : cityListPojo) {
                if(cityPojo.lan == latLng.latitude && cityPojo.lng == latLng.longitude){
                    cityPojo1 = cityPojo ;
                    break;
                }
            }
            assert cityPojo1 != null ;
            PictureShowActivity.start(MapActivity.this,cityPojo1);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public Drawable getDrawAble(int clusterNum) {
        int radius = dp2px(getApplicationContext(), 80);
        if (clusterNum == 1) {
            Drawable bitmapDrawable = mBackDrawAbles.get(1);
            if (bitmapDrawable == null) {
                bitmapDrawable =
                        getApplication().getResources().getDrawable(
                                R.drawable.defaultcluster);
                mBackDrawAbles.put(1, bitmapDrawable);
            }

            return bitmapDrawable;
        }
//        } else if (clusterNum < 5) {
//
//            Drawable bitmapDrawable = mBackDrawAbles.get(2);
//            if (bitmapDrawable == null) {
//                bitmapDrawable =
//                        getApplication().getResources().getDrawable(
//                                R.drawable.bbbb);
////                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
////                        Color.argb(159, 210, 154, 6)));
//                mBackDrawAbles.put(2, bitmapDrawable);
//            }
//
//            return bitmapDrawable;
//        } else if (clusterNum < 10) {
//            Drawable bitmapDrawable = mBackDrawAbles.get(3);
//            if (bitmapDrawable == null) {
//                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
//                        Color.argb(199, 217, 114, 0)));
//                mBackDrawAbles.put(3, bitmapDrawable);
//            }
//
//            return bitmapDrawable;}
        else {
            Drawable bitmapDrawable = mBackDrawAbles.get(4);
            if (bitmapDrawable == null) {
                bitmapDrawable =
                        getApplication().getResources().getDrawable(
                                R.drawable.flag_position);
//                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
//                        Color.argb(235, 215, 66, 2)));
                mBackDrawAbles.put(4, bitmapDrawable);
            }

            return bitmapDrawable;
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void onItemClick(View view, int position, Object o) {
        if(o instanceof CityPojo){
//            ToastUtils.showToast("点击了"+((CityPojo) o).city);
            PictureShowActivity.start(MapActivity.this,((CityPojo) o));

        }
    }

}
