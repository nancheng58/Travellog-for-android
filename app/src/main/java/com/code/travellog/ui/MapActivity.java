package com.code.travellog.ui;

import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.code.travellog.R;
import com.code.travellog.util.AssetsUtil;
import com.mvvm.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @description:
 * @date: 2021/3/17
 */
public class MapActivity extends BaseActivity {
    @BindView(R.id.map)
    MapView map;
    AMap aMap;
    @Override
    public int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        loadManager.showSuccess();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        // 定义经纬度坐标
        LatLng centerBJPoint= new LatLng(39.904989,116.405285);
        // 定义了一个配置 AMap 对象的参数类
        AMapOptions mapOptions = new AMapOptions();
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
        if(aMap== null)  aMap = map.getMap();
                //这个类就是设置地图移动的参数，CameraPosition，参数1---要移动到的经纬度，
                //参数2---地图的放缩级别zoom，参数3---地图倾斜度，参数4---地图的旋转角度
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                new CameraPosition(new LatLng(38.67, 116.50), 5, 0, 0));
        //带动画的移动，aMap添加动画监听时，会有动画效果。不添加不会开启动画
        aMap.animateCamera(mCameraUpdate, 5000, new AMap.CancelableCallback() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onCancel() {
            }
        });
        LatLng latLng = new LatLng(39.906901,116.397972);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
        aMap.getUiSettings().setRotateGesturesEnabled(false);//旋转
        aMap.getUiSettings().setTiltGesturesEnabled(false);//倾斜

//        //不带动画的移动
//        aMap.moveCamera(mCameraUpdate);
        //地图样式
        aMap.setCustomMapStyle(
                new com.amap.api.maps.model.CustomMapStyleOptions()
                        .setEnable(true)
                        .setStyleData(AssetsUtil.getAssetsStyle(MapActivity.this,"style.data"))
                        .setStyleExtraData(AssetsUtil.getAssetsStyle(MapActivity.this,"style_extra.data"))
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        map.onDestroy();
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
}
