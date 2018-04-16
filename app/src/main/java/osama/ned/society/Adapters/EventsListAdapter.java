package osama.ned.society.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import osama.ned.society.Others.Event;
import osama.ned.society.R;

public class EventsListAdapter extends ArrayAdapter<Event> {

    public EventsListAdapter(Context context, List<Event> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentView = convertView;

        if(currentView == null){
            currentView = LayoutInflater.from(getContext()).inflate(R.layout.home_event_list_item, parent, false);
        }

        Event event = getItem(position);

        ImageView eventImageView = (ImageView) currentView.findViewById(R.id.eventImageView);

        String imageURI = event.getImageResource();

        Glide.with(getContext()).load(imageURI).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(eventImageView);

        Log.i("Adapter Name", event.getName() + "");
        Log.i("Adapter Image", event.getImageResource() + "");
        Log.i("Adapter Venue", event.getVenue() + "");
        Log.i("Adapter Date", event.getDate() + "");
        Log.i("Adapter Time", event.getEventTime() + "");
        Log.i("Adapter Tag1", event.getTag1() + "");
        Log.i("Adapter Tag 2", event.getTag2() + "");
        Log.i("Adapter Des", event.getDescription() + "");

        TextView eventNameTextView = (TextView) currentView.findViewById(R.id.eventNameTextView);
        eventNameTextView.setText(event.getName());

        TextView eventVenueTextView = (TextView) currentView.findViewById(R.id.eventVenueTextView);
        eventVenueTextView.setText(event.getVenue());

        TextView eventTagTextView1 = (TextView) currentView.findViewById(R.id.eventTagTextView1);
        eventTagTextView1.setText("#"+event.getTag1());

        TextView eventTagTextView2 = (TextView) currentView.findViewById(R.id.eventTagTextView2);
        eventTagTextView2.setText("#"+event.getTag2());

        TextView eventDateTextView = (TextView) currentView.findViewById(R.id.eventDateTextView);

        String date = extractDate(event);
        String time = event.getEventTime();

        eventDateTextView.setText(date + ", " + time);

        return currentView;
    }

    private String extractDate(Event event){
        StringBuilder stringBuilder = new StringBuilder();

        String[] strings = event.getDate().split("-");

        stringBuilder.append(strings[0]);
        stringBuilder.append(" ");
        stringBuilder.append(extractMonth(Integer.parseInt(strings[1])));

        String lastTwoCharsFromYear = strings[2].substring(2, strings[2].length());

        stringBuilder.append(" "+ lastTwoCharsFromYear);

        return stringBuilder.toString();

    }

    private String extractMonth(int mon){
        switch (mon){
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
        }

        return null;
    }
}
