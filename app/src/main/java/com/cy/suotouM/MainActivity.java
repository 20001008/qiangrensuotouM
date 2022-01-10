package com.cy.suotouM;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.StatusBarManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.cy.suotouM.R;
import com.cy.suotouM.note.*;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.jaeger.library.StatusBarUtil;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cn.hzw.doodle.DoodleBitmap;
import cn.hzw.doodle.DoodleOnTouchGestureListener;
import cn.hzw.doodle.DoodlePen;
import cn.hzw.doodle.DoodleView;
import cn.hzw.doodle.core.IDoodleSelectableItem;
import tool.GridSpacingItemDecoration;

import static com.cy.suotouM.ty_tool.loadbitmap;


public class MainActivity extends AppCompatActivity {
    private static int KUOZHAN_SOUSUO = 0;
    private static int KUOZHAN_ZHANKAI = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private ImageView kuozhan_button;//扩展按钮
    private LinearLayout kuozhan_view;
    private RelativeLayout main_view;
    private LinearLayout kuozhan_ex;
    private TextView main_tip;
    private String[] Permission_data = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private int kuozhan_zt = KUOZHAN_SOUSUO;
    private int display_w;
    private int display_h;
    private int[] menuIcons = new int[]{R.drawable.ic_chevron_right_red_400_24dp};
    private DoodleView doodleView;
    private List<headchose_top_note> data_top;
    private headchose_bottom_helper bottom_helper;
    private headchose_top_helper top_helper;
    private ty_tool tyTool;
    private int select_pos = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UMConfigure.init(MainActivity.this, "5f17efb3b4fa6023ce1885fd", "cool", UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        setContentView(R.layout.activity_main);
        // RelativeLayout out_side = findViewById(R.id.main_outside);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorAccent));
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            // out_side.setPadding(0, config.getPixelInsetTop(false), 0, config.getPixelInsetBottom());
        }
        //权限获取
        for(String qx:Permission_data)
        {
            int permission_sulift = ActivityCompat.checkSelfPermission(MainActivity.this, qx);
            if (permission_sulift != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, Permission_data, 1);
            }
        }
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("强人锁头M");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(null);
        setSupportActionBar(toolbar);
        //判断是否第一次
       SharedPreferences preferences= getSharedPreferences("data",MODE_PRIVATE);
      boolean if_first= preferences.getBoolean("first_use",true);
       if (if_first)
       {
           new my_dialog().dialog_about(MainActivity.this);

       }
        //变量初始化
        display_h = getResources().getDisplayMetrics().heightPixels;
        display_w = getResources().getDisplayMetrics().widthPixels;


        //tintManager.setTintColor(Color.parseColor("#24b7a4"));


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        tyTool = new ty_tool();
        main_view = findViewById(R.id.main_view);
        main_tip=findViewById(R.id.main_tip);
        tyTool.setEnvent(new ty_tool.onEnvent() {
            @Override
            public void onIFSelect() {
                invalidateOptionsMenu();
            }

            @Override
            public void onIFenselect() {
                invalidateOptionsMenu();
            }
        });
