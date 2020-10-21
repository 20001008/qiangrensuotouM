package com.CY.suotou;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.CY.suotou.R;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.utils.bitmap.SaveBitmapCallBack;

import java.io.File;
import java.io.IOException;

import cn.forward.androids.ScaleGestureDetectorApi27;
import cn.hzw.doodle.DoodleOnTouchGestureListener;
import cn.hzw.doodle.DoodlePen;
import cn.hzw.doodle.DoodleText;
import cn.hzw.doodle.DoodleTouchDetector;
import cn.hzw.doodle.DoodleView;
import cn.hzw.doodle.IDoodleListener;
import cn.hzw.doodle.core.IDoodle;
import cn.hzw.doodle.core.IDoodleItem;
import cn.hzw.doodle.core.IDoodleSelectableItem;
import cn.hzw.doodle.core.IDoodleTouchDetector;
import cn.hzw.doodle.dialog.DialogController;

public class  ty_tool {
    public static String last_path=null;
    private  ScaleItemOnTouchGestureListener doodleOnTouchGestureListener = null;
    private DoodleView doodleView;
    public interface onEnvent
    {
        void onIFSelect();
        void onIFenselect();
    }
    private onEnvent Envent;
    public static int loadbitmap= R.drawable.xw;

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
                public void onSaved(final IDoodle doodle, final Bitmap doodleBitmap, Runnable callback) {

                  EasyPhotos.saveBitmapToDir(activity, getStoragePath("Picture/suotouM/"), "suotou", doodleBitmap, true, new SaveBitmapCallBack() {
                        @Override
                        public void onSuccess(File file) {
                          //  Log.d("s", "onSuccess: "+file.getAbsolutePath());
                            ty_tool.last_path=file.getAbsolutePath();
                            Toast.makeText(context,"已保存到目录:"+file.getAbsolutePath().trim(),Toast.LENGTH_LONG).show();
                            activity.invalidateOptionsMenu();
                            if (doodleBitmap!=null&&!doodleBitmap.isRecycled())
                            {
                                doodleBitmap.recycle();
                            }
                        }

                        @Override
                        public void onIOFailed(IOException exception) {
                           // Log.d("tag", "onIOFailed: "+exception);
                            exception.printStackTrace();
                            Toast.makeText(context,"图片输出失败,请检查是否给予软件权限",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCreateDirFailed() {
                            Toast.makeText(context,"创建文件夹失败，检查是否给予软件权限",Toast.LENGTH_LONG).show();
                        }
                    });

                }

                @Override
                public void onReady(IDoodle doodle) {
                    doodle.setDoodleScale(0.8f,doodle.getDoodleBitmap().getWidth()/2,doodle.getDoodleBitmap().getHeight()/2);

                    doodleView.refresh();
                }
            });
            doodleOnTouchGestureListener = new ScaleItemOnTouchGestureListener(doodleView, new DoodleOnTouchGestureListener.ISelectionListener() {
                @Override
                public void onSelectedItem(IDoodle doodle, IDoodleSelectableItem selectableItem, boolean selected) {
                    // doodleOnTouchGestureListener.setSelectedItem(selectableItem);
                    if (Envent!=null)
                    {
                        if (selected)
                        {
                            Envent.onIFSelect();
                        }else{
                            Envent.onIFenselect();
                        }
                    }


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
                      // Bitmap bitmap1=BitmapFactory.decodeResource(context.getResources(),loadbitmap);
                       //  IDoodleSelectableItem item = new DoodleBitmap(doodle, bitmap1, 80 * doodle.getUnitSize(), x, y);
                      // doodle.addItem(item);
                      // doodleOnTouchGestureListener.setSelectedItem(item);
                       // doodleView.setEditMode(true);
                      // doodle.refresh();

                   }else if(doodle.getPen()==DoodlePen.MOSAIC)
                   {

                   }

                }
                });

            IDoodleTouchDetector detector = new DoodleTouchDetector(context, doodleOnTouchGestureListener);
            doodleView.setDefaultTouchDetector(detector);
            return doodleView;
        }
    public void setEnvent(onEnvent envent) {
        Envent = envent;
    }

        private void  ty_save()
        {
            //涂鸦保存
        }



    public ScaleItemOnTouchGestureListener getDoodleOnTouchGestureListener() {
        return doodleOnTouchGestureListener;
    }
    public static String getStoragePath(String baseName) {
        String path = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(path)) {
                path = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
        }
        File file = new File(path + "/" + baseName);
        file.mkdirs();
        return file.getAbsolutePath() + "/";
    }

}
