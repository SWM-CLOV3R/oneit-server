package clov3r.domain.domains.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(JsonNode attribute) {
    try {
      return attribute == null ? null : objectMapper.writeValueAsString(attribute);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to convert JsonNode to String", e);
    }
  }

  @Override
  public JsonNode convertToEntityAttribute(String dbData) {
    try {
      return dbData == null ? null : objectMapper.readTree(dbData);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to convert String to JsonNode", e);
    }
  }
}