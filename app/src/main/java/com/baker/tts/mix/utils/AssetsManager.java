package com.baker.tts.mix.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AssetsManager {
    private AssetsManager() {
    }

    private static class InnerClass {
        private static final AssetsManager INSTANCE = new AssetsManager();
    }

    public static AssetsManager getInstance() {
        return InnerClass.INSTANCE;
    }


    public String getAssetsString(Context mContext, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = mContext.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String lineString;
            while ((lineString = bf.readLine()) != null) {
                stringBuilder.append(lineString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    public ArrayList<String> getAssetsStrings(Context mContext, String fileName) {
        ArrayList<String> strings = new ArrayList<>();
        try {
            AssetManager assetManager = mContext.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String lineString;
            while ((lineString = bf.readLine()) != null) {
                strings.add(lineString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }
}
