package com.lsc.todo_app.api.dto.task;

import java.util.List;

public class TaskPageDTO {
    private List<TaskSumarryDTO> tasks;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;

    public TaskPageDTO(List<TaskSumarryDTO> tasks, int currentPage, int totalPages, long totalElements, int pageSize) {
        this.tasks = tasks;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
    }

    public List<TaskSumarryDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskSumarryDTO> tasks) {
        this.tasks = tasks;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}