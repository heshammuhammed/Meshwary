package com.example.heshammuhammed.reminder;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.heshammuhammed.reminder.MainHome.MainActivity;
import com.example.heshammuhammed.reminder.MainHome.Signup;
import com.example.heshammuhammed.reminder.Permissions.CheckPermissions;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.Arrays;

public class HomePage extends AppCompatActivity {
    private static final String EMAIL = "email";
    private static final int RC_SIGN_IN = 123;

    private Button login;
    private Button signup;
    private EditText email;
    private EditText password;
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private Intent intent;
    private FirebaseAuth.AuthStateListener mListener;
    private String name;
    private FirebaseAuth mAuth;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AwesomeValidation awesomeValidation;

    // add 15_3 abdalla
    JSONObject obj,profile_pic_data,profile_pic_url;
    //end add 15_3  abdalla


    static String TAG = "tset";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
//        //abdalla start
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
//        //end abdalla start
      callbackManager = CallbackManager.Factory.create();
        if (AccessToken.getCurrentAccessToken() == null) {
            loginButton = findViewById(R.id.login_button);
            loginButton.setReadPermissions(Arrays.asList(EMAIL));

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code
                            Toast.makeText(HomePage.this, "login success", Toast.LENGTH_LONG).show();
                            handleFacebookAccessToken(loginResult.getAccessToken());
                            getUserDetails(loginResult);
                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                        }
                    });


            mListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {

                        name = user.getDisplayName();
                  //      Toast.makeText(HomePage.this, "fireBase User" + user.getDisplayName(), Toast.LENGTH_LONG).show();
                    } else {
              //          Toast.makeText(HomePage.this, "something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            };
        }else{

            Intent intent = new Intent(HomePage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

//        //abdalla end

        signup = findViewById(R.id.signup);
        email = findViewById(R.id.homeEmail);
        password = findViewById(R.id.homePassword);
        login = findViewById(R.id.loginac);
        share = getSharedPreferences("mypre", 0);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.homeEmail, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.homePassword, ".{8,}", R.string.passworderror);
        if (share.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);
            finish();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(new CheckPermissions(HomePage.this).checkInternet()) {
                if (awesomeValidation.validate()) {
                    Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
                    String emailstr = email.getText().toString();
                    String passwordstr = password.getText().toString();
                    editor = share.edit();
                    mAuth.signInWithEmailAndPassword(emailstr, passwordstr)
                            .addOnCompleteListener(HomePage.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                          //              Toast.makeText(getApplicationContext(), "Wellcom " + user.getDisplayName(), Toast.LENGTH_LONG).show();
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");

                                        editor.putString("name", user.getDisplayName());
                                        editor.putString("mail", user.getEmail());
                                        editor.putString("ph", user.getPhoneNumber());
                                        editor.putString("form", "login");
                                        editor.putString("logged", "logged");
                                        editor.commit();
                                        updateUI(user);
                                        Intent intent = new Intent(HomePage.this, MainActivity.class);

                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(HomePage.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
            }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
                finish();

            }
        });
    }

    //abdalla start
    private void handleFacebookAccessToken( AccessToken accessToken) {
        Log.d("myface", "handleFacebookAccessToken id : " + accessToken.getUserId());
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Log.w("hi", "signInWithCredential", task.getException());
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    Toast.makeText(HomePage.this, "Success", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(HomePage.this, MainActivity.class);
//                    finish();
//                    startActivity(intent);

                } else {
                    updateUI(null);
                    Toast.makeText(HomePage.this, "Authentication error",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        Intent intent = new Intent(HomePage.this, MainActivity.class);
                        intent.putExtra("userProfile", json_object.toString());
                        editor = share.edit();
                        editor.putString("userProfile", json_object.toString());
                        editor.commit();
                        //abdalla 3_15
//
                       // intent.putExtra("userProfile", json_object.toString());
                        startActivity(intent);
                        finish();
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void updateUI(FirebaseUser user) {
        //  hideProgressDialog();
        if (user != null) {
            //  mStatusTextView.setText(getString(R.string.facebook_status_fmt, user.getDisplayName()));
            // mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.login_button).setVisibility(View.GONE);
            //  findViewById(R.id.button_facebook_signout).setVisibility(View.VISIBLE);
        } else {
            //   mStatusTextView.setText(R.string.signed_out);
            //   mDetailTextView.setText(null);

            findViewById(R.id.login_button).setVisibility(View.VISIBLE);
            //  findViewById(R.id.button_facebook_signout).setVisibility(View.GONE);
        }

    }
    @Override
    public void onBackPressed() {

        finish();
    }
    //abdalla end

}