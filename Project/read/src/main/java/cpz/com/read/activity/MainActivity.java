package cpz.com.read.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cpz.com.read.R;
import cpz.com.read.fragment.ContactFragment;
import cpz.com.read.fragment.FindFragment;
import cpz.com.read.fragment.HomeFragment;
import cpz.com.read.fragment.MeFragment;
import cpz.com.read.view.ChangeColorIconWithTextView;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener
{

    private ViewPager vpContainer;
    private FragmentPagerAdapter mAdapter;
    private String[] mTitles = new String[]{"1",
            "2", "3", "4"};
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private List<ChangeColorIconWithTextView> mTabIcon = new ArrayList<ChangeColorIconWithTextView>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initUI();
    }

    private void initData()
    {
        mTabs.add(new HomeFragment());
        mTabs.add(new ContactFragment());
        mTabs.add(new FindFragment());
        mTabs.add(new MeFragment());
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }
        };
        initTabIndicator();

    }
    private void initTabIndicator() {
        ChangeColorIconWithTextView one = (ChangeColorIconWithTextView) findViewById(R.id.ctv_home);
        ChangeColorIconWithTextView two = (ChangeColorIconWithTextView) findViewById(R.id.ctv_contact);
        ChangeColorIconWithTextView three = (ChangeColorIconWithTextView) findViewById(R.id.ctv_find);
        ChangeColorIconWithTextView four = (ChangeColorIconWithTextView) findViewById(R.id.ctv_me);

        mTabIcon.add(one);
        mTabIcon.add(two);
        mTabIcon.add(three);
        mTabIcon.add(four);


        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        one.setIconAlpha(1.0f);
    }

    private void initUI()
    {
        vpContainer = (ViewPager) findViewById(R.id.vp_container);
        vpContainer.setOnPageChangeListener(this);
        vpContainer.setAdapter(mAdapter);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            ChangeColorIconWithTextView left = mTabIcon.get(position);
            ChangeColorIconWithTextView right = mTabIcon.get(position + 1);

            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        resetOtherTabs();

        switch (view.getId()) {
            case R.id.ctv_home:
                mTabIcon.get(0).setIconAlpha(1.0f);
                vpContainer.setCurrentItem(0, false);
                break;
            case R.id.ctv_contact:
                mTabIcon.get(1).setIconAlpha(1.0f);
                vpContainer.setCurrentItem(1, false);
                break;
            case R.id.ctv_find:
                mTabIcon.get(2).setIconAlpha(1.0f);
                vpContainer.setCurrentItem(2, false);
                break;
            case R.id.ctv_me:
                mTabIcon.get(3).setIconAlpha(1.0f);
                vpContainer.setCurrentItem(3, false);
                break;
        }
    }

    private void resetOtherTabs() {
        for (int i = 0, len = mTabIcon.size(); i < len; i++) {
            mTabIcon.get(i).setIconAlpha(0);
        }
    }
}
