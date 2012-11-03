package csci498.jlamson.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailForm extends Activity {
	
	EditText name;
    EditText address;
    RadioGroup types;
    EditText notes;
    
    RestaurantHelper helper;
    
    String restaurantId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_form);
        
        initDatabaseAccess();
        initFormElements();
        initRestaurantFromId();
    }
    
    private void initDatabaseAccess() {
    	helper = new RestaurantHelper(this);
    }
    
    private void initFormElements() {
    	name = (EditText)findViewById(R.id.name);
		address = (EditText)findViewById(R.id.addr);
		types = (RadioGroup)findViewById(R.id.types);
        notes = (EditText)findViewById(R.id.notes);
        
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
    }
    
    private void initRestaurantFromId() {
    	restaurantId = getIntent().getStringExtra(LunchList.ID_EXTRA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_form, menu);
        return true;
    }
    
    private View.OnClickListener onSave = new View.OnClickListener() {
		public void onClick(View v) {
			
			String type = "";
			
			switch (types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				type = "sit_down";
				break;
			case R.id.take_out:
				type = "take_out";
				break;
			case R.id.delivery:
				type = "delivery";
				break;
			}
			
//			helper.insert(name.getText().toString(),
//						address.getText().toString(),
//						type, 
//						notes.getText().toString());
//			restaurantCursor.requery();
		}
	};

}
