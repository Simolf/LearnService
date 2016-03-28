package com.example.cxx.learnservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {
    private EditText etData;
    private MyService.Binder binder;
    private TextView tvOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        etData=(EditText)findViewById(R.id.etDate);
        tvOut= (TextView) findViewById(R.id.tvOut);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn1:
                Intent i=new Intent(this,MyService.class);
                i.putExtra("data",etData.getText().toString());
                startService(i);
                break;
            case R.id.btn2:
                stopService(new Intent(this,MyService.class));
                break;
            case R.id.btn3:
                bindService(new Intent(this,MyService.class),this, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn4:
                unbindService(this);
                break;
            case R.id.btn5:
                if(binder!=null){
                    binder.setData(etData.getText().toString());
                }
                break;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder= (MyService.Binder) service;
        binder.getService().setCallback(new MyService.CallBack() {
            @Override
            public void onDataChange(String data) {
                Message msg=new Message();
                Bundle b=new Bundle();
                b.putString("data",data);
                msg.setData(b);
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
   private Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           tvOut.setText(msg.getData().getString("data"));
       }
   };

}
