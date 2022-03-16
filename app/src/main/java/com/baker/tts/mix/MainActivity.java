package com.baker.tts.mix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();
    }

    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
//            handler.sendEmptyMessageDelayed(1, 2000);
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的存储权限", 1, permissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull @NotNull List<String> perms) {
//        handler.sendEmptyMessageDelayed(1, 2000);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull @NotNull List<String> perms) {
        Toast.makeText(this, "请同意相关权限，否则部分功能无法使用", Toast.LENGTH_SHORT).show();
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