package nl.krijnschelvis.place2be.ui.authentication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import nl.krijnschelvis.place2be.R;
import nl.krijnschelvis.place2be.network.models.User;
import nl.krijnschelvis.place2be.network.repositories.UserRepository;
import nl.krijnschelvis.place2be.services.PasswordHasher;
import nl.krijnschelvis.place2be.ui.main.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends Activity {

    private EditText firstNameBox;
    private EditText lastNameBox;
    private EditText emailBox;
    private EditText passwordBox;
    private Button signUpButton;

    public SignUpActivity() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        firstNameBox = (EditText) findViewById(R.id.sign_up_first_name_box);
        lastNameBox = (EditText) findViewById(R.id.sign_up_last_name_box);
        emailBox = (EditText) findViewById(R.id.sign_up_email_box);
        passwordBox = (EditText) findViewById(R.id.sign_up_password_box);

        // Initialize signUpButton
        signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve data from text boxes
                String firstName = firstNameBox.getText().toString();
                String lastName = lastNameBox.getText().toString();
                String email = emailBox.getText().toString();
                String password = PasswordHasher.hash(passwordBox.getText().toString());

                // Build Retrofit
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.2.26:8080/authentication/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                // Create user repository
                UserRepository userRepository = retrofit.create(UserRepository.class);

                // Execute POST request
                Call<User> call = userRepository.registerUser(firstName, lastName, email, password);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        // Checks if user has been added to database
                        assert response.body() != null;
                        if (response.body().getEmail() == null) {
                            return;
                        }

                        // Opens shared preferences file "User"
                        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();

                        // Set access to true and email to inputted email
                        ed.putBoolean("access", true);
                        ed.putString("email", email);
                        ed.apply();

                        // Starts MainActivity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>> " + t.toString());
                    }
                });

            }
        });
    }
}
