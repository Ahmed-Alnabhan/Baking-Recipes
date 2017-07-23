package com.elearnna.www.bakingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.elearnna.www.bakingapp.R;
import com.elearnna.www.bakingapp.adapters.RecipesAdapter;
import com.elearnna.www.bakingapp.data.model.Recipe;
import com.elearnna.www.bakingapp.data.network.RecipeService;
import com.elearnna.www.bakingapp.utilities.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesActivity extends AppCompatActivity implements RecipesAdapter.RecipeAdapterClickListener{
    private RecyclerView rv;
    private RecipesAdapter adapter;
    private RecipeService recipeService;
    public static List<Recipe> recipes;
    private TextView errorMessage;
    private CollapsingToolbarLayout collapsingToolBarLayout;
    private Context context;
    private Intent intent;
    private Bundle savedInstance;
    private Parcelable rvSavedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        collapsingToolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        // Collapse the toolbar

        // Get the saved instance state
        savedInstance = savedInstanceState;
        collapsingToolBarLayout.setTitle(getResources().getString(R.string.app_name));
        errorMessage = (TextView) findViewById(R.id.error_message_connection);
        rv = (RecyclerView) findViewById(R.id.rv_recipes);
        adapter = new RecipesAdapter(this);
        // Determine number of columns based on the device and orientation
        int numOfColumns = getResources().getInteger(R.integer.recipe_columns);
        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(), numOfColumns);
        rv.setLayoutManager(glm);
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
    }


    @Override
    public void onClick(Recipe recipe) {
        intent = new Intent(RecipesActivity.this, RecipeListActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }

    private void loadRecipes() {
        recipeService = ApiUtils.getRecipeService();
        Call<List<Recipe>> call = recipeService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    recipes = response.body();
                    adapter.setRecipesData(recipes);
                    if(savedInstance != null){
                        rvSavedState = savedInstance.getParcelable("rvState");
                        rv.getLayoutManager().onRestoreInstanceState(rvSavedState);
                    }

                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                errorMessage.setVisibility(View.VISIBLE);
                Log.e("failure", String.valueOf(t.getCause()));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("allRecipes", (ArrayList<? extends Parcelable>) recipes);
        outState.putParcelable("rvState", rv.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        rvSavedState = savedInstanceState.getParcelable("rvState");
        recipes = savedInstanceState.getParcelableArrayList("allRecipes");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }
}
