package com.iceoton.canomcakeadmin.service;

import com.iceoton.canomcakeadmin.medel.response.GetAllCategoryResponse;
import com.iceoton.canomcakeadmin.medel.response.GetAllProductResponse;
import com.iceoton.canomcakeadmin.medel.response.GetCustomerResponse;
import com.iceoton.canomcakeadmin.medel.response.GetOrderByIdResponse;
import com.iceoton.canomcakeadmin.medel.response.GetProductByCategoryResponse;
import com.iceoton.canomcakeadmin.medel.response.GetProductByCodeResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface CanomCakeService {
    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetAllCategoryResponse> loadCategories(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetProductByCategoryResponse> loadProductByCategoryId(@Field("tag") String tag,
                                                               @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetAllProductResponse> loadAllProduct(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("customerApi.php")
    Call<GetProductByCodeResponse> loadProductByCode(@Field("tag") String tag,
                                                     @Field("data") String data);
    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetOrderByIdResponse> loadOrderByOrderId(@Field("tag") String tag, @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetCustomerResponse> loadCustomerByCustomerId(@Field("tag") String tag, @Field("data") String data);

}
