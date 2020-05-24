package si.uni_lj.fri.pbd.miniapp3.ui.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.RecyclerViewAdapter;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;
import si.uni_lj.fri.pbd.miniapp3.models.Mapper;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM;

public class FavoritesFragment extends Fragment {

    FavoritesViewModel mViewModel;

    RecyclerView recyclerView;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFav);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(),new ArrayList<>(), true));


        // Getting all recipes from database (all favorite recipes)
        mViewModel.getAllRecips().observe(getViewLifecycleOwner(), new Observer<List<RecipeDetails>>() {
            @Override
            public void onChanged(List<RecipeDetails> products) {

                ArrayList<RecipeSummaryIM> m = new ArrayList<>();

                for (RecipeDetails r: products){
                    m.add(Mapper.mapRecipeDetailsToRecipeSummaryIm(r));
                }

                recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), m, true));
            }
        });

        return view;
    }
}
