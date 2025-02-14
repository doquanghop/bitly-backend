package com.urlshortener.core.infrastucture.utils.model;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class PageResponse<T> {
    private int totalElements;
    private int totalPages;
    private T data;
}
