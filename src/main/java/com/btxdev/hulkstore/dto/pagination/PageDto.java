package com.btxdev.hulkstore.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageDto<T> {
    private int totalPages;
    private long totalElements;
    private List<T> elements;
}
