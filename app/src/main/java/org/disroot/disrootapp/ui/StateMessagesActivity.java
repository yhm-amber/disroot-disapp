package org.disroot.disrootapp.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.disroot.disrootapp.R;
import org.disroot.disrootapp.utils.HttpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StateMessagesActivity extends AppCompatActivity {

    Button button;
    private final String TAG = StateMessagesActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get data JSON
    static String incidentUrl0 ="https://status.disroot.org/issues/index.json";

    ArrayList<HashMap<String, String>> messageList;
    ArrayList<HashMap<String, String>> getDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_messages);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener( v -> onBackPressed() );

        button = findViewById(R.id.StateBtn);//StateBtn
        button.setOnClickListener( arg0 -> {
            Intent goState = new Intent(StateMessagesActivity.this, StateActivity.class);
            StateMessagesActivity.this.startActivity(goState);
        } );

        messageList = new ArrayList<>();
        getDate = new ArrayList<>();
        lv = findViewById(R.id.list);
        new GetList().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent goHome = new Intent(StateMessagesActivity.this, MainActivity.class);
            StateMessagesActivity.this.startActivity(goHome);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    @SuppressLint("StaticFieldLeak")
    class GetList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(StateMessagesActivity.this);
            pDialog.setMessage("Loading…");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStringIdents0 = sh.makeServiceCall( incidentUrl0 );

            Log.e(TAG, "Response from url: " + incidentUrl0 );

            if (jsonStringIdents0 != null) {//Incidents page
                try {
                    JSONObject jsonObj = new JSONObject( jsonStringIdents0 );

                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("pages");

                    // looping through All data
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        String title = c.getString("title");
                        String link = c.getString("permalink");
                        boolean resolved = c.getBoolean( "resolved" );
                        boolean informational = c.getBoolean( "informational" );
                        String lastMod = c.getString("lastMod");
                        String lastUpdated = "Last Updated: " + lastMod + '"';

                        // tmp hash map for single service
                        HashMap<String, String> service = new HashMap<>();

                        // adding each child node to HashMap key => value
                        service.put("title", title);
                        service.put("moreInfo", link);
                        if (informational == true) service.put("resolved", "informational" );
                        else service.put("resolved", Boolean.toString( resolved ) );
                        service.put("lastMod", lastUpdated);

                        // adding service to service list
                        messageList.add(service);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread( () -> Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG)
                            .show() );
                }
            }else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread( () -> Toast.makeText(getApplicationContext(),
                        "Couldn't get json from server. Is your internet connection ok?",
                        Toast.LENGTH_LONG)
                        .show() );
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

             //Updating parsed JSON data into ListView
            ListAdapter adapter = new SimpleAdapter(
                    StateMessagesActivity.this, messageList,
                    R.layout.list_service_messages, new String[]{"title","moreInfo", "lastMod", "resolved", "status"}, new int[]{R.id.name, R.id.description,
                    R.id.category, R.id.resolved, R.id.status})
            {


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);



                    //Make links work
                    TextView link = v.findViewById( R.id.description );
                    String linkValue = link.getText().toString();
                    link.setText( R.string.more_info);
                    v.setOnClickListener( arg0 -> {
                        Uri uri = Uri.parse( linkValue);
                        Intent statusLink = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                        startActivity(statusLink);
                    } );

                    //Make Last updated translatable
                    TextView updated = v.findViewById(R.id.category );
                    String updatedValue = updated.getText().toString();
                        if (updatedValue.startsWith("Last Updated: ")){
                            updated.setText(updatedValue.replace("Last Updated: ",getText(R.string.LastUpdated)));
                        }
                    //Make Scheduled at translatable
                    TextView resolved = v.findViewById(R.id.resolved );
                    String resolvedValue = resolved.getText().toString();
                    //Human_status
                    TextView status = v.findViewById(R.id.status );
                    Log.e("status", "status: "+resolvedValue);
                    switch (resolvedValue) {
                        case "true":
                            status.setTextColor(Color.GREEN);
                            status.setText(R.string.Fixed);
                            resolved.setVisibility(View.GONE  );
                            break;
                        case "informational":
                            status.setTextColor(Color.YELLOW);
                            status.setText(R.string.info);
                            resolved.setVisibility(View.GONE  );
                            break;
                        case "false":
                            status.setTextColor(Color.RED);
                            status.setText(R.string.down);
                            resolved.setVisibility(View.GONE  );
                            break;
                    }
                    return v;
                }
            };
            lv.setAdapter(adapter);
        }
    }
}