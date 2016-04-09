package com.iceoton.canomcakeadmin.service;

import com.iceoton.canomcakeadmin.medel.response.AddProductResponse;
import com.iceoton.canomcakeadmin.medel.response.DeleteProductResponse;
import com.iceoton.canomcakeadmin.medel.response.DeleteResponse;
import com.iceoton.canomcakeadmin.medel.response.DeleteTransactionResponse;
import com.iceoton.canomcakeadmin.medel.response.EditAdminResponse;
import com.iceoton.canomcakeadmin.medel.response.EditProductResponse;
import com.iceoton.canomcakeadmin.medel.response.ForgetPasswordResponse;
import com.iceoton.canomcakeadmin.medel.response.GetAllAdminResponse;
import com.iceoton.canomcakeadmin.medel.response.GetAllCategoryResponse;
import com.iceoton.canomcakeadmin.medel.response.GetAllCustomerResponse;
import com.iceoton.canomcakeadmin.medel.response.GetAllProductResponse;
import com.iceoton.canomcakeadmin.medel.response.GetAllTransactionsResponse;
import com.iceoton.canomcakeadmin.medel.response.GetCustomerResponse;
import com.iceoton.canomcakeadmin.medel.response.GetDashboardResponse;
import com.iceoton.canomcakeadmin.medel.response.GetOrderByIdResponse;
import com.iceoton.canomcakeadmin.medel.response.GetOrdersResponse;
import com.iceoton.canomcakeadmin.medel.response.GetProductByCategoryResponse;
import com.iceoton.canomcakeadmin.medel.response.GetProductByCodeResponse;
import com.iceoton.canomcakeadmin.medel.response.LoginAdminResponse;
import com.iceoton.canomcakeadmin.medel.response.RegisterAdminResponse;
import com.iceoton.canomcakeadmin.medel.response.SetOrderStatusResponse;

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
    Call<AddProductResponse> addNewProduct(@Field("tag") String tag,
                                           @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<EditProductResponse> editProduct(@Field("tag") String tag,
                                          @Field("data") String data);

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
    Call<GetAllCustomerResponse> loadAllCustomer(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetAllAdminResponse> loadAllAdmin(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<LoginAdminResponse> loginAdminToserver(@Field("tag") String tag, @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<ForgetPasswordResponse> sendForgetPassword(@Field("tag") String tag, @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<RegisterAdminResponse> registerAdmin(@Field("tag") String tag, @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<EditAdminResponse> editAdmin(@Field("tag") String tag, @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<DeleteResponse> deleteAdminById(@Field("tag") String tag, @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<DeleteResponse> deleteCustomerById(@Field("tag") String tag, @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetCustomerResponse> loadCustomerByCustomerId(@Field("tag") String tag, @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetOrdersResponse> loadOrdersByTag(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetDashboardResponse> loadDashboard(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<GetAllTransactionsResponse> loadAllTransactions(@Field("tag") String tag);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<DeleteTransactionResponse> deleteTransactionById(@Field("tag") String tag, @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<SetOrderStatusResponse> setOrderStatusById(@Field("tag") String tag, @Field("data") String data);

    @FormUrlEncoded
    @POST("adminApi.php")
    Call<DeleteProductResponse> deleteProductByCode(@Field("tag") String tag, @Field("data") String data);


}
