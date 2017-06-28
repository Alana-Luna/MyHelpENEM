package br.edu.ifpe.tads.pdm.myhelpenem;

import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import br.edu.ifpe.tads.pdm.myhelpenem.Utils.InternetConnection;

public class OpcoesUsuarioActivity extends AppCompatActivity {

    private CardView cardNovaQuestao;
    private CardView cardMinhasQuestoes;
    private CardView cardResponderQuestoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes_usuario);

        cardNovaQuestao = (CardView) findViewById(R.id.card_opcoes_novaquestao);
        cardMinhasQuestoes = (CardView) findViewById(R.id.card_opcoes_minhasquestoes);
        cardResponderQuestoes = (CardView) findViewById(R.id.card_opcoes_responderquestoes);

        cardNovaQuestao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastroDeQuestao.class);
                //Enviar informaçoes para a activity CadastroDeQuestao
                Bundle bundle = new Bundle();
                //Enviando o nome desta activity para identificar que é para criar uma nova questão
                bundle.putString("type", "" + OpcoesUsuarioActivity.class);
                //Adiciona o bundle na intent
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        cardMinhasQuestoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.CheckConnection(v.getContext())) {
                    Intent intent = new Intent(getApplicationContext(), ShowQuestoes.class);
                    startActivity(intent);
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

        cardResponderQuestoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.CheckConnection(v.getContext())) {
                    Intent intent = new Intent(getApplicationContext(), MenuPrincipalActivity.class);
                    startActivity(intent);
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
        /*Coloca o app em modo de espera, o deixa na pilha de aplicações,
        pra sair de vez da aplicaçao é só substituir o this.moveTaskToBack()
        para super.onBackPressed() ou remover o metodo, que já é padrão*/
        this.moveTaskToBack(true);
    }


}

