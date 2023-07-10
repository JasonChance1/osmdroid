package com.example.osmdroiddemo.utils;


import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.MapTileIndex;

/**
 * 图源
 */
public class TileSource extends TileSourceFactory {

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



//    http://192.168.110.181:8088/earthview/rest/services/etmserver/wmts?serviceName=大姚管网_排水污水
    public static final OnlineTileSourceBase wmtsTest = new OnlineTileSourceBase("大姚",0,18,256,".png",new String[]{
            "http://192.168.110.181:8088/earthview/rest/services/etmserver/wmts?serviceName=大姚管网_排水污水"
    }) {
        @Override
        public String getTileURLString(long pMapTileIndex) {

            return getBaseUrl();
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
     *
     * vec: 矢量底图
     * cva: 矢量注记图层 chinese vector annotation
     * eva: 矢量注记图层-英文注记
     * img: 影像底图
     * cia: 影像注记图层 chinese image annotation
     * eia: 影像注记图层 -英文
     * ter: 地形底图 terrain
     * cta: 地形注记图层
     * _c 经纬度 WGS1984
     * _w 墨卡托 CGCS2000
     */
    public static final OnlineTileSourceBase tianDiTuSatellite = new XYTileSource("tianDiTuSatellite", 0, 18, 256, ".png", tianDiTuServer) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            return getBaseUrl() + "/img_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

    /**
     * 天地图 影像注记
     */
    public static final OnlineTileSourceBase tianDiTuSatelliteLabel = new OnlineTileSourceBase("tianDiTuSatelliteLabel", 0, 18, 256, "PNG", tianDiTuServer) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/cia_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

    /**
     * 天地图 矢量底图
     */
    public static final OnlineTileSourceBase tianDiTuVector = new XYTileSource("tianDiTuVector", 0, 18, 256, ".png", tianDiTuServer) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            return getBaseUrl() + "/vec_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

    /**
     * 天地图 矢量注记
     */
    public static final OnlineTileSourceBase tianDiTuVectorLabel = new OnlineTileSourceBase("tianDiTuVectorLabel", 0, 18, 256, "PNG", tianDiTuServer) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/cva_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

    /**
     * 天地图 地形晕渲
     */
    public static final OnlineTileSourceBase tianDiTuTerrain = new XYTileSource("tianDiTuTerrain", 0, 18, 256, ".png", tianDiTuServer) {
        @Override
        public String getTileURLString(final long pMapTileIndex) {
            return getBaseUrl() + "/ter_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

    /**
     * 天地图 地形注记
     */
    public static final OnlineTileSourceBase tianDiTuTerrainLabel = new OnlineTileSourceBase("tianDiTuTerrainLabel", 0, 18, 256, "PNG", tianDiTuServer) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/cta_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cta&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

    /**
     * 天地图 全球境界
     */
    public static final OnlineTileSourceBase tianDiTuBoundary = new OnlineTileSourceBase("tianDiTuBoundary", 0, 18, 256, "PNG", tianDiTuServer) {
        @Override
        public String getTileURLString(long pMapTileIndex) {
            return getBaseUrl() + "/ibo_w/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ibo&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILECOL=" + MapTileIndex.getX(pMapTileIndex) + "&TILEROW=" + MapTileIndex.getY(pMapTileIndex) + "&TILEMATRIX=" + MapTileIndex.getZoom(pMapTileIndex) + "&tk=" + key;
        }
    };

}