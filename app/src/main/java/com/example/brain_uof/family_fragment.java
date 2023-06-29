package com.example.brain_uof;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import app.futured.donut.DonutProgressView;
import app.futured.donut.DonutSection;


public class family_fragment extends Fragment {
    //donut char
    DonutProgressView gfd1, gfd2, gfd3, gmd1, gmd2, gmd3, fd1, fd2, fd3, md1, md2, md3;
    String userId = "";

    int color;
    //할아버지
    float gfd111 = 0;
    float gfd222 = 0;
    float gfd333 = 0;
    //할머니
    float gmd111 = 0;
    float gmd222 = 0;
    float gmd333 = 0;
    //아버지
    float fd111 = 0;
    float fd222 = 0;
    float fd333 = 0;
    //어머니
    float md111 = 0;
    float md222 = 0;
    float md333 = 0;

    //날짜
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    public static String format_yyyyMMdd = "yyyyMMdd";
    String stringDateSelected;

    //파이어베이스
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    DatabaseReference myRef = database.getReference();

    Button resetbtn1;

    boolean isGroupMember = true;  // 그룹 멤버십 여부

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_family_fragment, container, false);

        databaseReference = database.getReference();

        // FirebaseAuth 인스턴스 가져오기
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // 현재 로그인된 사용자 가져오기
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        //donut char
        gfd1 = v.findViewById(R.id.gfd1);
        gfd2 = v.findViewById(R.id.gfd2);
        gfd3 = v.findViewById(R.id.gfd3);

        gmd1 = v.findViewById(R.id.gmd1);
        gmd2 = v.findViewById(R.id.gmd2);
        gmd3 = v.findViewById(R.id.gmd3);

        fd1 = v.findViewById(R.id.fd1);
        fd2 = v.findViewById(R.id.fd2);
        fd3 = v.findViewById(R.id.fd3);

        md1 = v.findViewById(R.id.md1);
        md2 = v.findViewById(R.id.md2);
        md3 = v.findViewById(R.id.md3);

        resetbtn1 = v.findViewById(R.id.resetbtn1);


        if (currentUser != null) {
            // UID 가져오기
            userId = currentUser.getUid();
            // UID를 사용하여 원하는 작업 수행
            // 예: 데이터베이스에 사용자 정보 저장 등
        }


        // 현재 날짜 가져오기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String getTime = dateFormat.format(date);

        stringDateSelected = getTime;


        resetbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMedoToFirebase1();
                loadMedoToFirebase2();
                loadMedoToFirebase3();
                loadMedoToFirebase4();

                loaddrdoToFirebase1();
                loaddrdoToFirebase2();
                loaddrdoToFirebase3();
                loaddrdoToFirebase4();

                loadsmdoToFirebase1();
                loadsmdoToFirebase2();
                loadsmdoToFirebase3();
                loadsmdoToFirebase4();
            }
        });



        return v;
        //return (ViewGroup)inflater.inflate(R.layout.fragment_family_fragment, container, false);
    }

    public void loadMedoToFirebase1() {
        if (isGroupMember) {
            databaseReference.child("users").child(userId).child("groups").child("healthData1").child(stringDateSelected).child("medicine").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        Long medoValue = dataSnapshot.getValue(Long.class);
                        if (medoValue != null) {
                            int medo = medoValue.intValue();

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

                            gfd1.setCap(3f);
                            gfd1.submitData(data);
                        } else {
                            // medoValue가 null인 경우 처리
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 데이터 가져오기가 실패한 경우 호출됨
                    Log.e("diary", "값을 읽어오지 못했습니다.", databaseError.toException());
                }
            });
        }

    }

    public void loadMedoToFirebase2() {
        if (isGroupMember) {
            databaseReference.child("users").child(userId).child("groups").child("healthData2").child(stringDateSelected).child("medicine").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        Long medoValue = dataSnapshot.getValue(Long.class);
                        if (medoValue != null) {
                            int medo = medoValue.intValue();

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

                            gmd1.setCap(3f);
                            gmd1.submitData(data);
                        } else {
                            // medoValue가 null인 경우 처리
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 데이터 가져오기가 실패한 경우 호출됨
                    Log.e("diary", "값을 읽어오지 못했습니다.", databaseError.toException());
                }
            });
        }

    }

    public void loadMedoToFirebase3() {
        if (isGroupMember) {
            databaseReference.child("users").child(userId).child("groups").child("healthData3").child(stringDateSelected).child("medicine").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        Long medoValue = dataSnapshot.getValue(Long.class);
                        if (medoValue != null) {
                            int medo = medoValue.intValue();

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

                            fd1.setCap(3f);
                            fd1.submitData(data);
                        } else {
                            // medoValue가 null인 경우 처리
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 데이터 가져오기가 실패한 경우 호출됨
                    Log.e("diary", "값을 읽어오지 못했습니다.", databaseError.toException());
                }
            });
        }

    }
    public void loadMedoToFirebase4() {
        if (isGroupMember) {
            databaseReference.child("users").child(userId).child("groups").child("healthData4").child(stringDateSelected).child("medicine").addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        Long medoValue = dataSnapshot.getValue(Long.class);
                        if (medoValue != null) {
                            int medo = medoValue.intValue();

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

                            md1.setCap(3f);
                            md1.submitData(data);
                        } else {
                            // medoValue가 null인 경우 처리
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 데이터 가져오기가 실패한 경우 호출됨
                    Log.e("diary", "값을 읽어오지 못했습니다.", databaseError.toException());
                }
            });
        }

    }


    public void loaddrdoToFirebase1() {
        if (isGroupMember) {
            DatabaseReference reference = databaseReference.child("users").child(userId).child("groups").child("healthData1").child(stringDateSelected).child("NO_drink");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Long drodoValue = dataSnapshot.getValue(Long.class);
                        if (drodoValue != null) {
                            Float gfd222 = drodoValue.floatValue();
                            DonutSection section1 = new DonutSection("section_1", Color.parseColor("#B6C3A2"), gfd222);
                            gfd2.setCap(1f);
                            List<DonutSection> data = new ArrayList<>();
                            data.add(section1);
                            gfd2.submitData(data);
                        } else {
                            // drodoValue가 null인 경우 처리
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 데이터 가져오기가 실패한 경우 호출됨
                    Log.e("diary", "값을 읽어오지 못했습니다.", databaseError.toException());
                }
            });
        }
    }
    public void loaddrdoToFirebase2() {
        if (isGroupMember) {
            DatabaseReference reference = databaseReference.child("users").child(userId).child("groups").child("healthData2").child(stringDateSelected).child("NO_drink");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Long drodoValue = dataSnapshot.getValue(Long.class);
                        if (drodoValue != null) {
                            Float gfd222 = drodoValue.floatValue();
                            DonutSection section1 = new DonutSection("section_1", Color.parseColor("#B6C3A2"), gfd222);
                            gmd2.setCap(1f);
                            List<DonutSection> data = new ArrayList<>();
                            data.add(section1);
                            gmd2.submitData(data);
                        } else {
                            // drodoValue가 null인 경우 처리
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 데이터 가져오기가 실패한 경우 호출됨
                    Log.e("diary", "값을 읽어오지 못했습니다.", databaseError.toException());
                }
            });
        }
    }
    public void loaddrdoToFirebase3() {
        if (isGroupMember) {
            DatabaseReference reference = databaseReference.child("users").child(userId).child("groups").child("healthData3").child(stringDateSelected).child("NO_drink");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Long drodoValue = dataSnapshot.getValue(Long.class);
                        if (drodoValue != null) {
                            Float gfd222 = drodoValue.floatValue();
                            DonutSection section1 = new DonutSection("section_1", Color.parseColor("#B6C3A2"), gfd222);
                            fd2.setCap(1f);
                            List<DonutSection> data = new ArrayList<>();
                            data.add(section1);
                            fd2.submitData(data);
                        } else {
                            // drodoValue가 null인 경우 처리
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 데이터 가져오기가 실패한 경우 호출됨
                    Log.e("diary", "값을 읽어오지 못했습니다.", databaseError.toException());
                }
            });
        }
    }
    public void loaddrdoToFirebase4() {
        if (isGroupMember) {
            DatabaseReference reference = databaseReference.child("users").child(userId).child("groups").child("healthData4").child(stringDateSelected).child("NO_drink");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Long drodoValue = dataSnapshot.getValue(Long.class);
                        if (drodoValue != null) {
                            Float gfd222 = drodoValue.floatValue();
                            DonutSection section1 = new DonutSection("section_1", Color.parseColor("#B6C3A2"), gfd222);
                            md2.setCap(1f);
                            List<DonutSection> data = new ArrayList<>();
                            data.add(section1);
                            md2.submitData(data);
                        } else {
                            // drodoValue가 null인 경우 처리
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 데이터 가져오기가 실패한 경우 호출됨
                    Log.e("diary", "값을 읽어오지 못했습니다.", databaseError.toException());
                }
            });
        }
    }


    public void loadsmdoToFirebase1() {

        DatabaseReference reference;
        if (isGroupMember) {
            reference = databaseReference.child("users").child(userId).child("groups").child("healthData1").child(stringDateSelected).child("NO_smoke");
        } else {
            reference = databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("NO_smoke");
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long smdoValue = dataSnapshot.getValue(Long.class);
                    if (smdoValue != null) {
                        Float gfd333 = smdoValue.floatValue();
                        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#C5E1DE"), gfd333);
                        gfd3.setCap(1f);
                        List<DonutSection> data = new ArrayList<>();
                        data.add(section1);
                        gfd3.submitData(data);

                    } else {
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

    public void loadsmdoToFirebase2() {

        DatabaseReference reference;
        if (isGroupMember) {
            reference = databaseReference.child("users").child(userId).child("groups").child("healthData2").child(stringDateSelected).child("NO_smoke");
        } else {
            reference = databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("NO_smoke");
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long smdoValue = dataSnapshot.getValue(Long.class);
                    if (smdoValue != null) {
                        Float gfd333 = smdoValue.floatValue();
                        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#C5E1DE"), gfd333);
                        gmd3.setCap(1f);
                        List<DonutSection> data = new ArrayList<>();
                        data.add(section1);
                        gmd3.submitData(data);

                    } else {
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

    public void loadsmdoToFirebase3() {

        DatabaseReference reference;
        if (isGroupMember) {
            reference = databaseReference.child("users").child(userId).child("groups").child("healthData3").child(stringDateSelected).child("NO_smoke");
        } else {
            reference = databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("NO_smoke");
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long smdoValue = dataSnapshot.getValue(Long.class);
                    if (smdoValue != null) {
                        Float gfd333 = smdoValue.floatValue();
                        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#C5E1DE"), gfd333);
                        fd3.setCap(1f);
                        List<DonutSection> data = new ArrayList<>();
                        data.add(section1);
                        fd3.submitData(data);

                    } else {
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

    public void loadsmdoToFirebase4() {

        DatabaseReference reference;
        if (isGroupMember) {
            reference = databaseReference.child("users").child(userId).child("groups").child("healthData4").child(stringDateSelected).child("NO_smoke");
        } else {
            reference = databaseReference.child("users").child(userId).child("health").child(stringDateSelected).child("NO_smoke");
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long smdoValue = dataSnapshot.getValue(Long.class);
                    if (smdoValue != null) {
                        Float gfd333 = smdoValue.floatValue();
                        DonutSection section1 = new DonutSection("section_1", Color.parseColor("#C5E1DE"), gfd333);
                        md3.setCap(1f);
                        List<DonutSection> data = new ArrayList<>();
                        data.add(section1);
                        md3.submitData(data);

                    } else {
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



}
