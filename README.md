#MultiView --- Android显示多图或item的View

gradle依赖
```
    compile 'cn.lemon:multiview:0.1.9'
```
    
###MultiView基本使用：

由于依赖了[RestHttp](https://github.com/llxdaxia/RestHttp)，所以初始化:
```
  RestHttp.initialize(this);
  if(BuildConfig.DEBUG){
       RestHttp.setDebug(true,"network");
  }
```

xml布局文件

```xml
    <cn.lemon.multi.MultiView
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

```
 multiView = (multiView) findViewById(R.id.cell_view);
 multiView.setLayoutParams(new LinearLayout.LayoutParams(900, ViewGroup.LayoutParams.WRAP_CONTENT));

 data.add("http://i02.pictn.sogoucdn.com/73a90748d5e19769");
 data.add("http://i01.pictn.sogoucdn.com/e19188bbc3966d6f");
 data.add("http://i02.pictn.sogoucdn.com/85db79c962886004");
 data.add("http://i01.pictn.sogoucdn.com/f44c1591194be8b9");

 multiView.setImages(data);   设置图片资源
```      

如果你不只是显示图片，需要自定义item的情况，这个时候就需要添加一个自定义Adapter继承Adapter：

```java
    class MyAdapter extends Adapter<String> {

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
            //view绑定数据
            textView.setText(object);
        }

        @Override
        public void setOnItemClick() {
            super.setOnItemClick();
            //item点击事件
        }
    }
```

设置Adapter，添加数据就好了

```
   adapter = new MyAdapter(this);
   multiView.setAdapter(adapter);
   adapter.addAll(data);
```

###ViewImageActivity 基本使用：

在manifests文件中添加

```xml
   <activity android:name="cn.lemon.multi.ui.ViewImageActivity"/>
```

不管是否使用MultiView，其实都可以使用ViewImageActivity，只需要在跳转Activity的时候绑定好数据，如：

```
   intent.putExtra(LookImageActivity.IMAGES_DATA_LIST, (Serializable) picUrlData);   //这里的数据集合必须是List<Stirng>
   intent.putExtra(LookImageActivity.IMAGE_NUM, data.indexOf(object));
```
主要还是使用在加载多张图片的时候和MultiView联合使用。

####注意事项

依赖的其他库
```
   compile 'com.android.support:appcompat-v7:23.1.1'
   compile 'com.android.support:support-v4:23.1.1'
   compile 'cn.alien95:resthttp:1.0.5'
```

###[详细用法请看Demo](https://github.com/llxdaxia/MultiView/tree/dev/demo)

####显示图片

<img src="multi_image.png" width="320" height="569" />
<img src="detail.png" width="320" height="569" />

####自定义item

<img src="multi_item.png" width="320" height="569" />