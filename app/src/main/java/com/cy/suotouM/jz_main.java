package com.cy.suotouM;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cy.suotouM.R;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.utils.bitmap.SaveBitmapCallBack;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.io.IOException;

public class jz_main extends AppCompatActivity {
//2
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jz_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorAccent));
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            // out_side.setPadding(0, config.getPixelInsetTop(false), 0, config.getPixelInsetBottom());
        }
        ImageView imageView=findViewById(R.id.jz_ewm);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Bitmap bitmap=  BitmapFactory.decodeResource(getResources(),R.drawable.zfb);
                EasyPhotos.saveBitmapToDir(jz_main.this, getStoragePath("Picture/"), "zfb", bitmap, true, new SaveBitmapCallBack() {
                    @Override
                    public void onSuccess(File file) {
                        Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(jz_main.this,"二维码已保存到相册，扫一扫界面右上角选择相册找到二维码即可",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onIOFailed(IOException exception) {

                    }

                    @Override
                    public void onCreateDirFailed() {

                    }
                });
                }
        });
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

