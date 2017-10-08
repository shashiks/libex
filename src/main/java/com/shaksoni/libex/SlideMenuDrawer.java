package com.shaksoni.libex;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shaksoni on 9/22/17.
 */

public class SlideMenuDrawer extends ArrayAdapter<SlideMenu> {


    Context mContext;
    int layoutResourceId;
    SlideMenu data[] = null;

    public SlideMenuDrawer(Context mContext, int layoutResourceId, SlideMenu[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
        TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);

        SlideMenu folder = data[position];


        imageViewIcon.setImageResource(folder.getIcon());
        textViewName.setText(folder.getTitle());

        return listItem;
    }
}