/*
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.cycd);


       doodleView = tyTool.ty_init(MainActivity.this, MainActivity.this, bitmap);

        main_view.addView(doodleView, 0);
        doodleView.setPen(DoodlePen.BITMAP);
        doodleView.setEditMode(false);
        */
        final SpeedDialView speedDialView = findViewById(R.id.main_speedDial);
        speedDialView.setOrientation(LinearLayout.VERTICAL);
        speedDialView.setMainFabAnimationRotateAngle(90);
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.sd_fab1, R.drawable
                .ic_add_white_24dp)
                .setLabel("添加头像")
                .create());
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.sd_fab2, R.drawable
                .ic_photo_white_24dp)
                .setLabel("载入图片")
                .create());
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.sd_fab3, R.drawable
                .ic_save_white_24dp)
                .setLabel("保存图片")
                .create());
        speedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.sd_fab4, R.drawable
                .iconmonstr_coin_2_32)
                .setLabel("打赏作者")
                .create());
        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                speedDialView.close();
                switch (actionItem.getId()) {
                    case R.id.sd_fab1:
                        if (doodleView!=null&&tyTool!=null)
                        HeaderChose(null);
                        break;
                    case R.id.sd_fab2:
                        EasyPhotos.createAlbum(MainActivity.this, true, GlideEngine.getInstance())//参数说明：上下文，是否显示相机按钮，[配置Glide为图片加载引擎](https://github.com/HuanTanSheng/EasyPhotos/wiki/12-%E9%85%8D%E7%BD%AEImageEngine%EF%BC%8C%E6%94%AF%E6%8C%81%E6%89%80%E6%9C%89%E5%9B%BE%E7%89%87%E5%8A%A0%E8%BD%BD%E5%BA%93)
                                .setFileProviderAuthority("com.CY.suotou.m.fileprovider")//参数说明：见下方`FileProvider的配置`
                                .start(100);
                        break;
                    case R.id.sd_fab3:
                        doodleView.setSaveEnabled(true);
                        doodleView.save();
                        break;
                    case R.id.sd_fab4:
                        Intent intent=new Intent(MainActivity.this,jz_main.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    if (doodleView!=null)
                    {
                        doodleView.clear();
                    }


                    if (data != null) {
                        ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                        String bitmap_path = resultPhotos.get(0).path;
                        Bitmap bitmap = BitmapFactory.decodeFile(bitmap_path);
                        if (bitmap != null) {
                            if (doodleView!=null)
                            {
                                main_view.removeView(doodleView);
                            }

                            main_tip.setVisibility(View.GONE);
                            doodleView = tyTool.ty_init(MainActivity.this, MainActivity.this, bitmap);
                            main_view.addView(doodleView, 0);
                            doodleView.setPen(DoodlePen.BITMAP);
                            doodleView.setDoodleScale(1, 1, 1);

                        }

                    }

                    break;
                case 200:
                     if (data != null) {
                         ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                         File file=MainActivity.this.getFilesDir();
                         File newfile=new File(file.getAbsolutePath()+"/MyImage/");
                         String newfile_path=file.getAbsolutePath()+"/MyImage/";
                         if (!newfile.exists())
                         {
                             newfile.mkdirs();
                         }
                         if (resultPhotos!=null)
                         {
                             int n=0;
                             for (Photo photo:resultPhotos)
                             {
                                 n++;
                                 String bitmap_path = photo.path;
                                 Bitmap bitmap = BitmapFactory.decodeFile(bitmap_path);
                                 GregorianCalendar gregorianCalendar=new GregorianCalendar();
                                 int year=gregorianCalendar.get(GregorianCalendar.YEAR);
                                 int month=gregorianCalendar.get(GregorianCalendar.MONTH);
                                 int day=gregorianCalendar.get(GregorianCalendar.HOUR_OF_DAY);
                                 int Hour=gregorianCalendar.get(GregorianCalendar.HOUR);
                                 int minute=gregorianCalendar.get(GregorianCalendar.MINUTE);
                                 int second=gregorianCalendar.get(GregorianCalendar.SECOND);
                                 int mc=gregorianCalendar.get(GregorianCalendar.MILLISECOND);
                                 try {
                                     FileOutputStream fileOutputStream=new FileOutputStream(newfile_path+year+month+day+Hour+minute+second+mc+"_"+n+".png");
                              bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                              fileOutputStream.flush();
                              fileOutputStream.close();
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }

                             }
                             if (top_helper!=null)
                             {
                                 loadDialogBottom_zdy();
                             }

                         }



                            // FileOutputStream fileOutputStream=new FileOutputStream();

                     }
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delect:
                DoodleOnTouchGestureListener doodle = tyTool.getDoodleOnTouchGestureListener();
                if (doodle.getSelectedItem() != null) {
                    IDoodleSelectableItem doodleItemBase = doodle.getSelectedItem();
                    doodleView.removeItem(doodleItemBase);
                    doodleView.invalidate();
                    doodle.setSelectedItem(null);
                    invalidateOptionsMenu();
                }
                break;
            case R.id.action_share:
                shareSingleImage(ty_tool.last_path);
                break;
            case R.id.action_edit:
                DoodleOnTouchGestureListener doodle2 = tyTool.getDoodleOnTouchGestureListener();

                itemattr(  doodle2.getSelectedItem());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (tyTool!=null&&doodleView!=null)
        {
            DoodleOnTouchGestureListener doodle = tyTool.getDoodleOnTouchGestureListener();
            if (doodle.getSelectedItem() != null) {
                menu.findItem(R.id.action_delect).setVisible(true);
                menu.findItem(R.id.action_edit).setVisible(true);
            } else {
                menu.findItem(R.id.action_delect).setVisible(false);
                menu.findItem(R.id.action_edit).setVisible(false);
            }
            if (ty_tool.last_path!=null)
            {
                menu.findItem(R.id.action_share).setVisible(true);
            }else
            {
                menu.findItem(R.id.action_share).setVisible(false);
            }
        }else{
            menu.findItem(R.id.action_delect).setVisible(false);
            menu.findItem(R.id.action_share).setVisible(false);
            menu.findItem(R.id.action_edit).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static boolean isRect(float x, float y, float left, float top, float right, float down) {
        if (x > left && x < right && y > top && y < down) {
            return true;
        }

        return false;
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    context.getContentResolver().openFileDescriptor(uri, "r");
            assert parcelFileDescriptor != null;
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                image.setConfig(Bitmap.Config.ARGB_8888);
            }

            parcelFileDescriptor.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
private void itemattr(final IDoodleSelectableItem doodleItemBase) {
        final MaterialDialog materialDialog=new my_dialog().dialog_itemattr(MainActivity.this);
    Button button_zd=materialDialog.getView().findViewById(R.id.itemedit_zd);
    Button button_dc=materialDialog.getView().findViewById(R.id.itemedit_dc);
    Button button_choose=materialDialog.getView().findViewById(R.id.itemedit_choose);
    ImageView imageView_close=materialDialog.getView().findViewById(R.id.head_close);
    imageView_close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (materialDialog.isShowing())
            {
                materialDialog.dismiss();
            }

        }
    });
    button_zd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (doodleView!=null&&doodleItemBase!=null)
            {
                doodleView.topItem(doodleItemBase);
                
                doodleItemBase.setSelected(false);
            }
        }
    });
    button_dc.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doodleView.bottomItem(doodleItemBase);
            doodleItemBase.setSelected(false);
        }
    });
   button_choose.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
        HeaderChose(doodleItemBase);
       }
   });
