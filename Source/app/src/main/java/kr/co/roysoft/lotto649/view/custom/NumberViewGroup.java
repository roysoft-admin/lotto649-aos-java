package kr.co.roysoft.lotto649.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import kr.co.roysoft.lotto649.R;

public class NumberViewGroup extends LinearLayout {

    private Context mContext;

    private NumberView textNumber1;
    private NumberView textNumber2;
    private NumberView textNumber3;
    private NumberView textNumber4;
    private NumberView textNumber5;
    private NumberView textNumber6;
    private NumberView textNumberBB;

    public NumberViewGroup(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public NumberViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public NumberViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    public NumberViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs){
        setOrientation(HORIZONTAL);

        textNumber1 = new NumberView(mContext);
        textNumber1.setLayoutParams(new LinearLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.px_30), mContext.getResources().getDimensionPixelSize(R.dimen.px_30)));
        addView(textNumber1);

        View viewPadding = new View(mContext);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 1);
        layoutParams.weight = 1;
        viewPadding.setLayoutParams(layoutParams);
        addView(viewPadding);

        textNumber2 = new NumberView(mContext);
        textNumber2.setLayoutParams(new LinearLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.px_30), mContext.getResources().getDimensionPixelSize(R.dimen.px_30)));
        addView(textNumber2);

        View viewPadding2 = new View(mContext);
        viewPadding2.setLayoutParams(layoutParams);
        addView(viewPadding2);

        textNumber3 = new NumberView(mContext);
        textNumber3.setLayoutParams(new LinearLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.px_30), mContext.getResources().getDimensionPixelSize(R.dimen.px_30)));
        addView(textNumber3);

        View viewPadding3 = new View(mContext);
        viewPadding3.setLayoutParams(layoutParams);
        addView(viewPadding3);

        textNumber4 = new NumberView(mContext);
        textNumber4.setLayoutParams(new LinearLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.px_30), mContext.getResources().getDimensionPixelSize(R.dimen.px_30)));
        addView(textNumber4);

        View viewPadding4 = new View(mContext);
        viewPadding4.setLayoutParams(layoutParams);
        addView(viewPadding4);

        textNumber5 = new NumberView(mContext);
        textNumber5.setLayoutParams(new LinearLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.px_30), mContext.getResources().getDimensionPixelSize(R.dimen.px_30)));
        addView(textNumber5);

        View viewPadding5 = new View(mContext);
        viewPadding5.setLayoutParams(layoutParams);
        addView(viewPadding5);

        textNumber6 = new NumberView(mContext);
        textNumber6.setLayoutParams(new LinearLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.px_30), mContext.getResources().getDimensionPixelSize(R.dimen.px_30)));
        addView(textNumber6);

        View viewPadding6 = new View(mContext);
        viewPadding6.setLayoutParams(layoutParams);
        addView(viewPadding6);

        textNumberBB = new NumberView(mContext);
        textNumberBB.setLayoutParams(new LinearLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.px_30), mContext.getResources().getDimensionPixelSize(R.dimen.px_30)));
        addView(textNumberBB);
    }

    public void setSize(boolean isLarge){
        int height;
        if(isLarge){
            height = getResources().getDimensionPixelSize(R.dimen.px_45);
        }
        else{
            height = getResources().getDimensionPixelSize(R.dimen.px_30);
        }

        LayoutParams layoutParams1 = (LayoutParams) textNumber1.getLayoutParams();
        layoutParams1.height = height;
        layoutParams1.width = height;
        textNumber1.setLayoutParams(layoutParams1);
        textNumber1.setSize(isLarge);

        LayoutParams layoutParams2 = (LayoutParams) textNumber2.getLayoutParams();
        layoutParams2.height = height;
        layoutParams2.width = height;
        textNumber2.setLayoutParams(layoutParams2);
        textNumber2.setSize(isLarge);

        LayoutParams layoutParams3 = (LayoutParams) textNumber3.getLayoutParams();
        layoutParams3.height = height;
        layoutParams3.width = height;
        textNumber3.setLayoutParams(layoutParams3);
        textNumber3.setSize(isLarge);

        LayoutParams layoutParams4 = (LayoutParams) textNumber4.getLayoutParams();
        layoutParams4.height = height;
        layoutParams4.width = height;
        textNumber4.setLayoutParams(layoutParams4);
        textNumber4.setSize(isLarge);

        LayoutParams layoutParams5 = (LayoutParams) textNumber5.getLayoutParams();
        layoutParams5.height = height;
        layoutParams5.width = height;
        textNumber5.setLayoutParams(layoutParams5);
        textNumber5.setSize(isLarge);

        LayoutParams layoutParams6 = (LayoutParams) textNumber6.getLayoutParams();
        layoutParams6.height = height;
        layoutParams6.width = height;
        textNumber6.setLayoutParams(layoutParams6);
        textNumber6.setSize(isLarge);

        LayoutParams layoutParamsBB = (LayoutParams) textNumberBB.getLayoutParams();
        layoutParamsBB.height = height;
        layoutParamsBB.width = height;
        textNumberBB.setLayoutParams(layoutParamsBB);
        textNumberBB.setSize(isLarge);
    }

    public void setNumbers(int num1, int num2, int num3, int num4, int num5, int num6) {
        // NumberDTO용: 6개 번호만 표시, 기존 스타일 유지
        textNumber1.setText(num1 == 0 ? "-" : num1 + "");
        textNumber1.setNumberStyle(num1, false);
        
        textNumber2.setText(num2 == 0 ? "-" : num2 + "");
        textNumber2.setNumberStyle(num2, false);
        
        textNumber3.setText(num3 == 0 ? "-" : num3 + "");
        textNumber3.setNumberStyle(num3, false);
        
        textNumber4.setText(num4 == 0 ? "-" : num4 + "");
        textNumber4.setNumberStyle(num4, false);
        
        textNumber5.setText(num5 == 0 ? "-" : num5 + "");
        textNumber5.setNumberStyle(num5, false);
        
        textNumber6.setText(num6 == 0 ? "-" : num6 + "");
        textNumber6.setNumberStyle(num6, false);
        
        // bb는 숨김
        textNumberBB.setText("-");
        textNumberBB.setVisibility(GONE);
    }

    public void setNumbersBlue(int num1, int num2, int num3, int num4, int num5, int num6) {
        // NumberDTO용: 6개 번호를 파란색 스타일로 표시, bb는 숨김
        textNumber1.setText(num1 == 0 ? "-" : num1 + "");
        textNumber1.setLottoBlueStyle(num1);

        textNumber2.setText(num2 == 0 ? "-" : num2 + "");
        textNumber2.setLottoBlueStyle(num2);

        textNumber3.setText(num3 == 0 ? "-" : num3 + "");
        textNumber3.setLottoBlueStyle(num3);

        textNumber4.setText(num4 == 0 ? "-" : num4 + "");
        textNumber4.setLottoBlueStyle(num4);

        textNumber5.setText(num5 == 0 ? "-" : num5 + "");
        textNumber5.setLottoBlueStyle(num5);

        textNumber6.setText(num6 == 0 ? "-" : num6 + "");
        textNumber6.setLottoBlueStyle(num6);

        textNumberBB.setText("-");
        textNumberBB.setVisibility(GONE);
    }

    public void setLotto649Numbers(int n1, int n2, int n3, int n4, int n5, int n6, int bb) {
        // Lotto649DTO용: 6개 번호 + 보너스 표시 (n1~n6: 파란색, bb: 초록색)
        textNumber1.setText(n1 == 0 ? "-" : n1 + "");
        textNumber1.setLottoBlueStyle(n1);

        textNumber2.setText(n2 == 0 ? "-" : n2 + "");
        textNumber2.setLottoBlueStyle(n2);

        textNumber3.setText(n3 == 0 ? "-" : n3 + "");
        textNumber3.setLottoBlueStyle(n3);

        textNumber4.setText(n4 == 0 ? "-" : n4 + "");
        textNumber4.setLottoBlueStyle(n4);

        textNumber5.setText(n5 == 0 ? "-" : n5 + "");
        textNumber5.setLottoBlueStyle(n5);

        textNumber6.setText(n6 == 0 ? "-" : n6 + "");
        textNumber6.setLottoBlueStyle(n6);

        textNumberBB.setText(bb == 0 ? "-" : bb + "");
        textNumberBB.setLottoGreenStyle(bb);
        textNumberBB.setVisibility(VISIBLE);
    }

    public boolean isNumber(){
        if(textNumber1.getText().toString().equals("-")
                || textNumber2.getText().toString().equals("-")
                || textNumber3.getText().toString().equals("-")
                || textNumber4.getText().toString().equals("-")
                || textNumber5.getText().toString().equals("-")
                || textNumber6.getText().toString().equals("-")){
            return false;
        }
        else{
            return true;
        }
    }

    public int[] getNumbers(){
        return new int[]{
                convertInt(textNumber1.getText().toString()),
                convertInt(textNumber2.getText().toString()),
                convertInt(textNumber3.getText().toString()),
                convertInt(textNumber4.getText().toString()),
                convertInt(textNumber5.getText().toString()),
                convertInt(textNumber6.getText().toString())};
    }

    private int convertInt(String str){
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
