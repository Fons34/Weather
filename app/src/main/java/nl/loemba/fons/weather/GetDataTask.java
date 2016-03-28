package nl.loemba.fons.weather;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class GetDataTask extends AsyncTask<CmdTyp,Integer,String[]> {
    private static final String TAG = "GetDataTask";
    private static final int readTimeOut = 3000; // ms

    int port;
    String address;

    public GetDataResponse delegate = null;

    public void setPort(int port) {
        this.port = port;
    }

    public void setIPAddress(String address) { this.address = address; }

    protected String[] doInBackground(CmdTyp... cmds) {
        String[] results = new String[2];

        try {
            Log.v(TAG, "doInBackground");
            Socket socket = new Socket(this.address, this.port);
            socket.setSoTimeout(readTimeOut);
            try {
                PrintWriter mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                if (cmds[0] == CmdTyp.CmdGet) {
                    mBufferOut.println("te1\r"); // message needs CR LF
                    mBufferOut.flush();
                    BufferedReader mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Log.v(TAG, "wait for response");


                    results[0] = mBufferIn.readLine();
                    mBufferOut.println("prs\r"); // message needs CR LF
                    mBufferOut.flush();
                    results[1] = mBufferIn.readLine();
                    mBufferIn.close(); // added later, not sure is required
                }
                else {
                    mBufferOut.println("rst\r"); // message needs CR LF
                    mBufferOut.flush();
                    BufferedReader mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Log.v(TAG, "wait for response");
                    results[0] = "";
                    results[1] = "";
                    mBufferIn.close(); // added later, not sure is required
                }
                mBufferOut.close();
                socket.close();
            } catch (IOException e) {
                Log.v(TAG, "Exception 1");
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.v(TAG, "Exception 2");
            e.printStackTrace();
        }
        Log.v(TAG, "return");
        return results;
    }

    @Override
    protected void onPostExecute(String[] results) {
        delegate.handleResponse(results);
    }

}
