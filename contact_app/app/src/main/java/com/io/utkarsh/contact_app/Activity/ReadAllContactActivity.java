package com.io.utkarsh.contact_app.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.io.utkarsh.contact_app.Adapter.ContactsAdapter;
import com.io.utkarsh.contact_app.HelperClass.MaiActivityHelper;
import com.io.utkarsh.contact_app.R;

public class ReadAllContactActivity extends AppCompatActivity {
    private ListView mListView;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_all_contact);
        init();
        startWorking();
    }

    private void startWorking() {
        setListView();
    }

    private void setListView() {
        if(MaiActivityHelper.result == null){
            Toast.makeText(ReadAllContactActivity.this,"You have to first update The Contact",Toast.LENGTH_SHORT).show();
        }else {
            ContactsAdapter adapter = new ContactsAdapter(MaiActivityHelper.result,ReadAllContactActivity.this,pDialog);
            mListView.setAdapter(adapter);
        }
    }

    private void init() {
        MaiActivityHelper maiActivityHelper =new MaiActivityHelper();
        mListView = (ListView) findViewById(R.id.list);


    }
}
