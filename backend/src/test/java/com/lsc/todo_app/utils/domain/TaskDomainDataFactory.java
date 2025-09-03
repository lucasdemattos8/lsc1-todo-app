package com.lsc.todo_app.utils.domain;

import java.time.LocalDateTime;

import com.lsc.todo_app.domain.entity.Task;
import com.lsc.todo_app.domain.entity.User;
import com.lsc.todo_app.domain.entity.enums.Status;

public class TaskDomainDataFactory {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    private UserDomainDataFactory userDataFactory;

    public TaskDomainDataFactory(Long id, String title, String description, Status status, LocalDateTime updatedAt, LocalDateTime createdAt, UserDomainDataFactory userDataFactory){
        LocalDateTime defaultUpdatedAt = LocalDateTime.now();
        LocalDateTime defaultCreatedAt = LocalDateTime.now().minusHours(3);

        this.id = (id != null) ? id : 1L;
        this.title = (title != null) ? title : "Learn Spring";
        this.description = (description != null) ? description : "Learn the basics of Spring";
        this.status = (status != null) ? status : Status.PENDING;
        this.updatedAt = (updatedAt != null) ? updatedAt : defaultUpdatedAt;
        this.createdAt = (createdAt != null) ? createdAt : defaultCreatedAt;
        this.userDataFactory = userDataFactory;
    }

    public TaskDomainDataFactory(){
        LocalDateTime defaultUpdatedAt = LocalDateTime.now();
        LocalDateTime defaultCreatedAt = defaultUpdatedAt.minusHours(3);

        this.id = 1L;
        this.title = "Learn Spring";
        this.description = "Learn the basics of Spring";
        this.status = Status.PENDING;
        this.updatedAt = defaultUpdatedAt;
        this.createdAt = defaultCreatedAt;
        this.userDataFactory = new UserDomainDataFactory();
    }

    public TaskDomainDataFactory(UserDomainDataFactory userDataFactory){
        LocalDateTime defaultUpdatedAt = LocalDateTime.now();
        LocalDateTime defaultCreatedAt = defaultUpdatedAt.minusHours(3);

        this.id = 1L;
        this.title = "Learn Spring";
        this.description = "Learn the basics of Spring";
        this.status = Status.PENDING;
        this.updatedAt = defaultUpdatedAt;
        this.createdAt = defaultCreatedAt;
        this.userDataFactory = userDataFactory;
    }

    public Task createTaskWithUser() {
        Task task = new Task();
        User user = createTestDomainUser();

        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setUpdatedAt(updatedAt);
        task.setCreatedAt(createdAt);
        task.setUser(user);

        return task;
    }

    public Task createTaskWithoutUser() {
        Task task = new Task();

        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setUpdatedAt(updatedAt);
        task.setCreatedAt(createdAt);

        return task;
    }

    public User createTestDomainUser() {
        return userDataFactory.createUserWithoutTasks();
    }
}
