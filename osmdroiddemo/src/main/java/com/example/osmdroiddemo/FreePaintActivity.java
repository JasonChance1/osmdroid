package com.example.osmdroiddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.example.osmdroiddemo.customview.CirclePlottingOverlay;
import com.example.osmdroiddemo.databinding.ActivityFreePaintBinding;
import com.example.osmdroiddemo.utils.CustomPaintingSurface;
import com.example.osmdroiddemo.utils.TileSource;
import com.example.osmdroiddemo.utils.Screen2GeoPoint;
import com.example.osmdroiddemo.utils.permissionutils.EvPermissionUtils;
import com.example.osmdroiddemo.utils.permissionutils.OnPermissionCallbackListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;

import java.util.ArrayList;
import java.util.List;

public class FreePaintActivity extends AppCompatActivity{
    private MyLocationNewOverlay mLocationOverlay;
    private MapView mMapView;
    private ActivityFreePaintBinding binding;

    private CustomPaintingSurface.Mode mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFreePaintBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();

        IMapController mapController = binding.mapview.getController();
        mapController.setZoom(18.0);
        bindEvent();


    }
    private void location(){
        //定位
        binding.mapview.getController().setCenter(mLocationOverlay.getMyLocation());
        binding.mapview.getController().setZoom(10);

    }
    private void initView() {
        // 设置地图源
//        setMapSources(TileSource.tianDiTuSatellite);

        binding.customSurface.init(binding.mapview);
        // 缩放按钮
        binding.mapview.setMultiTouchControls(true);
        // 最大缩放比例
        binding.mapview.setMaxZoomLevel(20.0);

        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        EvPermissionUtils.requestPermissions(FreePaintActivity.this, new OnPermissionCallbackListener() {
            @Override
            public void onGranted() {
                binding.mapview.setTileSource(TileSource.tianDiTuSatellite);

                // 添加"我的位置"
                mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(FreePaintActivity.this), binding.mapview);
                mLocationOverlay.enableMyLocation();
                binding.mapview.getOverlays().add(mLocationOverlay);
                mLocationOverlay.enableFollowLocation();//图标跟着位置移动
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                ToastUtils.showLong("缺少权限");
            }

            @Override
            public void onAskAgain(List<String> asPermissions) {

            }
        },permissions);

        binding.location.setBackgroundColor(Color.BLACK);
    }

    public void bindEvent(){
        binding.location.setOnClickListener(view -> {
//                location();
            binding.customSurface.setVisibility(View.GONE);
            binding.location.setBackgroundColor(Color.BLACK);
            binding.paint.setBackgroundColor(Color.TRANSPARENT);
        });

        binding.paint.setOnClickListener(view -> {
            binding.customSurface.setVisibility(View.VISIBLE);
            binding.location.setBackgroundColor(Color.TRANSPARENT);
            binding.paint.setBackgroundColor(Color.BLACK);
            new XPopup.Builder(FreePaintActivity.this).asCenterList("选择绘制类型", new String[]{"线", "面", "PolygonHole","圆"}, new OnSelectListener() {
                @Override
                public void onSelect(int position, String text) {
                    if(position==0)
                        binding.customSurface.setMode(CustomPaintingSurface.Mode.Polyline);
                    if (position==1) binding.customSurface.setMode(CustomPaintingSurface.Mode.Polygon);
                    if (position==2) binding.customSurface.setMode(CustomPaintingSurface.Mode.PolygonHole);
                    if (position==3) {
//                        binding.customSurface.setOnTouchListener(new View.OnTouchListener() {
//                            @Override
//                            public boolean onTouch(View view, MotionEvent motionEvent) {
//                                Screen2GeoPoint.transform(binding.mapview,motionEvent.getX(),motionEvent.getY());
//                                return false;
//                            }
//                        });
                        binding.mapview.getOverlays().add( new CirclePlottingOverlay(100));
                    }
                }
            }).show();


        });
    }
    // 绘制点
    public static class MyLocationOverlayWithClick extends MyLocationNewOverlay{

        public MyLocationOverlayWithClick(MapView mapView) {
            super(mapView);
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e, MapView map) {

            // 通过点击的x,y转换为经纬度坐标，并存放到IGeoPoint中
            IGeoPoint iGeoPoint = Screen2GeoPoint.transform(map,e.getX(),e.getY());

            if(iGeoPoint!=null){
                List<IGeoPoint> points = new ArrayList<>();
                SimplePointTheme pt = new SimplePointTheme(points, true);
                LabelledGeoPoint geoPoint = new LabelledGeoPoint(iGeoPoint.getLatitude(),iGeoPoint.getLongitude(),"点1");
                points.add(geoPoint);
                OverlayItem overlayItem = new OverlayItem("点1","",iGeoPoint);
                SimpleFastPointOverlayOptions opt = SimpleFastPointOverlayOptions.getDefaultStyle()
                        .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION).setSymbol(SimpleFastPointOverlayOptions.Shape.SQUARE)
                        .setRadius(30).setIsClickable(true).setCellSize(15);

                // create the overlay with the theme
                final SimpleFastPointOverlay sfpo = new SimpleFastPointOverlay(pt,opt);

                // add overlay
                map.getOverlays().add(sfpo);
            }
            return true;

        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


}