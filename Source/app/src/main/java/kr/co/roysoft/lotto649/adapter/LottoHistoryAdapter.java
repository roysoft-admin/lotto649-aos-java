package kr.co.roysoft.lotto649.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.co.roysoft.lotto649.data.Lotto649DTO;
import kr.co.roysoft.lotto649.view.custom.LottoItemView;

public class LottoHistoryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Lotto649DTO> list;

    public LottoHistoryAdapter(Context context){
        mContext = context;
        list = new ArrayList<>();
    }

    public void setData(ArrayList<Lotto649DTO> list){
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
        LottoItemView view;
        if (convertView == null) {
            view = new LottoItemView(mContext);
        } else {
            view = (LottoItemView) convertView;
        }

        final Lotto649DTO lotto = list.get(position);
        view.setLotto(lotto);
        if(position % 10 == 4){
            view.setAd(true);
        }
        else{
            view.setAd(false);
        }

        return view;
    }
}
