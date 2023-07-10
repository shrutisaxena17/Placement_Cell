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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView register, forgotpass;
    private EditText editTextemail , editTextpassword ;
    private Button signin;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register=(TextView) findViewById(R.id.gotoregister);
        register.setOnClickListener(this);

        forgotpass = (TextView) findViewById(R.id.forgotpassword);
        forgotpass.setOnClickListener(this);

        signin = (Button) findViewById(R.id.signin);
        signin.setOnClickListener(this);

        editTextemail=(EditText) findViewById(R.id.editTextemail);
        editTextpassword=(EditText) findViewById(R.id.editTextpassword);


        fAuth= FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gotoregister:
                startActivity(new Intent(this,RegisterActivity.class));
                break;


            case R.id.signin:
                userLogin();
                break;

            case R.id.forgotpassword:
                startActivity(new Intent(this, ForgotActivity.class));
                break;

        }
    }

    private void userLogin() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextemail.setError("Email is required!!");
            editTextemail.requestFocus();
            return;
        }

        if(email.equals("admin") && password.contentEquals("1234"))
        {
            startActivity(new Intent(LoginActivity.this , AddPostActivity.class));
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError("Please enter a valid email");
            editTextemail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextpassword.setError("Password is required!");
            editTextpassword.requestFocus();
            return;
        }

        if(password.length()<6){
            editTextpassword.setError("Minimum password length is 6 characters ");
            editTextpassword.requestFocus();
            return;
        }

        fAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        //redirect to main page
                        startActivity(new Intent(LoginActivity.this , MainActivity.class));
                        FirebaseUser user1 = fAuth.getCurrentUser();
                        if(task.getResult().getAdditionalUserInfo().isNewUser()){
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
                        }
                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your email to verify your account!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Failed to login! Please check your credentials again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}