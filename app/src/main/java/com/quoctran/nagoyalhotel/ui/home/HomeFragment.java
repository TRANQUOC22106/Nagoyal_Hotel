package com.quoctran.nagoyalhotel.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.quoctran.nagoyalhotel.adapter.HotelAdapter;
import com.quoctran.nagoyalhotel.pojo.Hotel;
import com.quoctran.nagoyalhotel.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference listHotel = db.collection("HotelList");
    private RecyclerView recyclerView;
    private HotelAdapter adapter;
    private View root;
    private boolean actionSearch = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        EditText edt_search_input = root.findViewById(R.id.edt_search_input);

        ImageView imv_search_btn = root.findViewById(R.id.imv_search_btn);
        imv_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionSearch) {
                    showAllHotelList();
                    actionSearch = false;
                    imv_search_btn.setImageResource(R.drawable.ic_search_black_);
                } else {
                    String inputSearchByName = edt_search_input.getText().toString();
                    if (inputSearchByName.equals("")) {
                        Toast.makeText(getContext(), getString(R.string.please_enter_text_search), Toast.LENGTH_LONG).show();
                    } else {
                        Query searchByName = listHotel.whereGreaterThanOrEqualTo("nameHotel", "!")
                                .whereLessThanOrEqualTo("nameHotel", inputSearchByName + "\uf8ff");
                        searchByNameFromFirebase(searchByName);
                        actionSearch = true;
                        imv_search_btn.setImageResource(R.drawable.ic_backto_search);

                    }
                }
            }
        });
        showAllHotelList();
        return root;
    }

    private void searchByNameFromFirebase(Query querySearch) {
        FirestoreRecyclerOptions<Hotel> options = new FirestoreRecyclerOptions.Builder<Hotel>()
                .setQuery(querySearch, Hotel.class)
                .build();
        adapter = new HotelAdapter(options);
        recyclerView.removeAllViews();

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void showAllHotelList() {
        Query queryShowAll = listHotel.orderBy("priceHotel", Query.Direction.DESCENDING);

        searchByNameFromFirebase(queryShowAll);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}