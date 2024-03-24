package com.tikalabs.commons.csv.recordprocessor;

@FunctionalInterface
public interface ProgressCallback {
    void updateProgress(int current, int total);
}