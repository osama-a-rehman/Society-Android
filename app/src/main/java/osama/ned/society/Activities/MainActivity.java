package osama.ned.society.Activities;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import osama.ned.society.Fragments.HomeFragment;
import osama.ned.society.Fragments.SocietiesFragment;
import osama.ned.society.Others.Helper;
import osama.ned.society.Others.Utilities;
import osama.ned.society.R;
import osama.ned.society.Users.NormalUser;
import osama.ned.society.Users.SocietyUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView = null;
    private Toolbar toolbar = null;

    FloatingActionButton fab;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private DatabaseReference societyUserDataDatabaseReference;

    private ValueEventListener valueEventListener;

    private FragmentTransaction fragmentTransaction;

    private boolean isBackPressed = false;

    private boolean isSocietyUser = false;

    private boolean isLoggedOut = false;

    private String imageURI;
    private String societyName;
    private String societyEmail;

    private void setUpDrawer() {
        // Fragments

        setTitle("Home");

        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.mainFragmentsContainer, homeFragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fabInsertEvent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddEventActivity.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpDrawer();

        mAuth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser = mAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        societyUserDataDatabaseReference = firebaseDatabase.getReference().child(Utilities.userDatabaseReference).child(Utilities.societyDatabaseReference).child(currentUser.getUid());

        ValueEventListener societyUserValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Data Keys Main Activity", dataSnapshot.getKey());

                if(dataSnapshot.getChildrenCount() > 0){
                    isSocietyUser = true;

                    fab.setVisibility(View.VISIBLE);
                    databaseReference = firebaseDatabase.getReference().child(Utilities.userDatabaseReference).child(Utilities.societyDatabaseReference).child(currentUser.getUid());

                }else{
                    isSocietyUser = false;

                    fab.setVisibility(View.GONE);
                    databaseReference = firebaseDatabase.getReference().child(Utilities.userDatabaseReference).child(Utilities.normalUserDatabaseReference).child(currentUser.getUid());
                }

                databaseReference.addListenerForSingleValueEvent(valueEventListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        societyUserDataDatabaseReference.addListenerForSingleValueEvent(societyUserValueEventListener);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Log.i("Data Keys", dataSnapshot.getKey());

                //Log.i("childCount", String.valueOf(dataSnapshot.getChildrenCount()));

                if (isSocietyUser) {

                    for(DataSnapshot user : dataSnapshot.getChildren()) {
                        SocietyUser societyUser = user.getValue(SocietyUser.class);

                        imageURI = societyUser.getImageUri();
                        societyName = societyUser.getSocietyName();
                        societyEmail = societyUser.getEmail();

                        Log.i("Society Name", "" + societyName);
                        Log.i("Society Email", "" + societyEmail);

                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        View view = navigationView.getHeaderView(0);

                        TextView profileSocietyUserName = (TextView) view.findViewById(R.id.profileSocietyUserName);
                        profileSocietyUserName.setText(societyName);

                        TextView profileSocietyEmail = (TextView) view.findViewById(R.id.profileSocietyEmail);
                        profileSocietyEmail.setText(societyEmail);

                        ImageView profileImageView = (ImageView) view.findViewById(R.id.profile_image);
                        Glide.with(profileImageView.getContext()).load(imageURI).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(profileImageView);
                    }
                } else {

                    for(DataSnapshot user : dataSnapshot.getChildren()){
                        NormalUser normalUser = user.getValue(NormalUser.class);

                        societyName = normalUser.getName();
                        societyEmail = normalUser.getEmail();

                        Log.i("Normal Name", "" + societyName);
                        Log.i("Normal Email", "" + societyEmail);

                        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                        View view = navigationView.getHeaderView(0);

                        TextView profileSocietyUserName = (TextView) view.findViewById(R.id.profileSocietyUserName);
                        profileSocietyUserName.setText(societyName);

                        TextView profileSocietyEmail = (TextView) view.findViewById(R.id.profileSocietyEmail);
                        profileSocietyEmail.setText(societyEmail);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Helper.makeTextShort(MainActivity.this, "Failed in onCancelled");
            }
        };

       // databaseReference.addValueEventListener(valueEventListener);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isBackPressed) {
                super.onBackPressed();
                System.exit(0);
            } else {
                Helper.makeTextShort(this, "Press BACK again to exit.");

                isBackPressed = true;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBackPressed = false;
                    }
                }, 2000);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment homeFragment = new HomeFragment();
            setTitle("Home");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.mainFragmentsContainer, homeFragment);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_societies) {
            SocietiesFragment societiesFragment = new SocietiesFragment();
            setTitle("Societies");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.mainFragmentsContainer, societiesFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_sign_out) {

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Sign Out!");
            alertDialogBuilder.setMessage("Do you really want to sign out ?");

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mAuth.signOut();
                    Helper.makeTextShort(MainActivity.this, "You\'ve been successfully Signed out!");

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra("LOGGED_OUT", true);
                    startActivity(intent);

                    MainActivity.this.finish();
                }
            });

            alertDialogBuilder.setNegativeButton("No", null);

            alertDialogBuilder.show();

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about_us) {

        } else if (id == R.id.nav_privacy_policy) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*@Override
    protected void onResume() {
        super.onResume();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mAuth.removeAuthStateListener(mAuthListener);
    }*/
}
