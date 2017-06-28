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
import br.edu.ifpe.tads.pdm.myhelpenem.model.Questao;


public class QuestoesAdapter extends RecyclerView.Adapter<QuestoesAdapter.MyViewHolder> {

    private List<Questao> questaoList;
    private LayoutInflater layoutInflater;
    private RecycleViewOnClickListener recycleViewOnClickListener;

    public QuestoesAdapter(List<Questao> questaoList, Context context) {
        this.questaoList = questaoList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_item_questao, parent, false);
        MyViewHolder mvH = new MyViewHolder(view);
        return mvH;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtCategoria.setText(questaoList.get(position).getCategoria());
        holder.txtPergunta.setText(questaoList.get(position).getPergunta());
    }

    @Override
    public int getItemCount() {
        return questaoList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtCategoria;
        private TextView txtPergunta;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtCategoria = (TextView) itemView.findViewById(R.id.txtcategoria);
            txtPergunta = (TextView) itemView.findViewById(R.id.txtpergunta);

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
