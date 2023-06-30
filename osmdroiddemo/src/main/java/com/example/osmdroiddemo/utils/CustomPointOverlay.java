package com.example.osmdroiddemo.utils;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.PointL;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;

/**
 * 自定义绘制的地图坐标
 */
public class CustomPointOverlay extends Overlay {

    private final Point mMapCoordsProjected = new Point();
    private final Point mMapCoordsTranslated = new Point();
    protected Paint mCirclePaint = new Paint();

    public CustomPointOverlay() {
    }

    static CustomPointOverlay mGisOverlay = null;

    public static CustomPointOverlay GetInstance() {
        if (mGisOverlay == null) {
            synchronized (CustomPointOverlay.class) {
                if (mGisOverlay == null) {
                    mGisOverlay = new CustomPointOverlay();
                }
            }
        }
        return mGisOverlay;
    }
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        GeoPoint geoPoint = new GeoPoint(23.12658183, 113.365588756);
        //经纬度坐标到屏幕坐标的转换
        mapView.getProjection().toPixels(geoPoint, mMapCoordsProjected);
        Projection pj = mapView.getProjection();
        pj.toPixelsFromProjected(new PointL(mMapCoordsProjected.x,mMapCoordsProjected.y), mMapCoordsTranslated);

//            final float radius = lastFix.getAccuracy()
//                    / (float) TileSystem.GroundResolution(lastFix.getLatitude(),
//                    mapView.getZoomLevel());
        final float radius = 10L;
        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mMapCoordsTranslated.x, mMapCoordsTranslated.y, radius, mCirclePaint);
    }

//
//    // 屏幕坐标转经纬度
//    public void fun(MapView mapView, Point point){
//
//    // 获取屏幕坐标点
//        int x = point.x; // 屏幕坐标的x值
//        int y = point.y; // 屏幕坐标的y值
//
//// 创建一个Point对象来保存屏幕坐标
//        Point screenPoint = new Point(x, y);
//
//// 获取投影对象
//        Projection projection = mapView.getProjection();
//
//// 将屏幕坐标转换为经纬度坐标
//        GeoPoint geoPoint = (GeoPoint) projection.fromPixels(screenPoint.x, screenPoint.y);
//
//// 获取经纬度坐标
//        double latitude = geoPoint.getLatitude();
//        double longitude = geoPoint.getLongitude();
//
//    }
}
