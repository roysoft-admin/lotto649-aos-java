package kr.co.roysoft.lotto649.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.Enums;
import kr.co.roysoft.lotto649.data.NumberDTO;
import kr.co.roysoft.lotto649.view.custom.MyNumberItemView;
import kr.co.roysoft.lotto649.view.custom.NumberViewGroup;

public class MyNumberAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<NumberDTO> list;

    public MyNumberAdapter(Context context){
        mContext = context;
        list = new ArrayList<>();
    }

    public void setData(ArrayList<NumberDTO> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyNumberItemView view;
        if (convertView == null) {
            view = new MyNumberItemView(mContext);
        } else {
            view = (MyNumberItemView) convertView;
        }

        final NumberDTO number = list.get(position);
        view.setNumber(number);
        view.setMore();
        if(position % 10 == 4){
            view.setAd(true);
        }
        else{
            view.setAd(false);
        }

        return view;
    }
}
