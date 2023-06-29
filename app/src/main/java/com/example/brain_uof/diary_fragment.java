package com.example.brain_uof;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;


public class diary_fragment extends Fragment {
    int numDrink;
    //홈 화면 버튼
    ImageView familybtn, diarybtn, rehabilitationbtn, informationbtn, homebtn;


    //donut char
    DonutProgressView medobtn, rundobtn, smdobtn, drdobtn;
    String name;
    int color;
    float medo = 0;
    float rundo = 0;
    float smdo = 0;
    float drdo = 0;
    Long medo1, rundo1, smdo1, drdo1;


    //캘린더뷰
    CalendarView Cal1;
    String stringDateSelected;

    //텍스트 뷰
    TextView text1, text2, text3, text4;

    //파이어베이스
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    DatabaseReference myRef = database.getReference();

    //리셋 버튼
    Button resetButton;

    String userId="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.fragment_diary_fragment, container, false);
        // 파이어베이스 초기화
        databaseReference = database.getReference();

        // FirebaseAuth 인스턴스 가져오기
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // 현재 로그인된 사용자 가져오기
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        //donut char
        medobtn = v.findViewById(R.id.medobtn);
        rundobtn = v.findViewById(R.id.rundobtn);
        smdobtn = v.findViewById(R.id.smdobtn);
        drdobtn = v.findViewById(R.id.drdobtn);

        medobtn.setVisibility(View.INVISIBLE);
        rundobtn.setVisibility(View.INVISIBLE);
        smdobtn.setVisibility(View.INVISIBLE);
        drdobtn.setVisibility(View.INVISIBLE);

        //캘린더 뷰
        Cal1 = v.findViewById(R.id.Cal1);

        //텍스트 뷰
        text1 = v.findViewById(R.id.text1);
        text2 = v.findViewById(R.id.text2);
        text3 = v.findViewById(R.id.text3);
        text4 = v.findViewById(R.id.text4);

        text1.setVisibility(View.INVISIBLE);
        text2.setVisibility(View.INVISIBLE);
        text3.setVisibility(View.INVISIBLE);
        text4.setVisibility(View.INVISIBLE);

        //리셋 버튼
        resetButton = v.findViewById(R.id.ResetButton);

        //캘린더 뷰 기능

