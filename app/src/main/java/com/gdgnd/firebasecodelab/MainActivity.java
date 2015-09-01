package com.gdgnd.firebasecodelab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Firebase mFirebaseRef;
    EditText mEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up Firebase context
        Firebase.setAndroidContext(this);

        //connection to root of our database
        mFirebaseRef = new Firebase("https://gdg-firebase-codelab.firebaseio.com/");

        mEditText = (EditText)this.findViewById(R.id.message_text);

    }

    public void onSendButtonClick(View view){
        String message = mEditText.getEditableText().toString();//message to be sent
        Firebase messageRef = mFirebaseRef.child("messages");// connection to message object
        Map<String, String> values = new HashMap<>();
        values.put("name","foo");
        values.put("message",message);
        messageRef.push().setValue(values);
        mEditText.setText("");
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
}
