package edu.scranton.lind.unisale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract.Listings;


public class NewListing extends Fragment {

    public interface DbProvider{
        SQLiteDatabase getReadableDb();
        SQLiteDatabase getWritableDb();
    }

    public static final String USER = "edu.scranton.lind.newlisting.USER";
    public static final String SCHOOL = "edu.scranton.lind.newlisting.SCHOOl";

    private Context mContext;
    private TextView mPrice;
    private SQLiteDatabase mReadableDatabase;
    private SQLiteDatabase mWritableDatabase;
    private int mUID;
    private int mSchool;
    private DbProvider provider;
    private EditText mTitle;
    private EditText mDescription;
    private Spinner mConSpinner;
    private Spinner mCatSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mUID = getArguments().getInt(USER);
        mSchool = getArguments().getInt(SCHOOL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_listing, container, false);
        mReadableDatabase = provider.getReadableDb();
        mWritableDatabase = provider.getWritableDb();
        mPrice = (TextView)view.findViewById(R.id.new_list_price);
        mTitle = (EditText)view.findViewById(R.id.new_list_title);
        mDescription = (EditText)view.findViewById(R.id.new_list_description);
        if(getActivity().findViewById(R.id.small_container) != null){
            Button changePrice = (Button)view.findViewById(R.id.price_button);
            changePrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //GET SMALL DEVICE AND TEST AND ADAPT THIS
                    ((HomeScreen)getActivity()).changePriceClicked();
                }
            });
        }
        else{
            assignButtons(view);
        }
        mConSpinner = (Spinner)view.findViewById(R.id.new_list_condition_spinner);
        ArrayAdapter<CharSequence> conAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.conditions_array, android.R.layout.simple_spinner_item);
        conAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mConSpinner.setAdapter(conAdapter);
        mCatSpinner = (Spinner)view.findViewById(R.id.new_list_category_spinner);
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.category_filter_array, android.R.layout.simple_spinner_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mCatSpinner.setAdapter(catAdapter);
        Button postButton = (Button)view.findViewById(R.id.post_listing_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields() == true){
                    writeToDatabase();
                    getActivity().onBackPressed();
                    Toast.makeText(mContext,
                            mTitle.getText().toString() + " - POSTED!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.messages_icon).setVisible(false);
        menu.findItem(R.id.filter_icon).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getActivity();
        provider = (DbProvider)getActivity();
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

    private void writeToDatabase(){
        int newestListNum = getNewListNum();
        ContentValues newListing = new ContentValues();
        newListing.put(Listings.USER_ID, mUID);
        newListing.put(Listings.LIST_NUM, newestListNum);
        newListing.put(Listings.TITLE, mTitle.getText().toString());
        newListing.put(Listings.DESCRIPTION, mDescription.getText().toString());
        newListing.put(Listings.PRICE, Double.parseDouble
                (mPrice.getText().toString().replaceAll("[^\\d.]", "")));
        newListing.put(Listings.CATEGORY, mCatSpinner.getSelectedItem().toString());
        newListing.put(Listings.CONDITION, mConSpinner.getSelectedItem().toString());
        newListing.put(Listings.FINISHED, 0);
        newListing.put(Listings.SCHOOL, mSchool);
        long id = mWritableDatabase.insert(Listings.TABLE_NAME, null, newListing);
    }

    private int getNewListNum(){
        String[] columns = {Listings.USER_ID, Listings.LIST_NUM};
        String selection = Listings.USER_ID + " = " + mUID;
        Cursor numCursor = mReadableDatabase.query(Listings.TABLE_NAME, columns, selection,
                null, null, null, null);
        int newNum = 0;
        if(numCursor.getCount() > 0){
            numCursor.moveToLast();
            newNum = numCursor.getInt(1) + 1;
        }
        numCursor.close();
        return newNum;
    }


    //THESE STILL DON"T WORK
    private boolean checkFields(){
        if(mTitle.getText().toString().equals("")){
            Toast.makeText(mContext, "Title is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mDescription.getText().toString().equals("")){
            Toast.makeText(mContext, "Description is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mCatSpinner.getSelectedItem().toString().equals
                (getResources().getString(R.string.filter_string_none))){
            Toast.makeText(mContext, "Category Needed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mConSpinner.getSelectedItem().toString().equals("CHOOSE")){
            Toast.makeText(mContext, "Condition Needed", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
