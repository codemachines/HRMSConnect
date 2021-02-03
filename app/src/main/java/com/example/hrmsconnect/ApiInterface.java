package com.example.hrmsconnect;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface
{
    @FormUrlEncoded
    @POST("Api/employeeregistration.php")
    public Call<JsonElement> employeeregister(@Field("emp_name") String emp_name, @Field("emp_email") String emp_email, @Field("emp_password") String emp_password, @Field("emp_mobile") String emp_mobile, @Field("emp_designation") String emp_designation, @Field("emp_gender") String emp_gender, @Field("emp_profile") String emp_profile,@Field("emp_address") String emp_address, @Field("emp_caddress") String emp_caddress, @Field("token") String token);

    @FormUrlEncoded
    @POST("Api/loginuser.php")
    public Call<JsonElement> employeelogin(@Field("emp_email") String emp_email, @Field("emp_pwd") String emp_pwd);

    @FormUrlEncoded
    @POST("Api/checkemployeemobile.php")
    public Call<JsonElement> mobileVerification(@Field("emp_mobile") String emp_mobile);

    @FormUrlEncoded
    @POST("Api/resetpassword.php")
    public Call<JsonElement> resetPassword(@Field("emp_mobile") String emp_mobile, @Field("emp_password") String emp_password);

    @FormUrlEncoded
    @POST("Api/employeeleave.php")
    public Call<JsonElement> empleave(@Field("emp_id") String emp_id, @Field("leave_type") String leave_type, @Field("start_date") String start_date, @Field("end_date") String end_date, @Field("leave_desc") String leave_desc, @Field("days") String days, @Field("balance") String balance, @Field("applied_to") String applied_to, @Field("cc_to") String cc_to);

    @FormUrlEncoded
    @POST("Api/empleavedata.php")
    public Call<JsonElement> empleavedata(@Field("emp_id") String emp_id);

    @FormUrlEncoded
    @POST("Api/empsalarydata.php")
    public Call<JsonElement> empSaldata(@Field("emp_id") String emp_id);

    @FormUrlEncoded
    @POST("Api/taskdata.php")
    public Call<JsonElement> taskdata(@Field("emp_id") String emp_id);

    @FormUrlEncoded
    @POST("Api/emptaskdata.php")
    public Call<JsonElement> emptaskdata(@Field("emp_id") String emp_id);

    @FormUrlEncoded
    @POST("Api/emptask.php")
    public Call<JsonElement> emptask(@Field("emp_id") String emp_id, @Field("task_title" ) String task_title, @Field("task_deadline") String task_deadline, @Field("task_givenby") String task_givenby, @Field("task_desc") String task_desc);

    @FormUrlEncoded
    @POST("Api/emptaskstatus.php")
    public Call<JsonElement> emptaskstatus(@Field("emp_task_id") String emp_task_id,@Field("task_status") String task_status);

    @FormUrlEncoded
    @POST("Api/emptimesheet.php")
    public Call<JsonElement> timesheet(@Field("emp_id") String emp_id,@Field("admin_task_id") String admin_task_id,@Field("task_title") String task_title,@Field("start_time") String start_time,@Field("end_time") String end_time,@Field("task_desc") String task_desc,@Field("task_status") String task_status);


    @FormUrlEncoded
    @POST("Api/dailystatusdata.php")
    public Call<JsonElement> dailystatusdata(@Field("emp_id") String emp_id);

    @FormUrlEncoded
    @POST("Api/updateprofilepic.php")
    public Call<JsonElement> updateprofilepic(@Field("emp_id") String emp_id,@Field("imgString") String imgString);

    @FormUrlEncoded
    @POST("Api/updateprofile.php")
    public Call<JsonElement> updateprofile(@Field("emp_id") String emp_id,@Field("emp_name") String emp_name,@Field("emp_mobile") String emp_mobile,@Field("emp_email") String emp_email,@Field("emp_designation") String emp_designation,@Field("emp_address") String emp_address);

    @FormUrlEncoded
    @POST("Api/employeetoken.php")
    public Call<JsonElement> employeetoken(@Field("emp_email") String emp_email);

    @FormUrlEncoded
    @POST("Api/deleteuser.php")
    public Call<JsonElement> deleteuser(@Field("emp_id") String emp_id);

    @FormUrlEncoded
    @POST("Api/mailsend.php")
    public Call<JsonElement> mailsend(@Field("from") String from,@Field("to") String to,@Field("subject") String subject,@Field("body") String body,@Field("document") String document);

    @FormUrlEncoded
    @POST("Api/fetchmail.php")
    public Call<JsonElement> fetchmail(@Field("emp_email") String emp_email);

    @FormUrlEncoded
    @POST("Api/mailnotification.php")
    public Call<JsonElement> mailnotification(@Field("emp_email") String emp_email);

    @FormUrlEncoded
    @POST("Api/balancegraph.php")
    public Call<JsonElement> balancegraph(@Field("emp_id") String emp_id);

    @FormUrlEncoded
    @POST("Api/taskgraph.php")
    public Call<JsonElement> taskgraph(@Field("emp_id") String emp_id);
}
