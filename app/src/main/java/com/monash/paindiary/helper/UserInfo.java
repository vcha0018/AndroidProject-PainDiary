package com.monash.paindiary.helper;

public class UserInfo {
    private static UserInfo INSTANCE;
    private String userEmail;
    private String userPassword;

    public static void setINSTANCE(String userEmail, String userPassword, boolean forceOverride) {
        if (INSTANCE == null || forceOverride)
            INSTANCE = new UserInfo(userEmail, userPassword);
    }

    public static UserInfo getInstance() {
        return INSTANCE;
    }

    private UserInfo(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public static String getUserEmail() {
        if (INSTANCE == null)
            return "";
        else
            return INSTANCE.userEmail;
    }
}
