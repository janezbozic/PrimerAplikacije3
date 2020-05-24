package si.uni_lj.fri.pbd.miniapp3.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.async.AsyncImageDownload;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;
import si.uni_lj.fri.pbd.miniapp3.models.Mapper;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeDetailsIM;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeDetailsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO;
import si.uni_lj.fri.pbd.miniapp3.rest.RestAPI;
import si.uni_lj.fri.pbd.miniapp3.rest.ServiceGenerator;
import si.uni_lj.fri.pbd.miniapp3.ui.favorites.FavoritesViewModel;
import timber.log.Timber;

public class DetailsActivity extends AppCompatActivity {

    ImageView imageView;
    TextView text1;
    TextView text2;
    long id;
    Boolean fav;
    FloatingActionButton button;

    private RestAPI mRestClient = ServiceGenerator.createService(RestAPI.class);
    private FavoritesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle newBundle = getIntent().getExtras();
        id = newBundle.getLong("id");
        fav = newBundle.getBoolean("fav");

        imageView = findViewById(R.id.imageViewDetails);
        text1 = findViewById(R.id.text1Details);
        text2 = findViewById(R.id.text2Details);

        button = findViewById(R.id.fab);

        mViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        if (fav) {
            initFromDB();
            mViewModel.findRec(id);
        }
        else
            initFromUrl();
    }

    private void initFromDB() {

        mViewModel.getSearchedRec().observe(this, new Observer<RecipeDetails>() {
            @Override
            public void onChanged(RecipeDetails recipeDetails) {
                if (recipeDetails != null){
                    showRecipe(Mapper.mapRecipeDetailsToRecipeDetailsIm(true, recipeDetails));
                    setActionButton(recipeDetails.getId());
                }
            }
        });

    }

    private void initFromUrl() {

        mRestClient.getRecipeWithId(id+"").enqueue(new Callback<RecipesByIdDTO>() {
            @Override
            public void onResponse(Call<RecipesByIdDTO> call, Response<RecipesByIdDTO> response) {
                if (response.isSuccessful()){
                    if (response.body().getMain().get(0) != null) {
                        RecipeDetailsDTO recipe = response.body().getMain().get(0);
                        showRecipe(Mapper.mapRecipeDetailsDtoToRecipeDetailsIm(false, recipe));
                        setActionButton(recipe);
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipesByIdDTO> call, Throwable t) {
                Timber.d("aa");
            }
        });



    }

    private void showRecipe(RecipeDetailsIM recipe) {

        if (!fav) {
            AsyncImageDownload asyncImageDownload = new AsyncImageDownload(imageView, this);
            asyncImageDownload.execute(recipe.getStrMealThumb());
        }
        else {
            try {
                byte [] encodeByte=Base64.decode(recipe.getImageBitaMap(),Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                imageView.setImageBitmap(bitmap);
            } catch(Exception e) {
                e.getMessage();
            }
        }

        text1.setText("Ingredients: \n\n" + format(recipe.getStrIngredient1(), recipe.getStrMeasure1())
                + format(recipe.getStrIngredient2(), recipe.getStrMeasure2())
                + format(recipe.getStrIngredient3(), recipe.getStrMeasure3())
                + format(recipe.getStrIngredient4(), recipe.getStrMeasure4())
                + format(recipe.getStrIngredient5(), recipe.getStrMeasure5())
                + format(recipe.getStrIngredient6(), recipe.getStrMeasure6())
                + format(recipe.getStrIngredient7(), recipe.getStrMeasure7())
                + format(recipe.getStrIngredient8(), recipe.getStrMeasure8())
                + format(recipe.getStrIngredient9(), recipe.getStrMeasure9())
                + format(recipe.getStrIngredient10(), recipe.getStrMeasure10())
                + format(recipe.getStrIngredient11(), recipe.getStrMeasure11())
                + format(recipe.getStrIngredient12(), recipe.getStrMeasure12())
                + format(recipe.getStrIngredient13(), recipe.getStrMeasure13())
                + format(recipe.getStrIngredient14(), recipe.getStrMeasure14())
                + format(recipe.getStrIngredient15(), recipe.getStrMeasure15())
                + format(recipe.getStrIngredient16(), recipe.getStrMeasure16())
                + format(recipe.getStrIngredient17(), recipe.getStrMeasure17())
                + format(recipe.getStrIngredient18(), recipe.getStrMeasure18())
                + format(recipe.getStrIngredient19(), recipe.getStrMeasure19())
                + format(recipe.getStrIngredient20(), recipe.getStrMeasure20()));

        text2.setText("Preparation: \n\n" + recipe.getStrInstructions());

        text1.setEnabled(false);
        text2.setEnabled(false);

    }

    private void setActionButton(RecipeDetailsDTO recipeDetails) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bm=((BitmapDrawable)imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                String temp= Base64.encodeToString(b, Base64.DEFAULT);
                mViewModel.insertRec(Mapper.mapRecipeDetailsDtoToRecipeDetails(true, recipeDetails, temp));
            }
        });

    }

    private void setActionButton (long id){
        button.setImageResource(R.drawable.ic_remove_circle_outline_black_24dp);
        button.setBackgroundColor(Color.red(1));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.deleteRec(id);
                button.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Recepi removed from favourites.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String format(String in, String q){
        if (in != null && q != null && in.length() > 0 && q.length() > 0)
            return "\t- " + in + ": " + q + "\n";
        else
            return "";
    }


}
