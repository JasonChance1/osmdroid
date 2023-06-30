package com.example.osmdroiddemo.utils;


import android.util.Log;

import com.example.osmdroiddemo.BuildConfig;

import org.mapsforge.map.layer.download.tilesource.TileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.MapTileIndex;

/*
  *
  * 上面代码中的mMapView为MapView，GooleTileSource为自定义的一个类，继承自TileSourceFactory，
  * 里面构造了谷歌地图以及高德地图的地图源，所以，mapView的setTileSource方法就是用来切换地图源的。
  * 下面放上GooleTileSource类的代码，可直接进行使用：
 */
public class GoogleTileSource extends TileSourceFactory {
    //谷歌卫星混合
    public static final OnlineTileSourceBase GoogleHybrid = new XYTileSource("Google-Hybrid",
            0, 19, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            Log.d("url", getBaseUrl() + "/vt/lyrs=y&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex));
            return getBaseUrl() + "/vt/lyrs=y&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };
    //谷歌卫星
    public static final OnlineTileSourceBase GoogleSat = new XYTileSource("Google-Sat",
            0, 19, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=s&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };
    //谷歌地图
    public static final OnlineTileSourceBase GoogleRoads = new XYTileSource("Google-Roads",
            0, 18, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=m&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };
    //谷歌地形
    public static final OnlineTileSourceBase GoogleTerrain = new XYTileSource("Google-Terrain",
            0, 16, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=t&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) +
                    "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };
    //谷歌地形带标注
    public static final OnlineTileSourceBase GoogleTerrainHybrid = new XYTileSource("Google-Terrain-Hybrid",
            0, 16, 512, ".png", new String[]{
            "http://mt0.google.cn",
            "http://mt1.google.cn",
            "http://mt2.google.cn",
            "http://mt3.google.cn",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/vt/lyrs=p&scale=2&hl=zh-CN&gl=CN&src=app&x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };
    //高德地图
    public static final OnlineTileSourceBase AutoNaviVector = new XYTileSource("AutoNavi-Vector",
            0, 20, 256, ".png", new String[]{
            "https://wprd01.is.autonavi.com/appmaptile?",
            "https://wprd02.is.autonavi.com/appmaptile?",
            "https://wprd03.is.autonavi.com/appmaptile?",
            "https://wprd04.is.autonavi.com/appmaptile?",
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "x=" + MapTileIndex.getX(pMapTileIndex) + "&y=" + MapTileIndex.getY(pMapTileIndex) + "&z="
                    + MapTileIndex.getZoom(pMapTileIndex) + "&lang=zh_cn&size=1&scl=1&style=7&ltype=7";
        }
    };

    private static String key = "9fd050612665197fc9cbf3a22aef4283";

    //天地图 影像底图
    public static final OnlineTileSourceBase SkyTiled = new XYTileSource("sky",0,20,256,"",  new String[]{
//
//            "http://t1.tianditu.com/DataServer?T=img_c",
//            "http://t2.tianditu.com/DataServer?T=img_c",
//            "http://t3.tianditu.com/DataServer?T=img_c",
//            "http://t4.tianditu.com/DataServer?T=img_c",
//            "http://t5.tianditu.com/DataServer?T=img_c",
//            "http://t6.tianditu.com/DataServer?T=img_c"
            "https://t0.tianditu.gov.cn/vec_c/wmts?T=img_c&x=208&y=105&l=8&tk="+key,
            "https://t0.tianditu.gov.cn/vec_c/wmts?T=img_w&x=208&y=105&l=8&tk="+key
//             "http://t0.tianditu.gov.cn/DataServer?x=208&y=105&l=8&T=img_c&tk="+key

    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "&X=" + MapTileIndex.getX(pMapTileIndex) + "&Y=" + MapTileIndex.getY(pMapTileIndex)
                    + "&L=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    // 天地图 影像注记
    public static final OnlineTileSourceBase SkySource = new XYTileSource("sky",1,18,256,".png",new String[]{
            "http://t1.tianditu.com/DataServer?T=cia_w",
            "http://t2.tianditu.com/DataServer?T=cia_w",
            "http://t3.tianditu.com/DataServer?T=cia_w",
            "http://t4.tianditu.com/DataServer?T=cia_w",
            "http://t5.tianditu.com/DataServer?T=cia_w",
            "http://t6.tianditu.com/DataServer?T=cia_w"
    }){
        @Override
        public String getTileURLString(long pMapTileIndex ){
            return getBaseUrl() + "&X=" + MapTileIndex.getX(pMapTileIndex) + "&Y=" + MapTileIndex.getY(pMapTileIndex)
                    + "&L=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };

    public static final OnlineTileSourceBase SkyTraffic = new OnlineTileSourceBase("sky",0,20,256,".png",new String[]{
            "http://gisserver.tianditu.gov.cn/TDTService/wfs"
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "&X=" + MapTileIndex.getX(pMapTileIndex) + "&Y=" + MapTileIndex.getY(pMapTileIndex)
                    + "&L=" + MapTileIndex.getZoom(pMapTileIndex);
        }
    };



    /*
     * 天地图
     */
    private static final String[] tianDiTuServer = {
            "https://t0.tianditu.gov.cn",
            "https://t1.tianditu.gov.cn",
            "https://t2.tianditu.gov.cn",
            "https://t3.tianditu.gov.cn",
            "https://t4.tianditu.gov.cn",
            "https://t5.tianditu.gov.cn",
            "https://t6.tianditu.gov.cn",
            "https://t7.tianditu.gov.cn"
    };

    /**
     * 天地图 影像底图
     */
    public static final OnlineTileSourceBase tianDiTuSatellite = new XYTileSource("tianDiTuSatellite", 1, 18, 256, ".png", tianDiTuServer) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            return getBaseUrl() + "/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

    /**
     * 天地图 影像注记
     */
    public static final OnlineTileSourceBase tianDiTuSatelliteLabel = new OnlineTileSourceBase("tianDiTuSatelliteLabel", 1, 18, 256, "PNG", tianDiTuServer) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

    /**
     * 天地图 矢量底图
     */
    public static final OnlineTileSourceBase tianDiTuVector = new XYTileSource("tianDiTuVector", 1, 18, 256, ".png", tianDiTuServer) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            return getBaseUrl() + "/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

    /**
     * 天地图 矢量注记
     */
    public static final OnlineTileSourceBase tianDiTuVectorLabel = new OnlineTileSourceBase("tianDiTuVectorLabel", 1, 18, 256, "PNG", tianDiTuServer) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

    /**
     * 天地图 地形晕渲
     */
    public static final OnlineTileSourceBase tianDiTuTerrain = new XYTileSource("tianDiTuTerrain", 1, 14, 256, ".png", tianDiTuServer) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            return getBaseUrl() + "/ter_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

    /**
     * 天地图 地形注记
     */
    public static final OnlineTileSourceBase tianDiTuTerrainLabel = new OnlineTileSourceBase("tianDiTuTerrainLabel", 1, 14, 256, "PNG", tianDiTuServer) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/cta_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cta&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

    /**
     * 天地图 全球境界
     */
    public static final OnlineTileSourceBase tianDiTuBoundary = new OnlineTileSourceBase("tianDiTuBoundary", 1, 18, 256, "PNG", tianDiTuServer) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/ibo_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ibo&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

}