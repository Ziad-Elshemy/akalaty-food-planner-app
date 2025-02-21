package eg.iti.mad.akalaty.ui.onboarding.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.ui.MainActivity;


public class ThirdScreen extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtFinish = view.findViewById(R.id.btnOnboardingFinish3);
        txtFinish.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
            onBoardingFinish();
        });
    }

    private void onBoardingFinish(){
        SharedPreferences preferences = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // make it true to show the onBoarding once in the first time,,, for developing it is false to test
        editor.putBoolean("isFinished",true);
        editor.apply();
    }

}