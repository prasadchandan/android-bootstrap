package com.trader.android.app.ui;

import com.trader.android.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.beardedhen.androidbootstrap.FontAwesomeText;


import java.util.ArrayList;

public class NavMenuItemAdapter extends ArrayAdapter<NavMenuItem> {

    ViewHolder viewHolder;

    private static class ViewHolder {
        private TextView itemText;
        private FontAwesomeText itemIcon;
    }

    public NavMenuItemAdapter(Context context, int textViewResourceId, ArrayList<NavMenuItem> items) {
        super(context, textViewResourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.drawer_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.itemText = (TextView) convertView.findViewById(R.id.text1);
            viewHolder.itemIcon = (FontAwesomeText) convertView.findViewById(R.id.menuIcon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NavMenuItem item = getItem(position);
        if (item!= null) {
            viewHolder.itemText.setText(item.getItemText());
            viewHolder.itemIcon.setIcon(item.getItemIconText());
        }

        return convertView;
    }
}