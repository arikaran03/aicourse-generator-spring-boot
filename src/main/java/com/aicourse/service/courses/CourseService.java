package com.aicourse.service.courses;

import com.aicourse.geminiConnection.geminiConnection;
import com.aicourse.model.Course;
import com.aicourse.model.Lesson;
import com.aicourse.model.Module;
import com.aicourse.repo.CourseRepo;
import com.aicourse.repo.ModuleRepo;
import com.aicourse.utils.id.SnowflakeIdGenerator;
import com.aicourse.utils.json.JsonParserUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private ModuleRepo moduleRepo;

    @Autowired
    private geminiConnection geminiConnection;

    @Transactional
    public Course generateCourse(Map<String, String> payload, String creator) {

        String title = payload.get("title");
        String difficulty = payload.getOrDefault("difficulty", "Beginner");
        String duration = payload.getOrDefault("duration", "2 Hours");

        Course course = new Course();
        course.setId(SnowflakeIdGenerator.generateId());
        course.setTitle(title);
        course.setCreator(creator);

        String prompt = """
            Create a comprehensive course outline about "%s".
            Difficulty: %s
            Duration: %s

            Respond ONLY with raw JSON:
            {
              "title": "Course Title",
              "description": "Course Description",
              "modules": [
                {
                  "title": "Module Title",
                  "lessons": ["Lesson 1", "Lesson 2"]
                }
              ]
            }
            """.formatted(title, difficulty, duration);

        String response = geminiConnection.getResponse(prompt);

        String cleanJson = JsonParserUtil.extractRawJson(response);
        JsonNode courseJson = JsonParserUtil.parseStringToJsonObject(cleanJson);

        course.setDescription(
                courseJson.has("description")
                        ? courseJson.get("description").asText()
                        : "Generated course for " + title
        );

        List<Module> modules = new ArrayList<>();

        JsonNode modulesNode = courseJson.get("modules");
        if (modulesNode == null || !modulesNode.isArray()) {
            throw new RuntimeException("Invalid AI response: modules missing");
        }

        for (JsonNode moduleNode : modulesNode) {

            Module module = new Module();
            module.setId(SnowflakeIdGenerator.generateId());
            module.setTitle(moduleNode.get("title").asText());
            module.setCourse(course);

            List<Lesson> lessons = new ArrayList<>();

            for (JsonNode lessonNode : moduleNode.get("lessons")) {
                Lesson lesson = new Lesson();
                lesson.setId(SnowflakeIdGenerator.generateId());
                lesson.setTitle(lessonNode.asText());
                lesson.setContent(JsonParserUtil.parseStringToJsonObject("[]"));
                lesson.setModule(module);

                lessons.add(lesson);
            }

            module.setLessons(lessons);
            modules.add(module);
        }

        course.setModules(modules);

        return courseRepo.save(course);
    }

    public List<Course> getCoursesByCreator(String creator) {
        return courseRepo.findByCreator(creator);
    }

    public Course getCourseById(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public List<Module> getModulesByCourseName(String courseName) {
        return moduleRepo.findByCourse_Title(courseName);
    }
}
