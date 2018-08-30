package com.example.test.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class register extends Activity {

    Button button_signin = null;
    Button button_goback = null;
    TextView text_name = null;
    TextView text_pass = null;
    TextView text_repass = null;
    Socket socket;
    ExecutorService mThreadPool;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mThreadPool = Executors.newCachedThreadPool();
        button_signin = (Button)findViewById(R.id.signin);
        button_goback = (Button)findViewById(R.id.goback);
        text_name = (TextView)findViewById(R.id.et_name);
        text_pass = (TextView)findViewById(R.id.et_pass);
        text_repass = (TextView)findViewById(R.id.et_repass);

        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = text_name.getText().toString().trim();
                final String pass = text_pass.getText().toString().trim();
                String repass = text_repass.getText().toString().trim();

                if((pass.length()==0) || (name.length()==0) || (repass.length()==0))
                {
                    Toast.makeText(register.this,"please input name and password",Toast.LENGTH_SHORT).show();
                }
                else if (!pass.equals(repass))
                {
                    Toast.makeText(register.this,"please input the same password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(register.this,"sign in successful",Toast.LENGTH_SHORT).show();
                    mThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                socket = new Socket("106.14.213.85",9999);
                                OutputStream outputStream = socket.getOutputStream();
                                DataOutputStream writer = new DataOutputStream(outputStream);
                                String temp = 'r' + name + '/' + pass;
                                writer.writeUTF(temp);
                                writer.close();
                                outputStream.close();
                                socket.close();
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });


                }
            }
        });

        button_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
