package si.uni_lj.fri.pbd.miniapp3.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeDetailsIM;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO;

//Interface for api calls to right endpoints and retrieving data
public interface RestAPI {

    @GET("list.php?i=list")
    Call<IngredientsDTO> getAllIngredients();

    @GET("filter.php?")
    Call<RecipesByIngredientDTO> getRecipeWithIngredient(@Query("i") String ingredient);

    @GET("lookup.php?")
    Call<RecipesByIdDTO> getRecipeWithId(@Query("i") String id);

}