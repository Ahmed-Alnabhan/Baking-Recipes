package com.elearnna.www.bakingapp.widget;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViewsService;

/**
 * Created by Ahmed on 7/13/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        RemoteViewsFactory rvf = new WidgetFactory(this.getApplicationContext(), intent);
        return (rvf);
    }
}
