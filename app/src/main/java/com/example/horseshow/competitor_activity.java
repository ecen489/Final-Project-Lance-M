package com.example.horseshow;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.horseshow.LoginScreen.EXTRA_MESSAGE;
//import static com.example.horseshow.MainActivity.EXTRA_MESSAGE;

public class competitor_activity extends AppCompatActivity {

    private static final String FILE_NAME = "people_competiting.txt";
    String[] riders_plus_horse_list;
    private ListView lvItems;
    private List<Product> lstProducts;
    int intCount = 0;

    //counting length of rows in text file
    InputStream inputStreamCounter;
    BufferedReader bufferedReaderCounter;

    //loading the vlaues in to text file string array
    InputStream inputStreamLoader;
    BufferedReader bufferedReaderLoader;

    // simplefile name--- Do not allow the path.
    private String simpleFileName = "people_competiting.txt";



    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitor_activity);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE); // alarm service
        Calendar calendar = Calendar.getInstance();

        Intent alarmIntent = new Intent("com.example.horseshow.action.DISPLAY_NOTIFICATION");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 100, new Intent(alarmIntent), PendingIntent.FLAG_UPDATE_CURRENT);
        calendar.add(Calendar.SECOND, 50);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent );

        //get Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        //capture layout TextVeiw and set the string as its text
        TextView compititon_name = findViewById(R.id.compeitionName3);
        compititon_name.setText(message);


        /* Code above is to connect to previous screen, code below is for the list view */

        //listview object linked to xml
        lvItems = (ListView) findViewById(R.id.lvItems);
        lstProducts = new ArrayList<>();

        lvItems.setAdapter(new CountdownAdapter(competitor_activity.this, lstProducts));


        String filepath = getExternalFilesDir(FILE_NAME).getAbsolutePath();
        File file = new File(filepath, "people_competiting.txt");
        //connect Inputstream and buffer into text file and each other, counters
        inputStreamCounter = new ByteArrayInputStream(Charset.forName("UTF-16").encode(message).array());//getResources().openRawResource(R.raw.people_competiting);
        bufferedReaderCounter = new BufferedReader(new InputStreamReader(inputStreamCounter));

        //loaders
        inputStreamLoader = new ByteArrayInputStream(Charset.forName("UTF-16").encode(message).array());//this.getResources().openRawResource(R.raw.people_competiting);
        bufferedReaderLoader = new BufferedReader(new InputStreamReader(inputStreamLoader));

        //count number of rows/lines in text file
        try {
            while (bufferedReaderCounter.readLine() != null) {
                intCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        riders_plus_horse_list = new String[intCount];


        //load rows or lines into string array

        try {



                int j = 55000; // about 4.5 minutes, put a 2 in front for 4.5

                for (int i = 0; i < intCount; i++) {
                    riders_plus_horse_list[i] = bufferedReaderLoader.readLine();
                    lstProducts.add(new Product(riders_plus_horse_list[i], System.currentTimeMillis() + j));
                    j = j + 255000; //add another 4.5 min

                }

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
                e.printStackTrace();
        }



        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), riders_plus_horse_list[position] + "was clicked", Toast.LENGTH_SHORT).show();
            }

        });
    }



    private class Product {
    String name;
    long expirationTime;

    public Product(String name, long expirationTime) {
        this.name = name;
        this.expirationTime = expirationTime;
    }
}


public class CountdownAdapter extends ArrayAdapter<Product> {

    private LayoutInflater lf;
    private List<ViewHolder> lstHolders;
    private Handler mHandler = new Handler();
    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lstHolders) {
                long currentTime = System.currentTimeMillis();
                for (ViewHolder holder : lstHolders) {
                    holder.updateTimeRemaining(currentTime);
                }
            }
        }
    };

    public CountdownAdapter(Context context, List<Product> objects) {
        super(context, 0, objects);
        lf = LayoutInflater.from(context);
        lstHolders = new ArrayList<>();
        startUpdateTimer();
    }

    private void startUpdateTimer() {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(updateRemainingTimeRunnable);
            }
        }, 1000, 1000);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = lf.inflate(R.layout.list_item, parent, false);
            holder.tvProduct = (TextView) convertView.findViewById(R.id.tvProduct);
            holder.tvTimeRemaining = (TextView) convertView.findViewById(R.id.tvTimeRemaining);
            convertView.setTag(holder);
            synchronized (lstHolders) {
                lstHolders.add(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setData(getItem(position));

        return convertView;
    }
}

    private class ViewHolder {
        TextView tvProduct;
        TextView tvTimeRemaining;
        Product mProduct;

        public void setData(Product item) {
            mProduct = item;
            tvProduct.setText(item.name);
            updateTimeRemaining(System.currentTimeMillis());
        }

        public void updateTimeRemaining(long currentTime) {
            long timeDiff = mProduct.expirationTime - currentTime;
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                //int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                tvTimeRemaining.setText("You're up in " + minutes + " mins " + seconds + " sec");
            } else {
                tvTimeRemaining.setText("Your UP!!");
            }
        }
    }
}
