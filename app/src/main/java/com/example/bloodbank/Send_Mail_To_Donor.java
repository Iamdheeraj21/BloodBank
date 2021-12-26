package com.example.bloodbank;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.tapadoo.alerter.Alerter;
import com.unknowncoder.bloodbank.R;
public class Send_Mail_To_Donor extends AppCompatActivity {
    EditText editText1,editText2,editText3;
    Button send_email_btn;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail_to_donor);
        initViews();
        send_email_btn.setOnClickListener(v->{
            String email_subject=editText2.getText().toString();
            String email_body=editText3.getText().toString();
            Intent intent = new Intent(Intent.ACTION_SEND);

            if(email.equals("") || email_subject.equals("") || email_body.equals(""))
                Alerter.create(Send_Mail_To_Donor.this)
                        .setTitle("Alert")
                        .setText("Fill the all blank..")
                        .setBackgroundColorRes(R.color.black)
                        .setDuration(2000)
                        .setOnClickListener(v1 -> {
                        }).setOnShowListener(() -> {
                }).setOnHideListener(() -> {
                }).show();
            else

                intent.putExtra(Intent.EXTRA_EMAIL,new String[] { email });
                intent.putExtra(Intent.EXTRA_SUBJECT, email_subject);
                intent.putExtra(Intent.EXTRA_TEXT, email_body);
                intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        });
    }

    private void initViews()
    {
        editText1=findViewById(R.id.email);
        editText2=findViewById(R.id.email_subject);
        editText3=findViewById(R.id.email_body);
        send_email_btn=findViewById(R.id.send_email_btn);
        email=getIntent().getStringExtra("email");
        editText1.setText(email);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startActivity(new Intent(Send_Mail_To_Donor.this,MainActivity.class));
    }
}