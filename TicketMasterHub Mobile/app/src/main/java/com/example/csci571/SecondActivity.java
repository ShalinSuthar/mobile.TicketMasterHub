package com.example.csci571;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

// This class if for loading the second part of the Appplication

public class SecondActivity extends AppCompatActivity implements TabLayoutMediator.TabConfigurationStrategy{
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    ArrayList<String> titles;

//    private int[] tabIcons = {
//            R.drawable.information_outline,
//            R.drawable.artist,
//            R.drawable.venue
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar myToolbar = findViewById(R.id.toolbar_event);
        setSupportActionBar(myToolbar);



        viewPager2 = findViewById(R.id.view_pager_event);
        tabLayout = findViewById(R.id.tabLayout_events);
        titles = new ArrayList<String>();
        titles.add("Details");
        titles.add("Artists");
        titles.add("Venue");
        setViewPagerAdapter();


        ImageButton backButton = (ImageButton) (findViewById(R.id.back_button_event));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), MainActivity.class);
//                v.getContext().startActivity(intent);
                onBackPressed();
            }});
        new TabLayoutMediator(tabLayout, viewPager2, this).attach();
    }

//    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//    }
    public void setViewPagerAdapter() {
        ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(this);
        ArrayList<Fragment> fragmentList = new ArrayList<>(); //creates an ArrayList of Fragments
        fragmentList.add(new DetailsFragment());
        fragmentList.add(new ArtistsFragment());
        fragmentList.add(new VenueFragment());
        viewPager2Adapter.setData(fragmentList);
        Log.d("second view adapter", "setViewPagerAdapter: ");//sets the data for the adapter
        viewPager2.setAdapter(viewPager2Adapter);

    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        int[] tabIcons = {
                R.drawable.information_outline,
                R.drawable.artist,
                R.drawable.venue
        };

        tab.setText(titles.get(position));
        tab.setIcon(tabIcons[position]);
    }


}
