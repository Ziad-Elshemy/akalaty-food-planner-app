package eg.iti.mad.akalaty.ui;

import android.app.Dialog;
import android.content.Intent;
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
import androidx.navigation.NavOptions;
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
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.mealDetailsFragment ||
                    navDestination.getId() == R.id.loginFragment ||
                    navDestination.getId() == R.id.registerFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (!SharedPref.getInstance(this).getIsLogged()) {
                if (itemId == R.id.myFavFragment || itemId == R.id.calenderFragment || itemId == R.id.profileFragment) {
                    showLoginDialog();
                    return false;
                }
            }

            // Back Stack
            navController.navigate(itemId, null, new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.homeFragment, false)
                    .build());

            return true;
        });



    }

    private void showLoginDialog() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_action_layout);
        dialog.getWindow().setBackgroundDrawableResource(R.color.md_theme_light_primaryContainer);
        TextView txt = dialog.findViewById(R.id.delete_txt);
        txt.setText(R.string.please_login_first);
        Button login = dialog.findViewById(R.id.btnAction);
        login.setText(R.string.login);
        Button cancel = dialog.findViewById(R.id.btnCancel);
        ImageButton close = dialog.findViewById(R.id.btnClose);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

//                navController.navigate(R.id.loginFragment);
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                MainActivity.this.finish();

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
//            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "internet back", Toast.LENGTH_SHORT).show();
        }
    }
}