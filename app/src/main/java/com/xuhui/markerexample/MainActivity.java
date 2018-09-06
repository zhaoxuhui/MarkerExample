package com.xuhui.markerexample;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView baiduMapView;
    private BaiduMap baiduMap;
    private List<LatLng> locs = new ArrayList<>();
    private List<Marker> markers = new ArrayList<>();
    private List<String> extra_info = new ArrayList<>();


    private Marker addMarker(BaiduMap baiduMap, LatLng loc, @DrawableRes int ID) {
        BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(ID);
        MarkerOptions markerOptions = new MarkerOptions().position(loc).icon(bd).zIndex(9);
        Marker marker = (Marker) baiduMap.addOverlay(markerOptions);
        return marker;
    }

    // 遍历寻找最近Marker索引
    private int findIndex(List<LatLng> list, LatLng loc) {
        double curLat = loc.latitude;
        double curLon = loc.longitude;
        double tempLat;
        double tempLon;
        double minDis = 100;
        double deltaDis;
        int minIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            tempLat = list.get(i).latitude;
            tempLon = list.get(i).longitude;
            deltaDis = Math.abs(curLat - tempLat) + Math.abs(curLon - tempLon);
            if (deltaDis < minDis) {
                minDis = deltaDis;
                minIndex = i;
            }
        }
        return minIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);

        setContentView(R.layout.activity_main);
        baiduMapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = baiduMapView.getMap();

        double lat = 32.9484;
        double lon = 117.3942;
        for (int i = 0; i < 10; i++) {
            LatLng temp = new LatLng(lat, lon);
            locs.add(temp);
            lat += 0.05;
            lon += 0.05;
            // 添加附加信息
            extra_info.add("这是第" + String.valueOf(i + 1) + "个Marker\n");
        }

        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                addMarker(baiduMap, locs.get(i), R.drawable.marker_red);
            } else {
                addMarker(baiduMap, locs.get(i), R.drawable.marker_blue);
            }

        }

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 根据Marker位置获得索引信息，由索引信息获得附加信息
                LatLng loc = marker.getPosition();
                int index = findIndex(locs, loc);
                String ext_info = extra_info.get(index);
                Toast.makeText(getApplicationContext(),
                        ext_info
                                + String.valueOf(marker.getPosition().latitude) + "\n"
                                + String.valueOf(marker.getPosition().longitude),
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        baiduMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        baiduMapView.onPause();
    }
}