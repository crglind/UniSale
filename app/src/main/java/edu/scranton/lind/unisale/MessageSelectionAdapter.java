package edu.scranton.lind.unisale;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class MessageSelectionAdapter extends ArrayAdapter<MessageRow> {

    public class MyListingTag {
        TextView username;
        TextView title;
    }

    private ArrayList<MessageRow> info;

    public MessageSelectionAdapter(Context context, ArrayList<MessageRow> fields) {
        super(context, 0, fields);
        this.info = fields;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MessageRow row = info.get(position);
        MyListingTag tag;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate
                    (R.layout.message_selection_rows, parent, false);
            tag = new MyListingTag();
            tag.title = (TextView) convertView.findViewById(R.id.message_sel_item);
            tag.username = (TextView) convertView.findViewById(R.id.message_sel_user);
            convertView.setTag(tag);
        }
        tag = (MyListingTag) convertView.getTag();
        tag.title.setText("- " + row.getTitle());
        tag.username.setText(row.getUsername());
        return convertView;
    }
}
