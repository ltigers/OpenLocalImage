package com.cntysoft.ganji.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cntysoft.ganji.R;
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
    private GridView largeView;
    private GridView middleView;
    private GridView smallView;
   
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
        super.onActivityCreated(savedInstanceState);
        spf = getActivity().getApplicationContext().getSharedPreferences("config", Context.MODE_PRIVATE);

        cityLinearLayout = (LinearLayout) view.findViewById(R.id.center_text_container);
        cityTitle = (TextView) view.findViewById(R.id.center_text);
        editSearch = (TextView) view.findViewById(R.id.center_edit);
        viewDivider = view.findViewById(R.id.weather_line);
        verticalSlideView = (VerticalSlideView) view.findViewById(R.id.vertical_slide);
        largeView = (GridView) view.findViewById(R.id.gv_large);
        middleView = (GridView) view.findViewById(R.id.gv_middle);
        smallView = (GridView) view.findViewById(R.id.gv_small);
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
}
