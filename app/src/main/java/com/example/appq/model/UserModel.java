package com.example.appq.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.picasso.RequestCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel implements Parcelable {
   public String avatar;
   public String name;
   public String email;
   public String phoneNumber;
   public String address;
    boolean owner, gender;
    private boolean enable;
    //Id người dùng ở đây là uid trong firebaseauthen
    String userID;

    RequestCreator compressionImageFit;

    public UserModel() {
    }

    public UserModel(String avatar, String name, String email, String phoneNumber, String address, boolean owner, boolean gender, boolean enable, String userID, RequestCreator compressionImageFit) {
        this.avatar = avatar;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.owner = owner;
        this.gender = gender;
        this.enable = enable;
        this.userID = userID;
        this.compressionImageFit = compressionImageFit;
    }


    protected UserModel(Parcel in) {
        avatar = in.readString();
        name = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
        owner = in.readByte() != 0;
        gender = in.readByte() != 0;
        enable = in.readByte() != 0;
        userID = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        owner = owner;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public RequestCreator getCompressionImageFit() {
        return compressionImageFit;
    }

    public void setCompressionImageFit(RequestCreator compressionImageFit) {
        this.compressionImageFit = compressionImageFit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatar);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(address);
        dest.writeByte((byte) (owner ? 1 : 0));
        dest.writeByte((byte) (gender ? 1 : 0));
        dest.writeString(userID);
    }
    public Map<String, Object> toMapLock() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("enable",enable);
        return map;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("email", email);
        map.put("enable", enable);
        map.put("phoneNumber", phoneNumber);
        return map;
    }


}

//    public Map<String, Object> toMapListRoom() {
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        map.put("list_room", list_room);
//        return map;
//    }

