package com.iceoton.canomcakeadmin.service;


import com.iceoton.canomcakeadmin.medel.response.UploadFileResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService {

    //reference: https://github.com/square/retrofit/issues/1063
    @Multipart
    @POST("/uploadImageApi.php")
    Call<UploadFileResponse> upload(@Part("image_file\"; filename=\"image.jpg\" ") RequestBody file);
}
