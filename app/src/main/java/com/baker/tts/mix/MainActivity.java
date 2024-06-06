package com.baker.tts.mix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();
    }

    private void getPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(permissions, 10010);

        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    public void onAuthClick(View view) {
        startActivity(new Intent(MainActivity.this, AuthActivity.class));
    }

    public void onSynthesisOnlineClick(View view) {
        startActivity(new Intent(MainActivity.this, SynthesisOnlineActivity.class));
    }

    public void onSynthesisOfflineClick(View view) {
        startActivity(new Intent(MainActivity.this, SynthesisOfflineActivity.class));
    }

    public void onSynthesisMixClick(View view) {
        startActivity(new Intent(MainActivity.this, SynthesisMixActivity.class));
    }
}