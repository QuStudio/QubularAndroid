package com.qubular.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qubular.DescriptionActivity;
import com.qubular.MainActivity;
import com.qubular.R;
import com.qubular.networking.RequestController;
import com.qubular.util.DataUtils;
import com.qubular.util.DividerItemDecoration;

import java.util.List;

import General.Entry;

/**
 * Created by sentosh1ne on 20.05.2016.
 */
public class WordSearchAdapter extends RecyclerView.Adapter<WordSearchAdapter.ViewHolder>{

    List<Entry> entries;
    int itemLayout;
    Context context;
    int lastPosition = -1;

    public WordSearchAdapter(List<Entry> entries, int itemLayout) {
        this.entries = entries;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(itemLayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // TODO creation logic and on touch event + change layout itself
        final Entry entry = entries.get(position);

        int color = DataUtils.getUiColor(entry);

        holder.circle.setBackgroundColor(color);

        holder.wordText.setText(DataUtils.capitalizeFirstLetter(entries.get(position).foreign.lemma.getString()));

        holder.equivalents.setText(DataUtils.getFormsString(entry));

        holder.equivalents.setTypeface(MainActivity.typefaceItalic);

        holder.wordText.setTypeface(MainActivity.typefaceNormal);
        setAnimation(holder.container, position);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DescriptionActivity.class);

                intent.putExtra("entry",RequestController.gson.toJson(entry));
                context.startActivity(intent);
            }
        });

    }

    public static void setupWordRecycler(final RecyclerView recyclerView,List<Entry> entries,int layout,Context ctx){
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new WordSearchAdapter(entries, layout));
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(ctx,R.drawable.divider));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView wordText;
        TextView equivalents;
        LinearLayout container;
        View circle;
        public ViewHolder(View itemView) {
            super(itemView);
            wordText = (TextView) itemView.findViewById(R.id.wordCard);
            equivalents = (TextView) itemView.findViewById(R.id.equivs);
            container = (LinearLayout) itemView.findViewById(R.id.card_layout);
            circle = itemView.findViewById(R.id.circle);
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
