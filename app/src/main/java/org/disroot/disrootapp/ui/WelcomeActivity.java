package org.disroot.disrootapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.webview.R;

public class WelcomeActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        button = (Button) findViewById(R.id.homeBtn);//Home
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent goHome = new Intent(WelcomeActivity.this, MainActivity.class);
                WelcomeActivity.this.startActivity(goHome);
            }

        });
    }
}
