package org.disroot.disrootapp.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.disroot.disrootapp.R;
import org.disroot.disrootapp.utils.HttpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StateActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get data JSON
    static String url = "https://state.disroot.org/api/v1/components";
    static String url1 = "https://state.disroot.org/api/v1/components?page=2";

    ArrayList<HashMap<String, String>> stateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        stateList = new ArrayList<>();

        lv = findViewById(R.id.list);

        new GetList().execute();
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
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr0 = sh.makeServiceCall(url);
            String jsonStr1 = sh.makeServiceCall(url1);

            Log.e(TAG, "Response from url: " + jsonStr0);

            if (jsonStr0 != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr0);

                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("data");

                    // looping through All data
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        String description = c.getString("description");
                        String updated_at = c.getString("updated_at");
                        String status_name = c.getString("status_name");

                        // tmp hash map for single service
                        HashMap<String, String> service = new HashMap<>();

                        // adding each child node to HashMap key => value
                        service.put("id", id);
                        service.put("name", name);
                        service.put("description", description);
                        service.put("updated_at", updated_at);
                        service.put("status_name", status_name);

                        // adding service to service list
                        stateList.add(service);
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
            }if (jsonStr1 != null) {//next page
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr1);

                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("data");

                    // looping through All data
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        String id = c.getString("id");
                        String name = c.getString("name");
                        String description = c.getString("description");
                        String updated_at = c.getString("updated_at");
                        String status_name = c.getString("status_name");

                        // tmp hash map for single service
                        HashMap<String, String> service = new HashMap<>();

                        // adding each child node to HashMap key => value
                        service.put("id", id);
                        service.put("name", name);
                        service.put("description", description);
                        service.put("updated_at", updated_at);
                        service.put("status_name", status_name);

                        // adding service to service list
                        stateList.add(service);
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
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
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
                    StateActivity.this, stateList,
                    R.layout.list_item, new String[]{"name", "description", "updated_at",
                    "status_name"}, new int[]{R.id.name,
                    R.id.description,R.id.updated_at, R.id.status_name});
            lv.setAdapter(adapter);
        }
    }
}