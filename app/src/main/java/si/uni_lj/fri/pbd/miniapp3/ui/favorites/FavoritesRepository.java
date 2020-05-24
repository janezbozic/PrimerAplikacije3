package si.uni_lj.fri.pbd.miniapp3.ui.favorites;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.database.Database;
import si.uni_lj.fri.pbd.miniapp3.database.dao.RecipeDao;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;

public class FavoritesRepository {

    private RecipeDao recipeDao;

    private LiveData<List<RecipeDetails>> allRecipes;
    private MutableLiveData<RecipeDetails> searchedRec = new MutableLiveData<>();

    public FavoritesRepository (Application application){
        Database db = Database.getDatabase(application);
        recipeDao = db.recipeDao();
        allRecipes = recipeDao.getAllReceps();
    }

    public void insertRecipe(final RecipeDetails newRecipe) {
        Database.dbWriteExec.execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.insertRecipe(newRecipe);
            }
        });
    }

    public void deleteRecipe(final long id) {
        Database.dbWriteExec.execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.deleteRecipe(id);
            }
        });
    }

    public LiveData<List<RecipeDetails>> getAllRecipes() {
        return allRecipes;
    }

    public MutableLiveData<RecipeDetails> getSearchedRec() {
        return searchedRec;
    }

    public void findRecepi(final long id) {

        Database.dbWriteExec.execute(new Runnable() {
            @Override
            public void run() {
                searchedRec.postValue(recipeDao.getRecepiById(id+""));
            }
        });
    }
}