        Cal1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayofMonth) {
                if (currentUser != null) {
                    // UID 가져오기
                    userId = currentUser.getUid();
                    // UID를 사용하여 원하는 작업 수행
                    // 예: 데이터베이스에 사용자 정보 저장 등
                }


                text1.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);
                text3.setVisibility(View.VISIBLE);
                text4.setVisibility(View.VISIBLE);

                medobtn.setVisibility(View.VISIBLE);
                rundobtn.setVisibility(View.VISIBLE);
                smdobtn.setVisibility(View.VISIBLE);
                drdobtn.setVisibility(View.VISIBLE);

                // 선택된 날짜를 yyyyMMdd 형식의 문자열로 변환
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date(year - 1900, month, dayofMonth);
                stringDateSelected = sdf.format(date);

                //변수 초기화
                resetValue();

                //변수 불러오기
                loadMedoToFirebase();
                loadrundoToFirebase();
                loadsmdoToFirebase();
                loaddrdoToFirebase();

                //약 donut char
                /*medobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        medo++; // medo 변수 증가

                        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#FB1D32"), medo);
                        DonutSection section2 = new DonutSection("section_2", Color.parseColor("#FFA500"), medo);
                        DonutSection section3 = new DonutSection("section_3", Color.parseColor("#80cee1"), medo);
                        medobtn.setCap(3f);
                        List<DonutSection> data = new ArrayList<>();
                        data.add(section1);
                        data.add(section2);
                        data.add(section3);
                        medobtn.submitData(data);

                        saveMedoToFirebase();

                    }
                });*/
                medobtn.setOnClickListener(new View.OnClickListener() {
                    private int clickCount = 0;  // 클릭 횟수를 저장하는 변수

                    @Override
                    public void onClick(View view) {
                        medo++; // medo 변수 증가
                        clickCount++; // 클릭 횟수 증가

                        List<DonutSection> data = new ArrayList<>();

                        if (medo >= 1) {
                            DonutSection section1 = new DonutSection("section_1", Color.parseColor("#FB1D32"), 1);
                            data.add(section1);
                        }

                        if (medo >= 2) {
                            DonutSection section2 = new DonutSection("section_2", Color.parseColor("#FFA500"), 1);
                            data.add(section2);
                        }

                        if (medo >= 3) {
                            DonutSection section3 = new DonutSection("section_3", Color.parseColor("#80cee1"), 1);
                            data.add(section3);
                        }

                        medobtn.setCap(3f);
                        medobtn.submitData(data);

                        saveMedoToFirebase();

                        if (clickCount >= 3) {
                            Toast.makeText(getActivity(), "오늘의 할당치를 채웠습니다.", Toast.LENGTH_SHORT).show();
                            medobtn.setEnabled(false);  // 클릭 비활성화
                        }
                    }
                });

                //산책 donut char
                rundobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        rundo++; // medo 변수 증가

                        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#E8C47E"), rundo);
                        rundobtn.setCap(1f);
                        List<DonutSection> data = new ArrayList<>();
                        data.add(section1);
                        rundobtn.submitData(data);

                        saveRundoToFirebase();

                    }
                });

                //담배 donut char
                smdobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        smdo++; // medo 변수 증가

                        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#B6C3A2"), smdo);
                        smdobtn.setCap(1f);
                        List<DonutSection> data = new ArrayList<>();
                        data.add(section1);
                        smdobtn.submitData(data);

                        saveSmdoToFirebase();

                    }
                });

                //술 donut char
                drdobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        drdo++; // medo 변수 증가

                        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#C5E1DE"), drdo);
                        drdobtn.setCap(1f);
                        List<DonutSection> data = new ArrayList<>();
                        data.add(section1);
                        drdobtn.submitData(data);

                        saveDrdoToFirebase();

                    }
                });


                //리셋 버튼 기능
                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("medicine").setValue(0f);
                        databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("run").setValue(0f);
                        databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("NO_drink").setValue(0f);
                        databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("NO_smoke").setValue(0f);
                        resetValue();

                    }
                });


            }
        });

        //return (ViewGroup)inflater.inflate(R.layout.fragment_diary_fragment, container, false);
        return v;
    }
    private void saveMedoToFirebase() {
        medo1 = Long.valueOf((long) medo);

        //medo1 = String.valueOf(medo);
        // 'medo' 변수를 Realtime Database에 저장
        databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("medicine").setValue(medo1);
    }
    private void saveRundoToFirebase() {
        rundo1 = Long.valueOf((long) rundo);
        //rundo1 = String.valueOf(rundo);
        databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("run").setValue(rundo1);
    }
    private void saveSmdoToFirebase() {
        smdo1 = Long.valueOf((long) smdo);
        //smdo1 = String.valueOf(smdo);
        databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("NO_smoke").setValue(smdo1);
    }
    private void saveDrdoToFirebase() {
        drdo1 = Long.valueOf((long) drdo);
        //drdo1 = String.valueOf(drdo);
        databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("NO_drink").setValue(drdo1);
    }

    public void loadMedoToFirebase() {

        databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("medicine").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long medoValue = dataSnapshot.getValue(Long.class);
                    //String medoValue = dataSnapshot.getValue(String.class);
                    if (medoValue != null ) {
                        medo = medoValue.floatValue();
                        //medo = Float.parseFloat(medoValue); // medo 변수에 값 할당
                        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#FB1D32"), medo);
                        medobtn.setCap(3f);
                        List<DonutSection> data = new ArrayList<>();
                        data.add(section1);
                        medobtn.submitData(data);

                    }
                    else{
                        medo =0;
                        rundo = 0;
                        smdo = 0;
                        drdo = 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 가져오기가 실패한 경우에 호출됩니다.
                Log.e("diary", "Failed to read value.", databaseError.toException());
            }
        });
    }
    public void loadrundoToFirebase() {
        databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("run").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long rundoValue = dataSnapshot.getValue(Long.class);
                    //String rundoValue = dataSnapshot.getValue(String.class);

                    if (rundoValue != null) {
                        Float rundo = rundoValue.floatValue();
                        //rundo = Float.parseFloat(rundoValue); // rundo 변수에 값 할당

                        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#FB1D32"), rundo);
                        rundobtn.setCap(1f);
                        List<DonutSection> data = new ArrayList<>();
                        data.add(section1);
                        rundobtn.submitData(data);

                    }
                    else{
                        medo =0;
                        rundo = 0;
                        smdo = 0;
                        drdo = 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 가져오기가 실패한 경우에 호출됩니다.
                Log.e("diary", "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void loadsmdoToFirebase() {
        databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("NO_smoke").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long smdoValue = dataSnapshot.getValue(Long.class);
                    //String smdoValue = dataSnapshot.getValue(String.class);


                    if (smdoValue != null) {
                        Float smdo = smdoValue.floatValue();
                        //smdo = Float.parseFloat(smdoValue); // smdo 변수에 값 할당

                        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#FB1D32"), smdo);
                        smdobtn.setCap(1f);
                        List<DonutSection> data = new ArrayList<>();
                        data.add(section1);
                        smdobtn.submitData(data);

                    }
                    else{
                        medo =0;
                        rundo = 0;
                        smdo = 0;
                        drdo = 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 가져오기가 실패한 경우에 호출됩니다.
                Log.e("diary", "Failed to read value.", databaseError.toException());
            }
        });
    }
    public void loaddrdoToFirebase() {
        databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("NO_drink").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long drodoValue = dataSnapshot.getValue(Long.class);
                    //String drodoValue = dataSnapshot.getValue(String.class);


                    if (drodoValue != null) {
                        Float drdo = drodoValue.floatValue();
                        //drdo = Float.parseFloat(drodoValue); // drdo 변수에 값 할당
                        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#FB1D32"), drdo);
                        drdobtn.setCap(1f);
                        List<DonutSection> data = new ArrayList<>();
                        data.add(section1);
                        drdobtn.submitData(data);

                    }
                    else{
                        medo =0;
                        rundo = 0;
                        smdo = 0;
                        drdo = 0;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 가져오기가 실패한 경우에 호출됩니다.
                Log.e("diary", "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void grouploaddrdoToFirebase() {
        DatabaseReference groupNamesRef = databaseReference.child("groupNames");

        groupNamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String retrievedGroupName = snapshot.getKey();
                        if (retrievedGroupName != null) {
                            String groupName = retrievedGroupName;

                            DatabaseReference groupRef = databaseReference.child("users").child(userId).child("health").child("groups").child(groupName).child(stringDateSelected).child("NO_drink");
                            groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Long drodoValue = dataSnapshot.getValue(Long.class);
                                        if (drodoValue != null) {
                                            Float drdo = drodoValue.floatValue();
                                            DonutSection section1 = new DonutSection("section_1", Color.parseColor("#FB1D32"), drdo);
                                            drdobtn.setCap(1f);
                                            List<DonutSection> data = new ArrayList<>();
                                            data.add(section1);
                                            drdobtn.submitData(data);
                                        } else {
                                            resetData();
                                        }
                                    } else {
                                        resetData();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("diary", "Failed to read value.", databaseError.toException());
                                }
                            });

                            break;
                        }
                    }
                } else {
                    resetData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("diary", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void resetData() {
        medo = 0;
        rundo = 0;
        smdo = 0;
        drdo = 0;
    }

    private void resetValue(){

        medo =0;
        rundo = 0;
        smdo = 0;
        drdo = 0;

        //그래프 초기화
        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#FB1D32"), medo);
        DonutSection section2 = new DonutSection("section_2", Color.parseColor("#FB1D32"), rundo);
        DonutSection section3 = new DonutSection("section_3", Color.parseColor("#FB1D32"), smdo);
        DonutSection section4 = new DonutSection("section_4", Color.parseColor("#FB1D32"), drdo);

        medobtn.setCap(5f);
        List<DonutSection> data = new ArrayList<>();
        data.add(section1);
        medobtn.submitData(data);

        rundobtn.setCap(5f);
        List<DonutSection> data1 = new ArrayList<>();
        data.add(section2);
        rundobtn.submitData(data1);

        smdobtn.setCap(5f);
        List<DonutSection> data2 = new ArrayList<>();
        data.add(section3);
        smdobtn.submitData(data2);

        drdobtn.setCap(5f);
        List<DonutSection> data3 = new ArrayList<>();
        data.add(section4);
        drdobtn.submitData(data3);
    }

    public String getSelectedDateString() {
        return stringDateSelected;
    }
}