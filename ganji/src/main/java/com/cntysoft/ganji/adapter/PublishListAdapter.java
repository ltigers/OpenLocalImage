package com.cntysoft.ganji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cntysoft.ganji.R;

/**
 * @Author：Tiger on 2015/4/16 15:00
 * @Email: ielxhtiger@163.com
 */
public class PublishListAdapter extends BaseAdapter {

    private Context mContext;
    //可以创建一个类实现
    private int[] imageArray = {R.drawable.publish_icon_jianli,R.drawable.publish_icon_full_time_job_rss
    ,R.drawable.publish_icon_second_hand_goods_rss,R.drawable.publish_icon_home_sale_rss
    ,R.drawable.publish_icon_car_sale_rss,R.drawable.publish_ic_lifestyle
    ,R.drawable.publish_icon_ticket_sale,R.drawable.publish_icon_pet_sale};
    private String[] titleArray = {"全职/兼职简历","全职/兼职招聘","二手物品","房产模块"
    ,"车辆买卖","生活/商务服务","票务","宠物"};
    private String[] desArray = {"学生兼职，技工/工人，销售，司机，服务员，钟点工"
            ,"服务员，销售，技工/工人，厨师，营业员，收银员"
            ,"手机/配件，家用电器，手机号/QQ，二手电脑","出租房，二手房出售，求出租，商铺出租"
    ,"二手汽车，摩托车，电动车，自行车，拼车","搬家，家政月嫂，保洁，维修，租车，美食"
            ,"消费卡/购物卡，门票，其他","宠物狗，宠物救助/赠送，晚赏鸟，观赏鱼，"};
    
    public PublishListAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return imageArray.length;
    }

    @Override
    public Object getItem(int position) {
        return imageArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pub_lv,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.pub_lv_icon);
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.pub_lv_title);
            viewHolder.desView = (TextView) convertView.findViewById(R.id.pub_lv_description);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(imageArray[position]);
        viewHolder.titleView.setText(titleArray[position]);
        viewHolder.desView.setText(desArray[position]);
        return convertView;
    }
    
    static class ViewHolder{
        ImageView imageView;
        TextView titleView;
        TextView desView;
    }
}
