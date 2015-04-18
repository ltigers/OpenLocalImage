package com.cntysoft.ganji.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cntysoft.ganji.R;
import com.cntysoft.ganji.adapter.PublishListAdapter;
import com.cntysoft.ganji.utils.CommonUtils;

/**
 * @Author：Tiger on 2015/4/16 14:30
 * @Email: ielxhtiger@163.com
 */
public class PublishFragment extends Fragment {

    private static final String TAG = "PublishFragment";

    private ListView listView;
    private TextView textView;
    private View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
       
        if(view == null){
            view = inflater.inflate(R.layout.fragment_publish,null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent != null){
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView = (TextView) view.findViewById(R.id.my_pub);
        listView = (ListView) view.findViewById(R.id.pub_lv_info);
        listView.setAdapter(new PublishListAdapter(getActivity()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    showDialog(1,"选择简历类型",new String[]{"全职简历","兼职简历"});
                }else if(position ==1){
                    showDialog(2,"选择招聘类型",new String[]{"全职招聘","兼职招聘"});
                }else{
                    Toast.makeText(getActivity(),"position"+ position,Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG,"onDestoryView发生");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestory发生");
        super.onDestroy();
    }

    //private AlertDialog dialog;
    private Dialog dialog;
    private TextView titleTextView;
    private TextView leftBtn;
    private TextView firstTextView;
    private TextView secondTextView;
    private void showDialog(final int who,String title,String[] item){
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.dialog);
        View contentView = View.inflate(getActivity(),R.layout.dialog_pub_two_choice,null);
        titleTextView = (TextView) contentView.findViewById(R.id.title_choices);
        leftBtn = (TextView) contentView.findViewById(R.id.left_text_btn);
        firstTextView = (TextView) contentView.findViewById(R.id.choice_one_text);
        secondTextView = (TextView) contentView.findViewById(R.id.choice_two_text);
        titleTextView.setText(title);
        firstTextView.setText(item[0]);
        secondTextView.setText(item[1]);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        firstTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(who == 1){
                    Toast.makeText(getActivity(),"简历被点击",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"招聘被点击",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        secondTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(who == 1){
                    Toast.makeText(getActivity(),"简历被点击",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"招聘被点击",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        //dialog = builder.create();
        // dialog.setView(contentView,0,0,0,0);
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        dialog = new Dialog(getActivity(),R.style.dialog);
        dialog.setContentView(contentView,new ViewGroup.LayoutParams(width - 60, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.show();
    }
}
