package com.pep.core.libimage;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

import androidx.fragment.app.Fragment;


/**
 * The type Image manager.
 *
 * @author sunbaixin
 */
public class EasyImageManager {

    private DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();

    private EasyImageManager() {
    }


    private static class PEPImageManagerInnerObject {
        private static EasyImageManager single = new EasyImageManager();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static EasyImageManager getInstance() {
        return PEPImageManagerInnerObject.single;
    }


    /**
     * Load circle crop.加载圆形图片
     *
     * @param activity  the activity
     * @param imageView the image view
     * @param url       the url
     */
    public void loadCircleCrop(Activity activity, final ImageView imageView, String url) {
        RequestOptions options = new RequestOptions()
                .circleCrop();
        Glide.with(activity).load(url).apply(options).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(imageView);
    }

    /**
     * Load circle crop.加载圆形图片
     *
     * @param activity  the activity
     * @param imageView the image view
     * @param url       the url
     */
    public void loadCircleCrop(Fragment activity, final ImageView imageView, String url) {
        RequestOptions options = new RequestOptions()
                .circleCrop();
        Glide.with(activity).load(url).apply(options).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(imageView);
    }

    /**
     * Load circle crop.加载圆形图片
     *
     * @param activity  the activity
     * @param imageView the image view
     * @param url       the url
     */
    public void loadRoundedCrop(Fragment activity, final ImageView imageView, String url) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(16);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(activity).load(url).apply(options).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(imageView);
    }

    /**
     * Load circle crop.加载圆形图片
     *
     * @param activity  the activity
     * @param imageView the image view
     * @param url       the url
     */
    public void loadRoundedCrop(Activity activity, final ImageView imageView, String url) {
        //设置图片圆角角度
        RoundedCorners roundedCorners = new RoundedCorners(16);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(activity).load(url).apply(options).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(imageView);
    }


    /**
     * Load. 加载正常图片
     *
     * @param activity  the activity
     * @param imageView the image view
     * @param url       the url
     */
    public void load(Activity activity, final ImageView imageView, String url) {
        Glide.with(activity).load(url).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(imageView);
    }

    /**
     * Load. 加载正常图片
     *
     * @param activity  the activity
     * @param imageView the image view
     * @param url       the url
     */
    public void load(Fragment activity, final ImageView imageView, String url) {
        Glide.with(activity).load(url).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(imageView);
    }


}
