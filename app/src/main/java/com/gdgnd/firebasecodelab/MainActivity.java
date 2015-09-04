package com.gdgnd.firebasecodelab;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Firebase mFirebaseRef;
    private Firebase mMessageRef;
    private String mUsername;
    private FirebaseListAdapter<Message> mListAdapter;
    EditText mEditText;
    ListView mListView;
    ProgressDialog mProgressDialog;
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
        mMessageRef.push().setValue(new Message(mUsername,message));
        mEditText.setText("");
    }

    public void onLoginButtonClicked(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enter your email address and password")
                .setTitle("Log in");

        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.signin_dialog, null));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mProgressDialog = new ProgressDialog(MainActivity.this);
                mProgressDialog.show();
                AlertDialog dlg = (AlertDialog) dialog;
                final String email = ((TextView)dlg.findViewById(R.id.email)).getText().toString();
                final String password =((TextView)dlg.findViewById(R.id.password)).getText().toString();

                mFirebaseRef.createUser(email, password, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        mFirebaseRef.authWithPassword(email, password, null);
                        mProgressDialog.dismiss();
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        mFirebaseRef.authWithPassword(email, password, null);
                        mProgressDialog.dismiss();
                    }
                });


            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        mFirebaseRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    mUsername = ((String) authData.getProviderData().get("email"));
                    findViewById(R.id.loginButton).setVisibility(View.INVISIBLE);
                } else {
                    mUsername = null;
                    findViewById(R.id.loginButton).setVisibility(View.VISIBLE);
                }
            }
        });


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
