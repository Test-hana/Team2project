package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class JoinActivity extends AppCompatActivity {

    private EditText tv_email, tv_password, tv_passwordCheck, tv_nickname;
    private Button btn_join, btn_login;
    private FirebaseAuth mAuth; //FirebaseAuth 인스턴스 선언
    private static final String TAG = "JoinActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mAuth = FirebaseAuth.getInstance(); //FirebaseAuth 인스턴스 초기화


        tv_email = (EditText)findViewById(R.id.tv_email);
        tv_password = (EditText)findViewById(R.id.tv_password);
        tv_passwordCheck = (EditText)findViewById(R.id.tv_passwordCheck);
        //tv_nickname = (EditText)findViewById(R.id.tv_nickname); //아직 미사용, DB에 id 업로드할때, email에서 id 추출이 안되면 사용하기로 보류.
        btn_join = (Button)findViewById(R.id.btn_join);
        btn_login = (Button)findViewById(R.id.btn_login);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Join();
            }

        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinActivity.this,GoogleLoginActivity.class);
                startActivity(intent);
            }
        });




    }//onCreate 마지막

    @Override
    public void onBackPressed() { //뒤로가기 버튼 클릭시, 앱 종료
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void Join(){ //회원가입 함수
        String email = tv_email.getText().toString();
        String password = tv_password.getText().toString();
        String passwordCheck = tv_passwordCheck.getText().toString();

        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0){
            if(password.equals(passwordCheck)){ //비밀번호와 비밀번호 확인이 동일하다면 신규 회원등록 진행
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //성공했을때
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("회원가입에 성공하였습니다.");

                                } else {
                                    //실패했을때
                                    if(task.getException()!=null){
                                        startToast(task.getException().toString()+"\n회원가입에 실패하였습니다. (비밀번호는 6자리 이상을 입력주세요.)");
                                    }
                                }
                            }
                        });
            }else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        }else {
            startToast("이메일 또는 비밀번호(6자리 이상)을 입력해주세요.");
        }

    }

    private void startToast(String msg){ //리스너에서 Toast msg가 불가해서
       Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}