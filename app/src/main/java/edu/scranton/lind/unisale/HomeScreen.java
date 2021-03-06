package edu.scranton.lind.unisale;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

import edu.scranton.lind.unisale.database_schema.UnisaleDbContract;
import edu.scranton.lind.unisale.database_schema.UnisaleDbOpenHelper;

public class HomeScreen extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,
    ListingFragment.DbProvider,
    MyListingsFragment.DbProvider,
    NewListing.DbProvider,
    CompletedListings.DbProvider,
    EditFragment.DbProvider,
    EditFragment.ListingProvider,
    ChangePriceFragment.ListingProvider,
    MessageSelectionFragment.OnMessageSelected {

    private int mUID;
    private int mSchoolID;
    private String mUsername;
    private Listing mEditListing;

    private SQLiteDatabase mReadableDb;
    private SQLiteDatabase mWritableDb;
    private RetainedDatabase mRetainedDatabase;

    private String mCurrentTitle;
    private Stack<String> mTitleList;
    private RetainedTitles mRetainedTitles;

    private String mListingTag = "LISTING";
    private String mMessageSelectTag = "MSGSELECT";
    private String mNewListingTag = "NEWLISTING";
    private String mMyListingTag = "MYLISTING";
    private String mCompletedTag = "COMPLETED";
    private String mEditListingTag = "EDIT";
    private String mPriceTag = "PRICE";
    private String mHomeTitle = "Home";
    private String mMessageTitle = "Message";
    private String mNewListingTitle = "New Listing";
    private String mMyListingTitle = "My Listings";
    private String mCompletedTitle = "My Completed Listings";
    private String mEditListingTitle = "Edit Listing";
    private String mPriceTitle = "Change Price";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupRetainedTitles();
        getIntentArgs();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        View header = navView.getHeaderView(0);
        TextView usernameInNav = (TextView)header.findViewById(R.id.username_nav);
        usernameInNav.setText(mUsername);
        setupRetainedDatabase();
        ListingFragment listing = new ListingFragment();
        Bundle args = new Bundle();
        args.putInt(ListingFragment.USER, mUID);
        args.putInt(ListingFragment.SCHOOL, mSchoolID);
        listing.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (findViewById(R.id.small_container) != null){
            if (savedInstanceState != null) {
                return;
            }
            ft.replace(R.id.small_container, listing, mListingTag);
            ft.commit();
        }
        else{
            if (savedInstanceState != null) {
                return;
            }
            ft.replace(R.id.large_container_normal, listing, mListingTag);
            ft.commit();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mRetainedDatabase.setDatabase(mReadableDb, mWritableDb);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_uni_sale, menu);
        menu.findItem(R.id.messages_icon).setVisible(true);
        //menu.findItem(R.id.search_icon).setVisible(true);
        menu.findItem(R.id.filter_icon).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (menuItem.getItemId()){
            case R.id.messages_icon:
                mTitleList.push(mCurrentTitle);
                mRetainedTitles.setTitlesStack(mTitleList);
                getSupportActionBar().setTitle(mMessageTitle);
                mCurrentTitle = mMessageTitle;
                mRetainedTitles.setCurrentTitle(mCurrentTitle);
                MessageSelectionFragment msgSelect = new MessageSelectionFragment();
                Bundle args = new Bundle();
                args.putInt(MessageSelectionFragment.ARG_ID, mUID);
                msgSelect.setArguments(args);
                if(findViewById(R.id.small_container) != null){
                    ft.replace(R.id.small_container, msgSelect, mMessageSelectTag);
                    ft.addToBackStack(mMessageSelectTag);
                    ft.commit();
                }
                else{
                    ft.replace(R.id.large_container_normal, msgSelect, mMessageSelectTag);
                    ft.addToBackStack(mMessageSelectTag);
                    ft.commit();
                }
                break;
            case R.id.filter_icon:
                if(findViewById(R.id.small_container) != null){
                    FiltersDialog dialog = new FiltersDialog();
                    dialog.show(getFragmentManager(), "filter_active");
                }
                else{
                    FiltersDialog dialog = new FiltersDialog();
                    dialog.show(getFragmentManager(), "filter_active");
                }
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mTitleList.push(mCurrentTitle);
        mRetainedTitles.setTitlesStack(mTitleList);
        switch (item.getItemId()){
            case R.id.my_listings:
                if(mCurrentTitle == mMyListingTitle){
                    mTitleList.pop();
                    mRetainedTitles.setTitlesStack(mTitleList);
                    break;
                }
                Bundle myListArgs = new Bundle();
                myListArgs.putInt(MyListingsFragment.USER, mUID);
                MyListingsFragment myList = new MyListingsFragment();
                myList.setArguments(myListArgs);
                getSupportActionBar().setTitle(mMyListingTitle);
                mCurrentTitle = mMyListingTitle;
                if(findViewById(R.id.small_container) != null){
                    ft.replace(R.id.small_container, myList, mMyListingTag);
                    ft.addToBackStack(mMyListingTag);
                    ft.commit();
                }
                else{
                    ft.replace(R.id.large_container_normal, myList, mMyListingTag);
                    ft.addToBackStack(mMyListingTag);
                    ft.commit();
                }
                break;
            case R.id.completed_listings:
                if(mCurrentTitle == mCompletedTitle){
                    mTitleList.pop();
                    mRetainedTitles.setTitlesStack(mTitleList);
                    break;
                }
                CompletedListings comList = new CompletedListings();
                Bundle comListArgs = new Bundle();
                comListArgs.putInt(CompletedListings.USER, mUID);
                comList.setArguments(comListArgs);
                getSupportActionBar().setTitle(mCompletedTitle);
                mCurrentTitle = mCompletedTitle;
                if (findViewById(R.id.small_container) != null){
                    ft.replace(R.id.small_container, comList, mCompletedTag);
                    ft.addToBackStack(mCompletedTag);
                    ft.commit();
                }
                else{
                    ft.replace(R.id.large_container_normal, comList, mCompletedTag);
                    ft.addToBackStack(mCompletedTag);
                    ft.commit();
                }
                break;
            case R.id.new_listing:
                if(mCurrentTitle == mNewListingTitle){
                    mTitleList.pop();
                    mRetainedTitles.setTitlesStack(mTitleList);
                    break;
                }
                NewListing newList = new NewListing();
                Bundle newListArgs = new Bundle();
                newListArgs.putInt(NewListing.USER, mUID);
                newListArgs.putInt(NewListing.SCHOOL, mSchoolID);
                newList.setArguments(newListArgs);
                getSupportActionBar().setTitle(mNewListingTitle);
                mCurrentTitle = mNewListingTitle;
                if (findViewById(R.id.small_container) != null){
                    ft.replace(R.id.small_container, newList, mNewListingTag);
                    ft.addToBackStack(mNewListingTag);
                    ft.commit();
                }
                else{
                    ft.replace(R.id.large_container_normal, newList, mNewListingTag);
                    ft.addToBackStack(mNewListingTag);
                    ft.commit();
                }
                break;
        }
        mRetainedTitles.setCurrentTitle(mCurrentTitle);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMessageSelected(Integer[] info, String otherUsername){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ConversationFragment conv = new ConversationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConversationFragment.ARG_USER, info[0].intValue());
        bundle.putInt(ConversationFragment.ARG_OWNER, info[1].intValue());
        bundle.putInt(ConversationFragment.ARG_LIST, info[2].intValue());
        bundle.putString(ConversationFragment.ARG_NAME, otherUsername);
        bundle.putString(ConversationFragment.ARG_MY_NAME, mUsername);
        bundle.putInt(ConversationFragment.ARG_MY_ID, mUID);
        conv.setArguments(bundle);
        mTitleList.push(mCurrentTitle);
        mRetainedTitles.setTitlesStack(mTitleList);
        getSupportActionBar().setTitle(mMessageTitle);
        mCurrentTitle = mMessageTitle;
        mRetainedTitles.setCurrentTitle(mCurrentTitle);
        if(findViewById(R.id.small_container) != null){
            ft.replace(R.id.small_container, conv, mMessageSelectTag);
            ft.addToBackStack(mMessageSelectTag);
            ft.commit();
        }
        else{
            ft.replace(R.id.large_container_normal, conv, mMessageSelectTag);
            ft.addToBackStack(mMessageSelectTag);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
            if(!mTitleList.isEmpty()){
                String oldTitle = mTitleList.pop();
                getSupportActionBar().setTitle(oldTitle);
                mCurrentTitle = oldTitle;
            }
            ListingFragment homeFrag =
                    (ListingFragment) getSupportFragmentManager().findFragmentByTag(mListingTag);
            if (homeFrag.isVisible()) {
                getSupportActionBar().setTitle(mHomeTitle);
            }
            mRetainedTitles.setTitlesStack(mTitleList);
            mRetainedTitles.setCurrentTitle(mCurrentTitle);
        }
    }

    public void changePriceClicked(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mTitleList.push(mCurrentTitle);
        mRetainedTitles.setTitlesStack(mTitleList);
        mCurrentTitle = mPriceTitle;
        mRetainedTitles.setCurrentTitle(mCurrentTitle);
        ChangePriceFragment frag = new ChangePriceFragment();
        ft.replace(R.id.small_container, frag, mPriceTag);
        ft.addToBackStack(mPriceTitle);
        ft.commit();
    }

    public void changePriceDoneClicked(Listing listing, double price){
        onBackPressed();
        onBackPressed();
        showEditFrag(listing, price);
    }

    public void showEditFrag(Listing listing, double price){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        listing.setPrice(price);
        mEditListing = listing;
        EditFragment frag = new EditFragment();
        mTitleList.push(mCurrentTitle);
        mRetainedTitles.setTitlesStack(mTitleList);
        getSupportActionBar().setTitle(mEditListingTitle);
        mCurrentTitle = mEditListingTitle;
        mRetainedTitles.setCurrentTitle(mCurrentTitle);
        if (findViewById(R.id.small_container) != null){
            ft.replace(R.id.small_container, frag, mEditListingTag);
            ft.addToBackStack(mEditListingTag);
            ft.commit();
        }
        else{
            ft.replace(R.id.large_container_normal, frag, mEditListingTag);
            ft.addToBackStack(mEditListingTag);
            ft.commit();
        }

    }

    //Implemented functions to pass data to fragments
    public Listing getListing(){return mEditListing;}
    public SQLiteDatabase getReadableDb() {return mReadableDb;}
    public SQLiteDatabase getWritableDb() {return mWritableDb;}

    //Private Functions called in on create
    private void setupRetainedTitles(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mRetainedTitles = (RetainedTitles) fm.findFragmentByTag("retained_titles");
        if(mRetainedTitles == null){
            getSupportActionBar().setTitle(mHomeTitle);
            mCurrentTitle = mHomeTitle;
            mTitleList = new Stack<>();
            mRetainedTitles = new RetainedTitles();
            ft.add(mRetainedTitles, "retained_titles").commit();
            mRetainedTitles.setTitlesStack(mTitleList);
            mRetainedTitles.setCurrentTitle(mCurrentTitle);
        }
        mTitleList = (Stack)mRetainedTitles.getTitlesStack().clone();
        mCurrentTitle = mRetainedTitles.getCurrentTitle();
        getSupportActionBar().setTitle(mCurrentTitle);
    }

    private void getIntentArgs(){
        Intent inputIntent = getIntent();
        if(inputIntent.hasExtra(UniSale_main.ARG_USER_ID_MAIN)){
            mUID = inputIntent.getIntExtra(UniSale_main.ARG_USER_ID_MAIN, -1);
            mSchoolID = inputIntent.getIntExtra(UniSale_main.ARG_SCHOOL_ID, -1);
            mUsername = inputIntent.getStringExtra(UniSale_main.ARG_USERNAME);
        }
        else {
            mUID = inputIntent.getIntExtra(NewUserActivity.ARG_USER_ID_NEW, -1);
            mSchoolID = inputIntent.getIntExtra(NewUserActivity.ARG_SCHOOL_ID, -1);
            mUsername = inputIntent.getStringExtra(NewUserActivity.ARG_USERNAME);
        }
        if(mUID == -1 || mSchoolID == -1)
            Toast.makeText(HomeScreen.this, "ERROR", Toast.LENGTH_SHORT).show();
    }

    private void setupRetainedDatabase(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mRetainedDatabase = (RetainedDatabase) fm.findFragmentByTag("retained_database");
        if(mRetainedDatabase == null) {
            UnisaleDbOpenHelper dbHelper = new UnisaleDbOpenHelper(this);
            mReadableDb = dbHelper.getReadableDatabase();
            mWritableDb = dbHelper.getWritableDatabase();
            mRetainedDatabase = new RetainedDatabase();
            ft.add(mRetainedDatabase, "retained_database").commit();
            mRetainedDatabase.setDatabase(mReadableDb, mWritableDb);
        }
        mReadableDb = mRetainedDatabase.getReadableDatabaseFromRetained();
        mWritableDb = mRetainedDatabase.getWritableDatabaseFromRetained();
    }
}
