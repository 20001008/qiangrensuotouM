package com.cy.suotou.m;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Field;

import cn.forward.androids.utils.Util;
import cn.hzw.doodle.DoodleColor;
import cn.hzw.doodle.DoodlePen;
import cn.hzw.doodle.DoodleTouchDetector;
import cn.hzw.doodle.DoodleView;
import cn.hzw.doodle.core.IDoodleColor;
import cn.hzw.doodle.core.IDoodleTouchDetector;


public class MainActivity extends AppCompatActivity {
    private static int KUOZHAN_SOUSUO = 0;
    private static int KUOZHAN_ZHANKAI = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private ImageView kuozhan_button;//扩展按钮
    private LinearLayout kuozhan_view;
    private ConstraintLayout main_view;
    private LinearLayout kuozhan_ex;
    private int kuozhan_zt = KUOZHAN_SOUSUO;
    private int display_w;
    private int display_h;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout out_side = findViewById(R.id.main_outside);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorAccent));
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            out_side.setPadding(0, config.getPixelInsetTop(false), 0, config.getPixelInsetBottom());
        }

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("强人锁头M");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        //变量初始化
        display_h = getResources().getDisplayMetrics().heightPixels;
        display_w = getResources().getDisplayMetrics().widthPixels;
        //控件初始化
        kuozhan_button = findViewById(R.id.kuozhan_an);
        kuozhan_view = findViewById(R.id.kuozhan_view);
        kuozhan_ex = findViewById(R.id.kuozhan_ex);
        main_view = findViewById(R.id.main_view);
        final LinearLayout kuozhan_in_tt = findViewById(R.id.kuozhan_in_tt);
        ImageView huabi_view = findViewById(R.id.kuozhan_huabi);
        kuozhan_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        kuozhan_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kuozhan_zt == KUOZHAN_SOUSUO) {
                    kuozhan_ex.setVisibility(View.VISIBLE);
                    kuozhan_zt = KUOZHAN_ZHANKAI;
                } else {
                    kuozhan_ex.setVisibility(View.GONE);
                    kuozhan_zt = KUOZHAN_SOUSUO;
                }
            }
        });
        kuozhan_view.setOnTouchListener(new View.OnTouchListener() {
            private int d_y;
            private int offsety = 0;
            private int last_y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int y = (int) event.getY();
                float downX = event.getX();
                float downY = event.getY();

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    kuozhan_view.setBackgroundResource(R.drawable.drawable_kuozhan_s);
                    d_y = y;

                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    kuozhan_view.setBackgroundResource(R.drawable.drawable_kuozhan_s);
                    //Log.d("s", "onTouch: "+event.getRawY()+" "+y);
                    //手指移动距离
                    offsety = (int) (y - d_y);
                    // Log.d("s", "onTouch: "+display_h+" "+event.getRawY()+" "+event.getY()+" "+kuozhan_view.getY()+" "+toolbar.getHeight()+" "+(event.getRawY()-kuozhan_view.getHeight()+event.getY())+" "+display_h);
                    if ((event.getRawY() - kuozhan_view.getHeight() - event.getY() / 3) >= toolbar.getHeight() && (event.getRawY() + kuozhan_view.getHeight() / 2) < display_h) {
                        kuozhan_view.offsetTopAndBottom(offsety);

                        last_y = (int) event.getRawY();
                    }
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (!isRect(downX, downY, kuozhan_in_tt.getX(), kuozhan_in_tt.getY(), kuozhan_in_tt.getX() + kuozhan_in_tt.getWidth(),kuozhan_in_tt.getY() + kuozhan_in_tt.getHeight())) {
                        kuozhan_view.setBackgroundResource(R.drawable.drawable_kuozhan_p);
                        int last_y = (int) kuozhan_view.getY();//记录
                        //给予子控件id
                        for (int i = 0; i < main_view.getChildCount(); ++i) {
                            View view = main_view.getChildAt(i);
                            ConstraintLayout.LayoutParams param = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                            int id = view.getId();
                            view.setId(i);
                        }

                        ConstraintSet c = new ConstraintSet();
                        c.clone(main_view);
                        c.setMargin(kuozhan_view.getId(), ConstraintSet.TOP, last_y);
                        //    c.connect(kuozhan_view.getId(),ConstraintSet.TOP,kuozhan_view.getId(),ConstraintSet.BOTTOM,100);
                        c.clear(R.id.kuozhan_view);
                        c.applyTo(main_view);


                    }else
                    {

                    }
                }
                return false;
            }
        });
        kuozhan_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("d", "onTouch: 6666666");
                }
                return false;
            }
        });
        /*扩展内事件*/
        kuozhan_in_tt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
              if(event.getAction()==MotionEvent.ACTION_DOWN&&event.getAction()!=MotionEvent.ACTION_MOVE)
              {
                  Log.d("s", "onTouch: "+"2222222222222");
                  return false;
              }
                return true;
            }
        });      // 自定义颜色
        //tintManager.setTintColor(Color.parseColor("#24b7a4"));


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.cycd);
        final DoodleView doodleView = new ty_tool().ty_init(MainActivity.this, MainActivity.this, bitmap);
        ConstraintLayout main_view = findViewById(R.id.main_view);
        main_view.addView(doodleView, 1);
        doodleView.setPen(DoodlePen.BITMAP);


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
}

