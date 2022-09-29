package com.project.amit.onlinefurniturestore.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.amit.onlinefurniturestore.R;
import com.project.amit.onlinefurniturestore.activites.PlaceOrderActivity;
import com.project.amit.onlinefurniturestore.adapter.MyCartAdapter;
import com.project.amit.onlinefurniturestore.models.MyCartViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {

    TextView overAllAmount;
    Toolbar toolbar;
    RecyclerView recyclerView;
    MyCartAdapter cartAdapter;
    List<MyCartViewModel> cartViewModelList;

     FirebaseAuth auth;
     FirebaseFirestore firestore;

//    Button buynow;

    public MyCartFragment(){
        // Require empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
            // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cart,container,false);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = root.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // this

        overAllAmount = root.findViewById(R.id.textView);

        cartViewModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(getActivity(),cartViewModelList); // this
        recyclerView.setAdapter(cartAdapter);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

//                                String documentId = documentSnapshot.getId();
                                MyCartViewModel cartViewModel = documentSnapshot.toObject(MyCartViewModel.class);

//                                cartViewModel.setDocumentId(documentId);
                                cartViewModelList.add(cartViewModel);
                                cartAdapter.notifyDataSetChanged();
                            }
                            calculateTotalAmount(cartViewModelList);
                        }

                    }
                });

        // buy the item in my cart cash on delivery
/*
        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PlaceOrderActivity.class);
                intent.putExtra("itemList", (Serializable) myCartViewModelList);
                startActivity(intent);
            }
        });
*/

        return root;
    }

    private void calculateTotalAmount(List<MyCartViewModel> cartViewModelList) {

        double totalAmount = 0.0;
        for (MyCartViewModel myCartViewModel : cartViewModelList){
            totalAmount += myCartViewModel.getTotalPrice();
        }
        overAllAmount.setText("Total Amount : ₹ " + totalAmount);
    }

}
