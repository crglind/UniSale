package edu.scranton.lind.unisale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class InterestedDialog extends DialogFragment {

    public static final String ARG_ID = "edu.scranton.lind.unisale.interesteddialog.ARG_ID";
    public static final String ARG_LISTNUM = "edu.scranton.lind.unisale.interesteddialog.ARG_LISTNUM";
    public static final String ARG_TITLE = "edu.scranton.lind.unisale.interesteddialog.ARG_TITLE";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.interested_layout, null);
        Bundle bundle = getArguments();
        int ownerId = bundle.getInt(ARG_ID);
        int listNum = bundle.getInt(ARG_LISTNUM);
        String title = bundle.getString(ARG_TITLE);
        builder.setView(view);
        builder.setTitle(title).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }
}
