package com.elearnna.www.bakingapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elearnna.www.bakingapp.R;
import com.elearnna.www.bakingapp.adapters.IngredientsAdapter;
import com.elearnna.www.bakingapp.data.model.Ingredient;
import com.elearnna.www.bakingapp.data.model.Recipe;
import com.elearnna.www.bakingapp.data.model.Step;
import com.elearnna.www.bakingapp.divider.RecyclerViewDividerDecoration;
import com.elearnna.www.bakingapp.fragments.RecipeDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Recipe selectedRecipe;
    private List<Step> recipeSteps;
    private List<Ingredient> ingredients;
    private Intent intent;
    private RecyclerView ingredientsRV;
    private TextView ingredientsCount;
    private TextView stepsCount;
    private Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle(selectedRecipe.getName());
        ActionBar actionBar = getSupportActionBar();

        // Show the Up button in the action bar.
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        arguments = new Bundle();

        // Get the recipes from the intent
        intent = getIntent();
        if (intent.hasExtra("recipe")) {
            selectedRecipe = intent.getParcelableExtra("recipe");
            recipeSteps = selectedRecipe.getSteps();
            ingredients = selectedRecipe.getIngredients();
            actionBar.setTitle(selectedRecipe.getName());
        }

        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredients);

        // Ingredients RecyclerView
        ingredientsRV = (RecyclerView) findViewById(R.id.rv_ingredients);

        ingredientsRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ingredientsRV.addItemDecoration(new RecyclerViewDividerDecoration(this));
        ingredientsRV.setAdapter(ingredientsAdapter);

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        ((RecyclerView) recyclerView).addItemDecoration(new RecyclerViewDividerDecoration(this));


        // Set the count of ingredients and steps
        ingredientsCount = (TextView) findViewById(R.id.ingredient_count);
        stepsCount = (TextView) findViewById(R.id.steps_count);
        ingredientsCount.setText(String.valueOf(ingredients.size()));
        stepsCount.setText(String.valueOf(recipeSteps.size()));
        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipeDetailFragment.ARG_ITEM_ID, selectedRecipe.getSteps().get(0));
            bundle.putParcelableArrayList("stepsList", (ArrayList<? extends Parcelable>) recipeSteps);
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, fragment)
                    .commit();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(recipeSteps));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Step> mValues;

        public SimpleItemRecyclerViewAdapter(List<Step> recipes) {
            mValues = recipes;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(position + " - " + mValues.get(position).getShortDescription());
            String videoURL = holder.mItem.getVideoURL();
            if (videoURL.equals("") || videoURL == null) {
                holder.videoImage.setVisibility(View.INVISIBLE);
            } else {
                holder.videoImage.setVisibility(View.VISIBLE);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Bundle arguments = new Bundle();
                    arguments.putParcelable(RecipeDetailFragment.ARG_ITEM_ID, holder.mItem);
                    arguments.putParcelableArrayList("stepsList", (ArrayList<? extends Parcelable>) mValues);
                    if (mTwoPane) {
                        RecipeDetailFragment fragment = new RecipeDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.recipe_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, RecipeDetailActivity.class);
                        intent.putExtras(arguments);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final ImageView videoImage;
            public Step mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.step_short_description);
                videoImage = (ImageView) view.findViewById(R.id.video_image);
            }

            @Override
            public String toString() {
                return super.toString();
            }
        }
    }
}
