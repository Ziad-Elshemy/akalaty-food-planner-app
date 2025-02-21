package eg.iti.mad.akalaty.ui.onboarding;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.ui.onboarding.screens.FirstScreen;
import eg.iti.mad.akalaty.ui.onboarding.screens.SecondScreen;
import eg.iti.mad.akalaty.ui.onboarding.screens.ThirdScreen;


public class ViewPagerFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FirstScreen());
        fragmentList.add(new SecondScreen());
        fragmentList.add(new ThirdScreen());

        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentList,requireActivity().getSupportFragmentManager(),getLifecycle());

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
    }
}