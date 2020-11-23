package com.synergy;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergy.EquipmentSearch.CheckListAddRequest;
import com.synergy.EquipmentSearch.EquipmentSearchResponse;
import com.synergy.EquipmentSearch.GetPmTaskItemsResponse;
import com.synergy.EquipmentSearch.GetUpdatePmTaskRequest;
import com.synergy.EquipmentSearch.GetUpdatePmTaskResponse;
import com.synergy.EquipmentSearch.UploadImageRequest;
import com.synergy.FaultReport.CreateFaultRequestPojo;
import com.synergy.FaultReport.UploadPictureRequest;
import com.synergy.Otp.OtpRequest;
import com.synergy.Otp.ResendOtpRequest;
import com.synergy.Search.EditFaultReportRequest;
import com.synergy.Search.EquipmentSearchResponseforEdit;
import com.synergy.Search.SearchResponse;
import com.synergy.Search.UploadFileRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.PUT;

public interface UserService {

    //login to user account
    @POST("authenticate")
    @Headers("Content-Type: application/json")
    Call<UserResponse> saveUser(@Body UserRequest userRequest);
    //otp call
    @POST("verify2fa")
    @Headers("Content-Type: application/json")
    Call<UserResponse> callOtp(@Body OtpRequest otpRequest);

    //resend otp call
    @POST("resendCode")
    @Headers("Content-Type: application/json")
    Call<Void> callResendOtp(@Body ResendOtpRequest resendOtpRequest);


    //generate workspace
    @GET("workspaces")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getWorkspace(@Header("Authorization") String token);

    //gen dep for fault
    @GET("general/departments/{workspaceIdPath}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenDep(@Header("workspaceId") String workspaceId
            , @Header("Authorization") String token
            , @Path("workspaceIdPath") String workspaceIdPath);

    //gen priority for fault
    @GET("general/priorty/{workspaceIdPath}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenproirity(@Header("Authorization") String token,
                                   @Path("workspaceIdPath") String workspaceIdPath);


    //gen division for fault
    @GET("general/divisions/{workspaceIdPath}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenDivisions(@Header("Authorization") String token
            , @Path("workspaceIdPath") String workspaceIdPath);

    //get building for fault

    @GET("general/buildings/{workspaceIdPath}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenBuildings(@Header("Authorization") String token,
                                    @Path("workspaceIdPath") String workspaceIdPath);

    //get fayult caTegories for fault
    @GET("general/faultCategories/{workspaceIdPath}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenFaultCat(@Header("Authorization") String token,
                                   @Path("workspaceIdPath") String workspaceIdPath);

    //get fault maint grp
    @GET("general/maintenanceGrpCategory/{workspaceIdPath}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenMaintGrp(@Header("Authorization") String token,
                                   @Path("workspaceIdPath") String workspaceIdPath);

    //get fault location grp
    @GET("general/locations/{buildId}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenLocation(@Header("Authorization") String token,
                                   @Path("buildId") Integer buildId);


    //get fault equipment list
    @GET("general/building/{buildId}/location/{locId}")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenEquip(@Header("Authorization") String token,
                                @Header("role") String role,
                                @Header("workspace") String workspace,
                                @Path("buildId") Integer buildId,
                                @Path("locId") Integer locId);

    //create fault
    @POST("faultreport")
    @Headers("Content-Type: application/json")
    Call<JsonObject> createFault(@Body CreateFaultRequestPojo createFaultRequestPojo,
                                 @Header("workspace") String workspace,
                                 @Header("Authorization") String token
                                , @Header("role") String role);

    //get search
    @GET("faultreport/search/?")
    @Headers("Content-Type: application/json")
    Call<List<SearchResponse>> getSearchResult(@Header("workspace") String dynamicWorkSpace,
                                               @Query("query") String param,
                                               @Header("Authorization") String token);
    //before image upload http://ifarms.com.sg:8086/lsme/api/faultreport/beforeimage
   /* @POST("ws/upload{before}image")
    @Headers("Content-Type: application/json")
    Call<Void> uploadCaptureImage(@Path("before") String before,
                                  @Header("X-Auth-Token") String token,
                                  @Header("workspace") String workspace,
                                  @Body UploadPictureRequest uploadPictureRequest);*/

    //equipment search
    @GET("faultreport/equipment/{equipmentCode}")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getCallEquipment(@Path("equipmentCode") String path,
                                      @Header("Authorization") String token,
                                      @Header("role") String role,
                                      @Header("workspace") String workspace);

