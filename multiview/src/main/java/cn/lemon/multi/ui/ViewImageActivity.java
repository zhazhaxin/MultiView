package cn.lemon.multi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.lemon.multi.util.Util;
import cn.lemon.multi.R;




/**
 * Created by linlongxin on 2016/1/22.
 */
public class ViewImageActivity extends AppCompatActivity {

    public static final String IMAGES_DATA_LIST = "DATA_LIST";
    public static final String IMAGE_NUM = "IMAGE_NUM";

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TextView number;
    private List<String> data;
    private int position;
    private int dataLength = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        Util.init(this);
        setTitle("返回");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        number = (TextView) findViewById(R.id.number);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = (List<String>) getIntent().getSerializableExtra(IMAGES_DATA_LIST);
        position = getIntent().getIntExtra(IMAGE_NUM, -1);
        dataLength = data.size();

        viewPager.setAdapter(new ImageAdapter());
        viewPager.setCurrentItem(position);
        number.setText(position + 1 + "/" + dataLength);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                number.setText(viewPager.getCurrentItem() + 1 + "/" + dataLength);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    class ImageAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(ViewImageActivity.this);
            image.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            image.setAdjustViewBounds(true);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(ViewImageActivity.this).load(data.get(position)).into(image);
            container.addView(image);
            return image;
        }


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_look_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.save) {
            Util.downloadImage(data.get(viewPager.getCurrentItem()));
        }
        return true;
    }


}
