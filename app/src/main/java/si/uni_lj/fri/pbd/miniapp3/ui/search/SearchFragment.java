package si.uni_lj.fri.pbd.miniapp3.ui.search;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.zhanghai.android.materialprogressbar.IndeterminateHorizontalProgressDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.RecyclerViewAdapter;
import si.uni_lj.fri.pbd.miniapp3.adapter.SpinnerAdapter;
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
    long time;
    ProgressBar progressBar;

    private ConnectivityManager mNwManager;
    private ConnectivityManager.NetworkCallback mNwCallback;
    private boolean internet;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);

        internet = false;

        mNwCallback = new ConnectivityManager.NetworkCallback(){
            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    internet = true;
                }
                else
                    internet = false;
            }
        };
        mNwManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        mNwManager.registerDefaultNetworkCallback(mNwCallback);

        spinner = view.findViewById(R.id.action_bar_spinner);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        progressBar = view.findViewById(R.id.indeterminate_horizontal_progress);

        progressBar.setIndeterminateDrawable(new IndeterminateHorizontalProgressDrawable(getActivity()));

        progressBar.setVisibility(view.GONE);

        time = -5000;

        getIngridients();

       recyclerView = view.findViewById(R.id.recyclerView);
       recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
       recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), new ArrayList<>(), false));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getIngridients();
            }
        });

        return view;
    }

    private void getMeals () {
        long dtime = System.currentTimeMillis();
        if (dtime - time > 5000) {
            if (internet && spinner.getSelectedItem() != null) {
                progressBar.setVisibility(View.VISIBLE);
                time = dtime;
                mRestClient.getRecipeWithIngredient(spinner.getSelectedItem().toString()).enqueue(new Callback<RecipesByIngredientDTO>() {
                    @Override
                    public void onResponse(Call<RecipesByIngredientDTO> call, Response<RecipesByIngredientDTO> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getMeals() != null)
                                if (response.body().getMeals() != null) {
                                    ArrayList<RecipeSummaryIM> list = new ArrayList<>();
                                    for (RecipeSummaryDTO summaryDTO : response.body().getMeals()) {
                                        RecipeSummaryIM m = new RecipeSummaryIM(summaryDTO.getStrMeal(), summaryDTO.getStrMealThumb(), summaryDTO.getIdMeal(), "");
                                        list.add(m);
                                    }
                                    recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), list, false));
                                } else {
                                    recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), new ArrayList<>(), false));
                                    Toast.makeText(getActivity(), "Ingredient has no recipes.", Toast.LENGTH_LONG).show();
                                }
                            else {
                                recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), new ArrayList<>(), false));
                                Toast.makeText(getActivity(), "Ingredient has no recipes.", Toast.LENGTH_LONG).show();
                            }
                            swipeRefreshLayout.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<RecipesByIngredientDTO> call, Throwable t) {
                        Toast.makeText(getActivity(), "Something went wrong while fetching the recipes.", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
            else {
                Toast.makeText(getActivity(), "There is no internet connection.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void getIngridients() {

        LinkedList<String> list = new LinkedList<>();

        progressBar.setVisibility(View.VISIBLE);

        if (internet) {
            mRestClient.getAllIngredients().enqueue(new Callback<IngredientsDTO>() {
                @Override
                public void onResponse(Call<IngredientsDTO> call, Response<IngredientsDTO> response) {
                    if (response.isSuccessful()) {
                        List<IngredientDTO> ingredients = response.body().getIngredients();
                        for (IngredientDTO ingredient : ingredients) {
                            list.add(ingredient.getStrIngredient());
                        }
                        spinner.setAdapter(new SpinnerAdapter(getActivity(), list));
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mRestClient.getRecipeWithIngredient(spinner.getSelectedItem().toString()).enqueue(new Callback<RecipesByIngredientDTO>() {
                                    @Override
                                    public void onResponse(Call<RecipesByIngredientDTO> call, Response<RecipesByIngredientDTO> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().getMeals() != null)
                                                if (response.body().getMeals() != null) {
                                                    ArrayList<RecipeSummaryIM> list = new ArrayList<>();
                                                    for (RecipeSummaryDTO summaryDTO : response.body().getMeals()) {
                                                        RecipeSummaryIM m = new RecipeSummaryIM(summaryDTO.getStrMeal(), summaryDTO.getStrMealThumb(), summaryDTO.getIdMeal(), "");
                                                        list.add(m);
                                                    }
                                                    recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), list, false));
                                                    getMeals();
                                                } else {
                                                    recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), new ArrayList<>(), false));
                                                    Toast.makeText(getActivity(), "Ingredient has no recipes.", Toast.LENGTH_LONG).show();
                                                }
                                            else {
                                                recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), new ArrayList<>(), false));
                                                Toast.makeText(getActivity(), "Ingredient has no recipes.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(Call<RecipesByIngredientDTO> call, Throwable t) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Something went wrong while fetching the recipes.", Toast.LENGTH_SHORT).show();
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
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Something went wrong with getting all ingredients.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(getActivity(), "There is no internet connection.", Toast.LENGTH_LONG).show();
        }

    }
}
