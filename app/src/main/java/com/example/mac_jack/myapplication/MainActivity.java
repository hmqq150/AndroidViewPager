package com.example.mac_jack.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.mac_jack.myapplication.Util.Data;
import com.example.mac_jack.myapplication.Util.DataAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    private View view1, view2, view3,view4;
    private ViewPager viewPager;  //对应的viewPager

    private List<View> viewList ;//view数组
    private List<String> titleList;
    private StringBuilder stringurl;
    private ListView listVieww;
    private List<Data> list = new ArrayList<Data>();

    private  List<Data> currListData = new ArrayList<Data>();
    private  List<List<Data>> listsum = new ArrayList<>();
    private  List<List<Data>> currlistsum = new ArrayList<>();
    public static final int DOWNLOAD_COMP = 1;
    private int Position = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_COMP:
                    //myBounceView.invalidate();
                    list = (List<Data>) msg.obj;
                    //标题list
                    if( list!=null ){
                        Set<String> strings = new HashSet<String>();
                        for( Data data:list ){
                            strings.add(data.getCategory());
                        }
                        Log.i("1","---------------------");
                        Log.i("1",strings.toString());
                        titleList.clear();
                        for(String s :strings ){
                            titleList.add(s);
                        }

                    }
                    for( String s:titleList ){
                        List<Data> ListData = new ArrayList<Data>();
                        for ( Data data:list ){
                            if( data.getCategory().equals(s) ){
                                ListData.add(data);
                            }
                        }
                        listsum.add(ListData);
                    }

                    currListData = loaddata(listsum.get(Position),null);
                    Log.i("1", Integer.toString(list.size()));
                    //fengchuang shuju
                    for( int i=0;i<titleList.size();i++ ){
                        currlistsum.add(loaddata(listsum.get(i),null));
                    }
                    listVieww.setAdapter(null);
                    DataAdapter dataAdapter = new DataAdapter(currlistsum.get(Position), MainActivity.this);
                    //ListView listView = (ListView)view1.findViewById(R.id.Listview);
                    dataAdapter.notifyDataSetChanged();
                    listVieww.setAdapter(dataAdapter);




                    break;
            }
            super.handleMessage(msg);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取sharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("hm", Context.MODE_PRIVATE);
        //获取editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onGetMESSAGE();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




        viewPager = (ViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater = getLayoutInflater();
//        view1 = inflater.inflate(R.layout.layout1, null);
 //       view2 = inflater.inflate(R.layout.layout2, null);
//        view3 = inflater.inflate(R.layout.layout3, null);
//        view4 = inflater.inflate(R.layout.layout4,null);
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
//        viewList.add(view1);
//        viewList.add(view2);
//        viewList.add(view3);
//        viewList.add(view4);
        view1 = inflater.inflate(R.layout.layoutdisplay,null);
        view2 = inflater.inflate(R.layout.layoutdisplay,null);
        view3 = inflater.inflate(R.layout.layoutdisplay,null);

//        listView = (ListView)findViewById(R.id.Listview);


        Log.i("--",list.toString());
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);


        titleList = new ArrayList<String>();// 每个页面的Title数据

        titleList.add("音箱");
        titleList.add("显示器");
        titleList.add("移动业务");

        PagerAdapter pagerAdapter = new PagerAdapter() {
            LayoutInflater layoutInflater;
            Context context ;
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LayoutInflater inflater = getLayoutInflater();


                container.addView(viewList.get(position));
                View view = inflater.inflate(R.layout.listviewitem,container);
                  listVieww = (ListView)view.findViewById(R.id.Listview);

                return viewList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {

                //return super.getPageTitle(position);
                return titleList.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LayoutInflater inflater = getLayoutInflater();

                final ListView listView;
                currListData.clear();
                DataAdapter dataAdapter;
                switch (position) {

                    case 0:
                        listView = (ListView) view1.findViewById(R.id.Listview);
                        //currlistsum.set(0,loaddata(listsum.get(0),null));
                        dataAdapter = new DataAdapter(currlistsum.get(0), MainActivity.this);
                        break;
                    case 1:
                        listView = (ListView) view2.findViewById(R.id.Listview);
                        //currlistsum.add(loaddata(listsum.get(1),null));
                        dataAdapter = new DataAdapter(currlistsum.get(1), MainActivity.this);
                        break;
                    case 2:
                        listView = (ListView) view3.findViewById(R.id.Listview);
                        //currlistsum.set(2,loaddata(listsum.get(2),null));

                        dataAdapter = new DataAdapter(currlistsum.get(2), MainActivity.this);
                        break;
                    default:
                        listView = (ListView) view1.findViewById(R.id.Listview);
                       //currlistsum.set(0,loaddata(listsum.get(0),null));
                        dataAdapter = new DataAdapter(currlistsum.get(0), MainActivity.this);
                }


//                String currTitle = titleList.get(position);
                //String currTitle = "显示器";

//                for ( Data data:list ){
//                    if( data.getCategory().equals(currTitle) ){
//                        currListData.add(data);
//                    }
//                }
                listView.setAdapter(null);
                //listview 适配器

                //ListView listView = (ListView)view1.findViewById(R.id.Listview);
                listVieww = listView;
                dataAdapter.notifyDataSetChanged();
                listView.setAdapter(dataAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = list.get(position).getPage_url();

//                            Intent intent = new Intent(MainActivity.this,WebDisplayActivity.class);
//                            intent.putExtra("weburl",str);
//                            Log.e("1111",str);
//                            startActivity(intent);

                        Uri uri = Uri.parse(str);//要跳转的网址
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);

                    }

                });
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    private int oldVisibleItem = 0;
                    private boolean touchFlg = true;

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    }
                });
                listView.setOnTouchListener(new View.OnTouchListener() {
                    float lastY;
                    boolean isFirstClick = true;
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(isFirstClick){
                            lastY=event.getY();
                            isFirstClick=false;//初始值是true，此处置为false。
                        }

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_MOVE:
                                float moveY = event.getY();
                                Log.e("moveY_START", moveY + "");
                                if (moveY < lastY) {
                                Log.e("1","上划");
                                    // 加载六条数据
                                    currlistsum.set(Position,loaddata(listsum.get(Position),currlistsum.get(Position)));
                                    DataAdapter dataAdapter = new DataAdapter(currlistsum.get(Position), MainActivity.this);
                                    dataAdapter.notifyDataSetChanged();
                                    listView.setAdapter(dataAdapter);
                                }else{
                                    Log.e("1","下划");


                                    //加载首6条数据
                                    currlistsum.set(Position,loaddata(listsum.get(Position),null));
                                    DataAdapter dataAdapter = new DataAdapter(currlistsum.get(Position), MainActivity.this);
                                    dataAdapter.notifyDataSetChanged();
                                    listView.setAdapter(dataAdapter);
                                }
                                lastY = moveY;
                                break;
                        }
                        return false;
                    }
                });
                listView.setAdapter(dataAdapter);
                Position = position;

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //Log.i("k",stringurl.toString());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onGetMESSAGE(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                BufferedReader bufferedReader = null;


                try{
                    URL url = new URL("http://mingke.veervr.tv:1920/test");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(8000);
                    httpURLConnection.setConnectTimeout(8000);

                    InputStream inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder  = new StringBuilder();
                    String line;
                    while( (line = bufferedReader.readLine())!=null ){
                        stringBuilder.append(line);

                    }
                    showResponse(stringBuilder);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void showResponse(final StringBuilder stringBuilder){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                System.out.print(string);
//                Log.i("--","------------------------");
                Log.i("ww",stringBuilder.toString());
                stringurl = stringBuilder;
                try{
                    JSONObject jsonObject =  new JSONObject(stringurl.toString());
                    JSONArray jsonArray = (JSONArray) jsonObject.get("data");

                    //
                    for(int i=0;i<jsonArray.length();i++){
                        Data data = new Data();
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                        data.setThumb_url(jsonObject1.get("thumb_url").toString());
                        data.setTitle(jsonObject1.get("title").toString());
                        data.setPage_url(jsonObject1.get("page_url").toString());
                        data.setCategory(jsonObject1.get("category").toString());
                        data.setId(jsonObject1.getInt("id"));
                        list.add(data);
                    }


                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = list;
                message.what = 1;
                handler.sendMessage(message);



            }
        });
    }
    public List<Data> loaddata(List<Data> totallist,List<Data> list){
        //totallist 总数据  list 传入的待追加数据（若第一次传入，则为null ）
        List<Data> list1 = new ArrayList<>();
if( list!=null ){
    int count = totallist.size()-list.size();
    if(count <=6){
        return totallist;
    }else{
        for( int i=0;i<6;i++ ){
            list.add(totallist.get(list.size()+i));
        }
        return list;
    }
}else{
    if( totallist.size()<=6 ){
        return totallist;
    }else{
        for( int i=0;i<6;i++ ){
            list1.add(totallist.get(i));
        }
        return list1;
    }
}


    }
}
