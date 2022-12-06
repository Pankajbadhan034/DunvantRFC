package com.dunvant.application;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Chapter> chapters;
    private LayoutInflater inflater;
    Typeface helvetica;
    Typeface linoType;
    String childName;

    public ChapterAdapter(Context context, ArrayList<Chapter> chapters, String childName) {
        this.context = context;
        this.chapters = chapters;
        this.inflater = LayoutInflater.from(context);
        this.childName = childName;
        helvetica = Typeface.createFromAsset(context.getAssets(),"fonts/HelveticaNeue.ttf");
        linoType = Typeface.createFromAsset(context.getAssets(),"fonts/LinotypeOrdinarRegular.ttf");

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.single_chapter, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final Chapter chapter = chapters.get(position);

        int i = chapter.id+1;
        holder.srNo.setText(""+i);

        if(chapter.status.equalsIgnoreCase("1")){
            holder.tvChapterName.setText(chapter.chapterName);
            holder.tvChapterName.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
        }else if(chapter.status.equalsIgnoreCase("0")){
            holder.tvChapterName.setText(chapter.chapterName);
            holder.tvChapterName.setBackgroundColor(context.getResources().getColor(R.color.lightGrey));
        }else{
            holder.tvChapterName.setText(chapter.chapterName);
            holder.tvChapterName.setBackgroundColor(context.getResources().getColor(R.color.red_trans));
        }

        holder.tvChapterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "DATE::"+chapter.chapterName+" STATUS::"+chapter.status+" CHILD_NAME::"+childName, Toast.LENGTH_SHORT).show();

            }
        });

        holder.srNo.setTypeface(helvetica);
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivChapter;
        public TextView tvChapterName;
        public TextView srNo;

        public CustomViewHolder(View itemView) {
            super(itemView);
            srNo = (TextView) itemView.findViewById(R.id.srNo);
            tvChapterName = (TextView) itemView.findViewById(R.id.tvChapterName);
            ivChapter = (ImageView) itemView.findViewById(R.id.ivChapter);


        }
    }
}