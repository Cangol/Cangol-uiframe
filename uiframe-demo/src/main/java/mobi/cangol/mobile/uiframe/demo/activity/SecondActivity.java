package mobi.cangol.mobile.uiframe.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.uiframe.demo.R;
import mobi.cangol.mobile.uiframe.demo.utils.CleanLeakUtils;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }	@Override
    protected void onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this);
        super.onDestroy();
    }
}
