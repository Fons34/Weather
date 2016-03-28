package nl.loemba.fons.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class WeatherActivity extends AppCompatActivity implements GetDataResponse {

    private static final String TAG = "WeatherActivity";

    public void getData(View view) {
        Button buttonGetData = (Button) findViewById(R.id.buttonGetData);
        Button buttonRst = (Button) findViewById(R.id.buttonRst);
        buttonGetData.setEnabled(false);
        buttonRst.setEnabled(false);
        Log.v(TAG, "getData");
        GetDataTask gdt = new GetDataTask();
        gdt.setPort(22);
        gdt.setIPAddress("192.168.178.24");
        gdt.delegate = this;
        gdt.execute(CmdTyp.CmdGet);
    }

    public void reset(View view) {
        Button buttonGetData = (Button) findViewById(R.id.buttonGetData);
        Button buttonRst = (Button) findViewById(R.id.buttonRst);
        buttonGetData.setEnabled(false);
        buttonRst.setEnabled(false);
        Log.v(TAG, "getData");
        GetDataTask gdt = new GetDataTask();
        gdt.setPort(22);
        gdt.setIPAddress("192.168.178.24");
        gdt.delegate = this;
        gdt.execute(CmdTyp.CmdRst);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
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
    public void handleResponse(String[] results){
        Button buttonGetData = (Button) findViewById(R.id.buttonGetData);
        Button buttonRst = (Button) findViewById(R.id.buttonRst);
        buttonGetData.setEnabled(true);
        buttonRst.setEnabled(true);
        Log.v(TAG, "handleResponse");
        TextView tvTemperatureMin = (TextView) findViewById(R.id.textViewTemperatureMin);
        TextView tvTemperatureCur = (TextView) findViewById(R.id.textViewTemperatureCur);
        TextView tvTemperatureMax = (TextView) findViewById(R.id.textViewTemperatureMax);
        TextView tvPressureMin = (TextView) findViewById(R.id.textViewPressureMin);
        TextView tvPressureCur = (TextView) findViewById(R.id.textViewPressureCur);
        TextView tvPressureMax = (TextView) findViewById(R.id.textViewPressureMax);

        if (results[0].length() == (3*5)) {
            String dispStr;

            dispStr = results[0].substring(0, 4) + "." + results[0].substring(4, 5) + " C";
            tvTemperatureMin.setText(dispStr);
            dispStr = results[0].substring(5, 9) + "." + results[0].substring(9, 10) + " C";
            tvTemperatureCur.setText(dispStr);
            dispStr = results[0].substring(10, 14) + "." + results[0].substring(14, 15) + " C";
            tvTemperatureMax.setText(dispStr);
        }
        else
            tvTemperatureCur.setText("--.-- C");
        if (results[1].length() == (3*6)) {
            String dispStr;

             dispStr = results[1].substring(0,4) + "." + results[1].substring(4, 6) + " hPa";
             tvPressureMin.setText(dispStr);
             dispStr = results[1].substring(6,10) + "." + results[1].substring(10, 12) + " hPa";
             tvPressureCur.setText(dispStr);
             dispStr = results[1].substring(12,16) + "." + results[1].substring(16, 18) + " hPa";
             tvPressureMax.setText(dispStr);
        }
        else
             tvPressureCur.setText("----.-- hPa");

    }
}


