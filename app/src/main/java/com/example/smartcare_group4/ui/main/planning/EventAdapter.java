package com.example.smartcare_group4.ui.main.planning;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.data.Event;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);
        ImageButton medTaken = convertView.findViewById(R.id.medTaken);
        Drawable drawable;
        switch (event.getTaken()) {
            case "yes":
                drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.tick, null);
                medTaken.setForeground(drawable);
                medTaken.setBackgroundResource(R.color.green);
                break;
            case "no":
                medTaken.setBackgroundResource(R.color.grey);
                break;
            case "wrong":
                drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.cross, null);
                medTaken.setForeground(drawable);
                medTaken.setBackgroundResource(R.color.red);
                break;
        }



        String eventTitle = event.getName();
        eventCellTV.setText(eventTitle);
        return convertView;
    }
}
