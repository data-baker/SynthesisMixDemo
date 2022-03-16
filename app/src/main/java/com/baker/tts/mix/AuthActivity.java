package com.baker.tts.mix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AuthActivity extends BakerBaseActivity {
    private EditText editOnlineId, editOnlineSecret, editOfflineId, editOfflineSecret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        setTitle("授权");

        editOnlineId = findViewById(R.id.edit_online_id);
        editOnlineSecret = findViewById(R.id.edit_online_secret);
        editOfflineId = findViewById(R.id.edit_offline_id);
        editOfflineSecret = findViewById(R.id.edit_offline_secret);

        editOnlineId.setText(SharedPreferencesUtil.getOnlineClientId(AuthActivity.this));
        editOnlineSecret.setText(SharedPreferencesUtil.getOnlineSecret(AuthActivity.this));
        editOfflineId.setText(SharedPreferencesUtil.getOfflineClientId(AuthActivity.this));
        editOfflineSecret.setText(SharedPreferencesUtil.getOfflineSecret(AuthActivity.this));
    }

    public void onSaveClick(View view) {
        SharedPreferencesUtil.saveOnlineClientId(AuthActivity.this, editOnlineId.getText().toString().trim());
        SharedPreferencesUtil.saveOnlineSecret(AuthActivity.this, editOnlineSecret.getText().toString().trim());
        SharedPreferencesUtil.saveOfflineClientId(AuthActivity.this, editOfflineId.getText().toString().trim());
        SharedPreferencesUtil.saveOfflineSecret(AuthActivity.this, editOfflineSecret.getText().toString().trim());

        toast("保存成功");
    }

    public void onCleanClick(View view) {
        SharedPreferencesUtil.removeOnlineClientId(AuthActivity.this);
        SharedPreferencesUtil.removeOnlineSecret(AuthActivity.this);
        SharedPreferencesUtil.removeOfflineClientId(AuthActivity.this);
        SharedPreferencesUtil.removeOfflineSecret(AuthActivity.this);

        editOnlineId.setText("");
        editOnlineSecret.setText("");
        editOfflineId.setText("");
        editOfflineSecret.setText("");

        toast("清除成功");
    }
}