package com.thbs.cpt.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thbs.cpt.DTO.UserCourseProgressDTO;
import com.thbs.cpt.DTO.UserProgressDTO;
import com.thbs.cpt.DTO.UserTopicProgressDTO;
import com.thbs.cpt.Exception.BatchIdNotFoundException;
import com.thbs.cpt.Exception.CourseNotFoundException;
import com.thbs.cpt.Exception.ResourceIdNotFoundException;
import com.thbs.cpt.Exception.TopicIdNotFoundException;
import com.thbs.cpt.Exception.UserNotFoundException;
import com.thbs.cpt.Repository.ProgressRepository;

@Service
public class UserProgressService {

    @Autowired
    private ProgressRepository progressRepository;

    public UserProgressDTO calculateOverallProgressForUser(Long userId) throws UserNotFoundException {
        List<Object[]> results = progressRepository.findOverallProgressForUser(userId);
        if (results != null && !results.isEmpty()) {
            Object[] result = results.get(0);
            if (result[0] != null && result[1] != null) {
                long userIdFromQuery = (long) result[0];
                double overallProgress = (double) result[1];
                return new UserProgressDTO(userIdFromQuery, overallProgress);
            }
        }
        throw new UserNotFoundException("User with ID " + userId + " not found.");
    }

    public UserCourseProgressDTO calculateCourseProgressForUser(Long userId, int courseId)
            throws UserNotFoundException, CourseNotFoundException {
        List<Object[]> results = progressRepository.findCourseProgressByUserAndCourse(userId, courseId);
        if (results != null && !results.isEmpty()) {
            Object[] result = results.get(0);
            if (result[1] != null && result[2] != null) {
                long userIdFromQuery = (long) result[1];
                double overallProgress = (double) result[2];
                return new UserCourseProgressDTO(userIdFromQuery, overallProgress);
            }
        }
        if (!results.isEmpty()) {
            throw new CourseNotFoundException("Course with ID " + courseId + " not found.");
        } else {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
    }

    public UserTopicProgressDTO calculateUserTopicProgress(Long userId, int courseId, int topicId)
            throws UserNotFoundException, CourseNotFoundException, TopicIdNotFoundException {
        List<Object[]> results = progressRepository.findTopicProgressByCourseAndUserId(userId, courseId, topicId);
        if (results != null && !results.isEmpty()) {
            Object[] result = results.get(0);
            if (result[1] != null && result[2] != null) {
                long userIdFromQuery = (long) result[0];
                double topicProgress = (double) result[3];
                return new UserTopicProgressDTO(userIdFromQuery, topicProgress);
            }
        }
        if (!results.isEmpty()) {
            throw new TopicIdNotFoundException("Topic with ID " + topicId + " not found.");
        } else {
            throw new CourseNotFoundException("Course with ID " + courseId + " not found.");
        }
    }
}