    //equipment search tasks
    @GET("task/equipment/{equipmentCode}")
    @Headers("Content-Type: application/json")
    Call<List<EquipmentSearchResponse>> getEquipmentTask(@Path("equipmentCode") String path,
                                      @Header("role") String role,
                                      @Header("Authorization") String token,
                                      @Header("workspace") String workspace);


    //getEquipment Scan code
    @GET("task/{equipmentCode}/{status}")
    @Headers("Content-Type: application/json")
    Call<List<TaskResponse>> getTaskOnQrList(@Path("equipmentCode") String path,
                                             @Path("status") String status,
                                             @Header("Authorization") String token);


    //pm View Task
    @GET("task/{id}")
    @Headers("Content-Type: application/json")
    Call<GetPmTaskItemsResponse> getCallPmTask(@Path("id") String id,
                                               @Header("Authorization") String token);

    //Update pm task
    @PUT("task/updateTask")
    @Headers("Content-Type: application/json")
    Call<GetUpdatePmTaskResponse> postPmTaskUpdate(@Body GetUpdatePmTaskRequest getUpdatePmTaskRequest,
                                                   @Header("Authorization") String token,
                                                   @Header("role") String role,
                                                   @Header("workspace") String workspace);

    //checklistActivityView
    @GET("task/{path}/checklist")
    @Headers("Content-Type: application/json")
    Call<List<GetCheckListResponse>> getChecklist(@Path("path") String path,
                                                  @Header("Authorization") String token);

    //checkListActivitiesSave
    @POST("task/updateChecklists")
    @Headers("Content-Type: application/json")
    Call<List<CheckListAddRequest>> postCheckList(@Body List<CheckListAddRequest> checkListAddRequest,
                                                  @Header("Authorization") String token);

    //get edit fault details
    @GET("faultreport/{frid}")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getEditfaultDetails(@Path("frid") String frid,
                                         @Header("WorkspaceId") String workspaceId,
                                         @Header("Authorization") String token,@Header("role")String role);

    //to get the technician list
    @GET("general/technicians")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getTechnicianCall(@Header("Authorization") String token,
                                      @Header("workspace") String workspace);

    //get cost center
    @GET("general/costcenter")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getCostCenter(@Header("Authorization") String token,
                                  @Header("workspace") String workspace);
    //upload image

    @POST("faultreport/{value}image")
    @Headers("Content-Type: application/json")
    Call<Void> uploadCaptureImage(@Path("value") String value,
                                  @Header("Authorization") String token,
                                  @Header("workspace") String workspace,
                                  @Body UploadPictureRequest uploadPictureRequest);

    // eq search http://192.168.1.117:8082/api/equip/acmv8310
    @GET("equip/{equipmentCode}")
    @Headers("Content-Type: application/json")
    Call<EquipmentSearchResponseforEdit> getSearchEquipment(@Path("equipmentCode") String path,
                                                            @Header("Authorization") String token);

    //update
    @PUT("faultreport")
    @Headers("Content-Type: application/json")
    Call<Void> updateReport(@Body EditFaultReportRequest editFaultReportRequest,
                            @Header("Authorization") String token,
                            @Header("workspace") String workspace,
                            @Header("role") String role);

    @GET("faultreport/{value}image/{frId}")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getBeforeImage(@Path("value") String value,
                                    @Path("frId") String frId,
                                    @Header("workspace") String workspace,
                                    @Header("Authorization") String token);

    //call alreaddy images
    @GET("faultreport/getimage/{imageName}")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> getImageBase64(@Path("imageName") String imagename,
                                      @Header("workspace") String workspace,
                                      @Header("Authorization") String token);


    //upload file
    @POST("faultreport/quotationUpload")
    @Headers("Content-Type: application/json")
    Call<ResponseBody> uploadFilePdf(@Body UploadFileRequest uploadFileRequest,
                                     @Header("Authorization" +
                                             "") String token,
                                     @Header("workspace") String workspace,
                                     @Header("role") String role);


    //enable notification
    @GET("ws/dr/not/{zero}/{path}")
    @Headers("Content-Type: application/json")
    Call<Void> getNotification(@Path("zero") String zeroOrOne,
                               @Path("path") String devicetoken,
                               @Header("Authentication") String token,
                               @Header("workspace") String workspace);


    //Logout user
    @GET("logout")
    @Headers("Content-Type: application/json")
    Call<Void> logoutUser(@Header("token") String token);

    //imageUploadTask
    @POST("task/{afterimage}image")
    @Headers("Content-Type: application/json")
    Call<Void> taskImageUpload(@Header("role") String role,
                               @Header("Authorization") String token,
                               @Path("afterimage") String imageValue,
                               @Body UploadImageRequest uploadImageRequest);

}
