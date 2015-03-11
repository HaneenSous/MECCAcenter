package com.mecca_center.app.meccacenter;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


import com.moshx.indicators.observer.ViewPagerObserver;
import com.moshx.indicators.title.TitleIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

@OptionsMenu(R.menu.menu_main)
@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity implements MaterialTabListener {



    @ViewById
    MaterialTabHost materialTabHost;
    @ViewById
    ViewPager MainPager;

    MainAdapter mainAdapter;

    @ViewById
    TitleIndicator titleIndicator;

    @ViewById
    Toolbar toolbar;
@InstanceState
    int CurrentPosition;
    @AfterViews
    public void Init() {

        mainAdapter = new MainAdapter(getSupportFragmentManager());
        MainPager.setAdapter(mainAdapter);

        setSupportActionBar(toolbar);
        ViewPagerObserver observer = new ViewPagerObserver(MainPager);
        observer.addObservableView(titleIndicator);

        observer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                materialTabHost.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageSelected(int position) {



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for (int i = 0; i < mainAdapter.getCount(); i++) {
            materialTabHost.addTab(
                    materialTabHost.newTab()
                            .setIcon( getResources().getDrawable(mainAdapter.iconsRes[i])
                                    )
                            .setTabListener(this)
            );
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        CurrentPosition=MainPager.getCurrentItem();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(CurrentPosition>=0){
            materialTabHost.setSelectedNavigationItem(CurrentPosition);

        }
    }

    @OptionsItem(R.id.action_settings)
    void onMenu() {
        SettingsActivity_.intent(this).start();
    }
    @OptionsItem(R.id.action_Notes)
    void onMenuNotes() {
        NoticeActivity_.intent(this).start();
    }


    @Override
    public void onTabSelected(MaterialTab materialTab) {
        MainPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }
}
