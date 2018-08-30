package com.example.test.myapplication;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class success extends Activity {

    Location global_location = null;
    Button button_in = null;
    Button button_out = null;
    Button button_show = null;
    OutputStream outputStream = null;

    private Socket socket;
    private ExecutorService mThreadPool;
    private TextView postionView;
    private LocationManager locationManager;
    private String locationProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);

        button_in = (Button) findViewById(R.id.button_in);
        button_out = (Button) findViewById(R.id.button_out);
        button_show = (Button) findViewById(R.id.button_show);

        mThreadPool = Executors.newCachedThreadPool();

        postionView = (TextView) findViewById(R.id.positionView);
        //获取地理位置管理器  

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle arg2) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
            @Override
            public void onLocationChanged(Location location) {
                //如果位置发生变化,重新显示  
                location.getAccuracy();
                global_location = location;
                showLocation(location);
            }
        };

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器  

        List<String> providers = locationManager.getProviders(true);

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS  
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network  
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "Please open the location service", Toast.LENGTH_SHORT).show();
        }

        final Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度  
            global_location = location;
            showLocation(location);
        }
        //监视地理位置变化  
        locationManager.requestLocationUpdates(locationProvider, 2000, 0, locationListener);


        button_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            socket = new Socket("106.14.213.85", 9999);
                            outputStream = socket.getOutputStream();
                            DataOutputStream writer = new DataOutputStream(outputStream);
                            DecimalFormat df = new DecimalFormat("######0.0000");
                            double a = global_location.getLatitude();
                            double b = global_location.getLongitude();
                            String c = df.format(a);
                            String d = df.format(b);
                            String temp = 'i' + c + '/' + d;
                            writer.writeUTF(temp);
                            writer.close();
                            outputStream.close();
                            socket.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });


        button_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            socket = new Socket("106.14.213.85", 9999);
                            outputStream = socket.getOutputStream();
                            DataOutputStream writer = new DataOutputStream(outputStream);
                            DecimalFormat df = new DecimalFormat("######0.0000");
                            double a = global_location.getLatitude();
                            double b = global_location.getLongitude();
                            String c = df.format(a);
                            String d = df.format(b);
                            String temp = 'o' + c + '/' + d;
                            writer.writeUTF(temp);
                            writer.close();
                            outputStream.close();
                            socket.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void showLocation(Location location)
    {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        month = month + 1;
        String locationStr = year + "." + month + "." + date + "." + hour + ":" + minute + ":" + second + "\n" + "latitude：" + location.getLatitude() + "\n" + "longitude：" + location.getLongitude();
        postionView.setText(locationStr);
    }
}
