package kr.co.roysoft.lotto649.view.text;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import kr.co.roysoft.lotto649.R;

public class LargeTextView extends androidx.appcompat.widget.AppCompatTextView {
    public LargeTextView(Context context) {
        super(context);

        init();
    }

    public LargeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public LargeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        // dp 단위 사용으로 시스템 폰트 크기 설정에 영향받지 않음
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_large));
        this.setTypeface(ResourcesCompat.getFont(getContext(), R.font.inter_bold));
        this.setTextColor(getResources().getColor(R.color.main_black, null));
    }
}
