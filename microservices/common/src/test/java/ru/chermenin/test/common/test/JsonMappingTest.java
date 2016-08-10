package ru.chermenin.test.common.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import ru.chermenin.test.common.dto.Failed;
import ru.chermenin.test.common.dto.Project;

import java.util.Objects;

/**
 * Tests to check DTO mapping to JSON.
 *
 * @author chermenin
 */
public class JsonMappingTest {

    final private static String PROJECT_JSON = "{\"id\":0,\"version\":0,\"data\":\"value\"}";
    final private static String FAILED_JSON = "{\"slave_port\":8081,\"response_status\":500,\"project_id\":0,\"version\":0,\"data\":\"value\"}";

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testProject() throws JsonProcessingException {
        Project project = new Project(0, 0, "value");
        String value = mapper
                .writeValueAsString(project)
                .replaceAll(" ", "");
        assert Objects.equals(value, PROJECT_JSON) : "Bad Project mapping: " + value;
    }

    @Test
    public void testFailedProject() throws JsonProcessingException {
        Failed failed = new Failed(0, 0, "value", 8081, 500);
        String value = mapper
                .writeValueAsString(failed)
                .replaceAll(" ", "");
        assert Objects.equals(value, FAILED_JSON): "Bad Failed mapping: " + value;
    }
}
