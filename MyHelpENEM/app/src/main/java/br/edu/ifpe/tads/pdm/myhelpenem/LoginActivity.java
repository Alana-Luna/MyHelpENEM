package br.edu.ifpe.tads.pdm.myhelpenem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import br.edu.ifpe.tads.pdm.myhelpenem.Utils.InternetConnection;

public class LoginActivity extends Activity implements Validator.ValidationListener {

    private CardView cardLogin;
    @NotEmpty
    @Email
    private EditText inputEmail;
    @NotEmpty
    @Password(scheme = Password.Scheme.ANY, message = "A senha deve conter 6 ou mais caracteres")
    private EditText inputPassword;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Validator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cardLogin = (CardView) findViewById(R.id.login_logar);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_senha);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipelogin);

        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipecolor));

        cardLogin.requestFocus();

        //Validação dos campos
        validator = new Validator(this);
        validator.setValidationListener(this);

        cardLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verifica a internet antes de realizar o login
                if (InternetConnection.CheckConnection(v.getContext())) {
                    validator.validate();
                } else {
                    Snackbar.make(v, "Sem internet!", Snackbar.LENGTH_INDEFINITE).setAction("Configurações", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(i);
                        }
                    }).setActionTextColor(getResources().getColor(android.R.color.holo_orange_dark)).show();
                }
            }

        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), LoginCadastroActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onValidationSucceeded() {

        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);
        lockFields();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(inputEmail.getText().toString(), inputPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Logado com sucesso!", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(getApplicationContext(), OpcoesUsuarioActivity.class);
                    startActivity(myIntent);
                } else {
                    clearFields();
                    Toast.makeText(getApplicationContext(), "Email ou senha incorretos!", Toast.LENGTH_LONG).show();
                }
                unlockFields();
                swipeRefreshLayout.setEnabled(false);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Toast.makeText(getApplicationContext(), "" + errors.get(0).getCollatedErrorMessage(this), Toast.LENGTH_LONG).show();
    }

    public void clearFields() {
        inputEmail.setText("");
        inputPassword.setText("");
        inputEmail.requestFocus();
    }

    public void lockFields() {
        inputEmail.setEnabled(false);
        inputPassword.setEnabled(false);
        cardLogin.setEnabled(false);
    }

    public void unlockFields() {
        inputEmail.setEnabled(true);
        inputPassword.setEnabled(true);
        cardLogin.setEnabled(true);
    }


}


