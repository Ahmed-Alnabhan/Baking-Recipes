package com.elearnna.www.bakingapp.utilities;

import com.elearnna.www.bakingapp.data.network.RecipeService;
import com.elearnna.www.bakingapp.data.network.RetrofitClient;

/**
 * Created by Ahmed on 6/18/2017.
 */

public final class ApiUtils {
    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    //private constructor
    private ApiUtils() {
        throw new AssertionError();
    }

    public static RecipeService getRecipeService() {
        RecipeService rs = RetrofitClient.getClient(BASE_URL).create(RecipeService.class);
        return rs;
    }

}
