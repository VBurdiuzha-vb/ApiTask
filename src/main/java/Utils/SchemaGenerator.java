package Utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import dto.PeopleDto;

public class SchemaGenerator {
    public static JsonNode generateSchemaFromDto(Class dto){
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
        SchemaGeneratorConfig config = configBuilder.build();
        com.github.victools.jsonschema.generator.SchemaGenerator generator = new com.github.victools.jsonschema.generator.SchemaGenerator(config);
        return generator.generateSchema(dto);
    }
}
