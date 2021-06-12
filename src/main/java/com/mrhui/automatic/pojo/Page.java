package com.mrhui.automatic.pojo;

import java.util.List;

public class Page<T> {
    private int limit = 10;
    private int page = 1;
    private int total;
    private List<T> items;
    private int currentPage;

    public Page(){

    }

    public Page(int limit, int page){
        this.limit = limit;
        this.page = page;
    }

    public Page(int limit, int page, int total, List<T> items, int currentPage) {
        this.limit = limit;
        this.page = page;
        this.total = total;
        this.items = items;
        this.currentPage = currentPage;
    }

    @Override
    public String toString() {
        return "Page{" +
                "limit=" + limit +
                ", page=" + page +
                ", total=" + total +
                ", items=" + items +
                ", currentPage=" + currentPage +
                '}';
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
