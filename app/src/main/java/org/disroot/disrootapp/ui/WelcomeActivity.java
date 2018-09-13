package org.disroot.disrootapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.webview.R;

import org.disroot.disrootapp.utils.Constants;

public class WelcomeActivity extends AppCompatActivity {
    ImageButton imageButton;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        imageButton = findViewById(R.id.homeBtn);//Home
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent goHome = new Intent(WelcomeActivity.this, MainActivity.class);
                WelcomeActivity.this.startActivity(goHome);
            }

        });

        imageButton = findViewById(R.id.fDroidBtn);//Home
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Uri uri = Uri.parse(String.valueOf(Constants.URL_FDROID));
                Intent fDroid = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(uri)));
                startActivity(fDroid);
            }

        });
    }
}
