package csci498.jlamson.lunchlist;

import android.app.ListActivity;
import android.os.Bundle;
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
}
