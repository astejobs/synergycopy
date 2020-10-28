package com.synergy;

import com.google.android.gms.common.util.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import com.synergy.EquipmentSearch.CheckListAddRequest;
import com.synergy.EquipmentSearch.EquipmentSearchResponse;
import com.synergy.EquipmentSearch.GetPmTaskItemsResponse;
import com.synergy.EquipmentSearch.GetUpdatePmTaskRequest;
import com.synergy.EquipmentSearch.GetUpdatePmTaskResponse;
import com.synergy.faultReport.CreateFaultRequestPojo;
import com.synergy.faultReport.CreateResponse;
import com.synergy.faultReport.FaultReportResponse;
import com.synergy.faultReport.Model;
import com.synergy.search.SearchResponse;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    //login
    @POST("authenticate")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getLoggedIn(@Body UserRequest userRequest);

    //login to user account
    @POST("authenticate")
    @Headers("Content-Type: application/json")
    Call<UserResponse> saveUser(@Body UserRequest userRequest );

    //generate workspace
    @GET("workspaces")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getWorkspace(@Header("Authorization") String token);

    //gen dep for fault
    @GET("general/departments/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenDep(@Header("workspaceId") String workspaceId, @Header("X-Auth-Token") String token);

    //gen priority for fault
    @GET("general/priorty/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenproirity(@Header("X-Auth-Token") String token);


    //gen division for fault
    @GET("general/divisions/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenDivisions(@Header("X-Auth-Token") String token);

    //get building for fault

    @GET("general/buildings/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenBuildings(@Header("X-Auth-Token") String token);

    //get fayult caTegories for fault
    @GET("general/faultCategories/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenFaultCat(@Header("X-Auth-Token") String token);

    //get fault maint grp
    @GET("general/maintenanceGrpCategory/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenMaintGrp(@Header("X-Auth-Token") String token);

    //get fault location grp
    @GET("general/locations/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenLocation(@Header("X-Auth-Token") String token);

    //create fault
    @POST("faultreport")
    @Headers("Content-Type: application/json")
    Call<FaultReportResponse> createFault(@Body CreateFaultRequestPojo createFaultRequestPojo, @Header("workspace") String workspace);
    //Call<Void> createFault(@Body CreateFaultRequestPojo createFaultRequestPojo, @Header("workspace") String workspace, @Header("X-Auth-Token") String token);

    //get search
    @GET("faultreport/search/?")
    @Headers("Content-Type: application/json")
    Call<List<SearchResponse>> getSearchResult(@Header("workspace") int dynamicWorkSpace,
                                               @Query("query") String param,
                                               @Header("X-Auth-Token") String token);
    //before image upload http://ifarms.com.sg:8086/lsme/api/faultreport/beforeimage
   /* @POST("ws/upload{before}image")
    @Headers("Content-Type: application/json")
    Call<Void> uploadCaptureImage(@Path("before") String before,
                                  @Header("X-Auth-Token") String token,
                                  @Header("workspace") String workspace,
                                  @Body UploadPictureRequest uploadPictureRequest);*/

    //equipment search
    @GET("equip/{equipmentCode}")
    @Headers("Content-Type: application/json")
    Call<EquipmentSearchResponse> getCallEquipment(@Path("equipmentCode") String path,
                                                   @Header("X-Auth-Token") String token);


    //getEquipment Scan code
    @GET("task/{equipmentCode}/{status}")
    @Headers("Content-Type: application/json")
    Call<List<TaskResponse>> getTaskOnQrList(@Path("equipmentCode") String path,
                                             @Path("status") String status,
                                             @Header("X-Auth-Token") String token);


    //pm View Task
    @GET("task/{id}")
    @Headers("Content-Type: application/json")
    Call<GetPmTaskItemsResponse> getCallPmTask(@Path("id") String id,
                                               @Header("X-Auth-Token") String token);

    //Update pm task
    @PUT("task/updateTask")
    @Headers("Content-Type: application/json")
    Call<GetUpdatePmTaskResponse> postPmTaskUpdate(@Body GetUpdatePmTaskRequest getUpdatePmTaskRequest,
                                                   @Header("X-Auth-Token") String token);

    //checklistActivityView
    @GET("task/{path}/checklist")
    @Headers("Content-Type: application/json")
    Call<List<GetCheckListResponse>> getChecklist(@Path("path") String path,
                                                  @Header("X-Auth-Token") String token);

    //checkListActivitiesSave
    @POST("task/updateChecklists")
    @Headers("Content-Type: application/json")
    Call<List<CheckListAddRequest>> postCheckList(@Body List<CheckListAddRequest> checkListAddRequest,
                                                  @Header("X-Auth-Token") String token);

    //get edit fault details
    @GET("faultreport/edit/{frid}")
    @Headers("Content-Type: application/json")
    Call<JsonObject> getEditfaultDetails(@Path("frid")String frid,
                                   @Header("WorkspaceId")String workspaceId);

}
