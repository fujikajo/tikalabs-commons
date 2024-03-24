package com.tikalabs.commons.csv.recordprocessor;

public interface RecordProcessor<T> {

    void process(T record);

}
