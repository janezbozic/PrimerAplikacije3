package si.uni_lj.fri.pbd.miniapp3.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.RecyclerViewAdapter;
import si.uni_lj.fri.pbd.miniapp3.adapter.SpinnerAdapter;
import si.uni_lj.fri.pbd.miniapp3.models.Mapper;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeDetailsIM;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipeSummaryDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientDTO;
import si.uni_lj.fri.pbd.miniapp3.rest.RestAPI;
import si.uni_lj.fri.pbd.miniapp3.rest.ServiceGenerator;

public class SearchFragment extends Fragment{

    View view;
    Spinner spinner;
    private RestAPI mRestClient = ServiceGenerator.createService(RestAPI.class);
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(int position) {
        SearchFragment s = new SearchFragment();
        return s;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);

        spinner = view.findViewById(R.id.action_bar_spinner);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        getIngridients();

       recyclerView = view.findViewById(R.id.recyclerView);
       recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
       recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), new ArrayList<>(), false));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRestClient.getRecipeWithIngredient(spinner.getSelectedItem().toString()).enqueue(new Callback<RecipesByIngredientDTO>() {
                    @Override
                    public void onResponse(Call<RecipesByIngredientDTO> call, Response<RecipesByIngredientDTO> response) {
                        if (response.isSuccessful()){
                            if (response.body().getMeals() != null)
                                if (response.body().getMeals() != null){
                                    ArrayList<RecipeSummaryIM> list = new ArrayList<>();
                                    for (RecipeSummaryDTO summaryDTO: response.body().getMeals()){
                                        RecipeSummaryIM m = new RecipeSummaryIM(summaryDTO.getStrMeal(), summaryDTO.getStrMealThumb(), summaryDTO.getIdMeal(), "");
                                        list.add(m);
                                    }
                                    recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), list, false));
                                }
                                else
                                    recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), new ArrayList<>(), false));
                            else
                                recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), new ArrayList<>(), false));
                        }
                    }

                    @Override
                    public void onFailure(Call<RecipesByIngredientDTO> call, Throwable t) {

                    }
                });
            }
        });

        return view;
    }

    private void getIngridients() {

        LinkedList<String> list = new LinkedList<>();

        mRestClient.getAllIngredients().enqueue(new Callback<IngredientsDTO>() {
            @Override
            public void onResponse(Call<IngredientsDTO> call, Response<IngredientsDTO> response) {
                if (response.isSuccessful()) {
                    List<IngredientDTO> ingredients = response.body().getIngredients();
                    for (IngredientDTO ingredient: ingredients){
                        list.add(ingredient.getStrIngredient());
                    }
                    spinner.setAdapter(new SpinnerAdapter(getActivity(), list));
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            mRestClient.getRecipeWithIngredient(spinner.getSelectedItem().toString()).enqueue(new Callback<RecipesByIngredientDTO>() {
                                @Override
                                public void onResponse(Call<RecipesByIngredientDTO> call, Response<RecipesByIngredientDTO> response) {
                                    if (response.isSuccessful()){
                                        if (response.body().getMeals() != null)
                                            if (response.body().getMeals() != null){
                                                ArrayList<RecipeSummaryIM> list = new ArrayList<>();
                                                for (RecipeSummaryDTO summaryDTO: response.body().getMeals()){
                                                    RecipeSummaryIM m = new RecipeSummaryIM(summaryDTO.getStrMeal(), summaryDTO.getStrMealThumb(), summaryDTO.getIdMeal(), "");
                                                    list.add(m);
                                                }
                                                recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), list, false));
                                            }
                                            else
                                                recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), new ArrayList<>(), false));
                                        else
                                            recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), new ArrayList<>(), false));
                                    }
                                }

                                @Override
                                public void onFailure(Call<RecipesByIngredientDTO> call, Throwable t) {

                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<IngredientsDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong with getting all ingredients.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
