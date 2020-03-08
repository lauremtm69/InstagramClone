package com.alenmutum21.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class tabAdapter extends FragmentPagerAdapter {
    public tabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AllPosts();
            case 1:
                return new ProfileTab();
            case 2:
                return new SharePictureTab();
            case 3:
                return new UsersTab();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Updates";

            case 1:
                return "Profile";
            case 2:
                return "Share";
            case 3:
                return "Users";

            default:
                return null;
        }
    }
}
