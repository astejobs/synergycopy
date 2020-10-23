package com.synergy;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.synergy.EquipmentSearch.EquipmentSearchResponse;
import com.synergy.faultReport.CreateFaultRequestPojo;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    Call<Void> createFault(@Body CreateFaultRequestPojo createFaultRequestPojo,@Header("workspace")String workspace);


    //equipment search
    @GET("equip/{equipmentCode}")
    @Headers("Content-Type: application/json")
    Call<EquipmentSearchResponse> getCallEquipment(@Path("equipmentCode") String path);

    @GET("task/{equipmentCode}/{status}")
    @Headers("Content-Type: application/json")
    Call<List<TaskResponse>> getTaskOnQrList(@Path("equipmentCode") String path,
                                    @Path("status") String status);

}
