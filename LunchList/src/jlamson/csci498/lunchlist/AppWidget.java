package jlamson.csci498.lunchlist;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

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
		
		for (int i=0; i < appWidgetIds.length; i++) {
			Intent serviceIntent = new Intent(ctx, ListWidgetService.class);
			
			serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
			
			RemoteViews widget = new RemoteViews(ctx.getPackageName(), R.layout.widget);
			widget.setRemoteAdapter(appWidgetIds[i], R.id.restaurants, serviceIntent);
			
			Intent clickIntent = new Intent(ctx, DetailForm.class);
			PendingIntent pi = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			widget.setPendingIntentTemplate(R.id.restaurants, pi);
			
			mgr.updateAppWidget(appWidgetIds[i], widget);
		}
	
		super.onUpdate(ctx, mgr, appWidgetIds);
	}
}
