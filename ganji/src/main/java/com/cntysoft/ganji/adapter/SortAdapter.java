package com.cntysoft.ganji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cntysoft.ganji.R;
import com.cntysoft.ganji.model.City;

import java.util.List;

/**
 * @Author：Tiger on 2015/4/9 17:02
 * @Email: ielxhtiger@163.com
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
    
    private List<City> cityList;
    private Context mContext;

    public SortAdapter(Context mContext, List<City> list) {
        this.mContext = mContext;
        this.cityList = list;
    }
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<City> list){
        this.cityList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        //City city = cityList.get(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.titleDivider = convertView.findViewById(R.id.title_divider);
            viewHolder.catalog = (TextView) convertView.findViewById(R.id.catalog);
            viewHolder.catalogDivider = convertView.findViewById(R.id.catalog_divider);
            viewHolder.catalogDivider1 = convertView.findViewById(R.id.catalog_divider1);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //根据position获取当前的分类块
        int section = getSectionForPosition(position);
        if(position == 0){
            viewHolder.catalog.setText("定位当前所在城市");
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.titleDivider.setVisibility(View.GONE);
            viewHolder.title.setText(cityList.get(position).getName());
        }else if(position == 1){
            viewHolder.catalog.setText("热门城市");
            viewHolder.catalog.setVisibility(View.VISIBLE);
            viewHolder.title.setText(cityList.get(position).getName());
            viewHolder.titleDivider.setVisibility(View.VISIBLE);
            viewHolder.catalogDivider.setVisibility(View.VISIBLE);
            viewHolder.catalogDivider1.setVisibility(View.VISIBLE);
        }else{
            if(position == getPositionForSection(section)){
                viewHolder.catalog.setText(cityList.get(position).getSortKey());
                viewHolder.catalog.setVisibility(View.VISIBLE);
                viewHolder.catalogDivider.setVisibility(View.VISIBLE);
                viewHolder.catalogDivider1.setVisibility(View.VISIBLE);
                viewHolder.titleDivider.setVisibility(View.VISIBLE);
            }else{
                viewHolder.catalog.setVisibility(View.GONE);
                viewHolder.catalogDivider.setVisibility(View.GONE);
                viewHolder.catalogDivider1.setVisibility(View.GONE);
            }
            viewHolder.titleDivider.setVisibility(View.VISIBLE);
            viewHolder.title.setText(cityList.get(position).getName());
        }
        if(position + 1 < cityList.size()){
            int nextSection = getSectionForPosition(position + 1);
            int nextPosition = getPositionForSection(nextSection);
            if(nextPosition == position +1){
                viewHolder.titleDivider.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据分类块获取其第一次出现该首字母的位置
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for(int i = 0 ; i< getCount();i++){
            String sortKey = cityList.get(i).getSortKey();
            char c = sortKey.charAt(0);
            if(sectionIndex == c){
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    @Override
    public int getSectionForPosition(int position) {
        return cityList.get(position).getSortKey().charAt(0);
    }
    
    static class ViewHolder{
        TextView catalog;
        TextView title;
        View catalogDivider;
        View titleDivider;
        View catalogDivider1;
    }
}
