package si.uni_lj.fri.pbd.miniapp3.models.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecipesByIngredientDTO {

    @SerializedName("meals")
    private ArrayList<RecipeSummaryDTO> meals;

    public ArrayList<RecipeSummaryDTO> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<RecipeSummaryDTO> meals) {
        this.meals = meals;
    }
}
