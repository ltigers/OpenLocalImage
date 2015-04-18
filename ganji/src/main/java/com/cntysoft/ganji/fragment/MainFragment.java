package com.cntysoft.ganji.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cntysoft.ganji.R;
import com.cntysoft.ganji.adapter.LargeGridAdapter;
import com.cntysoft.ganji.adapter.MediumGridAdapter;
import com.cntysoft.ganji.utils.CommonUtils;
import com.cntysoft.ganji.widget.MyGridView;
import com.cntysoft.ganji.widget.VerticalSlideView;

/**
 * @Author：Tiger on 2015/4/14 16:06
 * @Email: ielxhtiger@163.com
 */
public class MainFragment extends Fragment implements View.OnClickListener{
    private SharedPreferences spf;
    private VerticalSlideView verticalSlideView;
    private View view;
    private View viewDivider;
    private LinearLayout cityLinearLayout;
    private TextView cityTitle;
    private TextView editSearch;
    /*private MyGridView largeView;
    private MyGridView middleView;
    private MyGridView smallView;*/
    private TableLayout largeView;
    private TableLayout middleView;
    private TableLayout smallView;
   
    private Context context;
    private String[] entryName = {"全职工作","兼职工作","生活家政","房产","二手车","二手物品"};
    private String[] entryName1 = {"本地服务","人才简历库","宠物","票务",
            "婚恋交友","教育培训","百宝箱","生活电话薄"};
    private int[] entryIcon = {R.drawable.ic_entry_09,R.drawable.ic_entry_23
            ,R.drawable.ic_entry_10,R.drawable.ic_entry_14,R.drawable.ic_entry_11
            ,R.drawable.ic_entry_13,R.drawable.ic_entry_08,R.drawable.ic_entry_07};

    private String[] entryName2 = {"积分","抽奖","短租","贷款"
    ,"拼车","搬家","装修","交友"
    ,"彩票","游戏","查违章","火车票"
    ,"订机票","地铁图"};
    public MainFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        
        if(view == null){
            view = inflater.inflate(R.layout.fragment_main1,null);
        }
        
