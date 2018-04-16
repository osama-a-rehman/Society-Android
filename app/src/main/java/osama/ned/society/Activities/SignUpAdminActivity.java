package osama.ned.society.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import osama.ned.society.Others.Helper;
import osama.ned.society.Others.Utilities;
import osama.ned.society.R;
import osama.ned.society.Users.SocietyUser;

public class SignUpAdminActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_REQUEST = 1;

    private LinearLayout showDetailsPanel;
    private LinearLayout moreetailsPanel;
    private TextView signUpTextView;

    private EditText signUpEmailEditText;
    private EditText signUpPasswordEditText;
    private EditText signUpPresidentEditText;
    private EditText signUpSocietyEditText;
    private EditText signUpContactEditText;

    private RelativeLayout societyPictureLayout;
    private ImageView societyLogoImageView;
    private Uri imageURI;

    private String signUpEmail;
    private String signUpPassword;
    private String signUpPresident;
    private String signUpSociety;
    private String signUpContact;

    private String userUID = "";

    private FirebaseAuth mAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseDatabaseReference;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_admin);

        if(savedInstanceState != null){
            imageURI = savedInstanceState.getParcelable("EventImageURI");

            if(imageURI != null){
                societyLogoImageView.setImageURI(imageURI);
            }
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabaseReference = firebaseDatabase.getReference().child(Utilities.userDatabaseReference).child(Utilities.societyDatabaseReference);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("society_logos");

        showDetailsPanel = (LinearLayout) findViewById(R.id.showDetailsPanel);
        moreetailsPanel = (LinearLayout) findViewById(R.id.moreDetailsPanel);
        signUpTextView = (TextView) findViewById(R.id.signInTextView);

        signUpEmailEditText = (EditText) findViewById(R.id.signUpEmailEditText);
        signUpPasswordEditText = (EditText) findViewById(R.id.signUpPasswordEditText);
        signUpPresidentEditText = (EditText) findViewById(R.id.signUpPresidentEditText);
        signUpSocietyEditText = (EditText) findViewById(R.id.signUpSocietyNameEditText);
        signUpContactEditText = (EditText) findViewById(R.id.signUpContactEditText);

        societyPictureLayout = (RelativeLayout) findViewById(R.id.societyPictureLayout);
        societyLogoImageView = (ImageView) findViewById(R.id.societyPictureImageView);

        mAuth = FirebaseAuth.getInstance();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        showDetailsPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreetailsPanel.setVisibility(View.VISIBLE);
                showDetailsPanel.setVisibility(View.GONE);
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpAdminActivity.this, LoginActivity.class);
                startActivity(i);

                finish();
            }
        });

        societyPictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            if(data == null){
                Helper.makeTextShort(SignUpAdminActivity.this, "Invalid Image");
                return;
            }

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                societyLogoImageView.setImageBitmap(bitmap);

                imageURI = uri;
            } catch (IOException e) {
                e.printStackTrace();
                Helper.makeTextShort(SignUpAdminActivity.this, "Invalid Image");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(imageURI != null){
            outState.putParcelable("EventImageURI", imageURI);
        }
    }

    public void btnSignUpClick(View view) {
        Log.i("Click", "Sign Up Clicked");

        boolean isSuccessfulValidation = validateInformation();

        if (isSuccessfulValidation)
            signUpUser();
    }

    private boolean validateInformation() {
        signUpEmail = signUpEmailEditText.getText().toString();
        signUpPassword = signUpPasswordEditText.getText().toString();
        signUpPresident = signUpPresidentEditText.getText().toString();
        signUpSociety = signUpSocietyEditText.getText().toString();
        signUpContact = signUpContactEditText.getText().toString();

        if (signUpEmail.isEmpty()) {
            Helper.makeTextShort(this, getResources().getString(R.string.login_enter_email_toast));
            return false;
        }

        if (signUpPassword.isEmpty()) {
            Helper.makeTextShort(this, getResources().getString(R.string.login_enter_password_toast));
            return false;
        }

        if (signUpPassword.length() < 8) {
            Helper.makeTextShort(this, getResources().getString(R.string.login_enter_password_8_char_toast));
            return false;
        }

        if(societyLogoImageView.getDrawable() == null){
            Helper.makeTextShort(this, getResources().getString(R.string.login_enter_society_image));
            return false;
        }

        if (signUpSociety.isEmpty()) {
            Helper.makeTextShort(this, getResources().getString(R.string.sign_up_enter_society_toast));
            return false;
        }

        if (signUpPresident.isEmpty()) {
            Helper.makeTextShort(this, getResources().getString(R.string.sign_up_enter_president_toast));
            return false;
        }

        if (signUpContact.isEmpty()) {
            Helper.makeTextShort(this, getResources().getString(R.string.sign_up_enter_contact_toast));
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
                    Toast.makeText(SignUpAdminActivity.this, "Failed in Signing Up", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    userUID = task.getResult().getUser().getUid();

                    StorageReference logoReference = storageReference.child(imageURI.getLastPathSegment());

                    progressDialog.setMessage("Uploading Image.. It\'s almost Done");

                    logoReference.putFile(imageURI).addOnSuccessListener(SignUpAdminActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();

                            SocietyUser user = new SocietyUser(signUpEmail, signUpPassword, userUID, downloadUri.toString(), signUpSociety, signUpPresident, signUpContact);

                            firebaseDatabaseReference.child(userUID).push().setValue(user).addOnSuccessListener(SignUpAdminActivity.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignUpAdminActivity.this);
                                    sharedPreferences.edit().putBoolean(Utilities.IS_SOCIETY_TAG_NAME, true).commit();

                                    Toast.makeText(SignUpAdminActivity.this, "You have Signed up successfully", Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();

                                    Intent i = new Intent(SignUpAdminActivity.this, MainActivity.class);
                                    startActivity(i);

                                    finish();
                                }
                            });
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
