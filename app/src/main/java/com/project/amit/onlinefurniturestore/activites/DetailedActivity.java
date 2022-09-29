package com.project.amit.onlinefurniturestore.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.amit.onlinefurniturestore.R;
import com.project.amit.onlinefurniturestore.models.ShowAllModel;
import com.project.amit.onlinefurniturestore.models.AllProductsModel;
import com.project.amit.onlinefurniturestore.models.NewProductsModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {



    ImageView detailedImg;
    TextView description,name,price,rating,quantity;
    Button addToCart,buyNow;
    ImageView addItems,removeItems;

    Toolbar toolbar;
    int totalQuantity = 1;
    int totalPrice = 0;

    //New Products
    NewProductsModel newProductsModel = null;


    //All Product
    AllProductsModel allProductsModel = null;

    //ShowAll Item
    ShowAllModel showAllModel = null;

    FirebaseAuth auth; // use for addtocart
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object obj = getIntent().getSerializableExtra("detailed");

        if (obj instanceof NewProductsModel){
            newProductsModel = (NewProductsModel) obj;
        }else if (obj instanceof AllProductsModel){
            allProductsModel = (AllProductsModel) obj;
        }else if (obj instanceof AllProductsModel){
            allProductsModel = (AllProductsModel) obj;
        }

        detailedImg = findViewById(R.id.detailed_img);
        description = findViewById(R.id.detailed_desc);
        name = findViewById(R.id.detailed_name);
        price = findViewById(R.id.detailed_price);
        rating = findViewById(R.id.detailed_rating);
        quantity = findViewById(R.id.quantity);

        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);

        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);

        //New Products
        if (newProductsModel != null){
            Glide.with(getApplicationContext()).load(newProductsModel.getImg_url()).into(detailedImg);
            description.setText(newProductsModel.getDescription());
            name.setText(newProductsModel.getName());
            price.setText(String.valueOf(newProductsModel.getPrice()));
            rating.setText(newProductsModel.getRating());
            name.setText(newProductsModel.getName());

            totalPrice = newProductsModel.getPrice() * totalQuantity;
        }


        //All Product
        if (allProductsModel != null){
            Glide.with(getApplicationContext()).load(allProductsModel.getImg_url()).into(detailedImg);
            description.setText(allProductsModel.getDescription());
            name.setText(allProductsModel.getName());
            price.setText(String.valueOf(allProductsModel.getPrice()));
            rating.setText(allProductsModel.getRating());
            name.setText(allProductsModel.getName());

            totalPrice = allProductsModel.getPrice() * totalQuantity;
        }

            //ShowAll Product
            if (showAllModel != null) {
                Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
                description.setText(showAllModel.getDescription());
                name.setText(showAllModel.getName());
                price.setText(String.valueOf(showAllModel.getPrice()));
                rating.setText(showAllModel.getRating());
                name.setText(showAllModel.getName());

                totalPrice = showAllModel.getPrice() * totalQuantity;
            }

            //Buy Now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedActivity.this,AddressActivity.class);

                if (newProductsModel != null) {
                    intent.putExtra("item",newProductsModel);
                }
                if (allProductsModel != null) {
                    intent.putExtra("item",allProductsModel);
                }
                if (showAllModel != null){
                    intent.putExtra("item",showAllModel);
                }
                startActivity(intent);
            }
        });
            //Add to cart
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addtoCart();

                }
            });

            addItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(totalQuantity < 10){
                        totalQuantity++;
                        quantity.setText(String.valueOf(totalQuantity));

                        if (newProductsModel !=null){
                            totalPrice = newProductsModel.getPrice() * totalQuantity;
                        }if (allProductsModel !=null){
                            totalPrice = allProductsModel.getPrice() * totalQuantity;
                        }if (showAllModel !=null){
                            totalPrice = showAllModel.getPrice() * totalQuantity;
                        }
                    }

                }
            });

            removeItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(totalQuantity > 1){
                        totalQuantity--;
                        quantity.setText(String.valueOf(totalQuantity));
                    }

                }
            });
        }

        // Add to cart

        private void addtoCart(){

            String saveCurrentTime,saveCurrentDate;

            Calendar calForDate = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat(" dd-MM-yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calForDate.getTime());

            final HashMap<String,Object> cartMap = new HashMap<>();

            cartMap.put("productName",name.getText().toString());
            cartMap.put("productPrice",price.getText().toString());
            cartMap.put("currentTime",saveCurrentTime);
            cartMap.put("currentDate",saveCurrentDate);
            cartMap.put("totalQuantity",quantity.getText().toString());
            cartMap.put("totalPrice",totalPrice);


            firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                    .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(DetailedActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

        }
    }
