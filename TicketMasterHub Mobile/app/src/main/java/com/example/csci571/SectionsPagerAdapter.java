package com.example.csci571;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SectionsPagerAdapter extends FragmentStateAdapter {

    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
//                return new DetailsFragment();
            case 1:
//                return new ArtistFragment();
            case 2:
//                return new VenueFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }


//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        switch (position) {
//            case 0:
//                return mContext.getString(R.string.details_tab_title);
//            case 1:
//                return mContext.getString(R.string.artist_tab_title);
//            case 2:
//                return mContext.getString(R.string.venue_tab_title);
//            default:
//                return null;
//        }
//    }
}
