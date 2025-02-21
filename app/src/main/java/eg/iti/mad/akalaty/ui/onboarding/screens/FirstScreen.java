package eg.iti.mad.akalaty.ui.onboarding.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eg.iti.mad.akalaty.R;


public class FirstScreen extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //change the activity background color
        View rootView = requireActivity().findViewById(R.id.splash);
        rootView.setBackgroundResource(R.drawable.img_test_bg);

        ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
        TextView txtNext = view.findViewById(R.id.btnOnboardingNext1);
        txtNext.setOnClickListener(view1 -> {
            viewPager.setCurrentItem(1);
        });
    }
}