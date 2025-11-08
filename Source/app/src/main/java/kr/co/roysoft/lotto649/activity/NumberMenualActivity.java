package kr.co.roysoft.lotto649.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.Enums;
import kr.co.roysoft.lotto649.data.JsonDTO;
import kr.co.roysoft.lotto649.data.NumberDTO;
import kr.co.roysoft.lotto649.manager.NetworkManager;

public class NumberMenualActivity  extends BaseActivity {

    private TextView textLottoCount;
    private LinearLayout frameList;
    private ArrayList<View> listNumber;

    private int minValue = 1;
    private int maxValue = 50;
    private InputFilter[] inputFilter = new InputFilter[]{new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                String input = dest.toString() + source.toString();
                int value = Integer.parseInt(input);
                if (value >= minValue && value <= maxValue) {
                    return null;
                }
            } catch (NumberFormatException e) {
            }
            return "";
        }
    }};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_manual);
        initStatus();

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textLottoCount = findViewById(R.id.text_lotto_count);
        // StateManager의 LOTTO_DATE 사용
        if(stateManager.LOTTO_DATE != null && !stateManager.LOTTO_DATE.equals("")) {
            textLottoCount.setText(stateManager.LOTTO_DATE);
        } else {
            textLottoCount.setText("Next Drawing");
        }

        frameList = findViewById(R.id.frame_list);

        listNumber = new ArrayList();

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber();
            }
        });

        findViewById(R.id.btn_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postNumber();
            }
        });

        addNumber();
    }

    @Override
    public void onComplete(JsonDTO dto) {
        if(dto.type.equals(NetworkManager.REQUEST_POST_NUMBERS)){
            linkManager.executeToastMessage("The number information has been entered.");
            linkManager.executeMain();
        }
        else {
            super.onComplete(dto);
        }
    }

    private void addNumber(){
        View view = LayoutInflater.from(this).inflate(R.layout.view_input_number, null);
        ((EditText) view.findViewById(R.id.edit_number_1)).setFilters(inputFilter);
        ((EditText) view.findViewById(R.id.edit_number_2)).setFilters(inputFilter);
        ((EditText) view.findViewById(R.id.edit_number_3)).setFilters(inputFilter);
        ((EditText) view.findViewById(R.id.edit_number_4)).setFilters(inputFilter);
        ((EditText) view.findViewById(R.id.edit_number_5)).setFilters(inputFilter);
        ((EditText) view.findViewById(R.id.edit_number_6)).setFilters(inputFilter);
        listNumber.add(view);
        frameList.addView(view);

        final View btnDelete = view.findViewById(R.id.btn_delete);
        btnDelete.setTag(listNumber.size() - 1);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                frameList.removeView(listNumber.get(position));
                listNumber.remove(position);
                for(int i = 0 ; i < listNumber.size() ; i++){
                    View view1 = listNumber.get(i);
                    view1.findViewById(R.id.btn_delete).setTag(i);
                }
            }
        });
    }

    private void postNumber(){
        if(isNetworking) return;
        dataManager.PostNumbers = new ArrayList<>();
        for(int i = 0 ; i < listNumber.size() ; i++){
            View view = listNumber.get(i);
            int number1 = getTextNumber(view.findViewById(R.id.edit_number_1));
            int number2 = getTextNumber(view.findViewById(R.id.edit_number_2));
            int number3 = getTextNumber(view.findViewById(R.id.edit_number_3));
            int number4 = getTextNumber(view.findViewById(R.id.edit_number_4));
            int number5 = getTextNumber(view.findViewById(R.id.edit_number_5));
            int number6 = getTextNumber(view.findViewById(R.id.edit_number_6));
            if(number1 == -1
                || number2 == -1
                || number3 == -1
                || number4 == -1
                || number5 == -1
                || number6 == -1){
                linkManager.executeToastMessage("Invalid value entered.");
                return;
            }
            else if(compareNumbers(number1, number2, number3, number4, number5, number6)){
                linkManager.executeToastMessage("Please enter a different number in each field.");
                return;
            }
            else if(isOver(number1)
                    || isOver(number2)
                    || isOver(number3)
                    || isOver(number4)
                    || isOver(number5)
                    || isOver(number6)){
                linkManager.executeToastMessage("Please enter a number between 1 and 50.");
                return;
            }
            else{
                NumberDTO number = new NumberDTO();
                number.n1 = number1;
                number.n2 = number2;
                number.n3 = number3;
                number.n4 = number4;
                number.n5 = number5;
                number.n6 = number6;
                if(stateManager.LOTTO_DATE != null && !stateManager.LOTTO_DATE.equals("")) {
                    number.date = stateManager.LOTTO_DATE;
                }
                number.input_type = Enums.INPUT_TYPE_USER;
                dataManager.PostNumbers.add(number);
            }
        }
        if(dataManager.PostNumbers.size() > 0) {
            networkManager.request(this, NetworkManager.REQUEST_POST_NUMBERS, new HashMap<String, String>(){{
                put("user_id", stateManager.USER.id + "");
            }}, NetworkManager.PARAM_TYPE_BODY);
        }
        else{
            linkManager.executeToastMessage("Invalid value entered.");
        }
    }

    private int getTextNumber(TextView textView){
        if(textView.getText().equals("")){
            return -1;
        }
        else{
            try {
                return Integer.parseInt(textView.getText().toString());
            }
            catch (Exception e){
                return -1;
            }
        }
    }

    private boolean isOver(int number){
        if(number < 1 || number > 50){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean compareNumbers(int number1, int number2, int number3, int number4, int number5, int number6){
        if(number1 == number2
                || number1 == number3
                || number1 == number4
                || number1 == number5
                || number1 == number6
                || number2 == number3
                || number2 == number4
                || number2 == number5
                || number2 == number6
                || number3 == number4
                || number3 == number5
                || number3 == number6
                || number4 == number5
                || number4 == number6
                || number5 == number6){
            return true;
        }
        else{
            return false;
        }
    }
}
