package com.bignerdranch.andriod.tot;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Roy on 2017/4/12.
 */

public class RegisterFragment extends Fragment{
    private Account mAccount;
    private Button mNextStepButton;
    private EditText mPasswordEditText;
    private EditText mPhoneEditText;
    private TextInputLayout mTextInputLayout;

    private String RealPhoneNumber = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";

    private static final String TAG = "RegisterFragment";
    private Pattern mPattern;
    private Matcher mMatcher;



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        mAccount = new Account();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        final UserInfo userInfo = new UserInfo(getActivity(),"UserInfo");

        mTextInputLayout = (TextInputLayout)v.findViewById(R.id.register_text_layout);
        mPhoneEditText = (EditText)v.findViewById(R.id.phone_edit_text);
        mNextStepButton = (Button)v.findViewById(R.id.next_step);
        mPasswordEditText = (EditText) v.findViewById(R.id.register_password_edit_text);

        mPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAccount.setAccount(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAccount.setPassword(s.toString());
                Log.i(TAG," "+mAccount.getPassword());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNextStepButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (userInfo.contains(mAccount.getAccount())){
                    Toast.makeText(getActivity(),"该手机号已注册！",Toast.LENGTH_SHORT).show();
                }else {
                    mPattern = Pattern.compile(RealPhoneNumber);
                    mMatcher = mPattern.matcher(mAccount.getAccount());
                    if (mMatcher.matches()){
                        if (mAccount.getPassword().length() < 8){
                            Toast.makeText(getActivity(),"密码不能小于8位",Toast.LENGTH_SHORT).show();
                        }else{
                            userInfo.putString(mAccount.getAccount(),mAccount.getPassword());
                            Toast.makeText(getActivity(),"注册成功！",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(),ToTActivity.class);
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(getActivity(),"这不是一个有效的号码！",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return v;
    }
}
