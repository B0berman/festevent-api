package com.visitcardpro.api.services;

import com.visitcardpro.api.MyRetrofit;
import com.visitcardpro.beans.Group;
import com.visitcardpro.beans.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class GroupService extends Service {

    public GroupService(MyRetrofit r) {
        super(r);
    }


    public Call<Group> createGroup(Group group) {
        return retrofit.createGroup(group, token);
    }

    public Call<ResponseBody> leaveGroup(String id) {
        return retrofit.leaveGroup(token, id);
    }

    public Call<ResponseBody> addGroupMember(String id, String email) {
        return retrofit.addGroupMember(token, id, email);
    }

    public Call<List<User>> getGroupMembers(String id) {
        return retrofit.getGroupMembers(token, id);
    }
}
