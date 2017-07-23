package com.elearnna.www.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elearnna.www.bakingapp.R;
import com.elearnna.www.bakingapp.data.model.Ingredient;

import java.util.List;

/**
 * Created by Ahmed on 7/3/2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private final List<Ingredient> mIngredients;
    public IngredientsAdapter(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }
    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.ViewHolder holder, int position) {
        holder.mItem = mIngredients.get(position);
        holder.ingredientDescription.setText(position + " - " + holder.mItem.getIngredient());
        holder.ingredientQuantity.setText(String.valueOf(holder.mItem.getQuantity()) + " " + holder.mItem.getMeasure());
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView ingredientDescription;
        public final TextView ingredientQuantity;
        public Ingredient mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ingredientDescription = (TextView) itemView.findViewById(R.id.ingredient_description);
            ingredientQuantity = (TextView) itemView.findViewById(R.id.ingredient_quantity);
        }
    }
}
