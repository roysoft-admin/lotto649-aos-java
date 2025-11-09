package kr.co.roysoft.lotto649.view.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import kr.co.roysoft.lotto649.R;
import kr.co.roysoft.lotto649.data.NumberDTO;
import kr.co.roysoft.lotto649.view.text.MediumTextView;

public class MainAiNumberItemView extends LinearLayout {

    private Context mContext;

    private MediumTextView textPowerballDate;
    private MediumTextView textUserName;
    private MediumTextView textResult;

    private NumberDTO number;

    public MainAiNumberItemView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public MainAiNumberItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public MainAiNumberItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    public MainAiNumberItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(Color.TRANSPARENT);

        textPowerballDate = new MediumTextView(mContext);
        textPowerballDate.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textPowerballDate.setLines(1);
        textPowerballDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_ai_item));
        addView(textPowerballDate);

        View view = new View(mContext);
        LayoutParams layoutParams = new LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.px_10), LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        addView(view);

        textUserName = new MediumTextView(mContext);
        textUserName.setLayoutParams(new LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.px_100), LayoutParams.WRAP_CONTENT));
        textUserName.setLines(1);
        textUserName.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_ai_item));
        addView(textUserName);

        View view2 = new View(mContext);
        LayoutParams layoutParams2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams2.weight = 1;
        view2.setLayoutParams(layoutParams2);
        addView(view2);

        textResult = new MediumTextView(mContext);
        textResult.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textResult.setLines(1);
        textResult.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_ai_item));
        textResult.setTextColor(mContext.getResources().getColor(R.color.main_black, null));
        addView(textResult);
    }

    public void setNumber(NumberDTO number) {
        this.number = number;
        settingData();
    }

    private void settingData(){
        if(number.date != null && !number.date.equals("")) {
            textPowerballDate.setText(number.date);
        }
        // 사용자 닉네임 마스킹 노출 (앞 3자리 그대로, 나머지 *)
        if(number.user_nickname != null && !number.user_nickname.equals("")){
            String nickname = number.user_nickname;
            String masked;
            if(nickname.length() <= 3){
                masked = nickname;
            } else {
                String prefix = nickname.substring(0, 3);
                StringBuilder sb = new StringBuilder(prefix);
                for(int i = 3; i < nickname.length(); i++) sb.append('*');
                masked = sb.toString();
            }
            textUserName.setText(masked);
        }
        else{
            textUserName.setText("GEUST");
        }
        textResult.setText(number.getResult());
    }
}
