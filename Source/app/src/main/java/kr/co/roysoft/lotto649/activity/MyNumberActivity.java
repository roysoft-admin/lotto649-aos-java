package kr.co.roysoft.lotto649.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.adapter.MyNumberAdapter;
import kr.co.roysoft.lotto649.data.Enums;
import kr.co.roysoft.lotto649.data.JsonDTO;
import kr.co.roysoft.lotto649.data.NumberDTO;
import kr.co.roysoft.lotto649.manager.NetworkManager;

public class MyNumberActivity extends BottomBaseAuthActivity {

    private ListView listMy;
    private MyNumberAdapter adapter;
    private Button btnStart;
    private Button btnEnd;

    private String startDate;
    private String endDate;

    private ArrayList<NumberDTO> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_number);
        setupBottom();
        setBottomMenu(Enums.MENU_MY);
        initStatus();

        listMy = findViewById(R.id.list_number_history);
        adapter = new MyNumberAdapter(this);
        listMy.setAdapter(adapter);
        listMy.setSelector(android.R.color.transparent);
        listMy.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(startDate == null || endDate == null) return;
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

        btnStart = findViewById(R.id.btn_start_date);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // DatePickerDialog 생성
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MyNumberActivity.this,
                        (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                            selectedMonth += 1;
                            startDate = selectedYear + "-" + (selectedMonth < 10 ? "0" + selectedMonth : selectedMonth) + "-" + (selectedDay < 10 ? "0" + selectedDay : selectedDay);
                            if(Integer.parseInt(startDate.replaceAll("-", "")) > Integer.parseInt(endDate.replaceAll("-", ""))){
                                linkManager.executeToastMessage("The start date cannot be later than the end date.");
                                return;
                            }
                            btnStart.setText(startDate.substring(5));
                            initPage();
                            requestList();
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        startDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
        btnStart.setText(startDate.substring(5));

        btnEnd = findViewById(R.id.btn_end_date);
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // DatePickerDialog 생성
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MyNumberActivity.this,
                        (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                            selectedMonth += 1;
                            endDate = selectedYear + "-" + (selectedMonth < 10 ? "0" + selectedMonth : selectedMonth) + "-" + (selectedDay < 10 ? "0" + selectedDay : selectedDay);
                            if(Integer.parseInt(endDate.replaceAll("-", "")) < Integer.parseInt(startDate.replaceAll("-", ""))){
                                linkManager.executeToastMessage("The end date cannot be earlier than the start date.");
                                return;
                            }
                            btnEnd.setText(endDate.substring(5));
                            initPage();
                            requestList();
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        endDate = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
        btnEnd.setText(endDate.substring(5));
    }

    @Override
    protected void init() {
        super.init();
//        if(list.isEmpty()) requestList();
    }

    @Override
    public void onComplete(JsonDTO dto) {
        if(dto.type.equals(NetworkManager.REQUEST_GET_NUMBERS)){
            if(offsetList == 0){
                list = new ArrayList<>();
            }
            list.addAll(dataManager.Numbers);
            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
        else if(dto.type.equals(NetworkManager.REQUEST_DELETE_NUMBERS_NUMBER_ID)){
            for(int i = 0 ; i < list.size() ; i++){
                if(list.get(i).id == dataManager.id){
                    list.remove(i);
                    break;
                }
            }
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
        networkManager.request(this, NetworkManager.REQUEST_GET_NUMBERS, new HashMap<String, String>(){{
            put("limit", "10");
            put("offset", offsetList + "");
            put("user_id", stateManager.USER.id + "");
            put("started_at", startDate);
            put("ended_at", endDate);
        }}, NetworkManager.PARAM_TYPE_NORMAL);
    }
}
