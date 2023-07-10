package com.example.osmdroiddemo;
import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.osmdroiddemo.databinding.ActivityMainBinding;
import com.example.osmdroiddemo.other.TileSelectDrawerPopupView;
import com.example.osmdroiddemo.utils.BaseActivity;
import com.example.osmdroiddemo.utils.CustomPaintingSurface;
import com.example.osmdroiddemo.utils.TileSource;
import com.example.osmdroiddemo.utils.Screen2GeoPoint;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private ActivityMainBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        bindEvent();


        IMapController mapController = binding.mapview.getController();
        mapController.setZoom(18.0);

    }

    private void initView() {

        // 缩放按钮
        binding.mapview.setMultiTouchControls(true);
        // 最大缩放比例
        binding.mapview.setMaxZoomLevel(20.0);
        // 获取权限
        getPermission();
        // 设置地图源
        setMapSources(TileSource.tianDiTuBoundary);

        // 添加"我的位置"
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), binding.mapview);
        this.mLocationOverlay.enableMyLocation();
        binding.mapview.getOverlays().add(this.mLocationOverlay);
        mLocationOverlay.enableFollowLocation();//图标跟着位置移动


        // 添加指南针
        this.mCompassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this), binding.mapview);
        this.mCompassOverlay.enableCompass();
        binding.mapview.getOverlays().add(this.mCompassOverlay);
        binding.customSurface.init(binding.mapview);
    }


    private void bindEvent(){
        // 绘制图形
        binding.draw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                binding.customSurface.setVisibility(View.VISIBLE);
                binding.draw.setBackgroundColor(Color.BLACK);
                final CustomPaintingSurface.Mode[] mode = {CustomPaintingSurface.Mode.Polygon};
                new XPopup.Builder(MainActivity.this).asCenterList("选择类型", new String[]{"线", "面","PolygonHole","PolylineAsPath"}, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String text) {
                        switch (position){
                            case 0:
                                mode[0] = CustomPaintingSurface.Mode.Polyline;
                                break;
                            case 1:
                                mode[0] = CustomPaintingSurface.Mode.Polygon;
                                break;
                            case 2:
                                mode[0] = CustomPaintingSurface.Mode.PolygonHole;
                                break;
                            case 3:
                                mode[0] = CustomPaintingSurface.Mode.PolylineAsPath;
                                break;
                        }
                        binding.customSurface.setMode(CustomPaintingSurface.Mode.Polygon);
                    }
                }).show();

            }else{
                binding.draw.setBackgroundColor(Color.WHITE);
                binding.customSurface.setVisibility(View.GONE);
            }
        });

        // 定位
        binding.location.setOnClickListener(v-> location());
        /// 缩放
        binding.zoomin.setOnClickListener(v -> binding.mapview.getController().zoomIn());
        binding.zoomout.setOnClickListener(v -> binding.mapview.getController().zoomOut());

        //切换图源
        binding.btn.setOnClickListener(v->{
            TileSelectDrawerPopupView customDrawerPopupView = new TileSelectDrawerPopupView(MainActivity.this,binding.mapview);
            new XPopup.Builder(MainActivity.this).popupPosition(PopupPosition.Right)
                    .hasStatusBar(true)
                    .asCustom(customDrawerPopupView)
                    .show();
        });

        binding.freeDraw.setOnClickListener(v -> {
            MyLocationOverlayWithClick overlay = new MyLocationOverlayWithClick(binding.mapview);
            binding.mapview.getOverlayManager().add(overlay);
        });
    }
    private void location(){
        //定位
        binding.mapview.getController().setCenter(mLocationOverlay.getMyLocation());
        binding.mapview.getController().setZoom(10);

    }
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
    /**
     * 设置地图源
     */
    private void setMapSources(OnlineTileSourceBase o) {
        if (NetworkUtils.isConnected()) {
            binding.mapview.setTileSource(o);
            binding.mapview.invalidate();
        } else {
            ToastUtils.showShort("当前无网络，请选择离线地图包");
            if (binding.mapview.getOverlays().size() <= 0) {
//                mapViewOffline();
                //加载离线地图
            }
        }
    }
    private void getPermission(){
        onRequestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION},new onPermissionCallbackListener(){
            @Override
            public void onGranted() {
                Log.e("MainActivity", "onGranted: ");
            }
            @Override
            public void onDenied(List<String> deniedPermissions) {

            }
        });
    }
}



//          定点绘制
//       binding.draw.setOnClickListener(v -> {
////            List<IGeoPoint> points = new ArrayList<>();
////            points.add(new LabelledGeoPoint(mLocationOverlay.getMyLocation().getLatitude(),mLocationOverlay.getMyLocation().getAltitude(), "Point #" + 1));
////            points.add(new LabelledGeoPoint(23.12647183, 113.365558756 , "Point #" + 2));
////            // wrap them in a theme
////            SimplePointTheme pt = new SimplePointTheme(points, true);
////
////            // create label style
////            Paint textStyle = new Paint();
////            textStyle.setStyle(Paint.Style.FILL);
////            textStyle.setColor(Color.parseColor("#0000ff"));
////            textStyle.setTextAlign(Paint.Align.CENTER);
////            textStyle.setTextSize(24);
////
////            // set some visual options for the overlay
////            // we use here MAXIMUM_OPTIMIZATION algorithm, which works well with >100k points
////            SimpleFastPointOverlayOptions opt = SimpleFastPointOverlayOptions.getDefaultStyle()
////                    .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
////                    .setRadius(30).setIsClickable(true).setCellSize(15).setTextStyle(textStyle);
////
////            // create the overlay with the theme
////            final SimpleFastPointOverlay sfpo = new SimpleFastPointOverlay(pt, opt);
////
////            // onClick callback
////            sfpo.setOnClickListener(new SimpleFastPointOverlay.OnClickListener() {
////                @Override
////                public void onClick(SimpleFastPointOverlay.PointAdapter points, Integer point) {
////                    Toast.makeText(binding.mapview.getContext()
////                            , "You clicked " + ((LabelledGeoPoint) points.get(point)).getLabel()
////                            , Toast.LENGTH_SHORT).show();
////                }
////            });
////
////            // add overlay
////            binding.mapview.getOverlays().add(sfpo);
//               });