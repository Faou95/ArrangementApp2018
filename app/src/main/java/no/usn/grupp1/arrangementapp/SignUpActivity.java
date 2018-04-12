package no.usn.grupp1.arrangementapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPasswordRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mName = findViewById(R.id.name_signup);
        mEmail = findViewById(R.id.email_signup);
        mPassword = findViewById(R.id.password_signup);
        mPasswordRepeat = findViewById(R.id.password_repeat_signup);

        Button mSignUpButton = findViewById(R.id.email_sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup(){

        if(!isValid()){
            onSignupFailed();
            return;
        }


        for (String credential : getResources().getStringArray(R.array.userInfo)) {
            String email = mEmail.getText().toString();
            String[] pieces = credential.split(":");
            if (pieces[0].equals(email)) {
                onSignupFailed();
            }
            else {
                onSignupSuccess();
            }
        }
    }

    public void onSignupFailed() {
        Toast toast = Toast.makeText(getApplicationContext(), R.string.error_signup_failed, Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onSignupSuccess() {
        // TODO registrere bruker i databasen
        // TODO logg inn brukeren etter registrering og gå tilbake til hovedside
        // TODO eller tilbake til sign in og brukeren og logge inn selv

        finish();
    }


    private boolean isValid(){

        boolean valid = true;
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String passwordRepeat = mPasswordRepeat.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mName.setError(getString(R.string.error_invalid_name));
            valid = false;
        } else {
            mName.setError(null);
        }

        if (!email.matches("[a-zA-Z0-9\\.]+@[a-zA-Z0-9\\-\\_\\.]+\\.[a-zA-Z0-9]{3}")){
            mEmail.setError(getString(R.string.error_invalid_email));
            valid = false;
        } else{
            mEmail.setError(null);
        }

        if(password.length() < 4){
            mPassword.setError(getString(R.string.error_invalid_password));
            valid = false;
        } else{
            mPassword.setError(null);
        }

        if(!password.equals(passwordRepeat)){
            mPasswordRepeat.setError(getString(R.string.error_password_nomatch));
            valid = false;
        } else{
            mPasswordRepeat.setError(null);
        }

        return valid;
    }
}
