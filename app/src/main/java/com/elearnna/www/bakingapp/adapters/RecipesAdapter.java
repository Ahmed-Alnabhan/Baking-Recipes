package com.elearnna.www.bakingapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elearnna.www.bakingapp.R;
import com.elearnna.www.bakingapp.data.model.Recipe;
import com.elearnna.www.bakingapp.fragments.RecipeDetailFragment;

import java.util.List;

/**
 * Created by Ahmed on 6/18/2017.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    public static final String RECIPES_TAG = RecipesAdapter.class.getSimpleName();
    public static final String PREF_RECIPE = "preferredRecipe";
    public static final String PREF_RECIPE_ID = "recipeID";
    private Context context;
    private List<Recipe> recipes;
    private RecipeAdapterClickListener recipeAdapterListener;
    private String recipeImageUrl;
    private boolean isImage;
    private RequestOptions requestOptions;

    // Ingredient Adapter click listener
    public interface RecipeAdapterClickListener {
        void onClick(Recipe recipe);
    }

    // Constructor takes the click listener as a parameter
    public RecipesAdapter (RecipeAdapterClickListener listener) {
        this.recipeAdapterListener = listener;
        requestOptions = new RequestOptions();
        requestOptions.override(350, 427);
    }

    @Override
    public RecipesAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_card, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesAdapter.RecipeViewHolder holder, int position) {
        try {
            Recipe recipe = recipes.get(position);
            holder.recipeTxt.setText(recipe.getName().toString());
            holder.ingredientsCountTxt.setText(String.valueOf(recipe.getIngredients().size()));
            holder.stepsCountTxt.setText(String.valueOf(recipe.getSteps().size()));
            holder.servingsCountTxt.setText(String.valueOf(recipe.getServings()));
            // Get recipe image
            recipeImageUrl = recipe.getImage();
            isImage = RecipeDetailFragment.isUrlThumbnailImage(recipeImageUrl);
            if (recipeImageUrl != null && !recipe.equals("") && isImage) {
                Glide.with(context).load(recipeImageUrl).apply(requestOptions).into(holder.recipeImage);
            } else {
                Glide.with(context).load(R.drawable.cake).apply(requestOptions).into(holder.recipeImage);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (recipes != null) {
            return recipes.size();
        } else {
            return 0;
        }
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView recipeTxt;
        private TextView ingredientsCountTxt;
        private TextView stepsCountTxt;
        private TextView servingsCountTxt;
        private ImageView recipeImage;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            recipeTxt = (TextView) itemView.findViewById(R.id.tv_recipe);
            ingredientsCountTxt = (TextView) itemView.findViewById(R.id.tv_ingredients_count);
            stepsCountTxt = (TextView) itemView.findViewById(R.id.tv_steps_count);
            servingsCountTxt = (TextView) itemView.findViewById(R.id.tv_servings_count);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipe_image);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = recipes.get(adapterPosition);
            // Store preferred recipe
            SharedPreferences preferredRecipe = context.getSharedPreferences(PREF_RECIPE, 0);
            SharedPreferences.Editor editor = preferredRecipe.edit();
            editor.putInt(PREF_RECIPE_ID, recipe.getId());
            editor.commit();
            recipeAdapterListener.onClick(recipe);
        }
    }

    public void setRecipesData(List<Recipe> recipesData) {
        recipes = recipesData;
        notifyDataSetChanged();
    }
}
