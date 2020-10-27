package com.synergy;

import com.google.gson.JsonArray;
import com.synergy.EquipmentSearch.CheckListAddRequest;
import com.synergy.EquipmentSearch.EquipmentSearchResponse;
import com.synergy.EquipmentSearch.GetPmTaskItemsResponse;
import com.synergy.EquipmentSearch.GetUpdatePmTaskRequest;
import com.synergy.EquipmentSearch.GetUpdatePmTaskResponse;
import com.synergy.faultReport.CreateFaultRequestPojo;
import com.synergy.faultReport.CreateResponse;
import com.synergy.search.SearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

    @GET("workspaces")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getWorkspace();


    //gen dep for fault
    @GET("general/departments/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenDep(@Header("workspaceId") String workspaceId);

    //gen priority for fault
    @GET("general/priorty/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenproirity();


    //gen division for fault
    @GET("general/divisions/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenDivisions();

    //get building for fault

    @GET("general/buildings/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenBuildings();

    //get fayult caTegories for fault
    @GET("general/faultCategories/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenFaultCat();

    //get fault maint grp
    @GET("general/maintenanceGrpCategory/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenMaintGrp();

    //get fault location grp
    @GET("general/locations/1")
    @Headers("Content-Type: application/json")
    Call<JsonArray> getGenLocation();

    //create fault
    @POST("faultreport")
    @Headers("Content-Type: application/json")
    Call<Void> createFault(@Body CreateFaultRequestPojo createFaultRequestPojo, @Header("workspace") String workspace);

    //get search https://ifarms.com.sg:8085/lsme/api/faultreport/search/?query=00005
    @GET("faultreport/search/?")
    @Headers("Content-Type: application/json")
    Call<List<SearchResponse>> getSearchResult(@Header("workspace") int dynamicWorkSpace,
                                               @Query("query") String param);
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
    Call<EquipmentSearchResponse> getCallEquipment(@Path("equipmentCode") String path);

    @GET("task/{equipmentCode}/{status}")
    @Headers("Content-Type: application/json")
    Call<List<TaskResponse>> getTaskOnQrList(@Path("equipmentCode") String path,
                                             @Path("status") String status);

    @GET("task/{id}")
    @Headers("Content-Type: application/json")
    Call<GetPmTaskItemsResponse> getCallPmTask(@Path("id") String id);

    @POST("task/updateTask")
    @Headers("Content-Type: application/json")
    Call<GetUpdatePmTaskResponse> postPmTaskUpdate(GetUpdatePmTaskRequest getUpdatePmTaskRequest);

    //checklistActivityView
    @GET("task/{path}/checklist")
    @Headers("Content-Type: application/json")
    Call<List<GetCheckListResponse>> getChecklist(@Path("path") String path);

    //checkListActivitiesSave
    @POST("task/updateChecklists")
    @Headers("Content-Type: application/json")
    Call<List<CheckListAddRequest>> postCheckList(@Body List<CheckListAddRequest> checkListAddRequest);
}
