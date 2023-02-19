package edu.northeastern.team31project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class weather extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "WebServiceActivity";
    private EditText cityName;
    private TextView temperature, humidity, pressure, bodyTemperature;     // 温度 湿度 气压 体感

    @Override
    protected void onCreate(Bundle savedInstanceState) { //把所有的东西都创建出来
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_weather);

        cityName = (EditText)findViewById(R.id.cityName);
        temperature = (TextView)findViewById(R.id.temp);
        humidity = (TextView)findViewById(R.id.humid);
        pressure = (TextView)findViewById(R.id.pressure);
        bodyTemperature = (TextView)findViewById(R.id.bodyTemperature);

        Button getWeather = (Button) findViewById(R.id.getWeather);
        getWeather.setOnClickListener(weather.this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.getWeather){
            beginFindWeather();
        }
    }

    private void beginFindWeather() {   //点击图标，开始查找天气，启动两个线程
        String city = cityName.getText().toString();

//        use two threads: first is use dialog, second is use to find the weather
        inProcessThread threadInProcess = new inProcessThread(city);
        findWeatherThread threadFindWeather = new findWeatherThread(city);

        threadFindWeather.run(); //start()必须在新的线程中调用，否则闪退。run（）不会
        threadInProcess.run();
    }

    //    线程1：用于加载等待框
    class inProcessThread extends Thread{
        String city;

        //构造函数
        inProcessThread(String city) {
            if (city.isEmpty()) {
                this.city = "San Jose";
            } else {
                this.city = city;
            }
        }

        @Override
        public void run() {
            super.run();
            ProgressDialog loading = new ProgressDialog(weather.this);
            loading.setTitle("Connecting");
            loading.setMessage("Getting you the weather of " + city +" ...");
            loading.show();

//        旨在在单独的线程上执行以执行某些后台任务:这一段代码是让loading模块显示1秒钟
            Runnable loadingRunnable = loading::cancel;
            Handler canceller = new Handler();
            canceller.postDelayed(loadingRunnable, 1000);
        }
    }

}