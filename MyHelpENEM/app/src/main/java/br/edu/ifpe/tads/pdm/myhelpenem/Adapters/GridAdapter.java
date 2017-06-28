package br.edu.ifpe.tads.pdm.myhelpenem.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.edu.ifpe.tads.pdm.myhelpenem.R;
import br.edu.ifpe.tads.pdm.myhelpenem.model.GridItem;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private List<GridItem> gridItemList;

    public GridAdapter(Context context, List<GridItem> gridItemList) {
        this.context = context;
        this.gridItemList = gridItemList;
    }

    @Override
    public int getCount() {
        return gridItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Criaçao dos itens do gridview
        CardView cardView = new CardView(context);
        cardView.setRadius(10);
        cardView.setCardElevation(5);
        cardView.setMaxCardElevation(5);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);

        ImageView iv = new ImageView(context);
        iv.setAdjustViewBounds(true);
        iv.setImageDrawable(gridItemList.get(position).getDrawableId());

        TextView textView = new TextView(context);
        textView.setText(gridItemList.get(position).getName());
        textView.setTextColor(context.getResources().getColor(R.color.card_text_color));
        textView.setGravity(Gravity.CENTER);

        layout.addView(iv);
        layout.addView(textView);

        cardView.addView(layout);

        return cardView;
    }
}
