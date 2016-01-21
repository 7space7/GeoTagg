package com.ua.viktor.geotagg.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.ua.viktor.geotagg.R;

import java.io.File;
import java.util.Hashtable;

/**
 * Created by viktor on 20.01.16.
 */
public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final Hashtable<String, Boolean> markerSet;
    private Context context;
    private View myContentsView;

    public MapInfoWindowAdapter(Context context, Hashtable<String, Boolean> markerSet) {
        this.context = context;
        this.markerSet = markerSet;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myContentsView = inflater.inflate(R.layout.map_info_content, null);
        ImageView imageView = (ImageView) myContentsView.findViewById(R.id.imgView_map_info_content);



        Log.v("sdf",marker.getSnippet());
          //  Picasso.with(context).load(new File(marker.getSnippet())).into(imageView);
           File imgFile = new  File(marker.getSnippet());

           if(imgFile.exists()){

               Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
               imageView.setImageBitmap(myBitmap);

           }

        return myContentsView;
    }
}
