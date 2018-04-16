package osama.ned.society.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import osama.ned.society.Activities.CompetitionActivity;
import osama.ned.society.Others.Helper;
import osama.ned.society.R;

public class EventCategoryFragment extends DialogFragment implements View.OnClickListener {

    Button btnConfirm, btnCancel;

    private RadioGroup eventCategoryRadioGroup;

    RadioButton radioBtnCompetition, radioBtnNonTech,
            radioBtnMotivational, radioBtnInformational,
            radioBtnCareerDev, radioBtnIndustrialVisit,
            radioBtnProjectExhibition, radioBtnWorkshop,
            radioBtnOther;

    public EventCategoryFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setCancelable(false);

        View rootView = inflater.inflate(R.layout.fragment_event_category, null);

        btnConfirm = (Button) rootView.findViewById(R.id.btnRadioEventCategoryConfirm);
        btnCancel = (Button) rootView.findViewById(R.id.btnRadioEventCategoryCancel);

        eventCategoryRadioGroup = (RadioGroup) rootView.findViewById(R.id.eventCategoryRadioGroup);

        radioBtnCompetition = (RadioButton) rootView.findViewById(R.id.eventCategoryRadioBtnCompetition);
        radioBtnNonTech = (RadioButton) rootView.findViewById(R.id.eventCategoryRadioBtnNonTechnical);
        radioBtnMotivational = (RadioButton) rootView.findViewById(R.id.eventCategoryRadioBtnMotivational);
        radioBtnInformational = (RadioButton) rootView.findViewById(R.id.eventCategoryRadioBtnInformational);
        radioBtnCareerDev = (RadioButton) rootView.findViewById(R.id.eventCategoryRadioBtnCareerDev);
        radioBtnIndustrialVisit = (RadioButton) rootView.findViewById(R.id.eventCategoryRadioBtnIndustrialVisit);
        radioBtnProjectExhibition = (RadioButton) rootView.findViewById(R.id.eventCategoryRadioBtnProjectExhibition);
        radioBtnWorkshop = (RadioButton) rootView.findViewById(R.id.eventCategoryRadioBtnWorkshop);
        radioBtnOther = (RadioButton) rootView.findViewById(R.id.eventCategoryRadioBtnOther);

        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnRadioEventCategoryConfirm) {
            int selectedBtnId = eventCategoryRadioGroup.getCheckedRadioButtonId();

            switch (selectedBtnId) {
                case R.id.eventCategoryRadioBtnCompetition:
                    dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Competitions");
                    builder.setMessage("How many competitions are to be conducted in this event ?");

                    final EditText numOfCompetitionsEditText = new EditText(getActivity());
                    numOfCompetitionsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

                    builder.setView(numOfCompetitionsEditText);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(numOfCompetitionsEditText.getText().toString().isEmpty()){
                                Helper.makeTextShort(getActivity(), "Number of competitions can not be ZERO.");

                                return;
                            }

                            int numOfCompetitions = Integer.parseInt(numOfCompetitionsEditText.getText().toString());

                            Intent intent = new Intent(getActivity(), CompetitionActivity.class);

                            intent.putExtra("numOfCompetitions", numOfCompetitions);

                            startActivity(intent);

                        }
                    });

                    builder.setNegativeButton("Cancel", null);

                    break;
            }


        } else if (view.getId() == R.id.btnRadioEventCategoryCancel) {
            dismiss();
        }


    }
}
