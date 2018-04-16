package osama.ned.society.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import osama.ned.society.R;

public class CompetitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);

        LinearLayout competitionsLayout = (LinearLayout) findViewById(R.id.competitionsLayout);

        Intent intent = getIntent();

        int numOfCompetitions = intent.getIntExtra("numOfCompetitions", 0);

        for(int dex=0; dex<numOfCompetitions; dex++){
            View singleCompetitionView = LayoutInflater.from(this).inflate(R.layout.single_competition_layout, competitionsLayout, false);

            TextView competitionNum = (TextView) singleCompetitionView.findViewById(R.id.competitionNum);
            competitionNum.setText("Competition " + (dex+1) + ": ");

            competitionsLayout.addView(singleCompetitionView);
        }
    }
}
