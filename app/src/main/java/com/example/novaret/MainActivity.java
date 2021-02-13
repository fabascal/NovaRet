package com.example.novaret;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.novaret.Adapters.ViewPagerAdapter;
import com.example.novaret.Fragments.AnticiposFragment;
import com.example.novaret.Fragments.ContadoFragment;
import com.example.novaret.Fragments.CreditoFragment;
import com.example.novaret.Fragments.DashboardFragment;
import com.example.novaret.Fragments.ServiciosFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.raizlabs.android.dbflow.sql.language.Operator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;

    /*Iniciamos Fragments*/
    DashboardFragment dashboardFragment;
    ContadoFragment contadoFragment;
    CreditoFragment creditoFragment;
    AnticiposFragment anticiposFragment;
    ServiciosFragment serviciosFragment;

    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewPager.setOffscreenPageLimit(5);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_dashboard:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.action_contado:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.action_credito:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.action_anticipos:
                    viewPager.setCurrentItem(3);
                    return true;
                case R.id.action_servicios:
                    viewPager.setCurrentItem(4);
                    return true;
            }return false;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            viewPager.addOnUnhandledKeyEventListener(new View.OnUnhandledKeyEventListener() {
                @Override
                public boolean onUnhandledKeyEvent(View view, KeyEvent keyEvent) {
                    return false;
                }
            });
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                //Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /*  //Disable ViewPager Swipe

       viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

        */
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        dashboardFragment = new DashboardFragment();
        contadoFragment = new ContadoFragment();
        creditoFragment = new CreditoFragment();
        anticiposFragment = new AnticiposFragment();
        serviciosFragment = new ServiciosFragment();
        adapter.addFragment(dashboardFragment);
        adapter.addFragment(contadoFragment);
        adapter.addFragment(creditoFragment);
        adapter.addFragment(anticiposFragment);
        adapter.addFragment(serviciosFragment);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
    public void CallSettings(MenuItem menuItem){
        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(intent);

    }
    public void CallPrinter(MenuItem menuItem){
        Intent intent = new Intent(MainActivity.this, PrinterActivity.class);
        startActivity(intent);
        Toast.makeText(this,R.string.printer,Toast.LENGTH_SHORT).show();
    }
}
