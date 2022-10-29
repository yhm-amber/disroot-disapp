package org.disroot.disrootapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.disroot.disrootapp.ui.StateMessagesActivity;
import org.disroot.disrootapp.utils.HttpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.constraint.Constraints.TAG;


public class StatusService extends Service {

    //status report
    public SharedPreferences checkDate;
    // URL to get data JSON
    static String incidentUrl0 ="https://status.disroot.org/issues/index.json";
    ArrayList<HashMap<String, String>> messageList;
    ArrayList<HashMap<String, String>> getDate;

    public StatusService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException( "Not yet implemented" );

    }
    @Override
    public void onCreate() {
        super.onCreate();

        //Status report
        messageList = new ArrayList<>();
        getDate = new ArrayList<>();
        checkDate = getSharedPreferences("storeDate", Context.MODE_PRIVATE);

        //Check json for updates
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new StatusService.GetList().execute();
            }
        }, 100, 1800000);//100000=100sec
        
        
    }
    //status report
    @SuppressLint("StaticFieldLeak")
    class GetList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStringIdents0 = sh.makeServiceCall( incidentUrl0 );

            Log.e(TAG, "Response from url(Service): " + incidentUrl0 );

            if (jsonStringIdents0 != null) {//Incidents page
                try {
                    JSONObject jsonObj = new JSONObject(jsonStringIdents0);
                    JSONArray data = jsonObj.getJSONArray("pages");
                    int a=0;
                    JSONObject o = data.getJSONObject(a);
                    String title = o.getString("title");
                    String updated = o.getString("lastMod");
                    HashMap<String, String> date = new HashMap<>();
                    date.put("title", title);
                    date.put("updated", updated);
                    getDate.add(date);
                    String stateDate = date.put( "updated", updated );
                    String dateStored= checkDate.getString( "storeDate","" );

                    assert dateStored != null;
                    if (dateStored.equals( "" ))
                    {
                        checkDate.edit().putString( "storeDate", "stateDate").apply();
                        //return null;
                    }
                    else {
                        assert stateDate != null;
                        if (!stateDate.equals( dateStored )&& !stateDate.equals( "" ))//dateStored
                        {
                            checkDate.edit().putString( "storeDate", stateDate).apply();
                            Log.e(TAG, "date: " + dateStored);
                            Log.e(TAG, "date2: " + stateDate);
                            sendNotification();//Call notification
                            return null;
                        }
                        else
                            Log.e(TAG, stateDate+"updated json(service)"+dateStored);
                    }
                    return null;

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                }
            }else {
                Log.e(TAG, "Couldn't get json from server.");
            }
            return null;
        }
    }

    //Notification
    private void sendNotification() throws JSONException {
        String CHANNEL_ID = "3168654312";
        String CHANNEL_NAME = "StateNotification";
        HttpHandler sh = new HttpHandler();
        String jsonStringIdents0 = sh.makeServiceCall( incidentUrl0 );
        JSONObject jsonObj = new JSONObject(jsonStringIdents0);
        JSONArray data = jsonObj.getJSONArray("pages");
        int a=0;
        JSONObject o = data.getJSONObject(a);
        String title = o.getString( "title" );
        String permalink = o.getString( "permalink" );
        HashMap<String, String> date = new HashMap<>();
        date.put("title", title);
        date.put("permalink", permalink);
        getDate.add(date);
        Log.e(TAG, "message: " + title+" link "+permalink);

        Intent goState = new Intent( StatusService.this, StateMessagesActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent launchStateMessages = PendingIntent.getActivity(StatusService.this,0, goState, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService( Context.NOTIFICATION_SERVICE);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(permalink);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // I would suggest that you use IMPORTANCE_DEFAULT instead of IMPORTANCE_HIGH
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setLightColor( Color.rgb( 80,22,45 ));
            channel.enableLights(true);
            channel.setVibrationPattern(new long[]{50,500,100,300,50,300});
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setAutoCancel( true )
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_state)
                .setContentTitle( getString( R.string.NotificationTitle ) )
                .setContentText(title)//get text Title from json :-)
                .setContentInfo(permalink)//get text message from json :-)
                .setContentIntent(launchStateMessages);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound)
                .setVibrate(new long[]{50,500,100,300,50,300})
                .setLights(Color.BLUE, 3000, 3000);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationBuilder.setChannelId(CHANNEL_ID);
        }

        notificationManager.notify(CHANNEL_ID, 1, notificationBuilder.build());
    }
}
