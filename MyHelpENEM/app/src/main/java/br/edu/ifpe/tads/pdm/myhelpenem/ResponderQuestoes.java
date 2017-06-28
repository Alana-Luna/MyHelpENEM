package br.edu.ifpe.tads.pdm.myhelpenem;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import br.edu.ifpe.tads.pdm.myhelpenem.Adapters.RespostaAdapter;
import br.edu.ifpe.tads.pdm.myhelpenem.Interfaces.RecycleViewOnClickListener;
import br.edu.ifpe.tads.pdm.myhelpenem.model.Questao;

public class ResponderQuestoes extends AppCompatActivity implements RecycleViewOnClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    private List<Questao> questaoList;
    private List<String> stringList;

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;
    private TextView txtCategoria;
    private TextView txtPergunta;

    //RecyclerView para as opcoes de resposta
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RespostaAdapter respostaAdapter;

    //Variavel para armazenar a posicao da questao que o usuario esta
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_questoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inicializacao das listas e do count
        questaoList = new ArrayList<>();
        stringList = new ArrayList<>();
        count = 0;

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperesponderquestoes);
        linearLayout = (LinearLayout) findViewById(R.id.layout_responder_questoes);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_responder_questoes);
        txtCategoria = (TextView) findViewById(R.id.txtcategoria);
        txtPergunta = (TextView) findViewById(R.id.txtpergunta);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipecolor));

        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);
        recyclerView.setHasFixedSize(true);

        //LayoutManager para o RecyclerView
        linearLayoutManager = new LinearLayoutManager(this);
        //Layout na vertical
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //Adapter para o RecyclerView, envia-se a lista com as opcoes de resposta
        respostaAdapter = new RespostaAdapter(stringList, this);
        //Adiciona o listener no adapter
        respostaAdapter.setRecycleViewOnClickListener(this);
        //Adiciona o LayoutManager e o Adapter no RecyclerView
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(respostaAdapter);

        //Adiciona a seta de voltar na ActionBar da activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        //Pega os dados vindo da MenuPrincipalActivity
        Bundle bundle = getIntent().getExtras();

        //Inicializa a lista de questoes
        questaoList = new ArrayList<>();

        //Verifica se veio o nome do item escolhido na MenuPrincipalActivity
        if (bundle.getString("type", null) != null) {

            //Adiciona o nome do item escolhido no título da ActionBar
            getSupportActionBar().setTitle(bundle.getString("type"));

            //Pega a referencia para o nó Questões/tipo do item, ou nome da categoria escolhida
            reference = firebaseDatabase.getReference().child("Questões").child(bundle.getString("type"));

            //Pega todos os objetos no firebase e adiciona na lista de questoes
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Questao q = dataSnapshot.getValue(Questao.class);
                    questaoList.add(q);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Será executado após 5 seguntos

                /*Verifica se a lista foi preenchida, pode ocorrer de não dar tempo de buscar os dados no firebase
                velocidade da internet pode ser um fator*/
                if (questaoList.size() != 0) {

                    //Embaralha a lista de questões
                    Collections.shuffle(questaoList);

                    //Adiciona nos campos a questão de posição 0 da lista que ja foi embaralhada
                    txtCategoria.setText(questaoList.get(count).getCategoria());
                    txtPergunta.setText(questaoList.get(count).getPergunta());
                    //Adiciona na lista de String a resposta e as opcoes erradas
                    stringList.add(questaoList.get(count).getResposta());
                    stringList.add(questaoList.get(count).getAlternativa1());
                    stringList.add(questaoList.get(count).getAlternativa2());
                    stringList.add(questaoList.get(count).getAlternativa3());
                    stringList.add(questaoList.get(count).getAlternativa4());

                    //Embaralha a lista de String
                    Collections.shuffle(stringList);
                    //O Adapter já esta com a lista, como foi embaralhada apenas o notifica da mudança
                    respostaAdapter.notifyDataSetChanged();

                    //Mostra o layout completo após todos os dados estarem carregados
                    linearLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setEnabled(false);
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    //Se não der tempo de carregar, Toast
                    Toast.makeText(getApplicationContext(), "Não foi possível carregar as perguntas, tente novamente!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }, 5000);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onClickListener(View v, int position) {

        //Listener do Recycler, quando for seleciona a resposta

        //Bloqueia o layout
        linearLayout.setEnabled(false);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);

        /*Verifica se a resposta é correta, pega a posicao da lista que foi clicada e compara com a resposta
        correta da questao*/
        if (stringList.get(position).equals(questaoList.get(count).getResposta())) {
            Toast.makeText(v.getContext(), "Resposta correta", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(v.getContext(), "Resposta incorreta, resposta certa: " + questaoList.get(count).getResposta(), Toast.LENGTH_SHORT).show();
        }

        //Incrementa o count
        count++;
        //Zera a lista de String com as opcoes de respostas
        stringList = new ArrayList<>();

        //Verifica se o count e menor que a quantidade de questoes disponíveis
        if (count < questaoList.size()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Carrega todas as informaçoes da proxima questao, ja armazenada na lista de questoes
                    txtCategoria.setText(questaoList.get(count).getCategoria());
                    txtPergunta.setText(questaoList.get(count).getPergunta());
                    stringList.add(questaoList.get(count).getResposta());
                    stringList.add(questaoList.get(count).getAlternativa1());
                    stringList.add(questaoList.get(count).getAlternativa2());
                    stringList.add(questaoList.get(count).getAlternativa3());
                    stringList.add(questaoList.get(count).getAlternativa4());

                    //Embaralha a lista de opcoes de resposta
                    Collections.shuffle(stringList);
                    //Cria-se novamente o adapter
                    respostaAdapter = new RespostaAdapter(stringList, ResponderQuestoes.this);
                    //Adiciona novamente o listener
                    respostaAdapter.setRecycleViewOnClickListener(ResponderQuestoes.this);
                    //Seta o adapter no RecyclerView
                    recyclerView.setAdapter(respostaAdapter);

                    //Habilita o layout
                    linearLayout.setEnabled(true);
                    swipeRefreshLayout.setEnabled(false);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 3000);
        } else {

            //Se não houver mais questoes, Toast e finaliza a activity após 5 segundos

            swipeRefreshLayout.setEnabled(false);
            swipeRefreshLayout.setRefreshing(false);

            Toast.makeText(v.getContext(), "Não há mais questões para responder", Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            },5000);
        }

    }
}
