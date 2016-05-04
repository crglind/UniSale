package edu.scranton.lind.unisale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class FiltersDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_layout, null);
        Spinner priceSpinner = (Spinner)view.findViewById(R.id.price_spinner);
        ArrayAdapter<CharSequence> pAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.price_filter_array,android.R.layout.simple_spinner_item);
        pAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSpinner.setAdapter(pAdapter);
        Spinner catSpinner = (Spinner)view.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> cAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.category_filter_array, android.R.layout.simple_spinner_item);
        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(cAdapter);
        builder.setView(view);
        builder.setTitle("Filters")
                .setPositiveButton("SET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //RESET listing with filters

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }
}
