package osama.ned.society.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import osama.ned.society.R;
import osama.ned.society.Activities.SignUpAdminActivity;
import osama.ned.society.Activities.SignUpUserActivity;

public class SignUpFragment extends DialogFragment implements View.OnClickListener{

    Button btnConfirm, btnCancel;
    RadioButton radioBtnUser, radioBtnSociety;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);

        View rootView = inflater.inflate(R.layout.fragment_sign_up, null);

        btnConfirm = (Button) rootView.findViewById(R.id.btnRadioSignUpConfirm);
        btnCancel = (Button) rootView.findViewById(R.id.btnRadioSignUpCancel);
        radioBtnUser = (RadioButton) rootView.findViewById(R.id.signUpRadioBtnUser);
        radioBtnSociety = (RadioButton) rootView.findViewById(R.id.signUpRadioBtnSociety);

        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        getDialog().setTitle("Sign Up");

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRadioSignUpConfirm:
                dismiss();

                Intent intent;

                if(radioBtnUser.isChecked()){
                    intent = new Intent(getContext(), SignUpUserActivity.class);
                    startActivity(intent);

                    getActivity().finish();
                }else{
                    intent = new Intent(getContext(), SignUpAdminActivity.class);
                    startActivity(intent);

                    getActivity().finish();
                }
                break;

            case R.id.btnRadioSignUpCancel:
                dismiss();
                break;
        }
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;



        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
}
