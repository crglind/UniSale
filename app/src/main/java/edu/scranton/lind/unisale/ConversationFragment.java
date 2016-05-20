package edu.scranton.lind.unisale;


import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ConversationFragment extends ListFragment{

    public static final String ARG_USER = "edu.scranton.lindc4.unisale.conv.user";
    public static final String ARG_OWNER = "edu.scranton.lindc4.unisale.conv.owner";
    public static final String ARG_LIST = "edu.scranton.lindc4.unisale.conv.list";
    public static final String ARG_NAME = "edu.scranton.lindc4.unisale.conv.name";
    public static final String ARG_MY_NAME = "edu.scranton.lind.unisale.conv.myname";
    public static final String ARG_MY_ID = "edu.scranton.lind.unisale.conv.myid";


    private int mMyID;
    private int mOtherID;
    private int mOwnerID;
    private int mListNum;
    private String mOtherUser;
    private String mUsername;
    private EditText mMessage;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        mOtherID = bundle.getInt(ARG_USER);
        mOwnerID = bundle.getInt(ARG_OWNER);
        mListNum = bundle.getInt(ARG_LIST);
        mOtherUser = bundle.getString(ARG_NAME);
        mUsername = bundle.getString(ARG_MY_NAME);
        mMyID = bundle.getInt(ARG_MY_ID);
        ConversationAsyncTask ca = new ConversationAsyncTask(this);
        ca.execute(Integer.toString(mOtherID), Integer.toString(mOwnerID), Integer.toString(mListNum), mOtherUser, mUsername, Integer.toString(mMyID));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conversation_layout, container, false);
        mMessage = (EditText)view.findViewById(R.id.message_box);
        Button send = (Button)view.findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMessage.getText().toString().equals("")){
                    writeAsync(mMessage.getText().toString());
                }
            }
        });
        return view;
    }

    public void showConvo(ArrayList convo){
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, convo);
        setListAdapter(adapter);
    }

    private void writeAsync(String message){
        WriteMessageAsyncTask wa = new WriteMessageAsyncTask(this);
        wa.execute(message, Integer.toString(mOtherID), Integer.toString(mOwnerID), Integer.toString(mListNum), Integer.toString(mMyID));
    }

    public void success(){
        mMessage.clearComposingText();
    }

    public void unsuccessful(){
        Toast.makeText(getContext(), "Unable to Send Message", Toast.LENGTH_SHORT).show();
    }

    public void unableToConnect(){
        Toast.makeText(getContext(), "Network Error: Failed to Connect", Toast.LENGTH_SHORT).show();
    }
}
