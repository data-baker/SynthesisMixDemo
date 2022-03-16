package com.baker.tts.mix;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

public class BakerBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBack() {
        finish();
    }

    public void toast(String text) {
        Message message = Message.obtain();
        message.obj = text;
        handler.sendMessage(message);
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            Toast.makeText(BakerBaseActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
        }
    };
}
