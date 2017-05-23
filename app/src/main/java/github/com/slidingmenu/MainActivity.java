package github.com.slidingmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import github.com.slidingmenu.view.SlidingMenu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SlidingMenu mSlidingMenu= (SlidingMenu) findViewById(R.id.sliding_menu);
//        //切换菜单状态
//        mSlidingMenu.toggleMenu();
//        //监听菜单状态
//        mSlidingMenu.setOnSlidingMenuStatusChangeListenter(new SlidingMenu.OnSlidingMenuStatusChangeListenter() {
//            @Override
//            public void close() {
//                Toast.makeText(MainActivity.this,"菜单关闭",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void open() {
//                Toast.makeText(MainActivity.this,"菜单打开",Toast.LENGTH_SHORT).show();
//            }
//        });


    }
}
