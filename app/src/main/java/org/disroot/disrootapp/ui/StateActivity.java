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

public class StateActivity extends AppCompatActivity {

    Button button;

    private final String TAG = StateActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get data JSON
    static String url = "https://status.disroot.org/index.json";

    ArrayList<HashMap<String, String>> stateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener( v -> onBackPressed() );

        button = findViewById(R.id.StateMessageBtn);//StateMessageBtn
        button.setOnClickListener( arg0 -> {
            Intent goState = new Intent(StateActivity.this, StateMessagesActivity.class);
            StateActivity.this.startActivity(goState);
        } );

        stateList = new ArrayList<>();

        lv = findViewById(R.id.list);

        new GetList().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent goHome = new Intent(StateActivity.this, MainActivity.class);
            StateActivity.this.startActivity(goHome);
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
            pDialog = new ProgressDialog(StateActivity.this);
            pDialog.setMessage("Loading…");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr0 = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr0);

            if (jsonStr0 != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr0);

                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("systems");

                    // looping through All data
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        HashMap<String, String> service = new HashMap<>();
                        if (c.has("description")&&!c.isNull("description")){
                            String description = c.getString("description");
                            service.put("description", description);
                        }
                        else {
                            service.put("description", "No Description");
                        }

                        String name = c.getString("name");
                        String category = c.getString("category");
                        String status = c.getString("status");

                        // tmp hash map for single service
                        // adding each child node to HashMap key => value
                        service.put("name", name);
                        service.put("category", category);
                        service.put("status", status);

                        // adding service to service list
                        stateList.add(service);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread( () -> Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG)
                            .show() );
                }
            }
            else {
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
                    StateActivity.this, stateList, R.layout.list_services,
                    new String[]{"name", "description", "category","status"},
                    new int[]{R.id.name,R.id.description,R.id.category, R.id.status})

            //Change Color based on Status
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);

                    //Status
                    TextView status = v.findViewById(R.id.status);
                    String statusValue = status.getText().toString();
                    switch (statusValue) {
                        case "ok":
                            status.setTextColor(Color.GREEN);
                            status.setText(R.string.Operational);
                            break;
                        case "down":
                            status.setTextColor(Color.RED);
                            status.setText(R.string.down);
                            break;
                        default:
                            status.setTextColor(Color.RED);
                            break;

                    }
                    return v;
                }
            };
            lv.setAdapter(adapter);
        }
    }
}