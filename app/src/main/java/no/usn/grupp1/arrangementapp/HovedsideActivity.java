package no.usn.grupp1.arrangementapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;


import java.util.ArrayList;

public class HovedsideActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Arrangement> mArrData;
    private ArrAdapter ArrAdapter;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hovedside);
        session = new SessionManager(getApplicationContext());

        //Initialize the RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);

        //Set the Layout Manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize the ArrayLIst that will contain the data
        mArrData = new ArrayList<>();

        //Initialize the adapter and set it ot the RecyclerView
        ArrAdapter = new ArrAdapter(this, mArrData);
        mRecyclerView.setAdapter(ArrAdapter);


    initializeData();
    }

    private void initializeData() {
        //Get the resources from the XML file
        String[] arrListe = getResources().getStringArray(R.array.arr_tittel);
        String[] arrInfo = getResources().getStringArray(R.array.arr_info);
        TypedArray arrImageResources =
                getResources().obtainTypedArray(R.array.arr_images);


        //Clear the existing data (to avoid duplication)
        mArrData.clear();

        //Create the ArrayList of Sports objects with the titles and information about each sport
        for(int i=0;i<arrListe.length;i++){
            mArrData.add(new Arrangement(arrListe[i],arrInfo[i], arrImageResources.getResourceId(i,0)));
        }

        arrImageResources.recycle();

        //Notify the adapter of the change
        ArrAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(session.isLoggedIn()){
            getMenuInflater().inflate(R.menu.logedinmenu, menu);
        }else{
            getMenuInflater().inflate(R.menu.hovedmenu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                return true;
            case R.id.userInfo:
                Intent userIntent = new Intent(this, UserProfileActivity.class);
                startActivity(userIntent);
                return true;
            case R.id.logout:
                session.logoutUser();
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}
