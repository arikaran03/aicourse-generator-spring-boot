package com.aicourse.service.courses;

import com.aicourse.model.Course;
import com.aicourse.model.Module;

import java.util.List;
import java.util.Map;

public interface CourseService {

    Course generateCourse(Map<String, String> payload, String creator);

    List<Course> getCoursesByCreator(String creator);

    Course getCourseById(Long id);

    List<Module> getModulesByCourseName(String courseName);

    void deleteCourse(Long courseId);

    void updateCourse(Long courseID, Course courseDO);

}