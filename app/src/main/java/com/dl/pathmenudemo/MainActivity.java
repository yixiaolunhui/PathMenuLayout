package com.dl.pathmenudemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dl.pathmenu.PathMenuLayout;

public class MainActivity extends AppCompatActivity implements PathMenuLayout.PathMenuListener {

    private PathMenuLayout pathMenu;
    private Button btn;
    private int COUNT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pathMenu = findViewById(R.id.pathMenu);
        btn = findViewById(R.id.btn);
        pathMenu.setPathMenuListener(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathMenu.toggle();
            }
        });
//        addMenuItems();
    }

    @Override
    public void onOpen() {
        btn.setText("close");
    }

    @Override
    public void onClose() {
        btn.setText("open");
    }

    @Override
    public void menuClick(View view, int position) {
        Toast.makeText(this, "点击了第" + position + "个菜单", Toast.LENGTH_SHORT).show();
    }

    public void addMenuItems() {
        pathMenu.removeAllViews();
        for (int i = 0; i < COUNT; i++) {
            ImageView imageView=new ImageView(this);
            imageView.setImageResource(R.mipmap.ic_launcher_round);
            pathMenu.addView(imageView);
        }
    }
}
