package com.iricostruttori.meditazione.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iricostruttori.meditazione.R;

import java.util.ArrayList;

public class ItemDayMonthAdapter extends ArrayAdapter<ItemDayMonth> {

    public ItemDayMonthAdapter(Context context, ArrayList<ItemDayMonth> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ItemDayMonth item = getItem(position);
        int item_id = item.getId();
        String item_day_month = item.getDayMonth();
        String txt_day_month = item.getTxtDayMonth();
        int item_txt_id = parent.getContext().getResources().getIdentifier(item.getDayMonth(),
                "drawable", parent.getContext().getPackageName());

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pensieri, parent, false);
        }

        // Lookup view for data population
        //ImageView item_icon_view = (ImageView) convertView.findViewById(R.id.item_icon);
        TextView item_txt_daymonth_view = (TextView) convertView.findViewById(R.id.textPensiero);

        // Populate the data into the template view using the data object
        item_txt_daymonth_view.setText(txt_day_month);
        //item_icon_view.setImageResource(item_icon_id);

        // Return the completed view to render on screen
        return convertView;
    }
}