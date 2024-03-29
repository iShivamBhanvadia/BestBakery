package com.example.bestbakery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bestbakery.Common.Common;
import com.example.bestbakery.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

     EditText edtPhone,edtPassword;
     Button btnSignIn;

     @Override
    protected void onCreate(Bundle saveInstanceState){
         super.onCreate(saveInstanceState);
         setContentView(R.layout.activity_sign_in);

         //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
         edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
         btnSignIn = (Button)findViewById(R.id.btnSignIn);

         final FirebaseDatabase database = FirebaseDatabase.getInstance();
         final DatabaseReference table_user = database.getReference("User");

         btnSignIn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                 mDialog.setMessage("Please wait....");
                 mDialog.show();

                 table_user.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                         //Check if user not exist in DB
                         if(dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                             //Get user information
                             mDialog.dismiss();

                             User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                             user.setPhone(edtPhone.getText().toString());//set phone

                             if (user.getPassword().equals(edtPassword.getText().toString())) {
                                 Intent homeIntent = new Intent(SignIn.this,Home.class);
                                 Common.currentuser = user;
                                 startActivity(homeIntent);
                                 finish();
                                 Toast.makeText(SignIn.this,"Sign In Successful...",Toast.LENGTH_SHORT).show();
                             }
                             else {
                                 Toast.makeText(SignIn.this, "Wrong password,Sign In failed...", Toast.LENGTH_SHORT).show();
                             }
                         }
                         else {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this,"User not exist in DataBase",Toast.LENGTH_SHORT).show();
                             }


                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });
             }
         });
     }
}
