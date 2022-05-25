package com.app.gasaloapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.gasaloapp.Interfaces.DataBaseResponse;
import com.app.gasaloapp.Utils.DataBaseManager;
import com.app.gasaloapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view==binding.btnSignIn)
        {
            if(isValid())
            {
                login();
            }
        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,Screen2.class));
        }
    }

    private void login() {


        DataBaseManager.authenticate(binding.editTextEmail.getText().toString(), binding.editTextPass.getText().toString(), new DataBaseResponse() {
            @Override
            public void onResponse(boolean isSuccess, String message, Object Data) {

                if(isSuccess)
                {
                    finish();
                    startActivity(new Intent(MainActivity.this,Screen2.class));
                }
                else
                {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValid() {

        if(binding.editTextEmail.getText().toString().isEmpty())
        {
            binding.editTextEmail.setError("Required Field");
            binding.editTextEmail.requestFocus();
            return false;
        }
        if(binding.editTextPass.getText().toString().isEmpty())
        {
            binding.editTextPass.setError("Required Field");
            binding.editTextPass.requestFocus();
            return false;
        }
        return true;
    }

}