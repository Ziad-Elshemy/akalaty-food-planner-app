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

import com.airbnb.lottie.LottieAnimationView;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.ui.MainActivity;


public class SplashFragment extends Fragment {

    LottieAnimationView lottieAnimationView;

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

        lottieAnimationView.animate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE);
                boolean isOnBoardingFinished = (preferences.getBoolean("isFinished",false));

                if (isOnBoardingFinished){
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }else {

                    Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_viewPagerFragment);
                }


            }
        },2000);

    }
}