package org.disroot.disrootapp.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.disroot.disrootapp.R;
import org.disroot.disrootapp.ui.MainActivity;
import org.disroot.disrootapp.ui.StateMessagesActivity;
import org.disroot.disrootapp.utils.HttpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class CachetService extends IntentService {

    private static final String TAG = MainActivity.class.getSimpleName();

    //status report
    private ListView lv;
    public SharedPreferences checkDate;
    // URL to get data JSON
    static String incidenturl0 ="https://state.disroot.org/api/v1/incidents?sort=id&order=desc";
    ArrayList<HashMap<String, String>> messageList;
    ArrayList<HashMap<String, String>> getDate;



    public CachetService(String status) {
        super( CachetService.class.getName());
        setIntentRedelivery(true);
    }





    @Override
    protected void onHandleIntent(Intent intent) {

        messageList = new ArrayList<>();
        getDate = new ArrayList<>();
        lv = lv.findViewById( R.id.list);
        checkDate = getSharedPreferences("storeDate", Context.MODE_PRIVATE);

        //Check json for updates
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                /*runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        new CachetService.GetList().execute();
                    }
                });*/
            }
        }, 100, 100000);//100000=100sec
    }

    @SuppressLint("StaticFieldLeak")
    class GetList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStrincidents0 = sh.makeServiceCall(incidenturl0);

            Log.e(TAG, "Service response from url: " + incidenturl0);

            if (jsonStrincidents0 != null) {//Incidaetnts page
                try {
                    JSONObject jsonObj = new JSONObject(jsonStrincidents0);
                    JSONArray data = jsonObj.getJSONArray("data");
                    int a=0;
                    JSONObject o = data.getJSONObject(a);
                    String callid = o.getString("id");
                    String updated = o.getString("updated_at");
                    HashMap<String, String> date = new HashMap<>();
                    date.put("id", callid);
                    date.put("updated", updated);
                    getDate.add(date);
                    String stateDate = date.put( "updated", updated );
                    String dateStored= checkDate.getString( "storeDate","" );

                    if (dateStored.equals( "" ))
                    {
                        checkDate.edit().putString( "storeDate", stateDate).apply();
                        //return null;
                    }
                    else if (!stateDate.equals( dateStored )&& !stateDate.equals( "" ))//dateStored
                    {
                        checkDate.edit().putString( "storeDate", stateDate).apply();
                        Log.e(TAG, "service date: " + dateStored);
                        Log.e(TAG, "service date2: " + stateDate);
                        sendNotification();//Call notification
                        return null;
                    }
                    else
                        Log.e(TAG, "updated json service");
                    return null;

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                   /* runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });*/
                }
            }else {
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }

    }


    //Notification
    private void sendNotification() throws JSONException {
        HttpHandler sh = new HttpHandler();
        String jsonStrincidents0 = sh.makeServiceCall(incidenturl0);
        JSONObject jsonObj = new JSONObject(jsonStrincidents0);
        JSONArray data = jsonObj.getJSONArray("data");
        int a=0;
        JSONObject o = data.getJSONObject(a);
        String name = o.getString( "name" );
        String message = o.getString( "message" );
        HashMap<String, String> date = new HashMap<>();
        date.put("name", name);
        date.put("message", message);
        Log.e(TAG, "message: " + name);

        Intent goState = new Intent(CachetService.this, StateMessagesActivity.class);
        PendingIntent launchStateMessages = PendingIntent.getActivity(CachetService.this,0, goState, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setAutoCancel( true )
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.ic_state)
                        .setContentTitle( getString( R.string.NotificationTitle ) )
                        .setContentText(name)//get text Title from json :-)
                        .setContentInfo(message)//get text message from json :-)
                        .setContentIntent(launchStateMessages);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound)
                .setVibrate(new long[]{50,500,100,300,50,300})
                .setLights( Color.MAGENTA, 3000, 3000);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }
}