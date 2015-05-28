package org.shm.monitoring.helper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.shm.monitoring.domain.ProjectConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public final class MapperHelper {

    private MapperHelper() {
    }

    ;

    public static List<ProjectConfiguration> json2List(String json)
            throws IOException, JsonParseException, JsonMappingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<List<ProjectConfiguration>>() {
        });

    }

    public static Map<String, List<ProjectConfiguration>> json2Map(String json)
            throws IOException, JsonParseException, JsonMappingException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json,
                new TypeReference<Map<String, List<ProjectConfiguration>>>() {
                });

    }

    public static String list2json(List<ProjectConfiguration> projectConfigurations)
            throws IOException, JsonParseException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(projectConfigurations);
    }

    public static String list2json(
            Map<String, List<ProjectConfiguration>> configurations)
            throws IOException, JsonParseException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(configurations);

    }
}
