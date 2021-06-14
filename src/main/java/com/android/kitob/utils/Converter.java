package com.android.kitob.utils;


import com.android.kitob.payload.ApiResponse;
import com.android.kitob.payload.ApiResponseObject;
import com.android.kitob.payload.ApiResponseObjectByPageable;
import com.android.kitob.payload.StatusString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class Converter {

    /** For responses **/

    public ApiResponse api(String status, boolean success){
        return new ApiResponse(status,success);
    }

    public ApiResponse api(String status, boolean success,Object object){
        return new ApiResponseObject(status,success,object);
    }

    public ApiResponse api(String status, boolean success,Object object,long totalElements, Integer totalPages){
        return new ApiResponseObjectByPageable(status,success,object,totalElements,totalPages);
    }

    public ApiResponse apiError(){
        return api(StatusString.FAILED,false);
    }

    public ApiResponse apiError(String status){
        return api(status,false);
    }

    public ApiResponse apiError(Object object){
        return api(StatusString.FAILED,false,object);
    }

    public ApiResponse apiError(String status, Object object){
        return api(status,false,object);
    }


    public ApiResponse apiSuccess(){
        return api(StatusString.SUCCESS,true);
    }

    public ApiResponse apiSuccess(String status){
        return api(status,true);
    }

    public ApiResponse apiSuccess(Object object){
        return api(StatusString.SUCCESS,true,object);
    }

    public ApiResponse apiSuccess(String status, Object object){
        return api(status,true,object);
    }

    public ApiResponse apiSuccess(Object object,long totalElements, Integer totalPages){
        return api(StatusString.SUCCESS,true, object, totalElements, totalPages);
    }

    public ApiResponse apiSuccess(String status, Object object,long totalElements, Integer totalPages){
        return api(status,true, object, totalElements, totalPages);
    }

    public ApiResponse apiSuccessObject(Object object){
        return api(StatusString.SUCCESS,true,object);
    }

    public ApiResponse apiSuccessObject(Object object,long totalElements, Integer totalPages){
        return api(StatusString.SUCCESS,true, object, totalElements, totalPages);
    }





}
