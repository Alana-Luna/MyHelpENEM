package br.edu.ifpe.tads.pdm.myhelpenem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import br.edu.ifpe.tads.pdm.myhelpenem.Utils.InternetConnection;
import br.edu.ifpe.tads.pdm.myhelpenem.model.EventMessage;
import br.edu.ifpe.tads.pdm.myhelpenem.model.Questao;

public class CadastroDeQuestao extends Activity implements Validator.ValidationListener,View.OnFocusChangeListener {

    @NotEmpty(message = "Todos os campos são obrigatórios")
    private EditText editTextPergunta;
    @NotEmpty(message = "Todos os campos são obrigatórios")
    private EditText editTextResposta;
    @NotEmpty(message = "Todos os campos são obrigatórios")
    private EditText editTextAlternativa1;
    @NotEmpty(message = "Todos os campos são obrigatórios")
    private EditText editTextAlternativa2;
    @NotEmpty(message = "Todos os campos são obrigatórios")
    private EditText editTextAlternativa3;
    @NotEmpty(message = "Todos os campos são obrigatórios")
    private EditText editTextAlternativa4;

    private TextView txtEditarSalvar;

    private Bundle bundle;

    private Spinner mySpinner;
    private CardView cardSalvarQuestao;
    private CardView cardExcluirQuestao;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Validator validator;

    private DatabaseReference dbReference;
    private DatabaseReference userReference;
    private DatabaseReference ref;
    private FirebaseUser firebaseUser;

