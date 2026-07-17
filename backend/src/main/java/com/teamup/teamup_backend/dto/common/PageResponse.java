package com.teamup.teamup_backend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T>{

    private List<T> content;
    private int page; //page number
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
