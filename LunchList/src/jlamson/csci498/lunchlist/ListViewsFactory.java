package jlamson.csci498.lunchlist;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
public class ListViewsFactory implements RemoteViewsFactory {

	private Context ctx;
	private RestaurantHelper helper;
	private Cursor restaurants;
	
	public ListViewsFactory(Context ctx, Intent intent) {
		this.ctx = ctx;
	}
	
	public void onCreate() {
		helper = new RestaurantHelper(ctx);
		restaurants = helper.getAllIdsAndNames();
	}
	
	public void onDestroy() {
		restaurants.close();
		helper.close();
	}
	
	public int getCount() {
		return restaurants.getCount();
	}

	public long getItemId(int position) {
		restaurants.moveToPosition(position);
		return restaurants.getInt(0);
	}

	public RemoteViews getLoadingView() {
		return null;
	}

	public RemoteViews getViewAt(int position) {
		
		RemoteViews row = new RemoteViews(ctx.getPackageName(), R.layout.widget_row);
		restaurants.moveToPosition(position);
		
		row.setTextViewText(android.R.id.text1, restaurants.getString(1));
		
		Intent i = new Intent();
		Bundle extras = new Bundle();
		
		extras.putString(LunchList.ID_EXTRA, String.valueOf(restaurants.getInt(0)));
		i.putExtras(extras);
		
		row.setOnClickFillInIntent(android.R.id.text1, i);
		
		return row;
	
	}

	public int getViewTypeCount() {
		return 1;
	}

	public boolean hasStableIds() {
		return true;
	}

	public void onDataSetChanged() {
		//do nothing
	}

}
