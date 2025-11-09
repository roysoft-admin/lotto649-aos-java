package kr.co.roysoft.lotto649.activity;

import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.adapter.LottoHistoryAdapter;
import kr.co.roysoft.lotto649.data.Enums;
import kr.co.roysoft.lotto649.data.JsonDTO;
import kr.co.roysoft.lotto649.data.Lotto649DTO;
import kr.co.roysoft.lotto649.manager.NetworkManager;

public class LottoHistoryActivity  extends BottomBaseAuthActivity {

    private ListView listHistory;
    private LottoHistoryAdapter adapter;

    private ArrayList<Lotto649DTO> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotto_history);
        setupBottom();
        setBottomMenu(Enums.MENU_HISTORY);
        initStatus();

        listHistory = findViewById(R.id.list_lotto_history);
        adapter = new LottoHistoryAdapter(this);
        listHistory.setAdapter(adapter);
        listHistory.setSelector(android.R.color.transparent);
        listHistory.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount){
                    if(prevLastItemForEndOfScroll != lastItem){
                        prevLastItemForEndOfScroll = lastItem;
                        offsetList = list.size();
                        requestList();
                    }
                }
            }
        });
    }

    @Override
    protected void init() {
        super.init();
//        if(list.isEmpty()) requestList();
    }

    @Override
    public void onComplete(JsonDTO dto) {
        if(dto.type.equals(NetworkManager.REQUEST_GET_LOTTO649S)){
            if(offsetList == 0){
                list = new ArrayList<>();
            }
            list.addAll(dataManager.Lottos);
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
        else {
            super.onComplete(dto);
        }
    }

    private void requestList(){
        if(stateManager.USER == null) return;
        hideKeyboard();
        networkManager.request(this, NetworkManager.REQUEST_GET_LOTTO649S, new HashMap<String, String>(){{
            put("limit", "10");
            put("offset", offsetList + "");
        }}, NetworkManager.PARAM_TYPE_NORMAL);
    }
}