        ViewGroup parent = (ViewGroup)view.getParent();
        if(parent != null){
            parent.removeView(view);
        }
       
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        super.onActivityCreated(savedInstanceState);
        spf = getActivity().getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);

        cityLinearLayout = (LinearLayout) view.findViewById(R.id.center_text_container);
        cityTitle = (TextView) view.findViewById(R.id.center_text);
        editSearch = (TextView) view.findViewById(R.id.center_edit);
        viewDivider = view.findViewById(R.id.weather_line);
        verticalSlideView = (VerticalSlideView) view.findViewById(R.id.vertical_slide);
        /*largeView = (MyGridView) view.findViewById(R.id.large_entry_table);
          middleView = (MyGridView) view.findViewById(R.id.medium_entry_table);
         smallView = (MyGridView) view.findViewById(R.id.small_entry_table);
        largeView.setAdapter(new LargeGridAdapter(context));
        //initLargeTable();
        middleView.setAdapter(new MediumGridAdapter(context));
       */
        largeView = (TableLayout) view.findViewById(R.id.large_entry_table);
        middleView = (TableLayout) view.findViewById(R.id.medium_entry_table);
        smallView = (TableLayout) view.findViewById(R.id.small_entry_table);
        initLargeTable();
        initMediumTable();
        initSmallTable();
        verticalSlideView.setViewDivider(viewDivider);
        cityTitle.setText(spf.getString("city",""));
        cityLinearLayout.setOnClickListener(this);
        editSearch.setOnClickListener(this);
        verticalSlideView.setOnRefreshListener(new VerticalSlideView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                verticalSlideView.finishRefreshing();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(getActivity() instanceof  ViewClickListener){
            switch (v.getId()){
                case R.id.center_text_container:
                    ((ViewClickListener)getActivity()).switchActivity(1);
                    break;
                case R.id.center_edit:
                    ((ViewClickListener)getActivity()).switchActivity(2);
                    break;
            }
        }
    }
    
    public interface ViewClickListener{
        void switchActivity(int id);
    }
    
   private void initLargeTable(){
       largeView.removeAllViews();
        int n = (entryName.length % 3 == 0)? entryName.length / 3 :entryName.length / 3 + 1;
        for (int i = 0 ; i < n; i++){
            TableRow tableRow = new TableRow(context);
            for (int j = 0; j < 3 ; j++){
                if((i * 3 + j ) < entryName.length) {
                    RelativeLayout view = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.item_large_table, null);
                    TextView textView = (TextView) view.findViewById(R.id.entry_name);
                    ImageView imageView = (ImageView) view.findViewById(R.id.entry_icon);
                    textView.setText(entryName[i * 3 + j]);
                    imageView.setBackgroundResource(R.drawable.near_fangchan_default);
                    view.setBackgroundColor(0xFF20C968);
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, CommonUtils.dip2px(context,70));
                    layoutParams.weight = 1;
                    layoutParams.setMargins(3,3,3,3);
                    tableRow.addView(view,layoutParams);
                }
            }
            tableRow.setBackgroundColor(0xffefefef);
            largeView.addView(tableRow);
        }
    }
    
    private void initMediumTable(){
        middleView.removeAllViews();
        int n = (entryName1.length % 4 == 0)? entryName1.length / 4 : entryName1.length / 4 + 1;
        for (int i = 0 ; i < n; i++){
            TableRow tableRow = new TableRow(context);
            for (int j = 0; j < 4 ; j++){
                if((i * 4 + j ) < entryName1.length) {
                    RelativeLayout view = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.item_medium_table, null);
                    TextView textView = (TextView) view.findViewById(R.id.entry_name);
                    ImageView imageView = (ImageView) view.findViewById(R.id.entry_icon);
                    textView.setText(entryName1[i * 4 + j]);
                    imageView.setBackgroundResource(entryIcon[i * 4 + j]);
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, CommonUtils.dip2px(context,70));
                    layoutParams.weight = 1;
                    layoutParams.setMargins(0,0,1,1);
                    tableRow.addView(view,layoutParams);
                }
            }
            //tableRow.setBackgroundColor(0xffefefef);
            middleView.addView(tableRow);
        }
    }
    
    private void initSmallTable(){
        smallView.removeAllViews();
        int n = (entryName2.length % 4 == 0)? entryName2.length / 4 : entryName2.length / 4 + 1;
        for (int i = 0 ; i < n; i++){
            TableRow tableRow = new TableRow(context);
            /*TableLayout.LayoutParams tabLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tabLayoutParams.setMargins(0,3,0,0);*/
            for (int j = 0; j < 4 ; j++){
                RelativeLayout view = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.item_small_table, null);
                TextView textView = (TextView) view.findViewById(R.id.entry_name);
                ImageView imageView = (ImageView) view.findViewById(R.id.entry_icon);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, CommonUtils.dip2px(context,25));
                layoutParams.weight = 1;
                if((i * 4 + j ) < entryName2.length) {
                    textView.setText(entryName2[i * 4 + j]);
                    imageView.setImageResource(R.drawable.ic_jump_35);                 
                    layoutParams.setMargins(0,0,1,0);
                    tableRow.addView(view,layoutParams);
                }else{
                    textView.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    tableRow.addView(view,layoutParams);
                }
            }       
            smallView.addView(getTableRow());
            smallView.addView(tableRow);
            smallView.addView(getTableRow());   
        }
    }
    
    private TableRow getTableRow(){
        TableRow tableRow1 = new TableRow(context);
        TextView textView1 = new TextView(context);
        TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(context,10));
        tableRow1.setBackgroundResource(R.drawable.bg_white_turn_grey);
        tableRow1.addView(textView1,layoutParams1);
        return tableRow1;
    }
}
