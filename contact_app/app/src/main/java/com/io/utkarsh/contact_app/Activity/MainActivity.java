package com.io.utkarsh.contact_app.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.io.utkarsh.contact_app.HelperClass.MaiActivityHelper;
import com.io.utkarsh.contact_app.R;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {
    Button btn_send_contact_to_server,btn_read_contact_from_server;
    MaiActivityHelper maiActivityHelper;
    Activity activity;
    private ProgressDialog pDialog,progressDialog;
    private static final int REQUEST_READ_CONTACTS = 444;
    private Handler updateBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bindListner();
    }

    private void bindListner() {
        btn_send_contact_to_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mayRequestContacts()) {
                    return;
                }
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("updating Contact To Server...");
                pDialog.setCancelable(false);
                pDialog.show();

                updateBarHandler = new Handler();

                // Since reading contacts takes more time, let's run it on a separate thread.
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                    try{

                        maiActivityHelper.getContacts(activity,pDialog,updateBarHandler);
                    }catch (Exception e){

                        e.printStackTrace();
                    }
                    }
                }).start();
            }
        });
        btn_read_contact_from_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mayRequestContacts()) {
                    return;
                }
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Fetching Contact From Server...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                maiActivityHelper.readAllContactFromServer(activity,progressDialog);
            }
        });
    }

    private void init() {
        btn_send_contact_to_server = findViewById(R.id.btn_send_contact_to_server);
        btn_read_contact_from_server = findViewById(R.id.btn_read_contact_from_server);
        maiActivityHelper = new MaiActivityHelper();
        activity = MainActivity.this;
    }
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try{

                    maiActivityHelper.getContacts(activity,pDialog,updateBarHandler);
                }catch (Exception e){

                    e.printStackTrace();
                }
            }
        }
    }
}
