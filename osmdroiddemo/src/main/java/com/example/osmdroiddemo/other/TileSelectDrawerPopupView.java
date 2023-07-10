package com.example.osmdroiddemo.other;

import android.content.Context;
import android.graphics.Color;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.example.osmdroiddemo.R;
import com.example.osmdroiddemo.utils.TileSource;
import com.lxj.xpopup.core.DrawerPopupView;

import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

// 自定义抽屉弹窗，用于选择地图图源
public class TileSelectDrawerPopupView extends DrawerPopupView {
    MapView mapView;
    Context context;

    CheckBox gaode,skyTile,skyTileLabel,skyVector,skyVectorLabel,skyTerrain,skyTerrainLabel,skyBoundary,wmtsTest;
    public TileSelectDrawerPopupView(@NonNull Context context, MapView mapView) {
        super(context);
        this.context = context;
        this.mapView = mapView;
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_drawer_popup;
    }
    @Override
    protected void onCreate() {
        super.onCreate();
        gaode = findViewById(R.id.gaode);
        skyTile = findViewById(R.id.skyTile);
        skyTileLabel = findViewById(R.id.skyTileLabel);
        skyVector = findViewById(R.id.skyVector);
        skyVectorLabel = findViewById(R.id.skyVectorLabel);
        skyTerrain = findViewById(R.id.skyTerrain);
        skyTerrainLabel = findViewById(R.id.skyTerrainLabel);
        skyBoundary = findViewById(R.id.skyBoundary);
        wmtsTest = findViewById(R.id.wmtsTest);

        gaode.setOnCheckedChangeListener((compoundButton, b) -> loadLayer());
        skyTile.setOnCheckedChangeListener((compoundButton, b) -> loadLayer());
        skyTileLabel.setOnCheckedChangeListener((compoundButton, b) -> loadLayer());
        skyVector.setOnCheckedChangeListener((compoundButton, b) -> loadLayer());
        skyVectorLabel.setOnCheckedChangeListener((compoundButton, b) -> loadLayer());
        skyTerrain.setOnCheckedChangeListener((compoundButton, b) -> loadLayer());
        skyTerrainLabel.setOnCheckedChangeListener((compoundButton, b) -> loadLayer());
        wmtsTest.setOnCheckedChangeListener((compoundButton, b) -> loadLayer());
    }

    private MapTileProviderBasic provider = null;
    private TilesOverlay onLineTile2Layer(OnlineTileSourceBase o){
        provider = new MapTileProviderBasic(context,o);
        TilesOverlay layer = new TilesOverlay(provider, context);
        layer.setLoadingBackgroundColor(Color.TRANSPARENT);
        layer.setLoadingLineColor(Color.TRANSPARENT);
        return  layer;
    }

    private void addLayer(TilesOverlay layer){
        mapView.getOverlays().add(layer);
    }
    private void loadLayer(){
        mapView.getOverlays().clear();
        if(gaode.isChecked()) addLayer(onLineTile2Layer(TileSource.AutoNaviVector));
        if(skyTile.isChecked()) addLayer(onLineTile2Layer(TileSource.tianDiTuSatellite));
        if (skyTileLabel.isChecked()) addLayer(onLineTile2Layer(TileSource.tianDiTuSatelliteLabel));
        if (skyVector.isChecked()) addLayer(onLineTile2Layer(TileSource.tianDiTuVector));
        if (skyVectorLabel.isChecked()) addLayer(onLineTile2Layer(TileSource.tianDiTuVectorLabel));
        if (skyTerrain.isChecked()) addLayer(onLineTile2Layer(TileSource.tianDiTuTerrain));
        if (skyTerrainLabel.isChecked()) addLayer(onLineTile2Layer(TileSource.tianDiTuTerrainLabel));
        if (skyBoundary.isChecked()) addLayer(onLineTile2Layer(TileSource.tianDiTuBoundary));
        if (wmtsTest.isChecked()) addLayer(onLineTile2Layer(TileSource.wmtsTest));
//        if (googleTerrain.isChecked()) addLayer(onLineTile2Layer(GoogleTileSource.GoogleTerrain));
        mapView.invalidate();
    }

}