materialDialog.show();

}
    private void HeaderChose(@Nullable final IDoodleSelectableItem iDoodleSelectableItem) {
        final MaterialDialog materialDialog = new MaterialDialog.Builder(MainActivity.this)
                .customView(R.layout.layout_dialog_headchose, false)
                .show();


        Window window = materialDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.background_dialog_item);
            window.getDecorView().setPadding(0, 0, 0, 0);
            //getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            window.getAttributes().height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setGravity(Gravity.BOTTOM);
        }
        select_pos = 0;
        final String[] head_names = this.getResources().getStringArray(R.array.head_name);
        final List<headchose_bottom_note> bottom_data = new ArrayList<>();
        RecyclerView recyclerView_bottom = (RecyclerView) materialDialog.findViewById(R.id.head_bottom_rec);
        RecyclerView recyclerView_top = (RecyclerView) materialDialog.findViewById(R.id.head_top_rec);
        TextView textView_tittle = (TextView) materialDialog.findViewById(R.id.head_top_tittle);
       ImageView close= (ImageView) materialDialog.findViewById(R.id.head_close);
       close.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               materialDialog.dismiss();
           }
       });
        textView_tittle.getPaint().setFakeBoldText(true);
        for (int n = 0; n < head_names.length; n++) {
            if (n == 0) {
                bottom_data.add(new headchose_bottom_note(head_names[n], true));
            } else {
                bottom_data.add(new headchose_bottom_note(head_names[n], false));
            }

        }
        bottom_data.add(new headchose_bottom_note("自定义",false));
        bottom_helper = new headchose_bottom_helper(R.layout.list_headchose_bottom, bottom_data);
        recyclerView_bottom.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        //recyclerView_bottom.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerView_bottom.setAdapter(bottom_helper);
        bottom_helper.addChildClickViewIds(R.id.headchose_button);
        bottom_helper.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                select_pos = position;
                for(int x=0;x<bottom_data.size();x++)
                {
                    bottom_data.get(x).setIsselect(false);
                }
                switch (position) {
                    case 0:
                        //臭鼬
                        loadbDialogBottom(R.array.head_cy_list);
                        break;
                    case 1:
                        loadbDialogBottom(R.array.head_ys_list);
                        break;
                    case 2:
                        loadbDialogBottom(R.array.head_wz_list);
                        break;
                    case 3:
                        loadbDialogBottom(R.array.head_jojo_list);
                        break;
                    case 4:
                        loadbDialogBottom(R.array.head_qt_list);
                        break;
                    case 5:
                        loadDialogBottom_zdy();
                        break;
                }
                bottom_data.get(position).setIsselect(true);
                bottom_helper.notifyDataSetChanged();
            }

        });
        final TypedArray cy_head = getResources().obtainTypedArray(R.array.head_cy_list);
        final TypedArray ys_head = getResources().obtainTypedArray(R.array.head_ys_list);
        final TypedArray wz_head = getResources().obtainTypedArray(R.array.head_wz_list);
        final TypedArray jojo_head = getResources().obtainTypedArray(R.array.head_jojo_list);
        final TypedArray qt_head = getResources().obtainTypedArray(R.array.head_qt_list);
        //配置头像表
        data_top = new ArrayList<>();
        for (int s = 0; s < cy_head.length(); s++) {
            // Bitmap bm=BitmapFactory.decodeResource(getResources(),cy_head.getResourceId(s,R.drawable.xw)).copy(Bitmap.Config.ARGB_8888,true);
            Drawable drawable = ContextCompat.getDrawable(MainActivity.this, cy_head.getResourceId(s, R.drawable.xw));
            if (drawable != null) {
                data_top.add(new headchose_top_note(drawable));
            }
        }

        top_helper = new headchose_top_helper(R.layout.list_headchose_top, data_top);

        // int num=display_w/dip2px(MainActivity.this,30f)/4;
        final GridLayoutManager manager = new GridLayoutManager(this, 5);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView_top.setLayoutManager(manager);
        recyclerView_top.addItemDecoration(new GridSpacingItemDecoration(5, 30, true));
        recyclerView_top.setHasFixedSize(true);
        recyclerView_top.setAdapter(top_helper);
        //点击事件
        top_helper.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull final BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (select_pos == 5) {
                    final headchose_top_note headchose_top_note = (headchose_top_note) adapter.getItem(position);
                    Log.d("TAG", "onItemLongClick: " + headchose_top_note.path);
                    final File file = new File(headchose_top_note.getPath());
                    if (file.exists()) {
                        final MaterialDialog materialDialog1 = new MaterialDialog.Builder(MainActivity.this)
                                .title("提示")
                                .content("是否永久删除此头像？\n(路径为:" + file.getAbsolutePath()+")")
                                .positiveText("确定")
                                .negativeText("取消")
                                .show();
                        materialDialog1.getBuilder().onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                file.delete();
                                adapter.remove(headchose_top_note);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        Window window = materialDialog1.getWindow();
                        if (window != null) {
                            window.setBackgroundDrawableResource(R.drawable.background_dialog_item);
                            window.getDecorView().setPadding(0, 0, 0, 20);
                            //getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            //window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
                            window.getAttributes().height = WindowManager.LayoutParams.WRAP_CONTENT;
                            window.getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
                            window.setGravity(Gravity.BOTTOM);


                        }


                    }
                    return false;
                }
                return false;
            }
        });
        top_helper.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (iDoodleSelectableItem==null)
                {
                    switch (select_pos) {
                        case 0:
                            //臭鼬
                            intodraw_image(materialDialog,position,cy_head);
                            break;
                        case 1:
                            intodraw_image(materialDialog,position,ys_head);
                            break;
                        case 2:
                            intodraw_image(materialDialog,position,wz_head);
                            break;
                        case 3:
                            intodraw_image(materialDialog,position,jojo_head);
                            break;
                        case 4:
                            intodraw_image(materialDialog,position,qt_head);
                            break;
                        case 5:
                            if (position==0)
                            {
                                EasyPhotos.createAlbum(MainActivity.this, true, GlideEngine.getInstance())//参数说明：上下文，是否显示相机按钮，[配置Glide为图片加载引擎](https://github.com/HuanTanSheng/EasyPhotos/wiki/12-%E9%85%8D%E7%BD%AEImageEngine%EF%BC%8C%E6%94%AF%E6%8C%81%E6%89%80%E6%9C%89%E5%9B%BE%E7%89%87%E5%8A%A0%E8%BD%BD%E5%BA%93)
                                       .setCount(10)
                                        .setFileProviderAuthority("com.CY.suotou.m.fileprovider")//参数说明：见下方`FileProvider的配置`
                                        .start(200);
                            }else{
                                headchose_top_note headchose_top_note=(headchose_top_note) adapter.getItem(position);
                                intodraw_image_zdy(materialDialog,position,headchose_top_note.getBitmap());


                            }
                            break;
                    }
                }else{
                   if (iDoodleSelectableItem.getPen()==DoodlePen.BITMAP) {
                       DoodleBitmap doodleBitmap=(DoodleBitmap) iDoodleSelectableItem;
                       switch (select_pos) {
                           case 0:
                               //臭鼬
                               loaddraw_image(doodleBitmap,position,cy_head);
                               break;
                           case 1:
                               loaddraw_image(doodleBitmap,position,ys_head);
                               break;
                           case 2:
                               loaddraw_image(doodleBitmap,position,wz_head);
                               break;
                           case 3:
                               loaddraw_image(doodleBitmap,position,jojo_head);
                               break;
                           case 4:
                               loaddraw_image(doodleBitmap,position,qt_head);
                               break;
                           case 5:
                               if (position==0)
                               {
                                   EasyPhotos.createAlbum(MainActivity.this, true, GlideEngine.getInstance())//参数说明：上下文，是否显示相机按钮，[配置Glide为图片加载引擎](https://github.com/HuanTanSheng/EasyPhotos/wiki/12-%E9%85%8D%E7%BD%AEImageEngine%EF%BC%8C%E6%94%AF%E6%8C%81%E6%89%80%E6%9C%89%E5%9B%BE%E7%89%87%E5%8A%A0%E8%BD%BD%E5%BA%93)
                                           .setCount(10)
                                           .setFileProviderAuthority("com.CY.suotou.m.fileprovider")//参数说明：见下方`FileProvider的配置`
                                           .start(200);
                               }else{
                                   headchose_top_note headchose_top_note=(headchose_top_note) adapter.getItem(position);
                                   intodraw_image_zdy(materialDialog,position,headchose_top_note.getBitmap());


                               }
                               break;
                       }

                       iDoodleSelectableItem.setPen(DoodlePen.BITMAP);
                   }
                   }


            }
        });

    }
