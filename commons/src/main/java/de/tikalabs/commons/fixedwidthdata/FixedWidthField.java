package de.tikalabs.commons.fixedwidthdata;

public class FixedWidthField {

    private String name;
    private int length;
    private String alignment;
    private String padding;
    private String defaultValue;
    private FieldType type;

    public FixedWidthField(String name, String type, int length, String alignment, String padding,
                           String defaultValue) {

        this.setName(name);
        this.setLength(length);
        this.setAlignment(alignment);
        this.setPadding(padding);
        this.setDefaultValue(defaultValue);
        this.setType(type);

    }

    public FixedWidthField() {

    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(String type) {
        this.type = FieldType.valueOf(type.toUpperCase());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum FieldType {
        STRING, INTEGER, DOUBLE, DATE
    }
}
