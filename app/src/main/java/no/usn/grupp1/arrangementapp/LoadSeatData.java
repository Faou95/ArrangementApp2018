package no.usn.grupp1.arrangementapp;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;

public class LoadSeatData extends AppCompatActivity {

    int[] seatId=new int[20];
    List<Integer> sjekk=new ArrayList<>();;
    int eventID;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_seat_data);

        eventID = getIntent().getIntExtra("eventID", 0);
        title = getIntent().getStringExtra("title");
        seatData sd = new seatData();
        sd.execute((Void)null);

    }
    public class seatData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            initializeData();
            return null;
        }
    }

    private void initializeData() {


        String seatURL = getString(R.string.endpoint)+"/ticket?filter=EventID,eq," + eventID + "&transform=1";

        if (isOnline()){
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest JSONRequest = new JsonObjectRequest(Request.Method.GET, seatURL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray ticketsArray = response.getJSONArray("ticket");
                        //Log.d("JsonArray", response.toString());

                        for(int i = 0; i < ticketsArray.length(); i++){
                            JSONObject event = ticketsArray.getJSONObject(i);
                            int SeatID = event.getInt("SeatID");
                            String id = "denne" + seatId[i];
                            Log.d("BLIRDEENNEGJORT??: ", id);
                            sjekk.add(SeatID);
                            for (int j= 0; j<seatId.length; j++){

                                if(sjekk.contains(j)){
                                    seatId[j-1]=-1;
                                    seatId[j]=1;



                                }else{
                                    seatId[j]=1;
                                }}




                        }
                        /*for (int j= 0; j<seatId.length; j++){
                            String id = "" + seatId[j];
                            Log.d("Setenr: ", id);
                        }*/


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast toast = Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG);
                        toast.show();
                    }

                    newIntent();


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
    private void newIntent(){
        Intent intent = new Intent(getApplicationContext(),SeatSelectionActivity.class);
        intent.putExtra("OpptatteSeter", seatId);
        intent.putExtra("title", title);
        intent.putExtra("eventID", eventID);
        startActivity(intent);
    }
}
