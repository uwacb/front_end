package com.example.test.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    Button button_login = null;
    Button button_register = null;
    TextView text_name = null;
    TextView text_pass = null;
    TextView result_show = null;
    ExecutorService mThreadPool;
    Socket socket;
    Double read = 0.0;
    static Handler mMainHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        button_login = (Button)findViewById(R.id.login);
        button_register = (Button)findViewById(R.id.register);
        text_name = (TextView)findViewById(R.id.et_name);
        text_pass = (TextView)findViewById(R.id.et_pass);
        mThreadPool = Executors.newCachedThreadPool();
        result_show = (TextView)findViewById(R.id.login_result);


        mMainHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (read == 1.0)
                        {
                            Intent test = new Intent(MainActivity.this,success.class);
                            startActivity(test);
                            MainActivity.this.finish();
                        }
                        else
                        {
                            result_show.setText("wrong password or name doesn't exist");
                        }
                        break;
                }
            }
        };

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent login_to_register = new Intent(MainActivity.this,register.class);
                startActivity(login_to_register);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = text_name.getText().toString().trim();
                final String pass = text_pass.getText().toString().trim();

                if((pass.length()==0) || (name.length()==0) )
                {
                    Toast.makeText(MainActivity.this,"please input name and password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                socket = new Socket("106.14.213.85",9999);
                                OutputStream outputStream = socket.getOutputStream();
                                DataOutputStream writer = new DataOutputStream(outputStream);
                                String temp = 'l' + name + '/' + pass;
                                writer.writeUTF(temp);

                                InputStream is = socket.getInputStream();
                                DataInputStream dis = new DataInputStream(is);
                                read = dis.readDouble();

                                Message msg = Message.obtain();
                                msg.what = 0;
                                mMainHandler.sendMessage(msg);
                                dis.close();
                                is.close();
                                writer.close();
                                outputStream.close();
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }
}
