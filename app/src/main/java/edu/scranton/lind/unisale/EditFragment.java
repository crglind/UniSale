package edu.scranton.lind.unisale;

import android.content.ContentValues;
import android.content.Context;
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


public class EditFragment extends Fragment {

    public interface ListingProvider{Listing getListing();}
    public interface DbProvider{SQLiteDatabase getWritableDb();}

    private DbProvider dbProvider;
    private ListingProvider lProvider;
    private SQLiteDatabase mWritableDatabase;
    private Context mContext;
    private Listing mListing;
    private EditText mTitle;
    private EditText mDescription;
    private Spinner mConSpinner;
    private Spinner mCatSpinner;
    private TextView mPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mListing = lProvider.getListing();
        mWritableDatabase = dbProvider.getWritableDb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        NumberFormat cashFormat = NumberFormat.getCurrencyInstance();
        mPrice = (TextView)view.findViewById(R.id.new_list_price);
        mPrice.setText(cashFormat.format(mListing.getPrice()));
        mTitle = (EditText)view.findViewById(R.id.new_list_title);
        mTitle.setText(mListing.getListTitle());
        mDescription = (EditText)view.findViewById(R.id.new_list_description);
        mDescription.setText(mListing.getDescription());
        if(getActivity().findViewById(R.id.small_container) != null){
            Button changePrice = (Button)view.findViewById(R.id.price_button);
            changePrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
        String[] conds = getResources().getStringArray(R.array.conditions_array);
        mConSpinner.setSelection(indexOf(conds, mListing.getCondition()));
        mCatSpinner = (Spinner)view.findViewById(R.id.new_list_category_spinner);
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mCatSpinner.setAdapter(catAdapter);
        String[] cats = getResources().getStringArray(R.array.category_array);
        mCatSpinner.setSelection(indexOf(cats, mListing.getCategory()));
        Button editButton = (Button)view.findViewById(R.id.edit_listing_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields() == true){
                    startAsyncTask();
                    /*writeToDatabase();
                    getActivity().onBackPressed();
                    Toast.makeText(mContext,
                            mTitle.getText().toString() + " - EDITED!", Toast.LENGTH_SHORT).show();*/
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
        lProvider = (ListingProvider)getActivity();
        dbProvider = (DbProvider)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lProvider = null;
        dbProvider = null;
    }

    public void success(){
        getActivity().onBackPressed();
        Toast.makeText(mContext, "Post Edited!", Toast.LENGTH_SHORT).show();
    }

    public void failureToUpdate(){
        Toast.makeText(mContext, "Failed to Update", Toast.LENGTH_SHORT).show();
    }

    public void failedToConnect(){
        Toast.makeText(mContext, "Network Failure: Failed to Connect", Toast.LENGTH_SHORT).show();
    }

    private void startAsyncTask(){
        EditAsyncTask ea = new EditAsyncTask(this);
        ea.execute(mListing);
    }

    private void writeToDatabase(){
        ContentValues updateListing = new ContentValues();
        updateListing.put(Listings.TITLE, mTitle.getText().toString());
        updateListing.put(Listings.DESCRIPTION, mDescription.getText().toString());
        updateListing.put(Listings.PRICE, Double.parseDouble
                (mPrice.getText().toString().replaceAll("[^\\d.]", "")));
        updateListing.put(Listings.CATEGORY, mCatSpinner.getSelectedItem().toString());
        updateListing.put(Listings.CONDITION, mConSpinner.getSelectedItem().toString());
        updateListing.put(Listings.FINISHED, 0);
        updateListing.put(Listings.SCHOOL, mListing.getSchoolID());
        String selectClause = Listings.USER_ID + " = " + mListing.getUID() + " AND " +
                Listings.LIST_NUM + " = " + mListing.getListNum();
        long id = mWritableDatabase.update(Listings.TABLE_NAME, updateListing, selectClause, null);
    }

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
                (getResources().getString(R.string.string_choose))){
            Toast.makeText(mContext, "Category Needed", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mConSpinner.getSelectedItem().toString().equals
                (getResources().getString(R.string.string_choose))){
            Toast.makeText(mContext, "Condition Needed", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

    private int indexOf(String[] array, String string){
        int result = -1;
        for(int i=0; i<array.length; i++){
            if(array[i].equals(string)){
                result = i;
                break;
            }
        }
        return result;
    }
}
