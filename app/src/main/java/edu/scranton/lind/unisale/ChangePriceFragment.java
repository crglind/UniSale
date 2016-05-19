package edu.scranton.lind.unisale;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;


public class ChangePriceFragment extends Fragment {

    public interface ListingProvider{Listing getListing();}

    private TextView mPrice;
    private ListingProvider provider;
    private Listing mListing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListing = provider.getListing();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_price, container, false);
        mPrice = (TextView)view.findViewById(R.id.new_list_price);
        assignButtons(view);
        Button doneButton = (Button)view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeScreen)getActivity()).changePriceDoneClicked(mListing,
                        Double.parseDouble(mPrice.getText().toString().replaceAll("[^\\d.]", "")));
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        provider = (ListingProvider)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        provider = null;
    }

    private void assignButtons(View view){
        Button buttonOne = (Button)view.findViewById(R.id.button_one);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(0.01);
            }
        });
        Button buttonTwo = (Button)view.findViewById(R.id.button_two);
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(0.02);
            }
        });
        Button buttonThree = (Button)view.findViewById(R.id.button_three);
        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(0.03);
            }
        });
        Button buttonFour = (Button)view.findViewById(R.id.button_four);
        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(0.04);
            }
        });
        Button buttonFive = (Button)view.findViewById(R.id.button_five);
        buttonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(0.05);
            }
        });
        Button buttonSix = (Button)view.findViewById(R.id.button_six);
        buttonSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(0.06);
            }
        });
        Button buttonSeven = (Button)view.findViewById(R.id.button_seven);
        buttonSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(0.07);
            }
        });
        Button buttonEight = (Button)view.findViewById(R.id.button_eight);
        buttonEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(0.08);
            }
        });
        Button buttonNine = (Button)view.findViewById(R.id.button_nine);
        buttonNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(0.09);
            }
        });
        Button buttonZero = (Button)view.findViewById(R.id.button_zero);
        buttonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(0.00);
            }
        });
        Button backButton = (Button)view.findViewById(R.id.price_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(1);
            }
        });
    }

    private void buttonPressed(double number){
        NumberFormat cashFormat = NumberFormat.getCurrencyInstance();
        String currencyString = mPrice.getText().toString();
        String priceNum = currencyString.replaceAll("[^\\d.]", "");
        double priceDouble = Double.parseDouble(priceNum);
        //if not back add the given number
        if(number != 1){
            priceDouble = (priceDouble * 10) + number;
        }
        //otherwise move the lowest digit
        else{
            String lastDigitString = currencyString.substring(
                    currencyString.length()-1, currencyString.length());
            double lastDigit = Double.parseDouble(lastDigitString)/100.0;
            priceDouble = (priceDouble-lastDigit)/10;
        }
        String billString = cashFormat.format(priceDouble);
        mPrice.setText(billString);
    }
}
