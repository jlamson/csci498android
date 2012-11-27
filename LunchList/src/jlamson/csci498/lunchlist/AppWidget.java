package jlamson.csci498.lunchlist;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class AppWidget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context ctx, AppWidgetManager mgr, int[] appWidgetIds) {
		ctx.startService(new Intent(ctx, WidgetService.class));
	}
}
