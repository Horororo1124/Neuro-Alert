package com.example.brain_uof.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brain_uof.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class userChange extends AppCompatActivity {
    Button savebtn;
    EditText UID1, UID2, UID3, UID4;
    TextView choosefamily1, choosefamily2, choosefamily3, choosefamily4;
    ImageView xicon;
    String userId = null;

    // 파이어베이스 초기화
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change);
        savebtn = findViewById(R.id.savebtn);
        xicon = findViewById(R.id.xicon);
        choosefamily1 = findViewById(R.id.choosefamily1);
        choosefamily2 = findViewById(R.id.choosefamily2);
        choosefamily3 = findViewById(R.id.choosefamily3);
        choosefamily4 = findViewById(R.id.choosefamily4);
        UID1 = findViewById(R.id.UID1);
        UID2 = findViewById(R.id.UID2);
        UID3 = findViewById(R.id.UID3);
        UID4 = findViewById(R.id.UID4);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            userId = currentUser.getUid();
        }

        // X 아이콘 클릭 시 액티비티 종료
        xicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 저장 버튼 클릭 시 그룹 생성 함수 호출 및 액티비티 종료
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup();
                finish();
            }
        });
    }

    // 그룹 생성 함수
    // 그룹 생성 함수
    // 그룹 생성 함수
    private void createGroup() {
        // 그룹 정보 입력 받기

        String family1 = "할아버지";
        String uid1 = UID1.getText().toString().trim();
        String family2 = "할머니";
        String uid2 = UID2.getText().toString().trim();
        String family3 = "아버지";
        String uid3 = UID3.getText().toString().trim();
        String family4 = "어머니";
        String uid4 = UID4.getText().toString().trim();

        // 그룹 객체 생성
        Group group = new Group(family1, uid1, family2, uid2, family3, uid3, family4, uid4);

        // 비어 있는 변수들을 null로 설정
        if (family1.isEmpty() || uid1.isEmpty()) {
            group.setFamily1(null);
            group.setUid1(null);
        }
        if (family2.isEmpty() || uid2.isEmpty()) {
            group.setFamily2(null);
            group.setUid2(null);
        }
        if (family3.isEmpty() || uid3.isEmpty()) {
            group.setFamily3(null);
            group.setUid3(null);
        }
        if (family4.isEmpty() || uid4.isEmpty()) {
            group.setFamily4(null);
            group.setUid4(null);
        }

        // 파이어베이스 데이터베이스에 그룹 정보 저장
        DatabaseReference groupRef = databaseReference.child("users").child(userId).child("groups");
        groupRef.setValue(group);

        // 그룹 인원의 health data 가져와서 저장
        // UID1에 대한 건강 데이터 가져오기
        if (uid1 != null && !uid1.isEmpty()) {
            DatabaseReference healthDataRef1 = database.getReference("users").child(uid1).child("health");
            healthDataRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // 가져온 health data를 그룹 정보에 추가하여 저장
                    group.setHealthData1(snapshot.getValue());
                    // 파이어베이스에 그룹 정보 업데이트
                    groupRef.child("healthData1").setValue(group.getHealthData1());

                    // Toast 메시지로 그룹 생성 완료를 표시
                    Toast.makeText(userChange.this, "그룹이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show();

                    // 액티비티 종료
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 데이터 가져오기 실패 시 처리
                    // Toast 메시지로 실패를 표시
                    Toast.makeText(userChange.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // UID1이 없을 경우 해당 그룹 정보에서 healthData1 제거
            groupRef.child("healthData1").removeValue();

            // Toast 메시지로 그룹 생성 완료를 표시
            Toast.makeText(userChange.this, "그룹이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show();

            // 액티비티 종료
            finish();
        }

// UID2에 대한 건강 데이터 가져오기
        if (uid2 != null && !uid2.isEmpty()) {
            DatabaseReference healthDataRef2 = database.getReference("users").child(uid2).child("health");
            healthDataRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // 가져온 health data를 그룹 정보에 추가하여 저장
                    group.setHealthData2(snapshot.getValue());
                    // 파이어베이스에 그룹 정보 업데이트
                    groupRef.child("healthData2").setValue(group.getHealthData2());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 데이터 가져오기 실패 시 처리
                    // Toast 메시지로 실패를 표시
                    Toast.makeText(userChange.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // UID2이 없을 경우 해당 그룹 정보에서 healthData2 제거
            groupRef.child("healthData2").removeValue();
        }

// UID3에 대한 건강 데이터 가져오기
        if (uid3 != null && !uid3.isEmpty()) {
            DatabaseReference healthDataRef3 = database.getReference("users").child(uid3).child("health");
            healthDataRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // 가져온 health data를 그룹 정보에 추가하여 저장
                    group.setHealthData3(snapshot.getValue());
                    // 파이어베이스에 그룹 정보 업데이트
                    groupRef.child("healthData3").setValue(group.getHealthData3());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 데이터 가져오기 실패 시 처리
                    // Toast 메시지로 실패를 표시
                    Toast.makeText(userChange.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // UID3이 없을 경우 해당 그룹 정보에서 healthData3 제거
            groupRef.child("healthData3").removeValue();
        }

// UID4에 대한 건강 데이터 가져오기
        if (uid4 != null && !uid4.isEmpty()) {
            DatabaseReference healthDataRef4 = database.getReference("users").child(uid4).child("health");
            healthDataRef4.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // 가져온 health data를 그룹 정보에 추가하여 저장
                    group.setHealthData4(snapshot.getValue());
                    // 파이어베이스에 그룹 정보 업데이트
                    groupRef.child("healthData4").setValue(group.getHealthData4());

                    // Toast 메시지로 그룹 생성 완료를 표시
                    Toast.makeText(userChange.this, "그룹이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show();

                    // 액티비티 종료
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // 데이터 가져오기 실패 시 처리
                    // Toast 메시지로 실패를 표시
                    Toast.makeText(userChange.this, "데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // UID4이 없을 경우 해당 그룹 정보에서 healthData4 제거
            groupRef.child("healthData4").removeValue();

            // Toast 메시지로 그룹 생성 완료를 표시
            Toast.makeText(userChange.this, "그룹이 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show();

            // 액티비티 종료
            finish();
        }

    }






    // 그룹 클래스
    public class Group {
        private String family1;
        private String uid1;
        private String family2;
        private String uid2;
        private String family3;
        private String uid3;
        private String family4;
        private String uid4;
        private Object healthData1;
        private Object healthData2;
        private Object healthData3;
        private Object healthData4;

        public Group() {
            // Default constructor required for Firebase
        }

        public Group( String family1, String uid1, String family2, String uid2,
                     String family3, String uid3, String family4, String uid4) {
            this.family1 = family1;
            this.uid1 = uid1;
            this.family2 = family2;
            this.uid2 = uid2;
            this.family3 = family3;
            this.uid3 = uid3;
            this.family4 = family4;
            this.uid4 = uid4;
        }


        public String getFamily1() {
            return family1;
        }

        public void setFamily1(String family1) {
            this.family1 = family1;
        }

        public String getUid1() {
            return uid1;
        }

        public void setUid1(String uid1) {
            this.uid1 = uid1;
        }

        public String getFamily2() {
            return family2;
        }

        public void setFamily2(String family2) {
            this.family2 = family2;
        }

        public String getUid2() {
            return uid2;
        }

        public void setUid2(String uid2) {
            this.uid2 = uid2;
        }

        public String getFamily3() {
            return family3;
        }

        public void setFamily3(String family3) {
            this.family3 = family3;
        }

        public String getUid3() {
            return uid3;
        }

        public void setUid3(String uid3) {
            this.uid3 = uid3;
        }

        public String getFamily4() {
            return family4;
        }

        public void setFamily4(String family4) {
            this.family4 = family4;
        }

        public String getUid4() {
            return uid4;
        }

        public void setUid4(String uid4) {
            this.uid4 = uid4;
        }

        public Object getHealthData1() {
            return healthData1;
        }

        public void setHealthData1(Object healthData1) {
            this.healthData1 = healthData1;
        }

        public Object getHealthData2() {
            return healthData2;
        }

        public void setHealthData2(Object healthData2) {
            this.healthData2 = healthData2;
        }

        public Object getHealthData3() {
            return healthData3;
        }

        public void setHealthData3(Object healthData3) {
            this.healthData3 = healthData3;
        }

        public Object getHealthData4() {
            return healthData4;
        }

        public void setHealthData4(Object healthData4) {
            this.healthData4 = healthData4;
        }
    }
}
