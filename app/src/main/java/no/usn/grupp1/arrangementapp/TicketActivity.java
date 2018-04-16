package no.usn.grupp1.arrangementapp;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
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

public class TicketActivity extends AppCompatActivity {
    int maxSeter = 196, seterOpptatt;
    String title;
    int eventID;
    TextView antLedigBillett,tittel, description;
    Button reserverBtn;
    NumberPicker np;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        eventID = getIntent().getIntExtra("eventID", -1);
        title = getIntent().getStringExtra("title");

         np = findViewById(R.id.ticketArrPicker);

        np.setMinValue(1);
        np.setMaxValue(20);

        np.setOnValueChangedListener(onValueChangeListener);

        getSeatsTaken();
        antLedigBillett = findViewById(R.id.antLedige);
        tittel = findViewById(R.id.ticketArrTittel);
        tittel.setText(title);
        description = findViewById(R.id.arrDescription);
        reserverBtn = findViewById(R.id.ticketArrBtn);



    }
    NumberPicker.OnValueChangeListener onValueChangeListener =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                }
            };


    private void getSeatsTaken() {


        String events = getString(R.string.endpoint) + "/ticket?filter=EventID,eq,"+ eventID +"&transform=1";

        if (isOnline()) {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest JSONRequest = new JsonObjectRequest(Request.Method.GET, events, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray events = response.getJSONArray("ticket");


                        seterOpptatt = events.length();
                        antLedigBillett.setText("Antall ledige billetter: " + (maxSeter-seterOpptatt));
                        String bla = "" + eventID;
                        Log.d("shiit", bla);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast toast = Toast.makeText(getApplicationContext(), "Seats Taken" +e.toString(), Toast.LENGTH_LONG);
                        toast.show();
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
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
