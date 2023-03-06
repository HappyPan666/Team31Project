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

//    HTTPResponse处理url转换成string
//    我们首先使用 openConnection 方法打开一个 HttpURLConnection 连接。
//    然后，我们设置请求方法为 GET，使连接可以从服务器获取数据，然后链接。
//    通过 getInputStream 方法获取服务器返回的输入流，
//    并将其转换为字符串。
    private static String httpResponse(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);//表示这个连接是否能从服务器获取数据。默认情况下，HttpURLConnection 的 setDoInput 属性为 true，表示这个连接可以从服务器获取数据。如果将这个属性设置为 false，那么这个连接就不能从服务器获取数据。
        conn.setConnectTimeout(5000);
        conn.connect();//连上服务器

//        从服务器获取输入流
        InputStream inputStream = conn.getInputStream();
        String resp = convertStreamToString(inputStream);

        inputStream.close();
        conn.disconnect();
        return resp;
    }


    /*
    convertStreamToString 是一个将输入流转换为字符串的常用静态方法，
    它的作用是将从网络或文件读取的数据流转换成字符串，方便后续的处理和解析。
     */
    private static String convertStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();//stringBuilder有更多的方法可以调用
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line; //把数据读成一行一行的，因为bufferedReader只能逐行读取
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);  //整理api数据的格式
            }
            bufferedReader.close();
            return stringBuilder.toString().replace(",", ",\n");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    //    线程2：用于查询天气
    class findWeatherThread extends Thread{
        String city;
        //构造函数
        findWeatherThread(String city) {
            if (city.isEmpty()) {
                this.city = "San Jose";
            } else {
                this.city = city;
            }
        }
        @Override
        public void run() {
            super.run();

//            在textView，读取出string类型，（调用URL的类）转换成URL
//            JSONObject jObject = new JSONObject();

            try{
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=b1ee2452b1fb42482d20a39002fabd54&units=metric");
                String resp = httpResponse(url);

                //把string转换成json文件
                JSONObject jObject = new JSONObject(resp);
                JSONObject main = jObject.getJSONObject("main");

                String vTemp = main.getString("temp");
                String vHumidity = main.getString("humidity");
                String vPressure = main.getString("pressure");
                String vBodyTemperature = main.getString("feels_like");

                runOnUiThread(() -> {
                    temperature.setText(vTemp);
                    humidity.setText(vHumidity);
                    pressure.setText(vPressure);
                    bodyTemperature.setText(vBodyTemperature);
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}