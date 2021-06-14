package com.android.kitob.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ApiResponseObjectByPageable extends ApiResponseObject{
    private long totalElements;
    private long totalPages;

    public ApiResponseObjectByPageable(String message, boolean success, Object object, long totalElements, long totalPages) {
        super(message, success, object);
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
