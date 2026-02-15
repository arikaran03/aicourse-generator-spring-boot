package com.aicourse.service.courses;

import com.aicourse.model.Course;
import com.aicourse.model.Module;

import java.util.List;
import java.util.Map;

public interface CourseService {

    Course generateCourse(Map<String, String> payload, String creator) throws Exception;

    List<Course> getCoursesByCreator(String creator) throws Exception;

    Course getCourseById(Long id) throws Exception;

    List<Module> getModulesByCourseName(String courseName) throws Exception;

    void deleteCourse(Long courseId) throws Exception;

    void updateCourse(Long courseID, Course courseDO) throws Exception;

}