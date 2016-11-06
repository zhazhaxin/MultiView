package cn.alien95.multiviewlibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.lemon.multi.MultiView;
import cn.lemon.multi.adapter.MultiAdapter;
import cn.lemon.multi.util.Util;


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

        data.add("http://img03.sogoucdn.com/app/a/100520093/39ccc87f3e85c326-d833987af7322860-4ce19032c3b0f23baadb92fbb834ca57.jpg");
        data.add("http://img03.sogoucdn.com/app/a/100520093/ff997748674109a3-a39fb229dd0dbda7-694fe393ad45dc0aa9ea5a22823a4a89.jpg");
        data.add("http://img02.sogoucdn.com/app/a/100520093/398e25e72e0c6d43-69bdc558c3bd67b0-cfe3bbc83b1b97766b1f563b5a2ca7f7.jpg");
        data.add("http://img03.sogoucdn.com/app/a/100520093/11388287d0e56ad7-53b51a5be5b5a2db-9e20f21c3413f36b211a6543ad164d1f.jpg");
        data.add("http://img03.sogoucdn.com/app/a/100520093/11388287d0e56ad7-53b51a5be5b5a2db-0d4a965d46d4436ed1c7053eccb6fe70.jpg");
        data.add("http://img03.sogoucdn.com/app/a/100520093/33707f33b97c03ef-e989d519207501fc-417f0b65c0bd38f89fa860dc6e331204.jpg");
        data.add("http://img03.sogoucdn.com/app/a/100520093/ea54b1c5225b5b8f-1f7d693ce5c84217-c3bf467271f05ac7fb1b65bcd04075df.jpg");
        data.add("http://img01.sogoucdn.com/app/a/100520093/ea54b1c5225b5b8f-1f7d693ce5c84217-29186a9893391156126abf6b88edb947.jpg");
        data.add("http://img04.sogoucdn.com/app/a/100520024/a9fd5fa28fa88b93b3a551d77d7485af");
        data.add("http://img03.sogoucdn.com/app/a/100520024/5ebc6321d6d250cdc60b60c63d112398");
        data.add("http://img04.sogoucdn.com/app/a/100520024/da58c325457e35bc35ef5b88ff6e8f93");
        data.add("http://img04.sogoucdn.com/app/a/100520024/d8cd08e9ad5e594e6072b79b16a79cb9");
        data.add("http://img03.sogoucdn.com/app/a/100520024/e26644a572d792eb04c758fb7928cf6f");

        multiView.setImages(data);

//        adapter = new MyAdapter(this);
//        multiView.setAdapter(adapter);
//        adapter.addAll(data);
    }


    class MyAdapter extends MultiAdapter<String> {

        private TextView textView;
        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
            textView = (TextView) view.findViewById(R.id.text);
            return view;
        }

        @Override
        public void setData(String object) {
            super.setData(object);
            textView.setText(object);
        }

        @Override
        public void setOnItemClick() {
            super.setOnItemClick();
            Util.Toast("点击事件");
        }
    }

}
