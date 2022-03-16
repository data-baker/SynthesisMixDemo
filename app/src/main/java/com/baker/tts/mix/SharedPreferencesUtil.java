package com.baker.tts.mix;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static final String SP_TABLE_NAME = "sp_table_baker_mix";
    private static final String ONLINE_CLIENT = "sp_baker_online_id";
    private static final String ONLINE_SECRET = "sp_baker_online_secret";
    private static final String OFFLINE_CLIENT = "sp_baker_offline_id";
    private static final String OFFLINE_SECRET = "sp_baker_offline_secret";
    private static final String ONLINE_VOICE_NAME = "sp_baker_online_voice_name";

    private static SharedPreferences mSharedPreferences;

    public static void saveOnlineVoiceName(Context context, String ttsOnlineVoiceName) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putString(ONLINE_VOICE_NAME, ttsOnlineVoiceName).apply();
    }

    public static void removeOnlineVoiceName(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().remove(ONLINE_VOICE_NAME).apply();
    }

    public static String getOnlineVoiceName(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getString(ONLINE_VOICE_NAME,"");
    }

    public static void saveOnlineClientId(Context context, String ttsOnlineClientId) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putString(ONLINE_CLIENT, ttsOnlineClientId).apply();
    }

    public static void saveOnlineSecret(Context context, String ttsOnlineSecret) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putString(ONLINE_SECRET, ttsOnlineSecret).apply();
    }

    public static void saveOfflineClientId(Context context, String ttsOfflineClientId) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putString(OFFLINE_CLIENT, ttsOfflineClientId).apply();
    }

    public static void saveOfflineSecret(Context context, String ttsOfflineSecret) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putString(OFFLINE_SECRET, ttsOfflineSecret).apply();
    }

    public static void removeOnlineClientId(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().remove(ONLINE_CLIENT).apply();
    }

    public static void removeOnlineSecret(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().remove(ONLINE_SECRET).apply();
    }

    public static void removeOfflineClientId(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().remove(OFFLINE_CLIENT).apply();
    }

    public static void removeOfflineSecret(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().remove(OFFLINE_SECRET).apply();
    }

    public static String getOnlineClientId(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getString(ONLINE_CLIENT,"");
    }

    public static String getOnlineSecret(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getString(ONLINE_SECRET, "");
    }

    public static String getOfflineClientId(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getString(OFFLINE_CLIENT, "");
    }

    public static String getOfflineSecret(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(SP_TABLE_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getString(OFFLINE_SECRET, "");
    }
}
