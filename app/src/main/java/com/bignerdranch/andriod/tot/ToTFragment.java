package com.bignerdranch.andriod.tot;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Benj on 2017/4/11.
 */

public class ToTFragment extends Fragment{
    private Account mAccount;

    private ImageView mAvatorButton;
    private EditText mAccountEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private TextView mForgetPasswordTextView;
    private TextView mRegister;
    private TextInputLayout mTextInputLayout;

    private static final String TAG = "ToTFragment";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        mAccount = new Account();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_login,container,false);

        final UserInfo userInfo = new UserInfo(getActivity(),"UserInfo");



        mTextInputLayout = (TextInputLayout)v.findViewById(R.id.text_input_layout);
        mAvatorButton = (ImageView) v.findViewById(R.id.fragment_login_avtor);
        mAccountEditText = (EditText)v.findViewById(R.id.account_edit_text);
        mPasswordEditText = (EditText)v.findViewById(R.id.password_edit_text);
        mPasswordEditText = mTextInputLayout.getEditText();
        mLoginButton = (Button)v.findViewById(R.id.login_button);
        mForgetPasswordTextView = (TextView)v.findViewById(R.id.forget_password_text_view);
        mRegister = (TextView)v.findViewById(R.id.register_text_view);


        mAccountEditText.addTextChangedListener(new TextWatcher() {
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (userInfo.contains(mAccount.getAccount())){
                    if (userInfo.getString(mAccount.getAccount(),mAccount.getPassword()).equals(mAccount.getPassword())){
                        Toast.makeText(getActivity(),"登录成功！",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),HomeAcitivity.class);
                        startActivity(intent);
                    } else{
                        Toast.makeText(getActivity(),"密码错误！",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getActivity(),"账号不存在！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        return v;

    }
}
