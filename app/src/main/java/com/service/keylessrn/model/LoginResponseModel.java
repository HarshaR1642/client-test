package com.service.keylessrn.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LoginResponseModel implements Parcelable {
    @SerializedName("success")
    private String success;

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("id_token")
    private String id_token;

    @SerializedName("refresh_token")
    private String refresh_token;

    protected LoginResponseModel(Parcel in) {
        success = in.readString();
        access_token = in.readString();
        id_token = in.readString();
        refresh_token = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(success);
        dest.writeString(access_token);
        dest.writeString(id_token);
        dest.writeString(refresh_token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoginResponseModel> CREATOR = new Creator<LoginResponseModel>() {
        @Override
        public LoginResponseModel createFromParcel(Parcel in) {
            return new LoginResponseModel(in);
        }

        @Override
        public LoginResponseModel[] newArray(int size) {
            return new LoginResponseModel[size];
        }
    };

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }


}
