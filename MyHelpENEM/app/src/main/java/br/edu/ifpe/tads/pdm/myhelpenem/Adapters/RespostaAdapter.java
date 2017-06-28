package br.edu.ifpe.tads.pdm.myhelpenem.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import br.edu.ifpe.tads.pdm.myhelpenem.Interfaces.RecycleViewOnClickListener;
import br.edu.ifpe.tads.pdm.myhelpenem.R;

public class RespostaAdapter extends RecyclerView.Adapter<RespostaAdapter.MyViewHolder> {

    private List<String> respostas;
    private LayoutInflater layoutInflater;
    private RecycleViewOnClickListener recycleViewOnClickListener;

    public RespostaAdapter(List<String> respostas, Context context) {
        this.respostas = respostas;
        this.layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_item_responderquestao, parent, false);
        MyViewHolder mvH = new MyViewHolder(view);
        return mvH;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String questao = "";
        if(position == 0){
            questao = "a";
        }else if(position == 1){
            questao = "b";
        }else if(position == 2){
            questao = "c";
        }else if(position == 3){
            questao = "d";
        }else if(position == 4){
            questao = "e";
        }

        holder.txtResposta.setText(questao+") "+respostas.get(position));
    }

    @Override
    public int getItemCount() {
        return respostas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtResposta;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtResposta = (TextView) itemView.findViewById(R.id.txtresposta);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (recycleViewOnClickListener != null) {
                recycleViewOnClickListener.onClickListener(v, getAdapterPosition());
            }
        }
    }

    public void setRecycleViewOnClickListener(RecycleViewOnClickListener r) {
        recycleViewOnClickListener = r;
    }


}
