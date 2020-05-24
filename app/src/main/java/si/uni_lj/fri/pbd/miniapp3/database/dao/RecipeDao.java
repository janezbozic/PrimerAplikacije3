package si.uni_lj.fri.pbd.miniapp3.database.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM RecipeDetails WHERE idMeal = :idMeal")
    RecipeDetails getRecepiById(String idMeal);

    @Query("SELECT * FROM RecipeDetails")
    LiveData<List<RecipeDetails>> getAllReceps();

    @Insert
    void insertRecipe(RecipeDetails recipeDetails);

    @Query("DELETE FROM RecipeDetails WHERE id = :id")
    void deleteRecipe(Long id);

}
