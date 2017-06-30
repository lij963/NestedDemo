package com.tsing.nested.nesteddemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity  extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {
    String LOG = "MAIN";
    NestedScrollView scrollView;
    MyViewPager viewPager;

    SmartTabLayout tabLayout;
    FrameLayout flTab;
    private RelativeLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (MyViewPager) findViewById(R.id.vp);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        flTab = (FrameLayout) findViewById(R.id.fl_tab);
        scrollView = (NestedScrollView) findViewById(R.id.nested);
        tabLayout = (SmartTabLayout) findViewById(R.id.viewpagertab);
        tabLayout.setCustomTabView(R.layout.custom_tab, R.id.custom_text);
//        tabLayout.setBackgroundColor(Color.RED);
//        tabLayout.setAlpha(1f);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int tabLayoutTop = flTab.getTop();
                Log.w("Demo", "rlRoot.getTop=" + rlRoot.getTop() + ",viewPagerTop=" + tabLayoutTop + ",scrollX=" + scrollX + ",scrollY=" + scrollY + ",oldScrollX=" + oldScrollX + ",oldScrollY=" + oldScrollY);
                int dy = scrollY - tabLayoutTop;
                if (dy > 0) {
                    //此时TAB已经到顶点
                    flTab.setTranslationY(dy);
                } else {
                    flTab.setTranslationY(0);
                }
            }
        });
//        MyScrollView nestedScrollView = (MyScrollView) findViewById(R.id.nested);
//        nestedScrollView.onNestedPreScroll();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(ItemFragment.newInstance(1, 0, viewPager));
        fragments.add(ItemFragment.newInstance(2, 1, viewPager));
        fragments.add(ItemFragment.newInstance(3, 2, viewPager));
        QuickFragmentPageAdapter<Fragment> pageAdapter = new QuickFragmentPageAdapter<>(getSupportFragmentManager(), fragments, new String[]{"1", "2", "3"});
        viewPager.setAdapter(pageAdapter);
        tabLayout.setViewPager(viewPager);

        //计算并绘制第一个界面的高度
        viewPager.resetHeight(0);
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //重新绘制当前页面的高度
                viewPager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPager.addOnPageChangeListener(onPageChangeListener);
        tabLayout.setOnPageChangeListener(onPageChangeListener);


//        scrollView.listener
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Log.i(LOG, item.toString());
    }

    public class QuickFragmentPageAdapter<T extends Fragment> extends FragmentPagerAdapter {
        private List<T> mList;
        private String[] mStrings;

        /**
         * @param fm
         * @param list
         * @param titles PageTitles
         */
        public QuickFragmentPageAdapter(FragmentManager fm, List<T> list, String[] titles) {
            super(fm);
            mList = list;
            mStrings = titles;
        }


        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mStrings == null ? super.getPageTitle(position) : mStrings[position];
        }
    }
}
