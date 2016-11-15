package com.premsuraj.foldercleaner.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Premsuraj
 */

public class DataModelManager {
    private static final String SHARED_PREF_NAME = "Cleaner";
    private Context mContext;
    private DataModel mModel = null;

    public DataModelManager(Context context) {
        this.mContext = context;
    }

    public DataModel get() {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        if(!preferences.contains(DataModel.PREFERENCES_KEY)) {
            mModel = createDefault(preferences);
        } else {
            String jsonOfData = preferences.getString(DataModel.PREFERENCES_KEY, null);
            try {
                mModel = new Gson().fromJson(jsonOfData, DataModel.class);
            } catch (Exception ex) {
                mModel = createDefault(preferences);
            }
        }
        return mModel;
    }

    private DataModel createDefault(SharedPreferences preferences) {
        DataModel model  = getDefaultModel();
        String jsonOfData = new Gson().toJson(model);
        preferences.edit().putString(DataModel.PREFERENCES_KEY, jsonOfData).apply();
        return model;
    }

    private DataModel getDefaultModel() {
        DataModel model = new DataModel();
        model.daysToKeep = 7;
        String externalDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        model.foldersToClean = new String[] {
                externalDirectory + "/Whatsapp/media/whatsapp images",
                externalDirectory + "/Whatsapp/media/whatsapp images/sent",
                externalDirectory + "/Whatsapp/media/whatsapp video",
                externalDirectory + "/Whatsapp/media/whatsapp video/sent",
        };
        model.typesToIgnore = new String[] {
                ".nomedia"
        };
        return model;
    }

    public void update(DataModel model) {
        mModel = model;
        String jsonOfData = new Gson().toJson(mModel);
        mContext.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit()
                .putString(DataModel.PREFERENCES_KEY, jsonOfData).apply();
    }
}
