package com.pep.core.uibase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;


/**
 * The type Pep photo page fragment.大图左右展示的fragment
 * @author sunbaixin
 */
public class EasyPhotoPageFragment extends DialogFragment {


    private View contentView;
    private ArrayList<String> urls = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFullScreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.dialog_photo_page, container, false);
        initData();
        return contentView;
    }

    public void addUrls(ArrayList<String> urls ){
        Bundle bundle = new Bundle();
        bundle.putSerializable("urls", urls);
        setArguments(bundle);
    }

    @SuppressWarnings("unchecked")
    private void initData() {
        assert getArguments() != null;
        urls = (ArrayList<String>) getArguments().getSerializable("urls");
        if (urls == null || urls.size() == 0) {
            return;
        }
        ImageOriginPager viewPager = contentView.findViewById(R.id.viewPager);
        CirclePageIndicator circlePage = contentView.findViewById(R.id.circlePage);
        PhotoPageAdapter photoPageAdapter = new PhotoPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(photoPageAdapter);
        circlePage.setViewPager(viewPager);
    }


    /**
     * The type Photo page adapter.
     */
    public class PhotoPageAdapter extends FragmentPagerAdapter {

        /**
         * Instantiates a new Photo page adapter.
         *
         * @param fm the fm
         */
        PhotoPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return urls.size();
        }


        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("url", urls.get(position));
            PhotoPageViewFragment photoPageViewFragment = new PhotoPageViewFragment();
            photoPageViewFragment.setArguments(bundle);
            photoPageViewFragment.setOnClickListener(new PhotoPageViewFragment.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            return photoPageViewFragment;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

    }


}
