package edu.scranton.lind.unisale;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ListingAdapter extends ArrayAdapter<Listing> {

    public class MyListingTag{
        TextView title;
        TextView description;
        TextView price;
        TextView condition;
        TextView category;
        Button interested;
        Button message;
    }

    private ArrayList<Listing> mListings;
    private ListingFragment mFrag;
    private String mLayout;

    public ListingAdapter
            (Context context, ArrayList<Listing> objects, ListingFragment myFrag, String givenLayout){
        super(context, 0, objects);
        this.mListings = objects;
        mFrag = myFrag;
        mLayout = givenLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Listing listing = mListings.get(position);
        MyListingTag tag;
        if(convertView == null){
            if(mLayout == "small"){
                convertView = LayoutInflater.from(getContext()).inflate
                        (R.layout.listing_row_layout_small, parent, false);
            }
            else {
                convertView = LayoutInflater.from(getContext()).inflate
                        (R.layout.listing_row_layout, parent, false);
            }
            tag = new MyListingTag();
            tag.title = (TextView)convertView.findViewById(R.id.listing_title);
            tag.description = (TextView)convertView.findViewById(R.id.listing_description);
            tag.price = (TextView)convertView.findViewById(R.id.listing_price);
            tag.condition = (TextView)convertView.findViewById(R.id.listing_condition);
            tag.category = (TextView)convertView.findViewById(R.id.listing_category);
            convertView.setTag(tag);
            tag.interested = (Button)convertView.findViewById(R.id.interested_button);
            tag.interested.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //MAKE EDIT FRAGMENT AND ADD FUNCTIONALITY
                }
            });
            tag.message = (Button)convertView.findViewById(R.id.message_button);
            tag.message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        tag = (MyListingTag)convertView.getTag();
        tag.title.setText(listing.getListTitle());
        tag.description.setText(listing.getDescription());
        NumberFormat cashFormat = NumberFormat.getCurrencyInstance();
        double price = listing.getPrice();
        if(price == 0.00){
            tag.price.setText("PRICE: FREE!");
        }
        else{
            tag.price.setText("PRICE: " + cashFormat.format(price));
        }
        tag.condition.setText("CONDITION: " + listing.getCondition());
        tag.category.setText("CATEGORY: " + listing.getCategory());
        return convertView;
    }
}

