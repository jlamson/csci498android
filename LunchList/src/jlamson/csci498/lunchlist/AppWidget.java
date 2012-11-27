package jlamson.csci498.lunchlist;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AppWidget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context ctx, AppWidgetManager mgr, int[] appWidgetIds) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			onHCUpdate(ctx, mgr, appWidgetIds);
		} else {
			ctx.startService(new Intent(ctx, WidgetService.class));
		}
	}

	private void onHCUpdate(Context ctx, AppWidgetManager mgr, int[] appWidgetIds) {
		
	}
}
