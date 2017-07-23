package com.elearnna.www.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.elearnna.www.bakingapp.R;
import com.elearnna.www.bakingapp.data.model.Recipe;
import com.elearnna.www.bakingapp.data.network.RecipeService;
import com.elearnna.www.bakingapp.utilities.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.elearnna.www.bakingapp.activities.RecipesActivity.recipes;

/**
 * Created by Ahmed on 7/13/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private RecipeService recipeService;
    private Intent intent;


    public WidgetFactory(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void onCreate() {
        //loadRecipes();
    }

    @Override
    public void onDataSetChanged() {
        try {
            loadRecipes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (recipes != null) {
            return recipes.size();
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_recipes_widget_list);
        String ingredientName;
        String ingredientQuantity;
        String ingredientMeasure;
        remoteViews.setTextViewText(R.id.recipe_title_widget, recipes.get(position).getName());
        int ingredientsCount = recipes.get(position).getIngredients().size();
        for (int i = 0; i < ingredientsCount; i++) {
            RemoteViews ingredientsRV = new RemoteViews(context.getPackageName(), R.layout.ingredients_content_widget);
            ingredientName = recipes.get(position).getIngredients().get(i).getIngredient();
            ingredientQuantity = String.valueOf(recipes.get(position).getIngredients().get(i).getQuantity());
            ingredientMeasure = recipes.get(position).getIngredients().get(i).getMeasure();
            ingredientsRV.setTextViewText(R.id.ingredient_name_widget, ingredientName);
            ingredientsRV.setTextViewText(R.id.ingredient_quantity_widget, ingredientQuantity + " " + ingredientMeasure);
            remoteViews.addView(R.id.widget_ingredients_list, ingredientsRV);

        }
        Intent intent = new Intent();
        intent.putExtra("recipe", recipes.get(position));

        remoteViews.setOnClickFillInIntent(R.id.ing_list, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return (1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void loadRecipes() {
        recipeService = ApiUtils.getRecipeService();
        Call<List<Recipe>> call = recipeService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    recipes = response.body();
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("failure", String.valueOf(t.getCause()));
            }
        });
    }
}
