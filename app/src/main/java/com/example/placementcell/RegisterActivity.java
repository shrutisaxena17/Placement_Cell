package com.example.placementcell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView login;
    private FirebaseAuth fAuth;
    private EditText editTextname,editTextemail,editTextpassword,editTextusername;
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth=FirebaseAuth.getInstance();

        login=(TextView) findViewById(R.id.gotologin);
        login.setOnClickListener(this);

        register=(Button) findViewById(R.id.registerbutton);
        register.setOnClickListener(this);

        editTextname=(EditText) findViewById(R.id.fullName);
        editTextusername=(EditText) findViewById(R.id.username);
        editTextemail=(EditText) findViewById(R.id.email);
        editTextpassword=(EditText) findViewById(R.id.password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gotologin:
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.registerbutton:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email=editTextemail.getText().toString().trim();
        String password=editTextpassword.getText().toString().trim();
        String fullName=editTextname.getText().toString().trim();
        String username=editTextusername.getText().toString().trim();

        if (fullName.isEmpty()){
            editTextname.setError("Full Name is required");
            editTextname.requestFocus();
            return;
        }
        if(username.isEmpty()){
            editTextusername.setError("Age is required");
            editTextusername.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextemail.setError("Email is required");
            editTextemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError("Please provide valid email!");
            editTextemail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextpassword.setError("Password is required");
            editTextemail.requestFocus();
            return;
        }
        if(password.length() <6){
            editTextpassword.setError("Password length should be atleast 6 characters");
            editTextpassword.requestFocus();
            return;
        }

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(fullName,username,email);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                FirebaseUser user1 = fAuth.getCurrentUser();
                                String email = user1.getEmail();
                                String uid = user1.getUid();
                                HashMap<Object,String> hashMap = new HashMap<>();
                                hashMap.put("email",email);
                                hashMap.put("uid",uid);
                                hashMap.put("name","");
                                hashMap.put("phone","");
                                hashMap.put("age","");
                                hashMap.put("status","");
                                hashMap.put("image","");
                                hashMap.put("cover","");

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(uid).setValue(hashMap);
                            }else{
                                Toast.makeText(RegisterActivity.this, "Failed to register user! Try again!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity.this, "Failed to register user! Try again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}