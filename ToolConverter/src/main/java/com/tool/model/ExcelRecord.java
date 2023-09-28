package com.tool.model;

import java.util.HashMap;
import java.util.Map;

public class ExcelRecord {

    private String recordName;
    private Map<String, Object> data;

    public ExcelRecord(String recordName) {
        this.recordName = recordName;
        data = new HashMap<>();
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public void setValue(String columnName, Object value) {
        data.put(columnName, value);
    }

    public Object getValue(String columnName) {
        return data.get(columnName);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ExcelRecord " + data.toString();
    }
}
