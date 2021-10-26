package in.freshshoppe.extras;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import in.freshshoppe.activities.Login;


/**
 * Created by Narendra on 6/8/2017.
 */

public class SessionManager {

    SharedPreferences sharedprefernce;
    SharedPreferences.Editor editor;

    Context context;
    int PRIVATE_MODE=0;

    private static final String PREF_NAME="sharedcheckLogin";

    private static final String User_Id="userid";
    private static final String IS_LOGIN="islogin";
    private static final String userPassword="password";
    private static final String USERNAME="username";
    private static final String USERPHONENUMBER="userPhoneNumber";
    private static final String USEREMAIL="userEmail";
    private static final String UserCity="usercity";
    private static final String SessionId="sessionid";
    private static final String Photo="Photo";

    private static final String billFirstANme="billFirstANme";
    private static final String billAddress1="billAddres1";
    private static final String billAddres2="billAddres2";
    private static final String billPostCode="billPostCode";
    private static final String billPhone="billPhone";


    public SessionManager(Context context){

        this.context =  context;
        sharedprefernce = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor=sharedprefernce.edit();



    }

    public Boolean isLogin(){
        return sharedprefernce.getBoolean(IS_LOGIN, false);

    }
    public void setLogin(){

        editor.putBoolean(IS_LOGIN, true);
        editor.commit();

    }


//    Bill values


    public void setUserID(String id ){

     editor.putString(User_Id,id);
     editor.commit();


    }

    public String getUserID(){

        return  sharedprefernce.getString(User_Id,"DEFAULT");
    }

    public void setUserName(String name){
        editor.putString(USERNAME,name);
        editor.commit();

    }
    public String getUserName(){
        return sharedprefernce.getString(USERNAME,"DEFAULT");
    }

    public void setUserPhonenumber(String uphone){
        editor.putString(USERPHONENUMBER,uphone);
        editor.commit();
    }
    public String getUserPhonenumber(){
        return sharedprefernce.getString(USERPHONENUMBER,"DEFAULT");
    }

    public void setUserEmail(String name)
    {
        editor.putString(USEREMAIL,name);
        editor.commit();
    }
    public String getUserEmail(){
        return sharedprefernce.getString(USEREMAIL,"DEFAULT");
    }

    public void setUSERcity(String ucity)
    {
        editor.putString(UserCity,ucity);
        editor.commit();
    }
    public String getUSERcity(){
        return sharedprefernce.getString(UserCity,"DEFAULT");
    }

    public void setBillFirstANme(String name){
        editor.putString(billFirstANme,name);
        editor.commit();

    }
    public String getBillFirstANme(){
        return  sharedprefernce.getString(billFirstANme,"First Name");
    }

    public void setBillAddress1(String name){
        editor.putString(billAddress1,name);
        editor.commit();

    }
    public String getBillAddress1(){
        return  sharedprefernce.getString(billAddress1,"Address 1");
    }

    public void setBillAddres2(String name){
        editor.putString(billAddres2,name);
        editor.commit();

    }
    public String getBillAddres2(){
        return  sharedprefernce.getString(billAddres2,"Address 2");
    }

    public void setBillPostCode(String name){
        editor.putString(billPostCode,name);
        editor.commit();

    }
    public String getBillPostCode(){
        return  sharedprefernce.getString(billPostCode,"Pin Code");
    }

    public void setBillPhone(String name){
        editor.putString(billPhone,name);
        editor.commit();

    }
    public String getBillPhone(){
        return  sharedprefernce.getString(billPhone,"Phone");
    }

    public void setUserPassword(String userPass ){

        editor.putString(userPassword,userPass);
        editor.commit();

    }

    public String getUserPassword() {
        return sharedprefernce.getString(userPassword,"DEFAULT");
    }

    public void setSessionId(String sessionid ){

        editor.putString(SessionId,sessionid);
        editor.commit();

    }
    public  String getSessionId() {
        return sharedprefernce.getString(SessionId,"DEFAULT");
    }

    public  String getPhoto() {
        return sharedprefernce.getString(Photo,"DEFAULT");
    }
    public void setPhoto(String photo ){
        editor.putString(Photo,photo);
        editor.commit();

    }
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);





    }

}


