package com.main.pokyfun;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
        case 0:
            return new Pokes();
        case 1:
            return new Contacts();
        case 2:
            return new History();
        }
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
