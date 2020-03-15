package org.disroot.disrootapp.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
    private String TAG = StateMessagesActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get data JSON
    static String incidenturl0 ="https://state.disroot.org/api/v1/incidents?sort=id&order=desc";

    ArrayList<HashMap<String, String>> messageList;
    ArrayList<HashMap<String, String>> getDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_messages);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        button = findViewById(R.id.StateBtn);//StateBtn
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent goState = new Intent(StateMessagesActivity.this, StateActivity.class);
                StateMessagesActivity.this.startActivity(goState);
            }

        });

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
            String jsonStrincidents0 = sh.makeServiceCall(incidenturl0);

            Log.e(TAG, "Response from url: " + incidenturl0);

            if (jsonStrincidents0 != null) {//Incidaetnts page
                try {
                    JSONObject jsonObj = new JSONObject(jsonStrincidents0);

                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("data");

                    // looping through All data
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        String message = c.getString("message");
                        String scheduled_at = c.getString("scheduled_at");
                        String scheduledAt = "Scheduled at: " + '"' + scheduled_at + '"';
                        String updated_at = c.getString("updated_at");
                        String lastUpdated = "Last Updated: " + updated_at + '"';
                        String human_status = c.getString("human_status");

                        // tmp hash map for single service
                        HashMap<String, String> service = new HashMap<>();

                        // adding each child node to HashMap key => value
                        service.put("id", id);
                        service.put("name", name);
                        service.put("message", message);
                        service.put("scheduled_at", scheduledAt);
                        service.put("updated_at", lastUpdated);
                        service.put("human_status", human_status);

                        // adding service to service list
                        messageList.add(service);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            }else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Is your internet connection ok?",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
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
                    R.layout.list_item, new String[]{"name","message", "updated_at", "scheduled_at", "human_status"}, new int[]{R.id.name, R.id.message,
                    R.id.updated_at, R.id.scheduled_at, R.id.human_status})

                    //Change Color based on Status
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    //Name Email
                    TextView nameMail = v.findViewById(R.id.name);
                    String nameMailValue = nameMail.getText().toString();
                    switch (nameMailValue) {
                        case "Email Service":
                            nameMail.setText(R.string.EmailService);
                            break;
                        default:
                            break;
                    }
                    //Name WebMail
                    TextView nameWebmail = v.findViewById(R.id.name);
                    String nameWebmailValue = nameWebmail.getText().toString();
                    switch (nameWebmailValue) {
                        case "WebMail Service":
                            nameWebmail.setText(R.string.WebmailService);
                            break;
                        default:
                            break;
                    }
                    //Name Cloud
                    TextView nameCloud = v.findViewById(R.id.name);
                    String nameCloudValue = nameCloud.getText().toString();
                    switch (nameCloudValue) {
                        case "WebMail Service":
                            nameCloud.setText(R.string.Cloud);
                            break;
                        default:
                            break;
                    }
                    //Make Last updated translatable
                    TextView updated = v.findViewById(R.id.updated_at);
                    String updatedValue = updated.getText().toString();
                        if (updatedValue.startsWith("Last Updated: ")){
                            updated.setText(updatedValue.replace("Last Updated: ",getText(R.string.LastUpdated)));
                        }
                    //Make Scheduled at translatable
                    TextView scheduled = v.findViewById(R.id.scheduled_at);
                    String scheduledValue = scheduled.getText().toString();
                    if (scheduledValue.startsWith("Scheduled at: ")){
                        scheduled.setText(scheduledValue.replace("Scheduled at: ",getText(R.string.ScheduledAt)));
                    }
                    //Human_status
                    TextView humanStatus = v.findViewById(R.id.human_status);
                    String humanStatusValue = humanStatus.getText().toString();
                    switch (humanStatusValue) {
                        case "Fixed":
                            humanStatus.setTextColor(Color.GREEN);
                            humanStatus.setText(R.string.Fixed);
                            scheduled.setVisibility(View.GONE  );
                            break;
                        case "Scheduled":
                            humanStatus.setTextColor(Color.YELLOW);
                            humanStatus.setText(R.string.Scheduled);
                            scheduled.setVisibility(View.VISIBLE  );
                            break;
                        case "Investigating":
                            humanStatus.setTextColor(Color.RED);
                            scheduled.setVisibility(View.GONE  );
                            break;
                        case "Watching":
                            scheduled.setVisibility(View.GONE  );
                            humanStatus.setTextColor(Color.YELLOW);
                            humanStatus.setText(R.string.Investigating);
                            break;
                        case "Identified":
                            scheduled.setVisibility(View.GONE  );
                            humanStatus.setTextColor(Color.RED);
                            humanStatus.setText(R.string.Identified);
                            break;
                    }
                    return v;
                }
            };
            lv.setAdapter(adapter);
        }
    }
}