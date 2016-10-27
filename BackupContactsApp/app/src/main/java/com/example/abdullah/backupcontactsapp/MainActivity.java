package com.example.abdullah.backupcontactsapp;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap<String, String> HM = new HashMap<String, String>();
    SQLiteDatabase contactsDB = null;

    Button backupButton, searchButton, refreshButton;
    Button addButton, ignoreButton, callButton;

    EditText nameEditText, phoneEditText;
    TextView outTV, out2TV, phoneTV, nameTV;
    String globalPhone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backupButton = (Button) findViewById(R.id.backupButton);
        refreshButton = (Button) findViewById(R.id.refreshButton);
        searchButton = (Button) findViewById(R.id.searchButton);
        addButton = (Button) findViewById(R.id.buttonAdd);
        ignoreButton = (Button) findViewById(R.id.buttonIgnore);
        callButton = (Button) findViewById(R.id.buttonCall);

        phoneEditText = (EditText) findViewById(R.id.phoneEditId);
        nameEditText = (EditText) findViewById(R.id.nameEditId);

        outTV = (TextView) findViewById(R.id.outId);
        out2TV = (TextView) findViewById(R.id.out2Id);
        phoneTV = (TextView) findViewById(R.id.phoneId);
        nameTV = (TextView) findViewById(R.id.nameId);

        //At first disable all the buttons
        addButton.setVisibility(View.INVISIBLE);
        ignoreButton.setVisibility(View.INVISIBLE);
        callButton.setVisibility(View.INVISIBLE);
        searchButton.setVisibility(View.INVISIBLE);
        refreshButton.setVisibility(View.INVISIBLE);
        phoneEditText.setVisibility(View.INVISIBLE);
        nameEditText.setVisibility(View.INVISIBLE);
        phoneTV.setVisibility(View.INVISIBLE);
        nameTV.setVisibility(View.INVISIBLE);
        outTV.setVisibility(View.INVISIBLE);
        out2TV.setVisibility(View.INVISIBLE);

    }

    //Add the phone contacts into a global HashMap
    public void getContacts() {

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            HM.put(name, phoneNumber);
            //Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();
        }
        phones.close();

    }

    //Load the contacts in HashMap to the database
    public void loadDB(View view) {

        //From HashMap load the contacts into the database
        for(String key: HM.keySet()) {
            String contactName = key;
            String phone = HM.get(key);
            contactsDB.execSQL("INSERT INTO contacts (name, phone) VALUES ('"+ contactName + "', '" + phone + "');");
        }

    }

    //Create the database table and load the contacts into the database
    public void createDatabase(View view) {

        try {

            contactsDB = this.openOrCreateDatabase("MyContacts", MODE_PRIVATE, null);

            contactsDB.execSQL("CREATE TABLE IF NOT EXISTS contacts " +
            "(id integer primary key, name VARCHAR, phone integer);");

            File database = getApplicationContext().getDatabasePath("MyContacts.db");
            if(!database.exists()) {
                Toast.makeText(this, "Database created Successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Could not create Database!", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("ERROR", "Error Creating Database");
        }

        //Copy phone contacts to the HashMap
        getContacts();
        //From HashMap load the contacts into the database
        loadDB(view);
        Toast.makeText(this, "Contacts Backed Up Successfully!", Toast.LENGTH_LONG).show();

        //After Database creation for the first time enable other bittons
        searchButton.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
        phoneEditText.setVisibility(View.VISIBLE);
        phoneTV.setVisibility(View.VISIBLE);;
        //Make backupButton not clickable
        backupButton.setClickable(false);

    }

    //Refresh the database to see any new contact in the phone contacts
    public void refreshDB(View view) {

        //Call getContacts() again
        getContacts();
        loadDB(view);
        Toast.makeText(this, "Synced up  Database with the contacts!", Toast.LENGTH_SHORT).show();

    }

    //Add a contact when clicked on Add
    public void addContact(View view) {

        String contactName = nameEditText.getText().toString();
        //String phone = phoneEditText.getText().toString();
        String phone = globalPhone;

        contactsDB.execSQL("INSERT INTO contacts (name, phone) VALUES ('"+ contactName + "', '" + phone + "');");
        Toast.makeText(this, "Added contact to the database!", Toast.LENGTH_SHORT).show();

        //Make Add and other buttons invisible
        addButton.setVisibility(View.INVISIBLE);
        ignoreButton.setVisibility(View.INVISIBLE);
        phoneEditText.setText("");
        nameEditText.setText("");
        nameEditText.setVisibility(View.INVISIBLE);
        nameTV.setVisibility(View.INVISIBLE);
        outTV.setVisibility(View.INVISIBLE);

    }

    //Just clear the buttons when clicked on Ignore
    public void ignore(View view) {

        //Do nothing or just clear everything or disable the buttons
        //Toast.makeText(this, "ignore called!", Toast.LENGTH_SHORT).show();
        //Make Add and other buttons invisible
        addButton.setVisibility(View.INVISIBLE);
        ignoreButton.setVisibility(View.INVISIBLE);
        phoneEditText.setText("");
        nameEditText.setText("");
        nameEditText.setVisibility(View.INVISIBLE);
        nameTV.setVisibility(View.INVISIBLE);
        outTV.setVisibility(View.INVISIBLE);

    }

    //Make call to the searched phone number
    public void makeCall(View view) {

        //Make call to the number
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+globalPhone));
        startActivity(callIntent);

        //Toast.makeText(this, "makeCall called!", Toast.LENGTH_SHORT).show();
        //Make Call and Msg buttons invisible
        callButton.setVisibility(View.INVISIBLE);
        phoneEditText.setText("");
        out2TV.setVisibility(View.INVISIBLE);

    }

    //Search the database by phone number
    public void searchContacts(View view) {

        Cursor cursor = contactsDB.rawQuery("SELECT * FROM contacts", null);
        int idColumn = cursor.getColumnIndex("id");
        int nameColumn = cursor.getColumnIndex("name");
        int phoneColumn = cursor.getColumnIndex("phone");

        cursor.moveToFirst();
        String contactList = "";

        String searchPhone = phoneEditText.getText().toString();
        globalPhone = searchPhone;
        StringBuilder phone1 = removeSpecialChars(searchPhone);
        String nameFound = "Not Found!";
        if (cursor != null && (cursor.getCount() > 0)) {

            do {
                String id = cursor.getString(idColumn);
                String name = cursor.getString(nameColumn);
                String phone = cursor.getString(phoneColumn);
                StringBuilder phone2 = removeSpecialChars(phone);
                //Log.i("DBG1 :", phone1.toString());
                //Log.i("DBG2 :", phone2.toString());

                if ((phone1.toString()).equals(phone2.toString())){
                    nameFound = name;
                    break;
                }
            } while (cursor.moveToNext());

        } else {
            Toast.makeText(this, "Table is Empty!", Toast.LENGTH_SHORT).show();
            phoneEditText.setText("");
        }

        //Log.i("DBG2 :", nameFound);
        if (nameFound.equals("Not Found!")) {
            outTV.setVisibility(View.VISIBLE);
            outTV.setText(nameFound);
            //Enable Add and Ignore buttons
            nameTV.setVisibility(View.VISIBLE);
            nameEditText.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.VISIBLE);
            ignoreButton.setVisibility(View.VISIBLE);
        } else {
            out2TV.setVisibility(View.VISIBLE);
            out2TV.setText(nameFound);
            //Enable Call and Msg buttons
            callButton.setVisibility(View.VISIBLE);
        }

    }

    //Remove special characters before comparing phone numbers
    public StringBuilder removeSpecialChars(String s) {

        StringBuilder str = new StringBuilder();
        for (int i=0; i<s.length(); i++) {
            if ((s.charAt(i) == '(') || (s.charAt(i) == ')') || (s.charAt(i) == '-') || (s.charAt(i) == ' ')) {
                continue;
            } else {
                str.append(s.charAt(i));
            }

        }
        return(str);
    }

    //Close the database
    @Override
    protected void onDestroy() {

        contactsDB.close();
        super.onDestroy();

    }

}
