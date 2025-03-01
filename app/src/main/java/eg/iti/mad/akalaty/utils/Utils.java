package eg.iti.mad.akalaty.utils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import eg.iti.mad.akalaty.R;

public class Utils {

    public static void showCustomSnackbar(View parentView, String message) {
        Snackbar snackbar = Snackbar.make(parentView, "", Snackbar.LENGTH_LONG);

        @SuppressLint("RestrictedApi") Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarLayout.setBackgroundColor(Color.TRANSPARENT);

        LayoutInflater inflater = LayoutInflater.from(parentView.getContext());
        View customSnackbarView = inflater.inflate(R.layout.custom_snackbar, null);

        TextView textView = customSnackbarView.findViewById(R.id.snackbar_text);
        textView.setText(message);

        TextView actionButton = customSnackbarView.findViewById(R.id.snackbar_action);
        actionButton.setOnClickListener(v -> {
            snackbar.dismiss();
        });

        snackbarLayout.addView(customSnackbarView, 0);
        snackbar.show();
    }

}
