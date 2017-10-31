# PathMenuLayout
path菜单
# 效果图
![image](https://github.com/dalong982242260/PathMenuLayout/blob/master/gif/menu.gif?raw=true)

![image](https://github.com/dalong982242260/PathMenuLayout/blob/master/gif/menu1.gif?raw=true)

![image](https://github.com/dalong982242260/PathMenuLayout/blob/master/gif/menu2.gif?raw=true)

# 设置
|name|format|description|
|:---:|:---:|:---:|
| PathMenuIsRotate | boolean |菜单是否旋转打开
| PathMenuItemIntervalAngle | integer |菜单之间的间隙角度
| PathMenuPopupTime | integer |菜单弹出时间
| PathMenuRadius | dimension |菜单半径

# 使用

```xml
 <com.dl.pathmenu.PathMenuLayout
        android:id="@+id/pathMenu"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:PathMenuIsRotate="true"
        app:PathMenuItemIntervalAngle="25"
        app:PathMenuPopupTime="1000"
        app:PathMenuRadius="200dp">

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@mipmap/ic_launcher_round" />

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@mipmap/ic_launcher_round" />

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@mipmap/ic_launcher_round" />

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@mipmap/ic_launcher_round" />

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@mipmap/ic_launcher_round" />
    </com.dl.pathmenu.PathMenuLayout>

```

or

```
for (int i = 0; i < COUNT; i++) {
    ImageView imageView=new ImageView(this);
    imageView.setImageResource(R.mipmap.ic_launcher_round);
    pathMenu.addView(imageView);
}
```