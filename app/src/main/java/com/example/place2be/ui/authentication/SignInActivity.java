package com.example.place2be.ui.authentication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.place2be.R;
import com.example.place2be.ui.main.MainActivity;

public class SignInActivity extends Activity {

    private Button signInButton;
    private TextView signUpText;

    private ImageView logoIcon;
    private TextView appName;
    private ConstraintLayout loginBoxShape;
    private LinearLayout loginBoxFunctionality;

    public SignInActivity() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_sign_in);

        signInButton = (Button) findViewById(R.id.sign_in_button);

        signUpText = (TextView) findViewById(R.id.sign_up_text);
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starts sign up activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        logoIcon = (ImageView) findViewById(R.id.logo_icon);
        appName = (TextView) findViewById(R.id.app_name);
        loginBoxShape = (ConstraintLayout) findViewById(R.id.login_box_shape);
        loginBoxFunctionality = (LinearLayout) findViewById(R.id.login_box_functionality);
    }

    @Override
    public void onBackPressed() {

    }
}