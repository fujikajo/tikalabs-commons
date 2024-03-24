package com.tikalabs.commons.csv.recordprocessor;

import java.util.ArrayList;
import java.util.List;

public class ListRecordProcessor<T> implements RecordProcessor<T> {
    private List<T> records = new ArrayList<>();

    @Override
    public void process(T record) {
        records.add(record);
    }

    public List<T> getRecords() {
        return records;
    }
}
