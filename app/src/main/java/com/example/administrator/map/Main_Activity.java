package com.example.administrator.map;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;

public class Main_Activity extends AppCompatActivity implements LocationSource,AMapLocationListener
{
    private MapView mMapView;
    //显示地图的视图
    private AMap aMap;
    //定义AMap 地图对象的操作方法与接口。
    private OnLocationChangedListener mListener;
    //位置发生变化时的监听
    private AMapLocationClient mapLocationClient;
    //定位服务类。此类提供单次定位、持续定位、地理围栏、最后位置相关功能。
    private AMapLocationClientOption mapLocationClientOption;
    //定位参数设置，通过这个类可以对定位的相关参数进行设置
    //在AMapLocationClient进行定位时需要这些参数
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mMapView=(MapView)findViewById(R.id.mapview);
        //获取实例
        mMapView.onCreate(savedInstanceState);
        //必须调用
        init();
    }
    //实例化Amap对象
    public void init()
    {
        if(aMap==null)
        {
            aMap=mMapView.getMap();
            setConfigrationAmap();
        }
    }
    //配置Amap对象
    public void setConfigrationAmap()
    {
        aMap.setLocationSource(Main_Activity.this);
        //设置定位监听
        aMap.setMyLocationEnabled(true);
        //设置显示定位层，并可以触发定位
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        //设置显示定位按钮
    }
    //重写
    @Override
    public void onSaveInstanceState(Bundle outState,PersistableBundle outPersistentState)
    {
        super.onSaveInstanceState(outState,outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }
    //重写
    @Override
    protected void onResume()
    {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mMapView.onPause();
        deactivate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if(mapLocationClient!=null)
        {
            mapLocationClient.onDestroy();
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener)
    {
        System.out.println("已经激活定位-------------activate");
        mListener=onLocationChangedListener;
        if(mapLocationClient==null)
        {
            mapLocationClient=new AMapLocationClient(Main_Activity.this);
            mapLocationClientOption=new AMapLocationClientOption();
            mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位模式为高精度
            mapLocationClient.setLocationOption(mapLocationClientOption);
            //设置配置
            mapLocationClient.setLocationListener(this);
            //设置位置变化监听
            mapLocationClient.startLocation();
        }
    }
    //激活定位
    @Override
    public void deactivate()
    {
        mListener=null;
        if(mapLocationClient!=null)
        {
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient=null;
        System.out.println("已经关闭定位-------------deactivate");
    }
    //关闭定位

    @Override
    public void onLocationChanged(AMapLocation aMapLocation)
    {
        if(mListener!=null&&aMapLocation!=null)
        {
            if(aMapLocation!=null&&aMapLocation.getErrorCode()==0)
            {
                Toast.makeText(Main_Activity.this,"现在位于"+aMapLocation.getAddress(),Toast.LENGTH_SHORT).show();
                mListener.onLocationChanged(aMapLocation);
                System.out.println(aMapLocation.getLatitude()+"----"+aMapLocation.getLongitude()+"---------"+aMapLocation.getErrorCode());
            }
        }else
        {
            Toast.makeText(Main_Activity.this,"定位失败:"+aMapLocation.getErrorCode(),Toast.LENGTH_SHORT).show();
        }
    }

}