    private Questao questao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_questao);

        //Recebe as informações da activity que iniciou esta, ShowQuestoes ou OpcoesUsuarioActivity
        bundle = getIntent().getExtras();

        editTextPergunta = (EditText) findViewById(R.id.editTextPergunta);
        editTextResposta = (EditText) findViewById(R.id.editTextResposta);
        editTextAlternativa1 = (EditText) findViewById(R.id.editTextAlternativa1);
        editTextAlternativa2 = (EditText) findViewById(R.id.editTextAlternativa2);
        editTextAlternativa3 = (EditText) findViewById(R.id.editTextAlternativa3);
        editTextAlternativa4 = (EditText) findViewById(R.id.editTextAlternativa4);
        txtEditarSalvar = (TextView) findViewById(R.id.txteditar_salvar);
        cardSalvarQuestao = (CardView) findViewById(R.id.card_nova_questao);
        cardExcluirQuestao = (CardView) findViewById(R.id.card_excluir_questao);

        //SetRequestFocusListener
        editTextPergunta.setOnFocusChangeListener(this);
        editTextResposta.setOnFocusChangeListener(this);
        editTextAlternativa1.setOnFocusChangeListener(this);
        editTextAlternativa2.setOnFocusChangeListener(this);
        editTextAlternativa3.setOnFocusChangeListener(this);
        editTextAlternativa4.setOnFocusChangeListener(this);

        mySpinner = (Spinner) findViewById(R.id.spinnerDisciplina);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipenovaquestao);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.swipecolor));

        //Registro da activity no EventBus
        /*O EventBus serve para enviar mensagens para outras activitys ou fragments, o evento é enviado em forma
        de objetos, neste caso o EventMessage*/
        EventBus.getDefault().register(this);

        //Firebase - Padrão
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Referencia para o nó Questões
        dbReference = firebaseDatabase.getReference("Questões");

        //Validaçao dos campos
        validator = new Validator(this);
        validator.setValidationListener(this);

        final ArrayAdapter<CharSequence> adapterDisciplina = ArrayAdapter.createFromResource(getApplicationContext(), R.array.disciplinas, R.layout.spinner_item);
        mySpinner.setAdapter(adapterDisciplina);

        //Referencia para o nó Usuários e uma chave com o id do usuario do firebase
        userReference = firebaseDatabase.getReference("Usuários").child(firebaseUser.getUid());

        /*Verifica se é para mostrar as questoes ou para criar uma nova, esta informaçao vem da activity
        OpcoesUsuarioActivity e da ShowQuestoes atraves do bundle*/
        if (bundle.getString("type").equals("" + ShowQuestoes.class)) {

            //Caso venha da ShowQuestoes, irá buscar os dados do firebase

            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setRefreshing(true);
            //Muda o texto do botao salvar para editar
            txtEditarSalvar.setText("Editar");
            //Mostra o botao de exclusao
            cardExcluirQuestao.setVisibility(View.VISIBLE);
            //Bloqueio os campos
            lockFields();

            userReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Questao q = dataSnapshot.getValue(Questao.class);
                    /*Pega apenas o objeto que tenha a key que foi recebida da activity ShowQuestoes
                    e mostra nos campos*/
                    if (q.getKey().equals(bundle.getString("key"))) {
                        questao = q;
                        editTextPergunta.setText(questao.getPergunta());
                        editTextResposta.setText(questao.getResposta());
                        editTextAlternativa1.setText(questao.getAlternativa1());
                        editTextAlternativa2.setText(questao.getAlternativa2());
                        editTextAlternativa3.setText(questao.getAlternativa3());
                        editTextAlternativa4.setText(questao.getAlternativa4());
                        mySpinner.setSelection(adapterDisciplina.getPosition(questao.getCategoria()));

                        swipeRefreshLayout.setEnabled(false);
                        swipeRefreshLayout.setRefreshing(false);
                        cardSalvarQuestao.setEnabled(true);
                    }

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


        } else {
            swipeRefreshLayout.setEnabled(false);
            swipeRefreshLayout.setRefreshing(false);

        }


        cardSalvarQuestao.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //Verifica se é para editar ou para salvar
                if (txtEditarSalvar.getText().equals("Editar")) {
                    txtEditarSalvar.setText("Salvar");
                    editTextPergunta.requestFocus();
                    //Habilita os campos para ediçao
                    unlockFields();
                    //Spinner continua bloqueado
                    mySpinner.setEnabled(false);
                } else {
                    //Caso for para salvar, irá iniciar a validaçao dos campos para salvar no firebase
                    //Verifica a internet antes de salvar
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
            }
        });

        cardExcluirQuestao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Verifica a internet antes de excluir
                if (InternetConnection.CheckConnection(v.getContext())) {
                    //Bloqueia os campos
                    lockFields();

                    String ctg = "";

                    //Se adicionar mais categorias aqui, nao esquecer de adicionar no menu principal o icone respectivo
                    if (questao.getCategoria().equals("História") || questao.getCategoria().equals("Geografia") || questao.getCategoria().equals("Filosofia")) {
                        ctg = "Humanas";
                    } else if (questao.getCategoria().equals("Física") || questao.getCategoria().equals("Química") || questao.getCategoria().equals("Biologia")) {
                        ctg = "Natureza";
                    } else if (questao.getCategoria().equals("Matemática")) {
                        ctg = "Matemática";
                    } else if (questao.getCategoria().equals("Português") || questao.getCategoria().equals("Espanhol") || questao.getCategoria().equals("Inglês")) {
                        ctg = "Linguagem";
                    }

                    //Pega a referencia da questão atraves da key
                    ref = dbReference.child(ctg).child(questao.getKey());
                    //remove a questao do Nó Questões
                    ref.removeValue();

                    //Pega a referencia do Usuario e a sua questão atraves da key
                    ref = userReference.child(questao.getKey());
                    //remove a questao do Nó Usuários
                    ref.removeValue();

                    //Envia uma mensagem Através do EventBus
                    EventMessage msg = new EventMessage();
                    msg.setType("" + CadastroDeQuestao.class);
                    msg.setMessage("notifyDataSetChanged");
                    EventBus.getDefault().postSticky(msg);

                    Toast.makeText(getApplicationContext(), "Questão excluída com sucesso!", Toast.LENGTH_SHORT).show();

                    //Fecha a activity após 2 segundos
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
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
    public void onValidationSucceeded() {

        //Se a validação estiver correta

        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(true);

        //Bloqueia os campos
        lockFields();

        Questao q = new Questao();
        q.setPergunta(editTextPergunta.getText().toString());
        q.setResposta(editTextResposta.getText().toString());
        q.setAlternativa1(editTextAlternativa1.getText().toString());
        q.setAlternativa2(editTextAlternativa2.getText().toString());
        q.setAlternativa3(editTextAlternativa3.getText().toString());
        q.setAlternativa4(editTextAlternativa4.getText().toString());
        q.setCategoria(mySpinner.getSelectedItem().toString());

        String ctg = "";

        if (q.getCategoria().equals("História") || q.getCategoria().equals("Geografia") || q.getCategoria().equals("Filosofia")) {
            ctg = "Humanas";
        } else if (q.getCategoria().equals("Física") || q.getCategoria().equals("Química") || q.getCategoria().equals("Biologia")) {
            ctg = "Natureza";
        } else if (q.getCategoria().equals("Matemática")) {
            ctg = "Matemática";
        } else if (q.getCategoria().equals("Português") || q.getCategoria().equals("Espanhol") || q.getCategoria().equals("Inglês")) {
            ctg = "Linguagem";
        }

        //Verifica novamente se veio de ShowQuestoes
        if (bundle.getString("type").equals("" + ShowQuestoes.class)) {
            //Se sim, já existe uma referencia ao objeto no firebase, entao apenas será editado
            //Pega a referencia do objeto ja existente no Nó Questões/Categoria
            ref = dbReference.child(ctg).child(questao.getKey());
            //Adiciona a key no novo objeto criado para ediçao
            q.setKey(questao.getKey());
            //Atualiza o objeto no firebase
            ref.setValue(q);

            //Pega a referencia do objeto no nó Usuários para adicionar o listener
            ref = userReference.child(q.getKey());
        } else {
            //Se nao, vem da OpcoesUsuarioActivity, entao precisa criar um novo objeto
            //Pega a referencia de um novo objeto em Questões/Categoria atraves do push
            ref = dbReference.child(ctg).push();
            //A referencia contem uma key, adiciona a key no objeto
            q.setKey(ref.getKey());
            //Envia o objeto
            ref.setValue(q);

            //Pega a referencia do objeto que será criado no nó Usuário para adionar o listener
            ref = userReference.child(q.getKey());
        }

        //Listener do usuario, tanto para salvar quanto para editar
        ref.setValue(q).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setEnabled(false);
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getApplicationContext(), "Questão salva com sucesso! ", Toast.LENGTH_LONG).show();

                            //Verifica novamente de onde vem
                            if (bundle.getString("type").equals("" + ShowQuestoes.class)) {
                                lockFields();
                                cardExcluirQuestao.setEnabled(true);
                                cardSalvarQuestao.setEnabled(true);
                                txtEditarSalvar.setText("Editar");
                                //Envia uma mensagem Através do EventBus
                                EventMessage msg = new EventMessage();
                                msg.setType("" + CadastroDeQuestao.class);
                                msg.setMessage("notifyDataSetChanged");
                                EventBus.getDefault().postSticky(msg);

                            } else {
                                clearFields();
                                unlockFields();
                            }
                        }
                    }, 2000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            //Verifica novamente de onde vem
                            if (bundle.getString("type").equals("" + ShowQuestoes.class)) {
                                cardExcluirQuestao.setEnabled(true);
                                cardSalvarQuestao.setEnabled(true);
                                txtEditarSalvar.setText("Editar");
                                lockFields();
                            } else {
                                unlockFields();
                            }

                            swipeRefreshLayout.setEnabled(false);
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getApplicationContext(), "Não foi possível salvar a questão", Toast.LENGTH_LONG).show();
                        }
                    }, 2000);
                }
            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        Toast.makeText(getApplicationContext(), "" + errors.get(0).getCollatedErrorMessage(this), Toast.LENGTH_LONG).show();
    }


    public void clearFields() {
        editTextPergunta.setText("");
        editTextResposta.setText("");
        editTextAlternativa1.setText("");
        editTextAlternativa2.setText("");
        editTextAlternativa3.setText("");
        editTextAlternativa4.setText("");
        mySpinner.setSelection(0);
    }

    public void lockFields() {
        mySpinner.setEnabled(false);
        editTextPergunta.setEnabled(false);
        editTextResposta.setEnabled(false);
        editTextAlternativa1.setEnabled(false);
        editTextAlternativa2.setEnabled(false);
        editTextAlternativa3.setEnabled(false);
        editTextAlternativa4.setEnabled(false);
        cardSalvarQuestao.setEnabled(false);
    }

    public void unlockFields() {
        mySpinner.setEnabled(true);
        editTextPergunta.setEnabled(true);
        editTextResposta.setEnabled(true);
        editTextAlternativa1.setEnabled(true);
        editTextAlternativa2.setEnabled(true);
        editTextAlternativa3.setEnabled(true);
        editTextAlternativa4.setEnabled(true);
        cardSalvarQuestao.setEnabled(true);
    }

    //Nao utilizado nesta activity, mas nao deixa de receber as mensagens quando enviadas
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage event) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Remove o resgistro do EventBus
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            if (v.getId() == editTextPergunta.getId()){
                editTextPergunta.setSelection(editTextPergunta.getText().length());
            }else if (v.getId() == editTextResposta.getId()){
                editTextResposta.setSelection(editTextResposta.getText().length());
            }else if (v.getId() == editTextAlternativa1.getId()){
                editTextAlternativa1.setSelection(editTextAlternativa1.getText().length());
            }else if (v.getId() == editTextAlternativa2.getId()){
                editTextAlternativa2.setSelection(editTextAlternativa2.getText().length());
            }else if (v.getId() == editTextAlternativa3.getId()){
                editTextAlternativa3.setSelection(editTextAlternativa3.getText().length());
            }else if (v.getId() == editTextAlternativa4.getId()){
                editTextAlternativa4.setSelection(editTextAlternativa4.getText().length());
            }
        }
    }
}

