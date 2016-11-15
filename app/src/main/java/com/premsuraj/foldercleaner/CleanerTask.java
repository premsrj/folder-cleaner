package com.premsuraj.foldercleaner;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateUtils;

import com.premsuraj.foldercleaner.model.DataModel;
import com.premsuraj.foldercleaner.model.DataModelManager;

import java.io.File;
import java.util.List;

/**
 * Created by Premsuraj
 */
public class CleanerTask extends AsyncTask<Void, Void, Integer> {

    Listener mListener;
    Context mContext;
    List<String> foldersToClear;
    List<String> patternsToIgnore;
    long lastModifiedCutOff;

    public CleanerTask(Context mContext, Listener listener) {
        this.mContext = mContext;
        this.mListener = listener;
        DataModel data = new DataModelManager(mContext).get();
        foldersToClear = data.getFoldersToClean();
        patternsToIgnore = data.getTypesToIgnore();
        lastModifiedCutOff = System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS * data.getDaysToKeep();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int numberOfFilesDeleted = 0;
        for(String folderName : foldersToClear) {
            File[] files = listFiles(folderName);
            if(files != null) {
                for (File file : files) {
                    if (canDelete(file)) {
                        if (file.delete()) {
                            numberOfFilesDeleted++;
                        }
                    }
                }
            }
        }

        return numberOfFilesDeleted;
    }

    private boolean canDelete(File file) {
        if(!file.isFile()) return false;

        for(String pattern : patternsToIgnore) {
            if(file.getName().endsWith(pattern)) {
                return false;
            }
        }

        if (file.lastModified() > lastModifiedCutOff) {
            return false;
        }

        return true;
    }

    private File[] listFiles(String folderName) {
        return new File(folderName).listFiles();
    }

    @Override
    protected void onPostExecute(Integer count) {
        if (mListener != null) {
            mListener.onCleanerTaskDone(true, count);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mListener != null) {
            mListener.onCleanerTaskDone(false, 0);
        }
    }

    public static interface Listener {
        void onCleanerTaskDone(boolean isSuccess, int filesCleaned);
    }
}
