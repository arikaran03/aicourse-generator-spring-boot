package com.aicourse.controller;

import com.aicourse.geminiConnection.geminiConnection;
import com.aicourse.model.Course;
import com.aicourse.model.Lesson;
import com.aicourse.model.Module;
import com.aicourse.repo.CourseRepo;
import com.aicourse.repo.ModuleRepo;
import com.aicourse.utils.id.SnowflakeIdGenerator;
import com.aicourse.utils.json.JsonParserUtil;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private ModuleRepo moduleRepo;

    @Autowired
    private geminiConnection geminiConnection;


    @PostMapping("/generate")
    public Course createCourse(@RequestBody Map<String, String> payload, Authentication auth) {
        String title = payload.get("title");
        String difficulty = payload.getOrDefault("difficulty", "Beginner");
        String duration = payload.getOrDefault("duration", "2 Hours");

        Course course = new Course();
        course.setId(SnowflakeIdGenerator.generateId());
        course.setTitle(title);
        course.setDescription("Generated course for " + title);
        course.setCreator(auth.getName()); // user from JWT

        String prompt = """
                 Create a comprehensive course outline about "%s".
                 The difficulty level should be %s.
                 The estimated course duration is %s.
                
                The course should have a clear title and a brief, engaging description.
                It must be structured into 4 to 6 modules.
                Each module must contain 3 to 5 lesson titles.
                
                You MUST respond with ONLY a raw JSON object, without any markdown formatting, comments, or other text.
                The JSON object must follow this exact structure:
                {
                    "title": "Course Title",
                    "description": "Course Description",
                    "modules": [
                        {
                            "title": "Module 1 Title",
                            "lessons": ["Lesson 1.1 Title", "Lesson 1.2 Title"]
                        },
                        {
                            "title": "Module 2 Title",
                            "lessons": ["Lesson 2.1 Title", "Lesson 2.2 Title"]
                        }
                    ]
                }
                and i've added approximately course duration, based on this give the response
                """.formatted(title, difficulty, duration);
        String responce = geminiConnection.getResponse(prompt);
        JsonNode courseJson = JsonParserUtil.parseStringToJsonObject(responce);
        System.out.println(courseJson);
//
//        List<Module> modules = new ArrayList<>();
//        Module m = new Module();
//        m.setId(SnowflakeIdGenerator.generateId());
//        m.setTitle("hii");
//        m.setCourse(course);
//        modules.add(m);
//        course.setModules(modules);

        JsonNode modulesNode = courseJson.get("modules");
        List<Module> modules = new ArrayList<>();
        for (JsonNode moduleNode : modulesNode) {

            String moduleTitle = moduleNode.get("title").asText();
            Module module = new Module();
            module.setId(SnowflakeIdGenerator.generateId());
            module.setTitle(moduleTitle);
            module.setCourse(course);

            List<Lesson> lessons = new ArrayList<>();


            for (JsonNode lessonNode : moduleNode.get("lessons")) {
                Lesson lesson = new Lesson();
                lesson.setId(SnowflakeIdGenerator.generateId());
                lesson.setTitle(lessonNode.asText());
                lesson.setContent(
                        JsonParserUtil.parseStringToJsonObject("[]")
                );
                lesson.setModule(module);

                lessons.add(lesson);
            }

            module.setLessons(lessons);
            modules.add(module);
        }
        course.setModules(modules);
        return courseRepo.save(course);
    }

    @GetMapping
    public List<Course> getMyCourses(Authentication auth) {
        return courseRepo.findByCreator(auth.getName());
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Long id) {
        return courseRepo.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
    }

    @GetMapping("/{courseName}/modules")
    public List<Module> getModulesByCourseName(@PathVariable String courseName) {
        return moduleRepo.findByCourse_Title(courseName);
    }
}