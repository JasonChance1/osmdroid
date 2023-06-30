package com.example.osmdroiddemo;
import android.Manifest;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.osmdroiddemo.databinding.ActivityMainBinding;
import com.example.osmdroiddemo.utils.CustomPaintingSurface;
import com.example.osmdroiddemo.utils.GoogleTileSource;
import com.example.osmdroiddemo.utils.TransformToGeoPoint;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.mapsforge.map.layer.TileLayer;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private Boolean isHide = true;
    private ActivityMainBinding binding;
    private MapTileProviderBasic provider = null;
    private TilesOverlay layer = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        initView();

        bindEvent();

//        Context ctx = this.getApplicationContext();
//        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
//        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        IMapController mapController = binding.mapview.getController();
        mapController.setZoom(18.0);

    }

    private void initView() {

        // 添加"我的位置"
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), binding.mapview);
        this.mLocationOverlay.enableMyLocation();
        binding.mapview.getOverlays().add(this.mLocationOverlay);
        mLocationOverlay.enableFollowLocation();//图标跟着位置移动


        // 添加指南针
        this.mCompassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this), binding.mapview);
        this.mCompassOverlay.enableCompass();
        binding.mapview.getOverlays().add(this.mCompassOverlay);


        // 缩放按钮
        binding.mapview.setMultiTouchControls(true);
        // 最大缩放比例
        binding.mapview.setMaxZoomLevel(20.0);
        getPermission();
        setMapSources(GoogleTileSource.AutoNaviVector);

        binding.customSurface.init(binding.mapview);
    }


    private void bindEvent(){
        // 绘制图形
        binding.draw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
            }
        });


        // 定位
        binding.location.setOnClickListener(v-> location());
        /// 缩放
        binding.zoomin.setOnClickListener(v -> binding.mapview.getController().zoomIn());
        binding.zoomout.setOnClickListener(v -> binding.mapview.getController().zoomOut());

        //切换图源
        binding.btn.setOnClickListener(v->{
            isHide = !isHide;
            if(!isHide){
                binding.optionsBox.setVisibility(View.VISIBLE);
                binding.gaode.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                    if(isChecked) addLayer(GoogleTileSource.AutoNaviVector);
                    else {
                        binding.mapview.getOverlays().remove(layer);
                        ToastUtils.showLong("移除高德地图");
                    }
                    binding.mapview.invalidate();

                }));

                binding.skyTile.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(isChecked){
                        addLayer(GoogleTileSource.tianDiTuSatellite);
                    }else binding.mapview.getOverlays().remove(layer);
                    binding.mapview.invalidate();
                    ToastUtils.showLong(String.valueOf(isChecked));
                });

                binding.skyTileLabel.setOnCheckedChangeListener(((buttonView, isChecked) -> {

                    if(isChecked) addLayer(GoogleTileSource.tianDiTuSatelliteLabel);
                    else binding.mapview.getOverlays().remove(layer);
                    binding.mapview.invalidate();
                }));
                binding.skyVector.setOnCheckedChangeListener(((buttonView, isChecked) -> {

                    if(isChecked) addLayer(GoogleTileSource.tianDiTuVector);
                    else binding.mapview.getOverlays().remove(layer);
                    binding.mapview.invalidate();
                }));
                binding.skyVectorLabel.setOnCheckedChangeListener(((buttonView, isChecked) -> {

                    if(isChecked) addLayer(GoogleTileSource.tianDiTuVectorLabel);
                    else binding.mapview.getOverlays().remove(layer);
                    binding.mapview.invalidate();
                }));
                binding.skyTerrain.setOnCheckedChangeListener(((buttonView, isChecked) -> {

                    if(isChecked) addLayer(GoogleTileSource.tianDiTuTerrain);
                    else binding.mapview.getOverlays().remove(layer);
                    binding.mapview.invalidate();
                }));
                binding.skyTerrainLabel.setOnCheckedChangeListener(((buttonView, isChecked) -> {

                    if(isChecked) addLayer(GoogleTileSource.tianDiTuTerrainLabel);
                    else binding.mapview.getOverlays().remove(layer);
                    binding.mapview.invalidate();
                }));
                binding.skyBoundary.setOnCheckedChangeListener(((buttonView, isChecked) -> {

                    if(isChecked) addLayer(GoogleTileSource.tianDiTuBoundary);
                    else binding.mapview.getOverlays().remove(layer);
                    binding.mapview.invalidate();
                }));
            }else binding.optionsBox.setVisibility(View.GONE);
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
            IGeoPoint iGeoPoint = TransformToGeoPoint.transform(map,e.getX(),e.getY());

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



    private TilesOverlay addLayer(OnlineTileSourceBase o){
        provider = new MapTileProviderBasic(MainActivity.this,o);
        TilesOverlay layer = new TilesOverlay(provider, MainActivity.this);
        layer.setLoadingBackgroundColor(Color.TRANSPARENT);
        layer.setLoadingLineColor(Color.TRANSPARENT);
        binding.mapview.getOverlays().add(layer);
        return  layer;
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

//
//    @Override
//    public void onPause() {
//        super.onPause();
//        map.onPause();
//    }

        private void getPermission () {
            String[] permissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            if (PermissionUtils.isGranted(permissions[0]) && PermissionUtils.isGranted(permissions[1])) {
                //授权后的操作
            } else {
                // 权限许可请求
                PermissionUtils.permission(permissions)
                        .callback(new PermissionUtils.FullCallback() {
                            @Override
                            public void onGranted(List<String> granted) {
                                getPermission();
                            }

                            @Override
                            public void onDenied(List<String> deniedForever, List<String> denied) {
                                // 处理被拒绝权限的逻辑
                            }
                        })
                        .request();
            }
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