package com.cy.suotouM.note;

import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.cy.suotouM.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class headchose_bottom_helper extends BaseQuickAdapter {
    private List<headchose_bottom_note> data;
    public headchose_bottom_helper(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
        this.data=data;
    }

    public headchose_bottom_helper(int layoutResId) {
        super(layoutResId);
    }
    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, Object o) {
        Button button=baseViewHolder.itemView.findViewById(R.id.headchose_button);
        button.setText(data.get(baseViewHolder.getAdapterPosition()).getName());
        if (data.get(baseViewHolder.getAdapterPosition()).getisselect())
        {
            button.setAlpha(0.7f);
        }else
        {
            button.setAlpha(1f);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }
}
