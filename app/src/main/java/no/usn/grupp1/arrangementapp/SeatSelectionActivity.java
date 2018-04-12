package no.usn.grupp1.arrangementapp;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SeatSelectionActivity extends AppCompatActivity {

    GridView grid;
    int[] seatId = new int[20];
    List<Integer> sjekk = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeData();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        SeatAdapter adapter = new SeatAdapter(seatId,SeatSelectionActivity.this);
        grid = findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast = Toast.makeText(getApplicationContext(),"Sete er valgt",Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }
    private void initializeData() {


        String seatURL = getString(R.string.endpoint)+"/ticket?filter=EventID,eq,1&transform=1";

        if (isOnline()){
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest JSONRequest = new JsonObjectRequest(Request.Method.GET, seatURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray ticketsArray = response.getJSONArray("ticket");

                        for(int i = 0; i < ticketsArray.length(); i++){
                            JSONObject event = ticketsArray.getJSONObject(i);
                            int SeatID = event.getInt("SeatID");
                            sjekk.add(SeatID);
                            /*String seat = "" + SeatID;
                            seatId[(SeatID-1)]= R.drawable.opptattsete;
                            Toast toast = Toast.makeText(getApplicationContext(),seat,Toast.LENGTH_LONG);
                            toast.show();
                            Log.d("Sete", seat);*/
                            for (int j= 0; j<seatId.length; j++){

                                if(sjekk.contains(j)){
                                    seatId[j-1]=R.drawable.opptattsete;


                                }else{
                                    seatId[j]=R.drawable.ledigsete;
                                }}
                        }


                        //Notify the adapter of the change
                        //SeatAdapter.notifyDataSetChanged();


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


    }
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
