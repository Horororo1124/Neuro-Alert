package com.example.brain_uof;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brain_uof.Activity.longin;
import com.example.brain_uof.Activity.userChange;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import app.futured.donut.DonutSection;


public class mainhome_fragment extends Fragment {

    //신고 할 전화번호
    private static final String PHONE_NUMBER = "01046910848";
    private static final int SMS_PERMISSION_REQUEST_CODE = 101;

    Button loginbtn,logoutbtn,userbtn,btnPhoto,setText1;
    ImageView imPhoto;
    TextView logtxt;
    String userId = null;
    String Nick;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    DatabaseReference myRef = database.getReference();

    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            loadNickname(userId);
        } else {
            logtxt.setText("로그인이 필요합니다.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mainhome_fragment, container, false);

        //Gara
        setText1 = v.findViewById(R.id.setText1);

        //사진 전송 -이미지뷰, 버튼
        imPhoto = v.findViewById(R.id.imPhoto);
        btnPhoto = v.findViewById(R.id.btnPhoto);
        //로그인
        loginbtn = v.findViewById(R.id.loginbtn);
        logtxt = v.findViewById(R.id.logtxt);
        logoutbtn = v.findViewById(R.id.logoutbtn);
        //사용자
        userbtn = v.findViewById(R.id.userbtn);
        // FirebaseDatabase 인스턴스에서 DatabaseReference 가져오기
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // FirebaseAuth 인스턴스 가져오기
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // 현재 로그인된 사용자 가져오기
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            userId = currentUser.getUid();
            loadNickname(userId);  // 닉네임 로드

        } else {
            logtxt.setText("로그인이 필요합니다.");
        }

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), longin.class);
                startActivity(intent);



            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Toast.makeText(getContext(), "로그아웃 하였습니다.", Toast.LENGTH_SHORT).show();
                logtxt.setText("로그인이 필요합니다.");
            }
        });

        userbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), userChange.class);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 101);
        }

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 101);
        }

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 101);

            }
        });


        setText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이미지뷰에서 비트맵 가져오기
                Bitmap bitmap = ((BitmapDrawable) imPhoto.getDrawable()).getBitmap();

                // 이미지 압축을 위한 옵션 설정
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = calculateInSampleSize(bitmap.getWidth(), bitmap.getHeight(), 1024, 1024);  // 원하는 크기에 맞게 inSampleSize 계산

                // 이미지를 원하는 크기로 조정
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / options.inSampleSize, bitmap.getHeight() / options.inSampleSize, false);

                // 비트맵을 JPEG 형식으로 압축하여 byte 배열로 변환
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);  // 압축률을 조정하여 용량 최적화
                byte[] byteArray = stream.toByteArray();

                // 서버로 사진 전송
                sendPhotoToServer(byteArray);
            }
        });
        return v;
    }
    //닉네임 불러오기


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imPhoto.setImageBitmap(bitmap);
        }


    }

    private void sendPhotoToServer(byte[] byteArray) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 서버의 IP 주소와 포트 번호
                String serverIP = "172.20.10.5";
                int serverPort = 1234;

                try {
                    // 소켓 생성 및 서버에 연결
                    Socket socket = new Socket(serverIP, serverPort);

                    // 데이터를 서버로 전송하기 위한 출력 스트림 생성
                    OutputStream outputStream = socket.getOutputStream();

                    // 이미지 데이터의 크기를 전송
                    int dataSize = byteArray.length;
                    outputStream.write(String.valueOf(dataSize).getBytes());
                    outputStream.flush();

                    // 이미지 데이터를 출력 스트림에 쓰기
                    outputStream.write(byteArray);
                    outputStream.flush();

                    // 서버로부터 응답 데이터 받기
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    StringBuilder response = new StringBuilder();
                    long startTime = System.currentTimeMillis();

                    while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {
                        response.append(new String(buffer, 0, bytesRead));
                        // 3초 이상 경과하면 전송 완료로 간주
                        if (System.currentTimeMillis() - startTime > 3000) {
                            break;
                        }
                    }

                    // "True" 값을 받았을 때 신고 다이얼로그 표시 및 문자 보내기
                    if (response.toString().equals("True")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("119에 신고하시겠습니까?")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // SMS 전송 권한 체크
                                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                                                        != PackageManager.PERMISSION_GRANTED) {
                                                    // 권한이 없는 경우 권한 요청
                                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},
                                                            SMS_PERMISSION_REQUEST_CODE);
                                                } else {
                                                    // 권한이 있는 경우 SMS 전송
                                                    sendSMS();
                                                }
                                            }
                                        })
                                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // 취소 버튼을 눌렀을 때 처리할 내용
                                                dialog.dismiss();
                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "전송이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    // 소켓 및 출력 스트림 닫기
                    outputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void sendSMS() {
        // SMS 전송
        String message = "뇌졸중이 의심됩니다. 도와주세요."; // 전송할 메시지
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(PHONE_NUMBER, null, message, null, null);
        Toast.makeText(getActivity(), "문자가 전송되었습니다.", Toast.LENGTH_SHORT).show();
    }
    // onRequestPermissionsResult 메서드 추가
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // SMS 전송 권한이 허용된 경우 SMS 전송
                sendSMS();
            } else {
                Toast.makeText(getActivity(), "SMS 전송 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    //닉네임 로드
    private void loadNickname(String userId) {
        DatabaseReference nicknameRef = databaseReference.child("users").child(userId).child("nickname");
        nicknameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nickname = snapshot.getValue(String.class);
                    try {
                        String decodedNickname = URLDecoder.decode(nickname, "UTF-8");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                logtxt.setText(decodedNickname + "님 안녕하세요!");
                            }
                        });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "Failed to read nickname value.", error.toException());
            }
        });
    }



}