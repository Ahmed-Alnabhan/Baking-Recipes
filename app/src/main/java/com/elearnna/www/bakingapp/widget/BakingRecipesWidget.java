package com.elearnna.www.bakingapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import com.elearnna.www.bakingapp.R;
import com.elearnna.www.bakingapp.activities.RecipeListActivity;
import com.elearnna.www.bakingapp.activities.RecipesActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingRecipesWidget extends AppWidgetProvider {

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_recipes_widget);

        Intent intent = new Intent(context, RecipesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.widget_toolbar, pendingIntent);
        // Instruct the widget manager to update the widget
        Intent intnt = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.widget_ingredients_list_view, intnt);

        Intent ingListIntent = new Intent(context, RecipeListActivity.class);
        PendingIntent ingPendingIntent = PendingIntent.getActivity(context, 0, ingListIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_ingredients_list_view, ingPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int [] appWidgetIds = appWidgetManager.getAppWidgetIds( new ComponentName(context, BakingRecipesWidget.class));
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_list_view);
        }
        super.onReceive(context, intent);
    }

}