private void intodraw_image(MaterialDialog materialDialog,int pos,TypedArray typedArray)
{
    Bitmap bitmap1 = BitmapFactory.decodeResource(MainActivity.this.getResources(), typedArray.getResourceId(pos, R.drawable.xw));
    IDoodleSelectableItem item = new DoodleBitmap(doodleView, bitmap1, 80 * doodleView.getUnitSize(), doodleView.getDoodleBitmap().getWidth() / 2, doodleView.getDoodleBitmap().getHeight() / 2);

    materialDialog.dismiss();
    doodleView.addItem(item);
    doodleView.notifyItemFinishedDrawing(item);
    doodleView.setEditMode(true);
    DoodleOnTouchGestureListener doodleOnTouchGestureListener = tyTool.getDoodleOnTouchGestureListener();
    doodleOnTouchGestureListener.setSelectedItem(item);


}
    private void intodraw_image_zdy(MaterialDialog materialDialog,int pos,Bitmap bitmap)
    {
       IDoodleSelectableItem item = new DoodleBitmap(doodleView, bitmap, 80 * doodleView.getUnitSize(), doodleView.getDoodleBitmap().getWidth() / 2, doodleView.getDoodleBitmap().getHeight() / 2);

        materialDialog.dismiss();
        doodleView.addItem(item);
        doodleView.notifyItemFinishedDrawing(item);
        doodleView.setEditMode(true);
        DoodleOnTouchGestureListener doodleOnTouchGestureListener = tyTool.getDoodleOnTouchGestureListener();
        doodleOnTouchGestureListener.setSelectedItem(item);


    }
    private void loaddraw_image(DoodleBitmap doodleBitmap,int pos,TypedArray typedArray)
    {
        Bitmap bitmap1 = BitmapFactory.decodeResource(MainActivity.this.getResources(), typedArray.getResourceId(pos, R.drawable.xw));
        doodleBitmap.setBitmap(bitmap1);

    }
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void loadbDialogBottom(int res) {
        if (!data_top.isEmpty()) {
            data_top.clear();
            TypedArray cy_head = getResources().obtainTypedArray(res);
            for (int s = 0; s < cy_head.length(); s++) {
                // Bitmap bm=BitmapFactory.decodeResource(getResources(),cy_head.getResourceId(s,R.drawable.xw)).copy(Bitmap.Config.ARGB_8888,true);
                Drawable drawable = ContextCompat.getDrawable(MainActivity.this, cy_head.getResourceId(s, res));
                if (drawable != null) {
                    data_top.add(new headchose_top_note(drawable));

                }
            }
            top_helper.notifyDataSetChanged();
        }
    }
    public void loadDialogBottom_zdy(){
        //加载自定义
        if (!data_top.isEmpty()) {
            data_top.clear();
            data_top.add(new headchose_top_note(true));
            top_helper.notifyDataSetChanged();
        }
        //加载
        File file=MainActivity.this.getFilesDir();
        File newfile=new File(file.getAbsolutePath()+"/MyImage/");
        String newfile_path=file.getAbsolutePath()+"/MyImage/";
       if (newfile.exists())
       {
           File[] files=newfile.listFiles();
           if (files != null) {
               for(File f:files)
               {

                   if (f.getName().toLowerCase().substring(f.getName().lastIndexOf(".")).equals(".png")||f.getName().toLowerCase().substring(f.getName().lastIndexOf(".")).equals(".jpg"))
                   {
                       Bitmap bitmap=BitmapFactory.decodeFile(f.getAbsolutePath());
                       data_top.add(new headchose_top_note(f.getAbsolutePath(),bitmap));
                        bitmap=null;
                        System.gc();
                   }
               }
               top_helper.notifyDataSetChanged();
           }

       }

    }

    public void addbitmap(int res) {
        try {
            loadbitmap = res;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void shareSingleImage(String imagepath)
    {
        if (new File(imagepath).exists())
        {
            Uri uri=Uri.fromFile(new File(imagepath));
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM,uri);
            intent.setType("image/*");
            startActivity(Intent.createChooser(intent,"分享到"));
        }

    }
}

