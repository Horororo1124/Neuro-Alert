package com.example.brain_uof.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.example.brain_uof.R;
import com.example.brain_uof.UserPreferences;
import com.example.brain_uof.signin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class longin extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin,buttonsign;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_longin);

        // Firebase 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();

        // XML에서 뷰 찾기
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonsign = findViewById(R.id.buttonsign);

        // 로그인 버튼 클릭 이벤트 처리
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                // 이메일과 비밀번호를 사용하여 Firebase 인증 로그인
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 로그인 성공
                                    String userId = firebaseAuth.getCurrentUser().getUid();
                                    String userName = ""; // 사용자의 닉네임을 가져오는 코드를 추가해야 합니다.


                                    Toast.makeText(longin.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                    buttonLogin.setVisibility(View.GONE); // 로그인 버튼 숨기기
                                    finish();
                                } else {
                                    // 로그인 실패
                                    Toast.makeText(longin.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //회원가입 버튼 클릭 이벤트 처리
        buttonsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(longin.this, signin.class);
                startActivity(intent);
                finish();
            }
        });
    }


}