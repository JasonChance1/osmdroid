package com.example.osmdroiddemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.milestones.MilestoneBitmapDisplayer;
import org.osmdroid.views.overlay.milestones.MilestoneManager;
import org.osmdroid.views.overlay.milestones.MilestonePathDisplayer;
import org.osmdroid.views.overlay.milestones.MilestonePixelDistanceLister;

import java.util.ArrayList;
import java.util.List;

/**
 * A very simple borrowed from Android's "Finger Page" example, modified to generate polylines that
 * are geopoint bound after finger up.
 * created on 1/13/2017.
 *
 * @author Alex O'Ree
 */

public class CustomPaintingSurface extends View {
    public void setMode(Mode mode) {
        this.drawingMode = mode;
    }

    private Mode drawingMode = Mode.Polyline;

    public enum Mode {
        Polyline,
        Polygon,
        PolygonHole,
        PolylineAsPath
    }

    protected boolean withArrows = false;
    private Canvas mCanvas;
    private Path mPath;
    private MapView map;
    private List<Point> pts = new ArrayList<>();
    private final Paint mPaint;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;// 容差值，误差范围

    transient Polygon lastPolygon = null;


    public CustomPaintingSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);// 是否使用抖动，抖动可以让颜色更加平滑
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND); // 线连接处的样式
        mPaint.setStrokeCap(Paint.Cap.ROUND);  // 线冒样式，线两端的样式
        mPaint.setStrokeWidth(12);
//        mPaint.setColorFilter(ColorFilter filter);//设置颜色过滤器,可以通过颜色过滤器过滤掉对应的色值，比如去掉照片颜色，生成老照片效果；
//        mPaint.setFilterBitmap(true); //设置为true，会过滤掉Bitmap图像的优化操作，加快显示速度
//        mPaint.setMaskFilter();
//        mPaint.setShader(Shader shader); //设置渐变效果，包括BitmapShater位图图像渲染,LinearGradient线性渲染,SweepGradient扫描渐变和ComposeGradient组合渲染
    }


    //可以在这个方法中拿到view的宽高
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        final Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    public void init(MapView mapView) {
        map = mapView;
    }

    private void touch_start(float x, float y) {
        mPath.reset();// 清除线条和曲线，不改变填充样式
        mPath.moveTo(x, y);// 设置路径的起始点到x,y
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
        if (map != null) {
            Projection projection = map.getProjection();
            ArrayList<GeoPoint> geoPoints = new ArrayList<>();
            final Point unrotatedPoint = new Point();
            for (int i = 0; i < pts.size(); i++) {
                projection.unrotateAndScalePoint(pts.get(i).x, pts.get(i).y, unrotatedPoint);
                GeoPoint iGeoPoint = (GeoPoint) projection.fromPixels(unrotatedPoint.x, unrotatedPoint.y);
                geoPoints.add(iGeoPoint);
            }

            if (geoPoints.size() > 2) {
                //only plot a line unless there's at least one item
                switch (drawingMode) {
                    case Polyline:
                    case PolylineAsPath:
                        final boolean asPath = drawingMode == Mode.PolylineAsPath;
                        final int color = Color.argb(255, 255, 0, 0);
                        final Polyline line = new Polyline(map);
                        line.usePath(true);
                        line.setInfoWindow(
                                new BasicInfoWindow(org.osmdroid.library.R.layout.bonuspack_bubble, map));
                        line.getOutlinePaint().setColor(Color.WHITE);
                        line.setTitle("折线" + (asPath ? " as Path" : ""));
                        line.setPoints(geoPoints);
                        line.showInfoWindow();
                        line.getOutlinePaint().setStrokeCap(Paint.Cap.ROUND);
                        //example below

                        line.setOnClickListener((polyline, mapView, eventPos) -> {
                            line.showInfoWindow();
                            Toast.makeText(mapView.getContext(), "点数量 " + polyline.getActualPoints().size(), Toast.LENGTH_LONG).show();
                            return false;
                        });

                        if (withArrows) {
                            final Paint arrowPaint = new Paint();
                            arrowPaint.setColor(color);
                            arrowPaint.setStrokeWidth(10.0f);
                            arrowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                            arrowPaint.setAntiAlias(true);
                            final Path arrowPath = new Path(); // a simple arrow towards the right
                            arrowPath.moveTo(-10, -10);
                            arrowPath.lineTo(10, 0);
                            arrowPath.lineTo(-10, 10);
                            arrowPath.close();
                            final List<MilestoneManager> managers = new ArrayList<>();
                            managers.add(new MilestoneManager(
                                    new MilestonePixelDistanceLister(50, 50),
                                    new MilestonePathDisplayer(0, true, arrowPath, arrowPaint)
                            ));
                            line.setMilestoneManagers(managers);
                        }
                        line.setSubDescription(line.getBounds().toString());
                        map.getOverlayManager().add(line);
                        lastPolygon = null;
                        break;
                    case Polygon:
                        Polygon polygon = new Polygon(map);
                        polygon.setInfoWindow(
                                new BasicInfoWindow(org.osmdroid.library.R.layout.bonuspack_bubble, map));
                        polygon.getFillPaint().setColor(Color.argb(75, 255, 0, 0));
                        polygon.setPoints(geoPoints);
                        polygon.setTitle("图形样例");
                        polygon.showInfoWindow();
                        if (withArrows) {
                            final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), org.osmdroid.library.R.drawable.round_navigation_white_48);
                            final List<MilestoneManager> managers = new ArrayList<>();
                            managers.add(new MilestoneManager(
                                    new MilestonePixelDistanceLister(20, 200),
                                    new MilestoneBitmapDisplayer(90, true, bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2)
                            ));
                            polygon.setMilestoneManagers(managers);
                        }


                        polygon.setOnClickListener(new Polygon.OnClickListener() {
                            @Override
                            public boolean onClick(Polygon polygon, MapView mapView, GeoPoint eventPos) {
                                lastPolygon = polygon;
                                polygon.onClickDefault(polygon, mapView, eventPos);
                                Toast.makeText(mapView.getContext(), "点击图形", Toast.LENGTH_LONG).show();
                                return false;
                            }
                        });
                        //polygon.setSubDescription(BoundingBox.fromGeoPoints(polygon.getPoints()).toString());
                        map.getOverlayManager().add(polygon);
                        lastPolygon = polygon;
                        break;
                    case PolygonHole:
                        if (lastPolygon != null) {
                            List<List<GeoPoint>> holes = new ArrayList<>();
                            holes.add(geoPoints);
                            lastPolygon.setHoles(holes);
                        }
                        break;
                }

                map.invalidate();
            }
        }

        pts.clear();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        pts.add(new Point((int) x, (int) y));
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public void destroy() {
        map = null;
        this.lastPolygon = null;
    }

}
