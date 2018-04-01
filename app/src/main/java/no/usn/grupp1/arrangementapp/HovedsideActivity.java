package no.usn.grupp1.arrangementapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.os.AsyncTask;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Add pager behaviour
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

        // pager indicator
        //mRecyclerView.addItemDecoration(new LinePagerIndicatorDecoration());

        //Initialize the ArrayLIst that will contain the data
        mArrData = new ArrayList<>();

        //Initialize the adapter and set it ot the RecyclerView
        ArrAdapter = new ArrAdapter(this, mArrData);
        mRecyclerView.setAdapter(ArrAdapter);

        initData d = new initData();
        d.execute((Void) null);
    }

    public class initData extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            initializeData();
            return null;
        }
    }

    private void initializeData() {
        // IF WE NEED TO CHECK LANDSCAPE OR PORTRAIT
        /*
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

        }*/

        // OLD SHIT
        /*
        //Get the resources from the XML file
        String[] arrListe = getResources().getStringArray(R.array.arr_tittel);
        String[] arrInfo = getResources().getStringArray(R.array.arr_info);

        */

        String events = getString(R.string.endpoint)+"/event";

        if (isOnline()){
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest JSONRequest = new JsonObjectRequest(Request.Method.GET, events, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject events = response.getJSONObject("event");
                        JSONArray eventsArray = events.getJSONArray("records");

                        //Clear the existing data (to avoid duplication)
                        mArrData.clear();

                        for(int i = 0; i < eventsArray.length(); i++){
                            JSONArray event = eventsArray.getJSONArray(i);
                            int EventID = event.getInt(0);
                            String Name = event.getString(1);
                            String Date = event.getString(2);
                            String Time = event.getString(3);
                            String Comment = event.getString(4);
                            String Description = event.getString(5);
                            String Producer = event.getString(6);
                            String Age = event.getString(7);
                            int Fee = event.getInt(8);
                            int Active = event.getInt(9);
                            mArrData.add(new Arrangement(Name ,Comment, Date, Time, Age, Fee, i));
                        }

                        //Notify the adapter of the change
                        ArrAdapter.notifyDataSetChanged();

                        /*
                        if (tmp.getString("Password").equals(mPassword)){
                            session.createLoginSession(tmp.getString("Username"), tmp.getString("Email"));
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }
                        */

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast toast = Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG);
                        toast.show();
                    }



                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast toast = Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG);
                    toast.show();
                }
            });
            queue.add(JSONRequest);
        }

        // OLD SHIT
        /*
        //Create the ArrayList of Sports objects with the titles and information about each sport
        for(int i=0;i<arrListe.length;i++){
            mArrData.add(new Arrangement(arrListe[i],arrInfo[i], i));
        }
        */


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

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}