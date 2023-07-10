package com.example.osmdroiddemo.customview;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.osmdroiddemo.R;

import org.osmdroid.api.IMapView;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;

/**
 * created on 12/27/2017.
 *
 * @author Alex O'Ree
 */

public class DrawCircle10km extends BaseSampleFragment{
    @Override
    public String getSampleTitle() {
        return "Draw a circle 10km (long press)";

    }

    ImageButton painting, panning;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_free_paint, null);
        mMapView = v.findViewById(R.id.mapview);
        mMapView.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                Log.i(IMapView.LOGTAG, System.currentTimeMillis() + " onScroll " + event.getX() + "," + event.getY());
                //Toast.makeText(getActivity(), "onScroll", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                Log.i(IMapView.LOGTAG, System.currentTimeMillis() + " onZoom " + event.getZoomLevel());

                return true;
            }
        });

        RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(mMapView);
        mRotationGestureOverlay.setEnabled(true);
        mMapView.setMultiTouchControls(true);
        mMapView.getOverlayManager().add(mRotationGestureOverlay);



        panning = v.findViewById(R.id.location);
        panning.setVisibility(View.GONE);

        painting = v.findViewById(R.id.paint);
        painting.setVisibility(View.GONE);


        CirclePlottingOverlay plotter = new CirclePlottingOverlay(100);
        mMapView.getOverlayManager().add(plotter);

        return v;

    }

}
