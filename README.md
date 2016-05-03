#MultiView --- Android显示多图或item的View

gradle依赖

    compile 'cn.alien95:multiview:0.1.1'
    
###MultiView基本使用：

xml布局文件

```xml
    <cn.alien95.view.MultiView
        android:id="@+id/multi_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:background="#f1506d"
        app:divideSpace="8dp"
        app:placeholder="@drawable/holder"/>
```

 - 设置item之间的间隔`app:divideSpace="8dp"`
 - 设置占位图`app:placeholder="@drawable/holder"`
 
java代码:

```java
        multiView = (multiView) findViewById(R.id.cell_view);
        multiView.setLayoutParams(new LinearLayout.LayoutParams(900, ViewGroup.LayoutParams.WRAP_CONTENT));

        data.add("http://i02.pictn.sogoucdn.com/73a90748d5e19769");
        data.add("http://i01.pictn.sogoucdn.com/e19188bbc3966d6f");
        data.add("http://i02.pictn.sogoucdn.com/85db79c962886004");
        data.add("http://i01.pictn.sogoucdn.com/f44c1591194be8b9");
        data.add("http://i01.pictn.sogoucdn.com/e3702c8458c6e5ef");
        data.add("http://i03.pictn.sogoucdn.com/38dce26d6171afea");
        data.add("http://i04.pictn.sogoucdn.com/77f75ceaeb21f214");
        data.add("http://i03.pictn.sogoucdn.com/d9bfb2f7c2e2d996");
        data.add("http://i04.pictn.sogoucdn.com/da0909364b5c1724");
        data.add("http://i04.pictn.sogoucdn.com/c12cf1b1698529f4");
        data.add("http://i04.pictn.sogoucdn.com/9be326a0f5fede23");
        data.add("http://i03.pictn.sogoucdn.com/d8ac1d4d17e90755");
        data.add("http://i01.pictn.sogoucdn.com/4ed7d8270541db1c");
        data.add("http://i02.pictn.sogoucdn.com/0ee9291ce95ad25e");
        data.add("http://i02.pictn.sogoucdn.com/3819a032b8f5f79d");
        data.add("http://i04.pictn.sogoucdn.com/354f82e16a4999fa");
        
        multiView.setImages(data);   设置图片资源
```      

如果你不只是显示图片，需要自定义item的情况，这个时候就需要添加一个自定义Adapter继承Adapter：

```java
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
    
    如果要给每个item设置点击事件，就在Adapter的getView方法哪里设置监听就好了。
```

设置Adapter，添加数据就好了

```java
   adapter = new MyAdapter(this);
   multiView.setAdapter(adapter);
   adapter.addAll(data);
```

###ViewImageActivity 基本使用：

在manifests文件中添加

```xml
   <activity android:name="cn.alien95.view.ui.ViewImageActivity"/>
```

不管是否使用MultiView，其实都可以使用ViewImageActivity，只需要在跳转Activity的时候绑定好数据，如：

```java
   intent.putExtra(LookImageActivity.IMAGES_DATA_LIST, (Serializable) picUrlData);   //这里的数据集合必须是List<Stirng>
   intent.putExtra(LookImageActivity.IMAGE_NUM, data.indexOf(object));
```
主要还是使用在加载多张图片的时候和MultiView联合使用。

####注意事项

依赖的其他库
```java
   compile 'com.android.support:appcompat-v7:23.1.1'
   compile 'com.github.bumptech.glide:glide:3.6.1'
   compile 'com.android.support:support-v4:23.1.1'
```

###[Demo](https://github.com/llxdaxia/MultiView/tree/dev/app)

####显示图片

<img src="multi_image.png" width="320" height="569" />
<img src="detail.png" width="320" height="569" />

####自定义item

<img src="multi_item.png" width="320" height="569" />