package com.io.utkarsh.contact_app.HelperClass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.io.utkarsh.contact_app.Activity.ReadAllContactActivity;
import com.io.utkarsh.contact_app.Model.ContactModel;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

/**
 * Created by Utkarsh-PC on 8/13/2018.
 */

public class MaiActivityHelper {
    ArrayList<ContactModel> contactList;
    public static ArrayList<ContactModel> result;
    Cursor cursor;
    int counter;
    String name = "";
    String phoneNumber = "";
    JsonObject jsonObject;


    public void getContacts(Activity activity, final ProgressDialog pDialog, Handler updateBarHandler) {
        contactList = new ArrayList<>();

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        StringBuffer output;

        ContentResolver contentResolver = activity.getContentResolver();

        cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Iterate every contact in the phone
        if (cursor.getCount() > 0) {

            counter = 0;
            while (cursor.moveToNext()) {
                output = new StringBuffer();
                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                updateBarHandler.post(new Runnable() {
                    public void run() {
                        pDialog.setMessage("Updating contacts to Server : " + counter++ + "/" + cursor.getCount());
                    }
                });
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    output.append("\n First Name:" + name);

                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);
                    }

                    phoneCursor.close();
                }

                // Add the contact to the ArrayList
                contactList.add(new ContactModel(name, phoneNumber));
                result =contactList;
                for (int i = 0; i < contactList.size(); i++) {
                     jsonObject = new JsonObject();
                    contactList.get(i).setUserName(name);
                    contactList.get(i).setContactNumber(phoneNumber);
                    jsonObject.addProperty("name", contactList.get(i).getUserName());
                    jsonObject.addProperty("contact", contactList.get(i).getContactNumber());

                }
                try {
                    UpdateContactListToServer(jsonObject,activity,pDialog);
                }catch (Exception e){
                }
              ;
            }

        }
    }

    private void UpdateContactListToServer(JsonObject jsonObject, final Activity activity, final ProgressDialog pDialog) {
        Ion.with(activity)
                .load("http://192.168.43.154:8080/api/contacts/post")
                .setJsonObjectBody(jsonObject)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if(e!=null){
                            Toast.makeText(activity,"Contact Not Updated",Toast.LENGTH_SHORT).show();
                            pDialog.cancel();
                        }else {
                            Toast.makeText(activity,"Contact Updated on Server",Toast.LENGTH_SHORT).show();
                            pDialog.cancel();
                        }
                    }
                });
    }

    public void readAllContactFromServer(final Activity activity, final ProgressDialog progressDialog){
        Ion.with(activity)
                .load("http://192.168.43.154:8080/api/contacts/get")
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray jsonElements) {
                        if(e!=null){
                            Toast.makeText(activity,"Contact Not Loaded from server",Toast.LENGTH_SHORT).show();
                        }else {
                            progressDialog.cancel();
                            Intent intent = new Intent(activity,ReadAllContactActivity.class);
                            activity.startActivity(intent);
                        }
                    }

                });
    }
}


