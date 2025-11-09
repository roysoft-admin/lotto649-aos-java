package kr.co.roysoft.lotto649.view.button;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

import androidx.core.content.res.ResourcesCompat;

import kr.co.roysoft.lotto649.R;

public class BorderSmallButton extends androidx.appcompat.widget.AppCompatButton {
    public BorderSmallButton(Context context) {
        super(context);

        init();
    }

    public BorderSmallButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public BorderSmallButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        this.setBackground(getResources().getDrawable(R.drawable.back_round_border_8_white, null));
        this.setTextColor(getResources().getColor(R.color.main_black, null));
        this.setTypeface(ResourcesCompat.getFont(getContext(), R.font.inter_bold));
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_button_small));
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.setPadding(0,0,0,0);
    }
}
