package com.premsuraj.foldercleaner.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Premsuraj
 */

public class DataModel {
    static final String PREFERENCES_KEY = "data_model";

    int daysToKeep;
    String[] foldersToClean;
    String[] typesToIgnore;

    public int getDaysToKeep() {
        return daysToKeep;
    }

    public void setDaysToKeep(int daysToKeep) {
        this.daysToKeep = daysToKeep;
    }

    public List<String> getFoldersToClean() {
        if(foldersToClean == null)
            return null;

        return new ArrayList<>(Arrays.asList(foldersToClean));
    }

    public void setFoldersToClean(List<String> foldersToClean) {
        if(foldersToClean == null) {
            this.foldersToClean = null;
            return;
        }

        this.foldersToClean = foldersToClean.toArray(new String[]{});
    }

    public List<String> getTypesToIgnore() {
        if(typesToIgnore == null)
            return null;

        return new ArrayList<>(Arrays.asList(typesToIgnore));
    }

    public void setTypesToIgnore(List<String> typesToIgnore) {
        if(typesToIgnore == null) {
            this.typesToIgnore = null;
            return;
        }

        this.typesToIgnore = typesToIgnore.toArray(new String[]{});
    }
}
