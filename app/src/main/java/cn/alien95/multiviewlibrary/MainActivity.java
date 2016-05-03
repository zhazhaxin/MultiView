package cn.alien95.multiviewlibrary;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.alien95.view.MultiView;
import cn.alien95.view.adapter.Adapter;
import cn.alien95.view.util.Util;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MultiView multiView;
    private List<String> data = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("MultiView");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        multiView = (MultiView) findViewById(R.id.multi_view);

        data.add("http://i02.pictn.sogoucdn.com/73a90748d5e19769");
        data.add("http://i01.pictn.sogoucdn.com/e19188bbc3966d6f");
        data.add("http://i02.pictn.sogoucdn.com/85db79c962886004");
        data.add("http://i01.pictn.sogoucdn.com/f44c1591194be8b9");
        data.add("http://i01.pictn.sogoucdn.com/e3702c8458c6e5ef");
        data.add("http://i03.pictn.sogoucdn.com/38dce26d6171afea");
        data.add("http://i04.pictn.sogoucdn.com/77f75ceaeb21f214");
        data.add("http://i03.pictn.sogoucdn.com/d9bfb2f7c2e2d996");
//        data.add("http://i04.pictn.sogoucdn.com/da0909364b5c1724");
//        data.add("http://i04.pictn.sogoucdn.com/c12cf1b1698529f4");
//        data.add("http://i04.pictn.sogoucdn.com/9be326a0f5fede23");
//        data.add("http://i03.pictn.sogoucdn.com/d8ac1d4d17e90755");
//        data.add("http://i01.pictn.sogoucdn.com/4ed7d8270541db1c");
//        data.add("http://i02.pictn.sogoucdn.com/0ee9291ce95ad25e");
//        data.add("http://i02.pictn.sogoucdn.com/3819a032b8f5f79d");
//        data.add("http://i04.pictn.sogoucdn.com/354f82e16a4999fa");

        multiView.setImages(data);

//        adapter = new MyAdapter(this);
//        multiView.setAdapter(adapter);
//        adapter.addAll(data);
    }


    class MyAdapter extends Adapter<String> {

        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(ViewGroup parent, int position) {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setTextSize(16);
            textView.setText("MultiView");
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setBackgroundColor(Color.parseColor("#FF2F4F89"));
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.Toast("点击事件");
                }
            });
            return textView;
        }
    }

}
