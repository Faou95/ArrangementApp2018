package no.usn.grupp1.arrangementapp;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class SeatSelectionActivity extends AppCompatActivity {

    private SessionManager session;
    GridView grid;
    int[] seatId;
    List<Integer> sjekk = new ArrayList<>();
    String eventID;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);
        eventID = getIntent().getStringExtra("eventID");
        title = getIntent().getStringExtra("title");
        seatId = getIntent().getIntArrayExtra("OpptatteSeter");
        session = new SessionManager(getApplicationContext());
        //initializeData();




        /*Toast toast = Toast.makeText(getApplicationContext(),title.toString(),Toast.LENGTH_LONG);
        toast.show();*/

        SeatAdapter adapter = new SeatAdapter(seatId,SeatSelectionActivity.this);
        grid = findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast = Toast.makeText(getApplicationContext(),"Sete er valgt",Toast.LENGTH_LONG);
                toast.show();

                if(session.isLoggedIn()){
                    // ledig sete
                    if(seatId[position] == 1){
                        ImageView v = view.findViewById(R.id.seatImage);
                        Glide.with(getApplicationContext()).load(R.drawable.valgtsete).into(v);
                        velgSete((int) id, position);
                    }
                    // opptatt sete
                    else{
                        Log.d("SETE", "OPPTATT");
                    }
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }


            }
        });
        ImageView im = findViewById(R.id.sceneBilde);
        Glide.with(this).load(R.drawable.scene).into(im);

    }

    public void velgSete(int eventID, int seatID) {
        // Får tak i bruker ID fra session manager
        HashMap<String, String> user = session.getUserDetails();
        int id =  Integer.parseInt(user.get(SessionManager.KEY_ID));

        // lager et nytt ticket object
        JSONObject nyTicket = new JSONObject();
        try {
            nyTicket.put("EventID", Integer.toString(eventID+1));
            nyTicket.put("SeatID", Integer.toString(seatID+1));
            nyTicket.put("UserID", user.get(SessionManager.KEY_ID));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("TICKET", nyTicket.toString());

        RequestQueue queue = newRequestQueue(getApplicationContext());

        String ticket_URL =  getString(R.string.endpoint) + "/ticket";

        if(isOnline()){
            JsonObjectRequest JSONRequest = new JsonObjectRequest(Request.Method.POST, ticket_URL, nyTicket, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            queue.add(JSONRequest);
        }
    }

    private void initializeData() {


        String seatURL = getString(R.string.endpoint)+"/ticket?filter=EventID,eq,"+ eventID +"&transform=1";

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
                            for (int j= 0; j<seatId.length; j++){

                                if(sjekk.contains(j)){
                                    seatId[j-1]=R.drawable.opptattsete;
                                    seatId[j]=R.drawable.ledigsete;


                                }else{
                                    seatId[j]=R.drawable.ledigsete;
                                }}
                        }

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
