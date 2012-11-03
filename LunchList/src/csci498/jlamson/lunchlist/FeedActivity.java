package csci498.jlamson.lunchlist;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class FeedActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed, menu);
        return true;
    }
    
    private static class FeedTask extends AsyncTask<String, Void, Void> {
    	private Exception e;
    	private FeedActivity activity;
    	
    	FeedTask(FeedActivity activity) {
    		this.activity = activity;
    	}
    	
		@Override
		public Void doInBackground(String... urls) {
			
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet getMethod = new HttpGet(urls[0]);
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = client.execute(getMethod, responseHandler);
				Log.d("FeedActivity", responseBody);
			} catch (Exception e) {
				this.e = e;
			}
			
			return null;
		}
		
		@Override
		public void onPostExecute(Void unused) {
			if (e == null) {
				//TODO
			} else {
				Log.e("LunchList", "Exception parsing feed", e);
				activity.goBlooey(e);
			}
		}
    }
}
