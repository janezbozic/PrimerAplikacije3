package si.uni_lj.fri.pbd.miniapp3.ui.favorites;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;

public class FavoritesViewModel extends AndroidViewModel {

    private LiveData<List<RecipeDetails>> allRecips;
    private MutableLiveData<RecipeDetails> searchedRec;

    FavoritesRepository rep;

    public FavoritesViewModel (Application application){
        super(application);
        rep = new FavoritesRepository(application);
        allRecips = rep.getAllRecipes();
        searchedRec = rep.getSearchedRec();
    }

    public LiveData<List<RecipeDetails>> getAllRecips() {
        return allRecips;
    }

    public void setAllRecips(LiveData<List<RecipeDetails>> allRecips) {
        this.allRecips = allRecips;
    }

    public void insertRec(RecipeDetails recipeDetails){
        rep.insertRecipe(recipeDetails);
    }

    public void deleteRec(long id){
        rep.deleteRecipe(id);
    }

    public MutableLiveData<RecipeDetails> getSearchedRec() {
        return searchedRec;
    }

    public void findRec(long id){
        rep.findRecepi(id);
    }
}
