package osama.ned.society.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import osama.ned.society.Fragments.EventCategoryFragment;
import osama.ned.society.Others.Event;
import osama.ned.society.Others.Helper;
import osama.ned.society.Others.Utilities;
import osama.ned.society.R;

public class AddEventActivity extends AppCompatActivity {

    private RelativeLayout eventPictureLayout;
    private ImageView addEventPictureImageView;
    private TextView clickToChangeImageTextView;
    private EditText eventNameEditText;
    private EditText eventDescriptionEditText;
    private EditText eventVenueEditText;

    private EditText eventDatePickerEditText;
    private EditText eventTimePickerEditText;

    private EditText eventTag1;
    private EditText eventTag2;

    private ImageView imageAddTag;

    private LinearLayout showDetailsPanel;
    private LinearLayout moreetailsPanel;

    private LinearLayout eventTagsPanel;
    private int tagCount = 0;

    private Button btnNextEventFragment;

    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour = 10, selectedMin = 8;
    private String selectedAmOrPm;
    private boolean is24Selected = false;

    public static final int PICK_IMAGE_REQUEST = 1;

    private boolean isImageSelected = false;

    private Uri imageURI;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        setTitle("Add Event");

        /*if(savedInstanceState != null){
            imageURI = savedInstanceState.getParcelable("EventImageURI");

            if(imageURI != null){
                addEventPictureImageView.setImageURI(imageURI);
            }
        }*/

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        initializeDate(date);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        eventPictureLayout = (RelativeLayout) findViewById(R.id.eventPictureLayout);
        addEventPictureImageView = (ImageView) findViewById(R.id.addEventPictureImageView);
        clickToChangeImageTextView = (TextView) findViewById(R.id.clickToChangeImageTextView);

        eventNameEditText = (EditText) findViewById(R.id.eventNameEditText);
        eventDescriptionEditText = (EditText) findViewById(R.id.eventDescriptionEditText);
        eventVenueEditText = (EditText) findViewById(R.id.eventVenueEditText);

        eventDatePickerEditText = (EditText) findViewById(R.id.eventDatePickerEditText);

        eventTimePickerEditText = (EditText) findViewById(R.id.eventTimePickerEditText);

        imageAddTag = (ImageView) findViewById(R.id.imageAddTag);

        showDetailsPanel = (LinearLayout) findViewById(R.id.addEventShowDetailsPanel);
        moreetailsPanel = (LinearLayout) findViewById(R.id.addEventMoreDetailsPanel);

        eventTagsPanel = (LinearLayout) findViewById(R.id.eventTagsPanel);

        eventTag1 = (EditText) findViewById(R.id.eventTag);
        eventTag2 = (EditText) findViewById(R.id.eventTag2);

        btnNextEventFragment = (Button) findViewById(R.id.btnNextEventFragment);

        showDetailsPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreetailsPanel.setVisibility(View.VISIBLE);
                showDetailsPanel.setVisibility(View.GONE);
            }
        });

        eventPictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = monthOfYear;
                selectedDay = dayOfMonth;

                eventDatePickerEditText.setText(selectedDay + "-" + (selectedMonth+1) + "-" + selectedYear);
            }
        };

        final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String sHour;
                String sMin;

                selectedHour = i;
                selectedMin = i1;

                if(selectedHour < 12){
                    selectedAmOrPm = "AM";
                }else{
                    selectedAmOrPm = "PM";
                    selectedHour -= 12;
                }

                if(selectedHour < 10){
                    sHour = "0" + selectedHour;
                }else{
                    sHour = "" + selectedHour;
                }

                if(selectedMin < 10){
                    sMin = "0" + selectedMin;
                }else{
                    sMin = "" + selectedMin;
                }

                eventTimePickerEditText.setText(sHour + ":" + sMin + " " + selectedAmOrPm);
            }
        };

        eventDatePickerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this, onDateSetListener, selectedYear, selectedMonth, selectedDay);
                datePickerDialog.show();
            }
        });

        eventTimePickerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this, onTimeSetListener, selectedHour, selectedMin, is24Selected);
                timePickerDialog.show();
            }
        });

        btnNextEventFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextEventFragment();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            if(data == null){
                Helper.makeTextShort(AddEventActivity.this, "Invalid Image");
                return;
            }

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

               // Bitmap roundedBitmap = Helper.getRoundedCornerBitmap(bitmap, bitmap.);

                addEventPictureImageView.setImageBitmap(bitmap);

                clickToChangeImageTextView.setVisibility(View.VISIBLE);

                imageURI = uri;

                isImageSelected = true;
            } catch (IOException e) {
                e.printStackTrace();
                Helper.makeTextShort(AddEventActivity.this, "Invalid Image");
            }
        }
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(imageURI != null){
            outState.putParcelable("EventImageURI", imageURI);
        }
    }*/

    public void btnAddTagOnClick(View view){
        tagCount++;

        View inflatedView = LayoutInflater.from(AddEventActivity.this).inflate(R.layout.add_tag_layout, eventTagsPanel, false);

        EditText eventTagEditText = (EditText) inflatedView.findViewById(R.id.eventTag);
        eventTagEditText.setHint("Tag " + (tagCount+2));

        eventTagsPanel.addView(inflatedView);
    }

    private void nextEventFragment(){
        if(!isImageSelected){
            Helper.makeTextShort(this, "Please select an Image for Event");
            return;
        }

        if(eventNameEditText.getText().toString().isEmpty()){
            Helper.makeTextShort(this, "Please enter an Event\'s name");
            return;
        }

        if(eventDescriptionEditText.getText().toString().isEmpty()){
            Helper.makeTextShort(this, "Please enter an Event\'s Description");
            return;
        }

        if(eventVenueEditText.getText().toString().isEmpty()){
            Helper.makeTextShort(this, "Please enter an Event\'s Venue");
            return;
        }

        if(eventDatePickerEditText.getText().toString().isEmpty()){
            Helper.makeTextShort(this, "Please enter an Event\'s Date");
            return;
        }

        if(eventTimePickerEditText.getText().toString().isEmpty()){
            Helper.makeTextShort(this, "Please enter an Event\'s Time");
            return;
        }

        if(eventTag1.getText().toString().isEmpty() || eventTag2.getText().toString().isEmpty()){
            Helper.makeTextShort(this, "Please enter all Event\'s Tags");
            return;
        }

        if(eventDescriptionEditText.getText().toString().isEmpty()){
            Helper.makeTextShort(this, "Please enter an Event\'s description");
            return;
        }

        /*EventCategoryFragment eventCategoryFragment = new EventCategoryFragment();

        eventCategoryFragment.show(getSupportFragmentManager(), "Event Category");*/

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventsDatabaseReference = firebaseDatabase.getReference().child(Utilities.eventsDatabaseReference);

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child(Utilities.eventsStorageReference);

        if (!checkForInternet()) {
            Helper.makeTextShort(this, "Internet is not available");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Initializing the Event, Please Wait!");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String eventName = eventNameEditText.getText().toString();
        String eventDescription = eventDescriptionEditText.getText().toString();
        String eventVenue = eventVenueEditText.getText().toString();
        String eventDate = eventDatePickerEditText.getText().toString();
        String eventTime = eventTimePickerEditText.getText().toString();
        String eventTagName1 = eventTag1.getText().toString();
        String eventTagName2 = eventTag2.getText().toString();

        final Event event = new Event(imageURI.toString(), eventName, eventDescription, eventDate, eventVenue, eventTime, eventTagName1, eventTagName2);

        mAuth = FirebaseAuth.getInstance();

        final String userUID = mAuth.getCurrentUser().getUid().toString();

        eventsDatabaseReference.child(userUID).child(event.getName()).push().setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                storageReference = storageReference.child(userUID).child(event.getName()).child(imageURI.getLastPathSegment());

                progressDialog.setMessage("Uploading Image, Please Wait!");

                storageReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();

                        Toast.makeText(AddEventActivity.this, "Event Uploaded", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private boolean checkForInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    private void initializeDate(String date){
        String[] array = date.split("-");

        selectedYear = Integer.parseInt(array[0]);
        selectedMonth = Integer.parseInt(array[1]);
        selectedDay = Integer.parseInt(array[2]);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
