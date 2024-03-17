package de.tikalabs.commons.fixedwidthdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FixedWidthSchema {

    public static final String FIELDS = "fields";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_LENGTH = "length";
    public static final String FIELD_ALIGNMENT = "alignment";
    public static final char DEFAULT_PADDING_CHARACTER = ' ';
    public static final String DEFAULT_ALIGNMENT = "left";
    private static final Logger logger = LoggerFactory.getLogger(FixedWidthSchema.class);
    private final List<FixedWidthField> fixedWidthFields;
    private JsonNode fieldsNode;

    public FixedWidthSchema(String jsonSchema) {
        this.fixedWidthFields = new ArrayList<>();
        try {
            initialize(jsonSchema);
        } catch (JSONException | IOException e) {
            logger.error("{}", e);
            throw new IllegalArgumentException(e);
        }

    }

    private void initialize(String jsonSchema) throws JSONException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonSchema);
        this.fieldsNode = rootNode.path(FIELDS);
        for (JsonNode fieldNode : fieldsNode) {
            FixedWidthField field = objectMapper.readValue(fieldNode.toPrettyString(), FixedWidthField.class);
            fixedWidthFields.add(field);
        }
    }

    public JsonNode getFieldsNode() {
        return fieldsNode;
    }

    public void setFieldsNode(JsonNode fieldsNode) {
        this.fieldsNode = fieldsNode;
    }

    public List<FixedWidthField> getFixedWidthFields() {
        return fixedWidthFields;
    }

}
