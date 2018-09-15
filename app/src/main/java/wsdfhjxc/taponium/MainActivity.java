package wsdfhjxc.taponium;

import android.app.*;
import android.os.*;

import wsdfhjxc.taponium.engine.*;

public class MainActivity extends Activity {
    private EngineRunner engineRunner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        engineRunner = new EngineRunner(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        engineRunner.start();
    }

    @Override
    protected void onStop() {
        engineRunner.pause();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        engineRunner.stop();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // override default back key press behavior
        engineRunner.backPressed();
    }
}