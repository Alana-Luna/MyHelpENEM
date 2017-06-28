package br.edu.ifpe.tads.pdm.myhelpenem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
import br.edu.ifpe.tads.pdm.myhelpenem.Adapters.QuestoesAdapter;
import br.edu.ifpe.tads.pdm.myhelpenem.Interfaces.RecycleViewOnClickListener;
import br.edu.ifpe.tads.pdm.myhelpenem.model.EventMessage;
import br.edu.ifpe.tads.pdm.myhelpenem.model.Questao;

public class ShowQuestoes extends AppCompatActivity implements RecycleViewOnClickListener {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Questao> questaoList;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference;
    private FirebaseUser firebaseUser;
    private QuestoesAdapter questoesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Registro da Activity no EventBus
        EventBus.getDefault().register(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        questaoList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_show_questoes);
        recyclerView.setHasFixedSize(true);

        //LayoutManager para o RecyclerView
        linearLayoutManager = new LinearLayoutManager(this);
        //Layout Vertical
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        //Adapter para o RecyclerView
        questoesAdapter = new QuestoesAdapter(questaoList, this);
        //Listener do RecyclerView
        questoesAdapter.setRecycleViewOnClickListener(this);
        recyclerView.setAdapter(questoesAdapter);

        //Pega a referencia dos objetos do usuário no firebase
        userReference = firebaseDatabase.getReference("Usuários").child(firebaseUser.getUid());
        //Pega todos os objetos e armazena na lista
        userReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Questao q = dataSnapshot.getValue(Questao.class);
                questaoList.add(q);
                questoesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Questao q = dataSnapshot.getValue(Questao.class);
                questaoList.remove(q);
                questoesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClickListener(View v, int position) {
        Intent intent = new Intent(getApplicationContext(), CadastroDeQuestao.class);
        Bundle bundle = new Bundle();
        //Enviando o nome desta activity para identificar que é para criar uma nova questão
        bundle.putString("type", "" + ShowQuestoes.class);
        //Enviar tambem a key do item selecionado
        bundle.putString("key", questaoList.get(position).getKey());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Caso click na seta de voltar na ActionBar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    //Listener do EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage event) {
        //Verifica de onde vem a mensagem
        if (event.getType().equals("" + CadastroDeQuestao.class)) {
            //Verifica qual é a mensagem
            if (event.getMessage().equals("notifyDataSetChanged")) {
                //Recria a activity
                recreate();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Retira o registro da activity do EventBus
        EventBus.getDefault().unregister(this);
    }
}
