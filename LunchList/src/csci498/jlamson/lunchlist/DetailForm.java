package csci498.jlamson.lunchlist;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DetailForm extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail_form);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_form, menu);
        return true;
    }
}
