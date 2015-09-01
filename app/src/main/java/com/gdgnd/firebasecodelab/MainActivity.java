package com.gdgnd.firebasecodelab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Firebase mFirebaseRef;
    private Firebase mMessageRef;
    private FirebaseListAdapter<Message> mListAdapter;
    EditText mEditText;
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up Firebase context
        Firebase.setAndroidContext(this);

        //connection to root of our database
        mFirebaseRef = new Firebase("https://gdg-firebase-codelab.firebaseio.com/");
        mMessageRef = mFirebaseRef.child("messages");// connection to message object

        mEditText = (EditText)this.findViewById(R.id.message_text);
        mListView = (ListView)this.findViewById(R.id.listView);

        mListAdapter = new FirebaseListAdapter<Message>(mMessageRef,Message.class,R.layout.message_row,this) {
            @Override
            protected void populateView(View v, Message model) {
                ((TextView)v.findViewById(R.id.username_text_view)).setText(model.getName());
                ((TextView)v.findViewById(R.id.message_text_view)).setText(model.getMessage());
            }
        };
        mListView.setAdapter(mListAdapter);
    }

    public void onSendButtonClick(View view){
        String message = mEditText.getEditableText().toString();//message to be sent
        mMessageRef.push().setValue(new Message("foo",message));
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
