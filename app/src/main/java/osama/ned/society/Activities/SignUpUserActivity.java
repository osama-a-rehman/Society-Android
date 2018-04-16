package osama.ned.society.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import osama.ned.society.Others.Helper;
import osama.ned.society.Others.Utilities;
import osama.ned.society.R;
import osama.ned.society.Users.NormalUser;

public class SignUpUserActivity extends AppCompatActivity {

    private LinearLayout showDetailsPanel;
    private LinearLayout moreetailsPanel;
    private TextView signInTextView;

    private EditText signUpEmailEditText;
    private EditText signUpPasswordEditText;
    private EditText signUpNameEditText;
    private EditText signUpUniversityEditText;
    private EditText signUpDepartmentEditText;
    private EditText signUpYearOfStudyEditText;
    private EditText signUpRollNoEditText;

    private String signUpEmail;
    private String signUpPassword;
    private String signUpName;
    private String signUpUniversity;
    private String signUpDept;
    private String signUpYear;
    private String signUpRollNo;

    private String userUID = "";

    private FirebaseAuth mAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference().child(Utilities.userDatabaseReference).child(Utilities.normalUserDatabaseReference);

        showDetailsPanel = (LinearLayout) findViewById(R.id.showDetailsPanel);
        moreetailsPanel = (LinearLayout) findViewById(R.id.moreDetailsPanel);
        signInTextView = (TextView) findViewById(R.id.signInTextView);

        signUpEmailEditText = (EditText) findViewById(R.id.signUpUserEmailEditText);
        signUpPasswordEditText = (EditText) findViewById(R.id.signUpUserPasswordEditText);
        signUpNameEditText = (EditText) findViewById(R.id.signUpUserNameEditText);
        signUpUniversityEditText = (EditText) findViewById(R.id.signUpUserUniversityEditText);
        signUpDepartmentEditText = (EditText) findViewById(R.id.signUpDepartmentEditText);
        signUpYearOfStudyEditText = (EditText) findViewById(R.id.signUpUserYearEditText);
        signUpRollNoEditText = (EditText) findViewById(R.id.signUpUserRollNumberEditText);

        mAuth = FirebaseAuth.getInstance();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        showDetailsPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreetailsPanel.setVisibility(View.VISIBLE);
                showDetailsPanel.setVisibility(View.GONE);
            }
        });

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpUserActivity.this, LoginActivity.class);
                startActivity(i);

                finish();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Loading");
        builder.setMessage("Please Wait");

    }

    public void btnSignUpUserClick(View view) {
        Log.i("Click", "Sign Up Clicked");

        boolean isSuccessfulValidation = validateInformation();

        if (isSuccessfulValidation)
            signUpUser();
    }

    private boolean validateInformation() {
        signUpEmail = signUpEmailEditText.getText().toString();
        signUpPassword = signUpPasswordEditText.getText().toString();
        signUpName = signUpNameEditText.getText().toString();
        signUpUniversity = signUpUniversityEditText.getText().toString();
        signUpDept = signUpDepartmentEditText.getText().toString();
        signUpYear = signUpYearOfStudyEditText.getText().toString();
        signUpRollNo = signUpRollNoEditText.getText().toString();

        if (signUpEmail.isEmpty()) {
            Helper.makeTextShort(this, "Please enter your Email");
            return false;
        }

        if (signUpPassword.isEmpty()) {
            Helper.makeTextShort(this, "Please enter your Password");
            return false;
        }

        if (signUpPassword.length() < 8) {
            Helper.makeTextShort(this, "Password should be atleast 8 Characters Long");
            return false;
        }

        if (signUpName.isEmpty()) {
            Helper.makeTextShort(this, "Please enter your Name");
            return false;
        }

        if (signUpUniversity.isEmpty()) {
            Helper.makeTextShort(this, "Please enter your University Name");
            return false;
        }

        if (signUpDept.isEmpty()) {
            Helper.makeTextShort(this, "Please enter your Department\'s Name");
            return false;
        }

        if (signUpYear.isEmpty()) {
            Helper.makeTextShort(this, "Please enter your Year of Study");
            return false;
        }

        if (signUpRollNo.isEmpty()) {
            Helper.makeTextShort(this, "Please enter your Roll No");
            return false;
        }

        return true;
    }

    private void signUpUser() {

        // Sign Up System

        if (!checkForInternet()) {
            Helper.makeTextShort(this, "Internet is not available");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing you up, Please Wait!");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(signUpEmail, signUpPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUpUserActivity.this, "Failed in Signing Up", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    userUID = task.getResult().getUser().getUid();

                    NormalUser user = new NormalUser(signUpEmail, signUpPassword, userUID, signUpName, signUpUniversity, signUpDept, signUpYear, signUpRollNo);

                    firebaseDatabaseReference.child(userUID).push().setValue(user).addOnSuccessListener(SignUpUserActivity.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignUpUserActivity.this);
                            sharedPreferences.edit().putBoolean(Utilities.IS_SOCIETY_TAG_NAME, true).commit();

                            Toast.makeText(SignUpUserActivity.this, "You have Signed up successfully", Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();

                            Intent i = new Intent(SignUpUserActivity.this, MainActivity.class);
                            startActivity(i);

                            finish();
                        }
                    });


                }
            }
        });
    }

    private boolean checkForInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);

        finish();
    }

}
