package kr.co.roysoft.lotto649.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.Enums;

public class BottomBaseActivity extends BaseActivity{

    protected ImageView iconHome;
    protected TextView textHome;
    protected ImageView iconMyNumber;
    protected TextView textMyNumber;
    protected ImageView iconInput;
    protected TextView textInput;
    protected ImageView iconHistory;
    protected TextView textHistory;
    protected ImageView iconMy;
    protected TextView textMy;

    protected int selectedType;

    private View.OnClickListener onClickListenerBottom = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_home){
                if(selectedType == Enums.MENU_HOME) return;
                linkManager.executeMain();
            }
            else if(v.getId() == R.id.btn_my_number){
                if(selectedType == Enums.MENU_MY) return;
                linkManager.executeMy();
            }
            else if(v.getId() == R.id.btn_input){
                if(selectedType == Enums.MENU_INPUT) return;
                linkManager.executeNumberAI();
            }
            else if(v.getId() == R.id.btn_history){
                if(selectedType == Enums.MENU_HISTORY) return;
                linkManager.executeHistory();
            }
            else if(v.getId() == R.id.btn_my){
                if(selectedType == Enums.MENU_SETTING) return;
                linkManager.executeSetting();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupBottom(){
        LayoutInflater inflater = LayoutInflater.from(this);

        View viewBottom = inflater.inflate(R.layout.view_bottom, null);
        FrameLayout viewGroup = (FrameLayout) ((ViewGroup) BottomBaseActivity.this.findViewById(android.R.id.content)).getChildAt(0);
        if(viewGroup != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.gravity = Gravity.BOTTOM;
            viewBottom.setLayoutParams(layoutParams);

            viewGroup.addView(viewBottom);
        }

        iconHome = viewBottom.findViewById(R.id.icon_home);
        textHome = viewBottom.findViewById(R.id.text_home);
        textHome.setText(Enums.MENUS[Enums.MENU_HOME]);
        viewBottom.findViewById(R.id.btn_home).setOnClickListener(onClickListenerBottom);

        iconMyNumber = viewBottom.findViewById(R.id.icon_my_number);
        textMyNumber = viewBottom.findViewById(R.id.text_my_number);
        textMyNumber.setText(Enums.MENUS[Enums.MENU_MY]);
        viewBottom.findViewById(R.id.btn_my_number).setOnClickListener(onClickListenerBottom);

        iconInput = viewBottom.findViewById(R.id.icon_input);
        textInput = viewBottom.findViewById(R.id.text_input);
        textInput.setText(Enums.MENUS[Enums.MENU_INPUT]);
        viewBottom.findViewById(R.id.btn_input).setOnClickListener(onClickListenerBottom);

        iconHistory = viewBottom.findViewById(R.id.icon_history);
        textHistory = viewBottom.findViewById(R.id.text_history);
        textHistory.setText(Enums.MENUS[Enums.MENU_HISTORY]);
        viewBottom.findViewById(R.id.btn_history).setOnClickListener(onClickListenerBottom);

        iconMy = viewBottom.findViewById(R.id.icon_my);
        textMy = viewBottom.findViewById(R.id.text_my);
        textMy.setText(Enums.MENUS[Enums.MENU_SETTING]);
        viewBottom.findViewById(R.id.btn_my).setOnClickListener(onClickListenerBottom);
    }

    protected void setBottomMenu(int type){
        selectedType = type;
        if(type == Enums.MENU_HOME){
            textHome.setTextColor(getResources().getColor(R.color.main_black, null));
            iconHome.setImageResource(R.drawable.icon_home);
        }
        else if(type == Enums.MENU_MY){
            textMyNumber.setTextColor(getResources().getColor(R.color.main_black, null));
            iconMyNumber.setImageResource(R.drawable.icon_favorite);
        }
        else if(type == Enums.MENU_INPUT){
            textInput.setTextColor(getResources().getColor(R.color.main_black, null));
            iconInput.setImageResource(R.drawable.icon_star);
        }
        else if(type == Enums.MENU_HISTORY){
            textHistory.setTextColor(getResources().getColor(R.color.main_black, null));
            iconHistory.setImageResource(R.drawable.icon_book_open);
        }
        else if(type == Enums.MENU_SETTING){
            textMy.setTextColor(getResources().getColor(R.color.main_black, null));
            iconMy.setImageResource(R.drawable.icon_user);
        }
    }
}
