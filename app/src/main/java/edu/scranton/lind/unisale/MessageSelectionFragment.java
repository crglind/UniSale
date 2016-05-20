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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageSelectionFragment extends ListFragment {

    public static final String ARG_ID = "edu.scranton.lindc4.msgselect.ID";

    public interface OnMessageSelected{void onMessageSelected(Integer[] info, String otherUsername);}

    private int mUID;
    private ArrayList<Integer[]> mInfo;
    private ArrayList<String> mUsernames;
    private OnMessageSelected mListener;

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        setHasOptionsMenu(true);
        mUID = getArguments().getInt(ARG_ID);
        MessageSelectionAsyncTask msa = new MessageSelectionAsyncTask(this);
        msa.execute(mUID);
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

    public void noMessages(){
        Toast.makeText(getContext(), "No Messages!", Toast.LENGTH_SHORT).show();
    }

    public void makeList(ArrayList<Integer[]> info){
        mInfo = info;
        ArrayList<Integer> otherUser = new ArrayList<>();
        for(int i=0; i<info.size(); i++){
            Integer[] lineInfo = info.get(i);
            otherUser.add(lineInfo[0]);
        }
        FindUsersAsyncTask fa = new FindUsersAsyncTask(this);
        fa.execute(otherUser);
    }

    public void getListings(ArrayList<String> usernames){
        mUsernames = usernames;
        ArrayList<Integer[]> listingKey = new ArrayList<>();
        for(int i=0; i<mInfo.size(); i++){
            Integer[] lineInfo = mInfo.get(i);
            Integer[] keys = new Integer[2];
            keys[0] = lineInfo[1];
            keys[1] = lineInfo[2];
            listingKey.add(keys);
        }
        FindListingsAsyncTask fla = new FindListingsAsyncTask(this);
        fla.execute(listingKey);
    }

    public void setup(ArrayList<String> titles){
        ArrayList<MessageRow> fields = new ArrayList<>();
        for(int i=0; i<mUsernames.size(); i++){
            MessageRow row = new MessageRow();
            row.setUsername(mUsernames.get(i));
            row.setTitle(titles.get(i));
            fields.add(row);
        }
        MessageSelectionAdapter adapter = new MessageSelectionAdapter(getContext(), fields);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        try {
            mListener = (OnMessageSelected) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString());
        }
        mListener.onMessageSelected(mInfo.get(position), mUsernames.get(position));
    }
}
