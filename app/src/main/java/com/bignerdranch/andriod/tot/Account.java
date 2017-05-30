package com.bignerdranch.andriod.tot;

import android.widget.Button;

import java.util.UUID;

/**
 * Created by Roy on 2017/4/12.
 */

public class Account {
    private int id;
    private UUID mId;
    private String mAccount;
    private String mPassword;
    private boolean mIsChecked;

    public Account(){
        mId = UUID.randomUUID();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        mAccount = account;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }
}
