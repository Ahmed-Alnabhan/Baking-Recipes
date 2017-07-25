package com.elearnna.www.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.elearnna.www.bakingapp.R;
import com.elearnna.www.bakingapp.adapters.RecipesAdapter;
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
    private int preferredRecipeID;
    private Recipe selectedRecipe;
    private boolean setWidget = false;


    public WidgetFactory(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void onCreate() {
        int x = 0;
    }

    @Override
    public void onDataSetChanged() {
        try {
            SharedPreferences preferredRecipe = context.getSharedPreferences(RecipesAdapter.PREF_RECIPE, 0);
            preferredRecipeID = preferredRecipe.getInt(RecipesAdapter.PREF_RECIPE_ID, 0);
            loadRecipes();
            selectedRecipe = recipes.get(preferredRecipeID - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (selectedRecipe != null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (selectedRecipe.getId() == preferredRecipeID) {
            String ingredientName;
            String ingredientQuantity;
            String ingredientMeasure;
            RemoteViews remoteViews;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_recipes_widget_list);
            remoteViews.setTextViewText(R.id.recipe_title_widget, selectedRecipe.getName());
            int ingredientsCount = selectedRecipe.getIngredients().size();
            for (int i = 0; i < ingredientsCount; i++) {
                RemoteViews ingredientsRV = new RemoteViews(context.getPackageName(), R.layout.ingredients_content_widget);
                ingredientName = selectedRecipe.getIngredients().get(i).getIngredient();
                ingredientQuantity = String.valueOf(selectedRecipe.getIngredients().get(i).getQuantity());
                ingredientMeasure = selectedRecipe.getIngredients().get(i).getMeasure();
                ingredientsRV.setTextViewText(R.id.ingredient_name_widget, ingredientName);
                ingredientsRV.setTextViewText(R.id.ingredient_quantity_widget, ingredientQuantity + " " + ingredientMeasure);
                remoteViews.addView(R.id.widget_ingredients_list, ingredientsRV);
            }
            Intent intent = new Intent();
            intent.putExtra("recipe", selectedRecipe);

            remoteViews.setOnClickFillInIntent(R.id.ing_list, intent);
            return remoteViews;
        } else {
            return null;
        }
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
