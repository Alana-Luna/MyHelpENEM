package br.edu.ifpe.tads.pdm.myhelpenem;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifpe.tads.pdm.myhelpenem.Adapters.GridAdapter;
import br.edu.ifpe.tads.pdm.myhelpenem.Utils.InternetConnection;
import br.edu.ifpe.tads.pdm.myhelpenem.model.GridItem;


public class MenuPrincipalActivity extends AppCompatActivity{

    private int[] drawables;
    private String[] names;
    private List<GridItem> gridItemList;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        gridView = (GridView)findViewById(R.id.griditens);

        gridItemList = new ArrayList<>();

        //Pode adicionar mais itens aqui
        drawables = new int[]{R.drawable.grupohumanas,R.drawable.gruponatureza,R.drawable.grupomatematica,R.drawable.grupolinguagem};
        //Se adicionar, não esquecer de colocar o nome
        names = new String[]{"Humanas","Natureza","Matemática","Linguagem"};


        //Cria os drawables para adicionar no grid, criei um objeto para armazenar os dados em uma lista
        for (int i=0;i<drawables.length;i++){
            Drawable d = getResources().getDrawable(drawables[i]);
            GridItem gI = new GridItem();
            gI.setDrawableId(d);
            gI.setName(names[i]);
            gridItemList.add(gI);
        }

        //Cria o adapter para o GridView
        GridAdapter gridAdapter = new GridAdapter(this,gridItemList);
        //Seta o adapter no GridView
        gridView.setAdapter(gridAdapter);

        //Listener do GridView
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (InternetConnection.CheckConnection(view.getContext())) {
                    Intent intent = new Intent(getApplicationContext(),ResponderQuestoes.class);
                    Bundle bundle = new Bundle();
                    //Adiciona o nome do item clicado no GridView ao bundle
                    bundle.putString("type",gridItemList.get(position).getName());
                    //Adiciona o bundle na intent
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "Sem internet!", Snackbar.LENGTH_INDEFINITE).setAction("Configurações", new View.OnClickListener() {
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



}
