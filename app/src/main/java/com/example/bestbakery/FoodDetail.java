package com.example.bestbakery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.bestbakery.Database.Database;
import com.example.bestbakery.Model.Food;
import com.example.bestbakery.Model.Order;
import com.example.bestbakery.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class FoodDetail extends AppCompatActivity {


        TextView food_name,food_price,food_description;
        ImageView food_image;
        CollapsingToolbarLayout collapsingToolbarLayout;
        FloatingActionButton btnCart;
        ElegantNumberButton numberButton;


        String foodId="";

        FirebaseDatabase database;
        DatabaseReference foods;

        Food currentFood;

    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //firebasw
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //init view
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);

        //FloatingActionButton btnCart = findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

               /* Intent c = new Intent(FoodDetail.this,Cart.class);
                startActivity(c);*/
                //startActivity(new Intent(FoodDetail.this,Cart.class));
                //putextra

                HashMap<String,String> dataToUpload = new HashMap<>();
                //dataToUpload.put("Key", "Value");
//                 Add values here..
             //   dataToUpload.put("User", )
                dataToUpload.put("foodID", foodId); //....
                dataToUpload.put("name", currentFood.getName() );
                dataToUpload.put("quantity", numberButton.getNumber());
                dataToUpload.put("price", currentFood.getPrice());
                FirebaseDatabase.getInstance().getReference("Cart").push().setValue(dataToUpload).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i("Info", "Uploaded");
                        } else {
                            Log.i("Info", "Error");
                        }
                    }
                });

//                      new Database(getBaseContext()).addToCart(new Order(
//                        foodId,
//                        currentFood.getName(),
//                        numberButton.getNumber(),
//                        currentFood.getPrice()
//
//
//                ));

                Toast.makeText(FoodDetail.this,"Added to Cart",Toast.LENGTH_SHORT).show();
            }
        });

        food_description = (TextView)findViewById(R.id.food_description);
        food_name = (TextView)findViewById(R.id.food_name);
        food_price = (TextView)findViewById(R.id.food_price);
        food_image = (ImageView)findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get food id from intent
        if(getIntent()!=null)
            foodId = getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty())
        {
            getDetailFood(foodId);
        }

    }

    private void getDetailFood(final String foodId) {

        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Food food = dataSnapshot.getValue(Food.class);
               currentFood = dataSnapshot.getValue(Food.class);

                //set image
                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_image);
                //Picasso.with(getBaseContext().load(food.getImage()).into(food_image));

                collapsingToolbarLayout.setTitle(currentFood.getName());
                //collapsingToolbarLayout.setTitle(food.getName());
                food_price.setText(currentFood.getPrice());
                //food_price.setText(food.getPrice());
                food_name.setText(currentFood.getName());
                //food_name.setText(food.getName());
                food_description.setText(currentFood.getDescription());
                //food_description.setText(food.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
