package edu.scranton.lind.unisale;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import edu.scranton.lind.unisale.database_schema.UnisaleDbOpenHelper;

public class HomeScreen extends AppCompatActivity
    implements ListingFragment.OnListingSelectedListener,
    NavigationView.OnNavigationItemSelectedListener,
    ListingFragment.DbProvider,
    MyListingsFragment.DbProvider{

    private SQLiteDatabase mReadableDb;
    private SQLiteDatabase mWritableDb;
    private RetainedDatabase mRetainedDatabase;
    private String mListingTag = "LISTING";
    private String mMessageSelectTag = "MSGSELECT";
    private String mNewListingTag = "NEWLISTING";
    private String mMyListingTag = "MYLISTING";
    private int mUID;
    private int mSchoolID;
    private String mHomeTitle = "Home";
    private String mMessageTitle = "Message";
    private String mNewListingTitle = "New Listing";
    private String mMyListingTitle = "My Listings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mHomeTitle);
        Intent inputIntent = getIntent();
        if(inputIntent.hasExtra(UniSale_main.ARG_USER_ID_MAIN)){
            mUID = inputIntent.getIntExtra(UniSale_main.ARG_USER_ID_MAIN, -1);
            mSchoolID = inputIntent.getIntExtra(UniSale_main.ARG_SCHOOL_ID, -1);
        }
        else {
            mUID = inputIntent.getIntExtra(NewUserActivity.ARG_USER_ID_NEW, -1);
            mSchoolID = inputIntent.getIntExtra(NewUserActivity.ARG_SCHOOL_ID, -1);
        }
        if(mUID == -1 || mSchoolID == -1) Toast.makeText(HomeScreen.this, "ERROR", Toast.LENGTH_SHORT).show();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction rft = getSupportFragmentManager().beginTransaction();
        mRetainedDatabase = (RetainedDatabase) fm.findFragmentByTag("retained_database");
        if(mRetainedDatabase == null) {
            UnisaleDbOpenHelper dbHelper = new UnisaleDbOpenHelper(this);
            mReadableDb = dbHelper.getReadableDatabase();
            mWritableDb = dbHelper.getWritableDatabase();
            mRetainedDatabase = new RetainedDatabase();
            rft.add(mRetainedDatabase, "retained_database").commit();
            mRetainedDatabase.setDatabase(mReadableDb, mWritableDb);
        }
        mReadableDb = mRetainedDatabase.getReadableDatabaseFromRetained();
        mWritableDb = mRetainedDatabase.getWritableDatabaseFromRetained();
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
                //Toast.makeText(HomeScreen.this, "Messages", Toast.LENGTH_SHORT).show();
                if(findViewById(R.id.small_container) != null){
                    getSupportActionBar().setTitle(mMessageTitle);
                    MessageSelectionFragment msgSelect = new MessageSelectionFragment();
                    ft.replace(R.id.small_container, msgSelect, mMessageSelectTag);
                    ft.addToBackStack(mMessageSelectTag);
                    ft.commit();
                }
                else{
                    getSupportActionBar().setTitle(mMessageTitle);
                    MessageSelectionFragment msgSelect = new MessageSelectionFragment();
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

    public void onListingChosen(int position, int listingId, int listNum, String title){
        Bundle args = new Bundle();
        args.putInt(InterestedDialog.ARG_ID, listingId);
        args.putInt(InterestedDialog.ARG_LISTNUM, listNum);
        args.putString(InterestedDialog.ARG_TITLE, title);
        InterestedDialog dialog = new InterestedDialog();
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "interested_shown");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (item.getItemId()){
            case R.id.my_listings:
                Bundle args = new Bundle();
                args.putInt(MyListingsFragment.USER, mUID);
                MyListingsFragment myList = new MyListingsFragment();
                myList.setArguments(args);
                getSupportActionBar().setTitle(mMyListingTitle);
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
                break;
            case R.id.new_listing:
                NewListing newList = new NewListing();
                getSupportActionBar().setTitle(mNewListingTitle);
                if (findViewById(R.id.small_container) != null){
                    ft.replace(R.id.small_container, newList, mNewListingTag);
                    ft.addToBackStack(mListingTag);
                    ft.commit();
                }
                else{
                    ft.replace(R.id.large_container_normal, newList, mNewListingTag);
                    ft.addToBackStack(mListingTag);
                    ft.commit();
                }
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
            int fragCount = getSupportFragmentManager().getBackStackEntryCount();
            if (fragCount != 0) {
                String lastFrag =
                        getSupportFragmentManager().getBackStackEntryAt(fragCount - 1).getName();
                if (lastFrag.equals(mMessageSelectTag)) {
                    getSupportActionBar().setTitle(mHomeTitle);
                } else if (lastFrag.equals(mNewListingTag)) {
                    getSupportActionBar().setTitle(mHomeTitle);
                }
            }
            ListingFragment homeFrag =
                    (ListingFragment) getSupportFragmentManager().findFragmentByTag(mListingTag);
            if (homeFrag.isVisible()) {
                getSupportActionBar().setTitle(mHomeTitle);
            }
        }
    }

    public SQLiteDatabase getReadableDb() {
        return mReadableDb;
    }

}
