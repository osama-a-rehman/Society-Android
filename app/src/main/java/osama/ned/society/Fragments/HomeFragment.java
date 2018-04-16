package osama.ned.society.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import osama.ned.society.Activities.EventDetailsActivity;
import osama.ned.society.Adapters.EventsListAdapter;
import osama.ned.society.Others.Event;
import osama.ned.society.Others.Utilities;
import osama.ned.society.R;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ChildEventListener childEventListener;
    public static List<Event> tempEvents;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ListView homeEventsListView = (ListView) rootView.findViewById(R.id.homeEventsListView);
        List<Event> eventList = new ArrayList<>();

        tempEvents = new ArrayList<>();

        final ProgressBar progressBarList = (ProgressBar) rootView.findViewById(R.id.progressBarList);

        progressBarList.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.progressBarColor), android.graphics.PorterDuff.Mode.SRC_IN);

        mAuth = FirebaseAuth.getInstance();

        String userUID = mAuth.getCurrentUser().getUid().toString();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(Utilities.eventsDatabaseReference).child(userUID);

        /*eventList.add(new Event());
        eventList.add(new Event(R.drawable.sentec_17, "SENTEC'17", "NED University of Engr. and Technology", "#SENTEC", "#2k17"));
        eventList.add(new Event(R.drawable.iba_probattle, "Pro-Battle", "IBA-Karachi", "#Battle", "#IBA"));*/

        final EventsListAdapter adapter = new EventsListAdapter(getActivity(), tempEvents);

        homeEventsListView.setAdapter(adapter);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot eventSnapshot: dataSnapshot.getChildren()){
                    Event event = eventSnapshot.getValue(Event.class);

                    if(progressBarList.getVisibility() == View.VISIBLE)
                        progressBarList.setVisibility(View.GONE);

                    //adapter.add(event);

                    tempEvents.add(event);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference.addChildEventListener(childEventListener);

        homeEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), EventDetailsActivity.class);
                intent.putExtra("EventPosition", i);

                startActivity(intent);

                //getActivity().overridePendingTransition(R.anim.activity_slide_out_from_right, R.anim.activity_slide_in_from_right);
            }
        });

        return rootView;
    }

}
