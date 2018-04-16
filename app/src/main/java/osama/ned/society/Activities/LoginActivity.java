package osama.ned.society.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import osama.ned.society.Fragments.SignUpFragment;
import osama.ned.society.R;

public class LoginActivity extends AppCompatActivity {

    private EditText signInUserNameEditText;
    private EditText signInPasswordEditText;
    private Button btnSignIn;

    private TextView signUpTextView;

    private String userNameString = "";
    private String passwordString = "";

    private LinearLayout contentView;
    private ImageView logoImage;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        setTitle("Sign In!");

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        signInUserNameEditText = (EditText) findViewById(R.id.loginUserNameEditText);
        signInPasswordEditText = (EditText) findViewById(R.id.loginPasswordEditText);
        btnSignIn = (Button) findViewById(R.id.btnLogin);
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);

        contentView = (LinearLayout) findViewById(R.id.contentView);
        logoImage = (ImageView) findViewById(R.id.logoImage);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d("SIGNED-IN", "onAuthStateChanged:signed_in:" + user.getUid());

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);

                    finish();
                } else {
                    Log.d("SIGNED-IN", "onAuthStateChanged:signed_out");
                }
            }
        };

        // Checks if soft keyboard is visible or not
        changeLogoVisibility();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment signUpFragment = new SignUpFragment();

                signUpFragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void signInUser(){
        userNameString = signInUserNameEditText.getText().toString();
        passwordString = signInPasswordEditText.getText().toString();

        if(userNameString.equals("")){
            makeTextThroughToast(getResources().getString(R.string.login_enter_username_toast));
            return;
        }

        if(passwordString.equals("")){
            makeTextThroughToast(getResources().getString(R.string.login_enter_username_toast));
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing you in, Please Wait!");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(userNameString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    makeTextThroughToast("You Signed In Successfuly");

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);

                    startActivity(i);

                    finish();
                }else{
                    makeTextThroughToast("Error in Signing In!");
                }

                progressDialog.dismiss();
            }
        });
    }

    private boolean checkInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    private void makeTextThroughToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void changeLogoVisibility(){
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();

                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    logoImage.setVisibility(View.GONE);
                }
                else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            logoImage.setVisibility(View.VISIBLE);
                        }
                    }, 200);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }
}
