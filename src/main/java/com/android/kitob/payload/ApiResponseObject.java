package com.android.kitob.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ApiResponseObject extends ApiResponse {
    private Object object;


    public ApiResponseObject(String message, boolean success, Object object) {
        super(message, success);
        this.object = object;
    }

}
