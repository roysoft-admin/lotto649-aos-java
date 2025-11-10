package kr.co.roysoft.lotto649.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import kr.co.roysoft.lotto649.R;

@SuppressLint("AppCompatCustomView")
public class NumberView extends TextView {

    private Context mContext;
    private GradientDrawable drawable;

    public NumberView(Context context) {
        super(context);
        mContext = context;
        init(null);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    public NumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs){
        this.setGravity(Gravity.CENTER);
        drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL); // 동그라미 모양으로 변경
        // 기본 색상: 흰색 배경, 검은색 글씨, 검은색 테두리
        drawable.setColor(Color.WHITE);
        drawable.setStroke(2, Color.BLACK); // 2px 검은색 테두리 추가
        this.setBackground(drawable);
        this.setTextColor(Color.BLACK);
        
        // dp 단위로 완전 고정 크기 설정 (시스템 폰트 무시)
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f);
        this.setTypeface(ResourcesCompat.getFont(getContext(), R.font.inter_bold));
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        // 색상 변경 로직 제거 - 기본 색상 유지
    }

    public void setSize(boolean isLarge){
        if(isLarge){
            // 동그라미 모양이므로 cornerRadius 설정 불필요
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.text_number_selected));
        }
        else{
            // 동그라미 모양이므로 cornerRadius 설정 불필요
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.text_number_default));
        }
    }

    public void setPowerballStyle(boolean isPowerball){
        if(isPowerball){
            // 파워볼 번호: 빨간색 배경, 흰색 글씨, 빨간색 테두리
            drawable.setColor(Color.RED);
            drawable.setStroke(2, Color.RED);
            this.setBackground(drawable);
            this.setTextColor(Color.WHITE);
        }
        else{
            // 일반 번호: 흰색 배경, 검은색 글씨, 검은색 테두리
            drawable.setColor(Color.WHITE);
            drawable.setStroke(2, Color.BLACK);
            this.setBackground(drawable);
            this.setTextColor(Color.BLACK);
        }
    }

    public void setNumberStyle(int number, boolean isLastNumber){
        if(number == 0){
            // 숫자가 없는 경우: 회색 배경, 검은색 글씨, 검은색 테두리
            drawable.setColor(Color.GRAY);
            drawable.setStroke(2, Color.BLACK);
            this.setBackground(drawable);
            this.setTextColor(Color.BLACK);
        }
        else if(isLastNumber){
            // 마지막 번호(number6): 빨간색 배경, 흰색 글씨, 빨간색 테두리
            drawable.setColor(Color.RED);
            drawable.setStroke(2, Color.RED);
            this.setBackground(drawable);
            this.setTextColor(Color.WHITE);
        }
        else{
            // 일반 번호: 흰색 배경, 검은색 글씨, 검은색 테두리
            drawable.setColor(Color.WHITE);
            drawable.setStroke(2, Color.BLACK);
            this.setBackground(drawable);
            this.setTextColor(Color.BLACK);
        }
    }

    public void setLottoBlueStyle(int number){
        if(number == 0){
            drawable.setColor(Color.GRAY);
            drawable.setStroke(2, Color.BLACK);
            this.setBackground(drawable);
            this.setTextColor(Color.BLACK);
            return;
        }
        int blue = mContext.getResources().getColor(R.color.lotto_blue, null);
        drawable.setColor(blue);
        drawable.setStroke(2, blue);
        this.setBackground(drawable);
        this.setTextColor(Color.WHITE);
    }

    public void setLottoGreenStyle(int number){
        if(number == 0){
            drawable.setColor(Color.GRAY);
            drawable.setStroke(2, Color.BLACK);
            this.setBackground(drawable);
            this.setTextColor(Color.BLACK);
            return;
        }
        int green = mContext.getResources().getColor(R.color.lotto_green, null);
        drawable.setColor(green);
        drawable.setStroke(2, green);
        this.setBackground(drawable);
        this.setTextColor(Color.WHITE);
    }

    public void setRedStyle(int number){
        if(number == 0){
            drawable.setColor(Color.GRAY);
            drawable.setStroke(2, Color.BLACK);
            this.setBackground(drawable);
            this.setTextColor(Color.BLACK);
            return;
        }
        drawable.setColor(Color.RED);
        drawable.setStroke(2, Color.RED);
        this.setBackground(drawable);
        this.setTextColor(Color.WHITE);
    }

    public void setRedTextStyle(int number){
        if(number == 0){
            drawable.setColor(Color.GRAY);
            drawable.setStroke(2, Color.BLACK);
            this.setBackground(drawable);
            this.setTextColor(Color.BLACK);
            return;
        }
        drawable.setColor(Color.WHITE);
        drawable.setStroke(2, Color.RED);
        this.setBackground(drawable);
        this.setTextColor(Color.RED);
    }
}
