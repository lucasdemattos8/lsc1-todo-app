package com.lsc.todo_app.utils.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.lsc.todo_app.api.dto.task.CreateTaskRequest;
import com.lsc.todo_app.api.dto.task.TaskDTO;
import com.lsc.todo_app.api.dto.task.TaskPageDTO;
import com.lsc.todo_app.api.dto.task.TaskSummaryDTO;
import com.lsc.todo_app.api.dto.task.UpdateTaskRequest;
import com.lsc.todo_app.api.dto.user.UserSummaryDTO;
import com.lsc.todo_app.domain.entity.enums.Status;

public class TaskDTODataFactory {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime updatedAt ;
    private LocalDateTime createdAt;

    private UserDTODataFactory userDTODataFactory;
    private UserSummaryDTO testUser;

    public TaskDTODataFactory(Long id, String title, String description, Status status, LocalDateTime updatedAt, LocalDateTime createdAt, UserDTODataFactory userDTOFactory){
        LocalDateTime defaultUpdatedAt = LocalDateTime.now();
        LocalDateTime defaultCreatedAt = defaultUpdatedAt.minusHours(3);

        this.id = (id != null) ? id : 1L;
        this.title = (title != null) ? title : "Learn Spring";
        this.description = (description != null) ? description : "Learn the basics of Spring";
        this.status = (status != null) ? status : Status.PENDING;
        this.updatedAt = (updatedAt != null) ? updatedAt : defaultUpdatedAt;
        this.createdAt = (createdAt != null) ? createdAt : defaultCreatedAt;
        
        this.userDTODataFactory = userDTOFactory;
        this.testUser = createTestUser();
    }

    public TaskDTODataFactory() {
        LocalDateTime defaultUpdatedAt = LocalDateTime.now();
        LocalDateTime defaultCreatedAt = defaultUpdatedAt.minusHours(3);

        this.id = 1L;
        this.title = "Learn Spring";
        this.description = "Learn the basics of Spring";
        this.status = Status.PENDING;
        this.updatedAt = defaultUpdatedAt;
        this.createdAt = defaultCreatedAt;
        
        this.userDTODataFactory = new UserDTODataFactory();
        this.testUser = createTestUser();
    }

    public TaskDTODataFactory(UserDTODataFactory userDTODataFactory) {
        LocalDateTime defaultUpdatedAt = LocalDateTime.now();
        LocalDateTime defaultCreatedAt = defaultUpdatedAt.minusHours(3);

        this.id = 1L;
        this.title = "Learn Spring";
        this.description = "Learn the basics of Spring";
        this.status = Status.PENDING;
        this.updatedAt = defaultUpdatedAt;
        this.createdAt = defaultCreatedAt;
        
        this.userDTODataFactory = userDTODataFactory;
        this.testUser = createTestUser();
    }

    public TaskDTO createTaskDTO() {
        TaskDTO testTask = new TaskDTO();
        UserSummaryDTO testUser = userDTODataFactory.userSummaryDTO();

        testTask.setId(id);
        testTask.setTitle(title);
        testTask.setDescription(description);
        testTask.setStatus(status);
        testTask.setCreatedAt(createdAt);
        testTask.setUpdatedAt(updatedAt);
        testTask.setUser(testUser);

        return testTask;
    }

    public TaskSummaryDTO createTaskSummaryDTO() {
        TaskSummaryDTO testTask = new TaskSummaryDTO();

        testTask.setId(id);
        testTask.setTitle(title);
        testTask.setDescription(description);

        return testTask;
    }

    public TaskPageDTO createTaskPageDTO() {
        TaskSummaryDTO taskSumarry = createTaskSummaryDTO();

        List<TaskSummaryDTO> tasksList = Arrays.asList(taskSumarry);
        TaskPageDTO contentPage = new TaskPageDTO(tasksList, 0, 1, 1, 20);

        return contentPage;
    }

    public CreateTaskRequest createCreateTaskRequest() {
        return new CreateTaskRequest(title, description, testUser.getId());
    }

    public UpdateTaskRequest createUpdateTaskRequest() {
        return new UpdateTaskRequest(title, description, status);
    }

    private UserSummaryDTO createTestUser() {
        return userDTODataFactory.userSummaryDTO();
    }
}
