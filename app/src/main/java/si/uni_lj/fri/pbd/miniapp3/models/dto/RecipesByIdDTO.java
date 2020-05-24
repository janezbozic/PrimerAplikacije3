package si.uni_lj.fri.pbd.miniapp3.models.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecipesByIdDTO {

    @SerializedName("meals")
    private ArrayList<RecipeDetailsDTO> main;

    public ArrayList<RecipeDetailsDTO> getMain() {
        return main;
    }

    public void setMain(ArrayList<RecipeDetailsDTO> main) {
        this.main = main;
    }
}
