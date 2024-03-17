package de.tikalabs.commons.fixedwidthdata;

import java.util.ArrayList;
import java.util.List;

public class FixedWidthRecord {
    private final List<FixedWidthField> fields = new ArrayList<>();

    public void addField(FixedWidthField field) {
        fields.add(field);
    }

    public Object[] parseLine(String line) {
        Object[] values = new Object[fields.size()];
        int i = 0;
        int currentPosition = 0;
        for (FixedWidthField field : fields) {
            String rawValue = line.substring(currentPosition, currentPosition + field.getLength());
            switch (field.getType()) {
                case STRING:
                    values[i] = rawValue;
                    break;
                case INTEGER:
                    values[i] = Integer.parseInt(rawValue);
                    break;
                case DOUBLE:
                    values[i] = Double.parseDouble(rawValue) / 100; // Beispiel für Dezimalumrechnung
                    break;
                case DATE:
                    values[i] = rawValue.substring(0, 4) + "-" + rawValue.substring(4, 6) + "-" + rawValue.substring(6, 8); // Beispiel
                    // für
                    // Datumsformat
                    break;
            }
            i++;
            currentPosition += field.getLength();
        }
        return values;
    }
}
