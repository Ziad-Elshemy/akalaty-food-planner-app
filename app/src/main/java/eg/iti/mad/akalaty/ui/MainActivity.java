package eg.iti.mad.akalaty.ui;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import eg.iti.mad.akalaty.R;
import eg.iti.mad.akalaty.utils.NetworkUtils;
import eg.iti.mad.akalaty.utils.SharedPref;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;
    NavController navController;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // stop orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
//        //to add action bar do not forget to change the theme
//        NavigationUI.setupActionBarWithNavController(this,navController);
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.mealDetailsFragment || navDestination.getId() == R.id.loginFragment || navDestination.getId() == R.id.registerFragment ) {
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }

//            if (navDestination.getId() == R.id.myFavFragment || navDestination.getId() == R.id.calenderFragment || navDestination.getId() == R.id.profileFragment ) {
//                bottomNavigationView.setVisibility(View.GONE);
//            } else {
//                bottomNavigationView.setVisibility(View.VISIBLE);
//            }

            if (!SharedPref.getInstance(this).getIsLogged()) {
                Log.i(TAG, "onCreate: isLogged = false");
                Log.i(TAG, "onCreate: id: "+SharedPref.getInstance(this).getUserId());
                Log.i(TAG, "onCreate: user: "+SharedPref.getInstance(this).getUserName());
                Log.i(TAG, "onCreate: email: "+SharedPref.getInstance(this).getUserEmail());
                bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                    if (item.getItemId() == R.id.myFavFragment || item.getItemId() == R.id.calenderFragment || item.getItemId() == R.id.profileFragment)
                        showLoginDialog();
                    else {
                        if (item.getItemId() == R.id.searchFragment)
                            navController.navigate(R.id.searchFragment);
                        else if (item.getItemId() == R.id.homeFragment)
                            navController.navigate(R.id.homeFragment);
                    }
                    return true;
                });
            }

        });


    }

    private void showLoginDialog() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_action_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.color.md_theme_light_primaryContainer);
        TextView txt = dialog.findViewById(R.id.delete_txt);
        txt.setText("Please Login First!");
        Button login = dialog.findViewById(R.id.btnAction);
        login.setText("Login");
        Button cancel = dialog.findViewById(R.id.btnCancel);
        ImageButton close = dialog.findViewById(R.id.btnClose);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                navController.navigate(R.id.loginFragment);

//                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //internet connection
        if (!NetworkUtils.isInternetAvailable(this)) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "internet back", Toast.LENGTH_SHORT).show();
        }
    }
}