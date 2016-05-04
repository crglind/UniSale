package edu.scranton.lind.unisale;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageSelectionFragment extends ListFragment {

    private String[] users = {"User1", "User2"};
    private String[] date = {"4/11/2016", "4/10/2016"};
    private String[] exTitleArray;

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        setHasOptionsMenu(true);
        exTitleArray = getResources().getStringArray(R.array.example_titles);
        List<HashMap<String, String>> exMsgList = new ArrayList<>();
        for (int i=0; i<2; i++){
            HashMap<String, String> hm = new HashMap<>();
            hm.put("user", users[i]);
            hm.put("item", " - " + exTitleArray[i]);
            hm.put("date", date[i]);
            exMsgList.add(hm);
        }
        String[] from = {"user", "item", "date"};
        int[] to = {R.id.message_sel_user, R.id.message_sel_item, R.id.message_sel_date};
        SimpleAdapter adapter = new SimpleAdapter(
                getActivity().getBaseContext(), exMsgList, R.layout.message_selection_rows, from, to);
        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.messages_icon).setVisible(false);
        //menu.findItem(R.id.search_icon).setVisible(false);
        menu.findItem(R.id.filter_icon).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
