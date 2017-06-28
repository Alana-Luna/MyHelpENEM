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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import br.edu.ifpe.tads.pdm.myhelpenem.Utils.InternetConnection;

public class CadastroActivity extends Activity implements Validator.ValidationListener {

    @NotEmpty(message = "Todos os campos são obrigatórios")
    private EditText editTextUserName;
    @NotEmpty(message = "Todos os campos são obrigatórios")
    @Email(message = "Email inválido")
    private EditText editTextUserEmail;
    @NotEmpty(message = "Todos os campos são obrigatórios")
    @Password(scheme = Password.Scheme.ANY, message = "A senha deve conter 6 ou mais caracteres")
    private EditText editTextPassword;
    @NotEmpty(message = "Todos os campos são obrigatórios")
    @ConfirmPassword(message = "As senhas não conferem")
    private EditText editTextConfirmPassword;
    private CardView cardCreateAccount;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Validator validator;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextUserEmail = (EditText) findViewById(R.id.editTextUserEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        cardCreateAccount = (CardView) findViewById(R.id.cadastro_cadastrar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipecadastro);

        editTextUserName.requestFocus();
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipecolor));

        //Validaçao dos campos
        validator = new Validator(this);
        validator.setValidationListener(this);

        mAuth = FirebaseAuth.getInstance();

        cardCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verifica a internet antes de realizar o cadastro
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

        String userEmail = editTextUserEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);
        lockFields();

        mAuth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Cadastro Realizado com sucesso!", Toast.LENGTH_LONG).show();
                            clearFields();
                        } else {
                            Toast.makeText(getApplicationContext(), "Não foi possível realizar o cadastro!", Toast.LENGTH_LONG).show();
                        }
                        swipeRefreshLayout.setEnabled(false);
                        swipeRefreshLayout.setRefreshing(false);
                        unlockFields();
                    }
                });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Toast.makeText(getApplicationContext(), "" + errors.get(0).getCollatedErrorMessage(this), Toast.LENGTH_LONG).show();
    }

    public void clearFields() {
        editTextUserName.setText("");
        editTextUserEmail.setText("");
        editTextPassword.setText("");
        editTextConfirmPassword.setText("");
        editTextUserName.requestFocus();
    }

    public void lockFields() {
        editTextUserName.setEnabled(false);
        editTextUserEmail.setEnabled(false);
        editTextPassword.setEnabled(false);
        editTextConfirmPassword.setEnabled(false);
        cardCreateAccount.setEnabled(false);
    }

    public void unlockFields() {
        editTextUserName.setEnabled(true);
        editTextUserEmail.setEnabled(true);
        editTextPassword.setEnabled(true);
        editTextConfirmPassword.setEnabled(true);
        cardCreateAccount.setEnabled(true);
    }
}



