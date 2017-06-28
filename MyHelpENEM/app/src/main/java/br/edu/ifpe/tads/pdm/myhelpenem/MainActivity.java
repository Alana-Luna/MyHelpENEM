package br.edu.ifpe.tads.pdm.myhelpenem;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CardView cardNovoJogo;
    private CardView cardInstrucoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardNovoJogo = (CardView) findViewById(R.id.card_main_novojogo);
        cardInstrucoes = (CardView) findViewById(R.id.card_main_instrucoes);


        cardNovoJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(getApplicationContext(),LoginCadastroActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        cardInstrucoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.tela_de_instrucoes);
                dialog.show();
            }
        });


    }


}
