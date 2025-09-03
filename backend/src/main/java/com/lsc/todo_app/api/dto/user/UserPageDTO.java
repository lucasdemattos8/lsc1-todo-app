package com.lsc.todo_app.api.dto.user;

import java.util.List;

public class UserPageDTO {

    List<UserSummaryDTO> users;
    int currentPage;
    int totalPages;
    long totalElements;
    int pageSize;

    public UserPageDTO(List<UserSummaryDTO> users, int currentPage, int totalPages, long totalElements, int pageSize) {
        this.users = users;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
    }

    public List<UserSummaryDTO> getUsers() {
        return users;
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
