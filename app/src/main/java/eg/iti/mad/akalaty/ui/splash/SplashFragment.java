package eg.iti.mad.akalaty.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.ui.MainActivity;
import eg.iti.mad.akalaty.utils.SharedPref;


public class SplashFragment extends Fragment {

    LottieAnimationView lottieAnimationView;
    TextView txtAppName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lottieAnimationView = view.findViewById(R.id.lottieImg);
        txtAppName = view.findViewById(R.id.txtAppName);

        Animation scaleFromCenter = AnimationUtils.loadAnimation(requireActivity(), R.anim.splash_scale_up_from_center);
        txtAppName.startAnimation(scaleFromCenter);
        txtAppName.setVisibility(View.VISIBLE);

        lottieAnimationView.animate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean isOnBoardingFinished = SharedPref.getInstance(requireActivity()).getIsFinished();

                if (isOnBoardingFinished){
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }else {

                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_viewPagerFragment);
                }


            }
        },4000);

    }
}