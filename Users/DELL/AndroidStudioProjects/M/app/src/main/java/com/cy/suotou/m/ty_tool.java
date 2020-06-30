package com.cy.suotou.m;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import cn.forward.androids.ScaleGestureDetectorApi27;
import cn.hzw.doodle.DoodleActivity;
import cn.hzw.doodle.DoodleBitmap;
import cn.hzw.doodle.DoodleColor;
import cn.hzw.doodle.DoodleItemBase;
import cn.hzw.doodle.DoodleOnTouchGestureListener;
import cn.hzw.doodle.DoodlePaintAttrs;
import cn.hzw.doodle.DoodleParams;
import cn.hzw.doodle.DoodlePen;
import cn.hzw.doodle.DoodleText;
import cn.hzw.doodle.DoodleTouchDetector;
import cn.hzw.doodle.DoodleView;
import cn.hzw.doodle.IDoodleListener;
import cn.hzw.doodle.core.IDoodle;
import cn.hzw.doodle.core.IDoodleColor;
import cn.hzw.doodle.core.IDoodleItem;
import cn.hzw.doodle.core.IDoodleItemListener;
import cn.hzw.doodle.core.IDoodlePen;
import cn.hzw.doodle.core.IDoodleSelectableItem;
import cn.hzw.doodle.core.IDoodleTouchDetector;
import cn.hzw.doodle.dialog.DialogController;

import static android.content.ContentValues.TAG;


public class  ty_tool {
    private  ScaleItemOnTouchGestureListener doodleOnTouchGestureListener = null;
    private DoodleView doodleView;
    private static class ScaleItemOnTouchGestureListener extends DoodleOnTouchGestureListener {

        public ScaleItemOnTouchGestureListener(DoodleView doodle, ISelectionListener listener) {
            super(doodle, listener);
        }

        @Override
        public boolean onScale(ScaleGestureDetectorApi27 detector) {
            if (getSelectedItem() != null) {
                IDoodleItem item = getSelectedItem();
                item.setSize(item.getSize() * detector.getScaleFactor());
                return true;
            } else {
                return super.onScale(detector);
            }
        }
    }
        public  DoodleView ty_init(final Context context, final Activity activity, final Bitmap bitmap)
        {

            //初始化涂鸦
            boolean optimizeDrawing = true;
            doodleView =new DoodleView(context, bitmap, optimizeDrawing,new IDoodleListener() {
                @Override
                public void onSaved(IDoodle doodle, Bitmap doodleBitmap, Runnable callback) {

                }

                @Override
                public void onReady(IDoodle doodle) {
                   doodle.setDoodleScale(0.8f,doodleView.getWidth()/2,doodleView.getHeight()/2);
                    doodleView.refresh();
                }
            });
            doodleOnTouchGestureListener = new ScaleItemOnTouchGestureListener(doodleView, new DoodleOnTouchGestureListener.ISelectionListener() {
                @Override
                public void onSelectedItem(IDoodle doodle, IDoodleSelectableItem selectableItem, boolean selected) {
                  //  doodleOnTouchGestureListener.setSelectedItem(selectableItem);
                }

                @Override
                public void onCreateSelectableItem(final IDoodle doodle, final float x, final float y) {
                   if(doodle.getPen()==DoodlePen.TEXT)
                   {
                       DialogController.showInputTextDialog(activity, null,
                               new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       String text = (v.getTag() + "").trim();
                                       if (TextUtils.isEmpty(text)) {
                                           return;
                                       }
                                       IDoodleSelectableItem item = new DoodleText(doodle, text, 20*doodle.getUnitSize(), doodle.getColor().copy(), x, y);
                                       doodle.addItem(item);
                                       doodleOnTouchGestureListener.setSelectedItem(item);
                                       doodleView.setEditMode(true);
                                       doodle.refresh();
                                   }
                               }, null);
                   }else if(doodle.getPen()==DoodlePen.BITMAP)
                   {
                       Bitmap bitmap1=BitmapFactory.decodeResource(context.getResources(),R.drawable.cy1);
                         IDoodleSelectableItem item = new DoodleBitmap(doodle, bitmap1, 80 * doodle.getUnitSize(), x, y);
                       doodle.addItem(item);
                       doodleOnTouchGestureListener.setSelectedItem(item);
                        doodleView.setEditMode(true);
                       doodle.refresh();

                   }else if(doodle.getPen()==DoodlePen.MOSAIC)
                   {

                   }

                }
                });

            IDoodleTouchDetector detector = new DoodleTouchDetector(context, doodleOnTouchGestureListener);
            doodleView.setDefaultTouchDetector(detector);
            return doodleView;
        }


        private void  ty_save()
        {
            //涂鸦保存
        }

}
