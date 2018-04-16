package osama.ned.society.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import osama.ned.society.Fragments.HomeFragment;
import osama.ned.society.Others.Event;
import osama.ned.society.R;

public class EventDetailsActivity extends AppCompatActivity {

    private int eventPostition;

    private ImageView eventDetailsImageView;
    private TextView eventDetailsDateTimeTextView, eventDetailsVenueTextView, eventDetailsDescTextView, eventDetailsTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Intent intent = getIntent();

        eventPostition = intent.getIntExtra("EventPosition", -1);

        if(eventPostition == -1){
            Toast.makeText(this, "Error Occured while retrieving Event", Toast.LENGTH_SHORT).show();
            finish();
        }

        Event currentEvent = HomeFragment.tempEvents.get(eventPostition);

        setTitle(currentEvent.getName());

        eventDetailsImageView = (ImageView) findViewById(R.id.detailsEventImageView);

        eventDetailsDateTimeTextView = (TextView) findViewById(R.id.detailsEventDateAndTime);
        eventDetailsVenueTextView = (TextView) findViewById(R.id.detailsEventVenue);
        eventDetailsDescTextView = (TextView) findViewById(R.id.detailsEventDescription);
        eventDetailsTags = (TextView) findViewById(R.id.detailsEventTags);

        Glide.with(eventDetailsImageView.getContext()).load(currentEvent.getImageResource()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(eventDetailsImageView);

        Log.i("ImageEventDetaisl", currentEvent.getImageResource() + "");

        eventDetailsDateTimeTextView.setText(currentEvent.getDate() + " " + currentEvent.getEventTime());
        eventDetailsVenueTextView.setText(currentEvent.getVenue());
        eventDetailsDescTextView.setText(currentEvent.getDescription());
        eventDetailsTags.setText("#" + currentEvent.getTag1() + "\n" + "#" + currentEvent.getTag2());
    }
}
