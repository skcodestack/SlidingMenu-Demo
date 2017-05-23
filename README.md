# SlidingMenu-Demo
仿QQ侧滑效果

### 1.使用：

  
    <github.com.slidingmenu.view.SlidingMenu
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:right_padding="80dp">

      <LinearLayout
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:orientation="horizontal">
          <!-- 菜单 -->
         <include
             layout="@layout/menu_layout"></include>

          <!--内容-->
          <include
              layout="@layout/content_layout"></include>

      </LinearLayout>

    </github.com.slidingmenu.view.SlidingMenu>
    
    
    
 ### 2.回调方法.
        SlidingMenu mSlidingMenu= (SlidingMenu) findViewById(R.id.sliding_menu);
        //切换菜单状态
        mSlidingMenu.toggleMenu();
        //监听菜单状态
        mSlidingMenu.setOnSlidingMenuStatusChangeListenter(new SlidingMenu.OnSlidingMenuStatusChangeListenter() {
            @Override
            public void close() {
                Toast.makeText(MainActivity.this,"菜单关闭",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void open() {
                Toast.makeText(MainActivity.this,"菜单打开",Toast.LENGTH_SHORT).show();
            }
        });
