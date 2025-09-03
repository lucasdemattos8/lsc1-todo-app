package com.lsc.todo_app.api.dto.task;

import java.util.List;

public class TaskPageDTO {
    private List<TaskSummaryDTO> tasks;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;

    public TaskPageDTO(List<TaskSummaryDTO> tasks, int currentPage, int totalPages, long totalElements, int pageSize) {
        this.tasks = tasks;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
    }

    public List<TaskSummaryDTO> getTasks() {
        return tasks;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getPageSize() {
        return pageSize;
    }

}