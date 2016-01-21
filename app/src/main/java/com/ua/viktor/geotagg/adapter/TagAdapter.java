package com.ua.viktor.geotagg.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ua.viktor.geotagg.R;
import com.ua.viktor.geotagg.data.TagsContract;

/**
 * Created by viktor on 17.01.16.
 */
public class TagAdapter extends CursorAdapter {
    private static final String LOG_TAG = TagAdapter.class.getName();
    private Context mContext;
    private static int sLoaderID;



    public TagAdapter(Context context, Cursor c, int flags, int loaderID){
        super(context, c, flags);
        Log.d(LOG_TAG, "TagAdapter");
        mContext = context;
        sLoaderID = loaderID;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.tagging_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView  textView = (TextView) view.findViewById(R.id.text_view_active_list_item_name);

        final String versionName = cursor.getString(cursor.getColumnIndexOrThrow(TagsContract.TagEntry.COLUMN_ICON));;
        textView.setText(versionName);

       // int imageIndex = cursor.getColumnIndex(FlavorsContract.FlavorEntry.COLUMN_ICON);
        //int image = cursor.getInt(imageIndex);
      //  Log.i(LOG_TAG, "Image reference extracted: " + image);

       // viewHolder.imageView.setImageResource(image);
    }

    public void updateList(){
        notifyDataSetChanged();
    }
}
