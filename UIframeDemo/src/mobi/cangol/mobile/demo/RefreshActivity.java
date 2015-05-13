package mobi.cangol.mobile.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

public class RefreshActivity extends Activity implements OnRefreshListener {
	private SwipeRefreshLayout swipeLayout;
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_refresh);
	    swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
	    swipeLayout.setOnRefreshListener(this);
	    swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
	            android.R.color.holo_green_light, 
	            android.R.color.holo_orange_light, 
	            android.R.color.holo_red_light);
	}
	public void onRefresh() {
	    new Handler().postDelayed(new Runnable() {
	    	public void run() {
	            swipeLayout.setRefreshing(false);
	        }
	    }, 5000);
	}
}
