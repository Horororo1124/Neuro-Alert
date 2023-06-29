package com.example.brain_uof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class signin extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword, editTextName, editTextNickname, editTextPhone;
    private Button buttonRegister;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Firebase 인스턴스 초기화
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // XML에서 뷰 찾기
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextName = findViewById(R.id.name1);
        editTextNickname = findViewById(R.id.nickname1);
        editTextPhone = findViewById(R.id.phone1);
        buttonRegister = findViewById(R.id.buttonRegister);

        // 회원가입 버튼 클릭 이벤트 처리
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String name = editTextName.getText().toString();
                String nickname = editTextNickname.getText().toString();
                String phone = editTextPhone.getText().toString();

                // Firebase 인증 회원가입
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 회원가입 성공
                                    String userId = firebaseAuth.getCurrentUser().getUid();

                                    // 사용자 정보를 HashMap으로 저장
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("name", name);
                                    userMap.put("nickname", nickname);
                                    userMap.put("phone", phone);

                                    // Firebase Realtime Database에 사용자 정보 저장
                                    databaseReference.child("users").child(userId).setValue(userMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(signin.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                                        finish(); // 회원가입 성공 후 현재 Activity 종료
                                                    } else {
                                                        Toast.makeText(signin.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // 회원가입 실패
                                    Toast.makeText(signin.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}