package com.lsc.todo_app.api.dto.user;

import java.util.List;

public class UserPageDTO {

    List<UserSumarryDTO> users;
    int currentPage;
    int totalPages;
    long totalElements;
    int pageSize;

    public UserPageDTO(List<UserSumarryDTO> users, int currentPage, int totalPages, long totalElements, int pageSize) {
        this.users = users;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageSize = pageSize;
    }

    public List<UserSumarryDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserSumarryDTO> users) {
        this.users = users;
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
