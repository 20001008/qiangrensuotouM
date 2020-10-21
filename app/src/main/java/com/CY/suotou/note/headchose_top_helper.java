package com.CY.suotou.note;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.CY.suotou.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class headchose_top_helper extends BaseQuickAdapter {
    private List<headchose_top_note> data;
    public headchose_top_helper(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
        this.data=data;
    }

    public headchose_top_helper(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Object o) {
        ImageView imageView=baseViewHolder.itemView.findViewById(R.id.headchose_top_image);
        Drawable drawable=data.get(baseViewHolder.getAdapterPosition()).getImage();
        if (drawable!=null)
        {
            Glide.with(getContext()).load(drawable).into(imageView);
        }
       int width= getContext().getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        //设置图片的相对于屏幕的宽高比
        params.width = width/5/2;
        params.height = width/5/2 ;
        //imageView.setLayoutParams(params);



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
}
