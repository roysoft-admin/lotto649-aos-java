package kr.co.roysoft.lotto649.view.button;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

import androidx.core.content.res.ResourcesCompat;

import kr.co.roysoft.lotto649.R;

public class LargeButton extends androidx.appcompat.widget.AppCompatButton {
    public LargeButton(Context context) {
        super(context);

        init();
    }

    public LargeButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public LargeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        this.setBackground(getResources().getDrawable(R.drawable.back_round_8_main_color, null));
        this.setTextColor(Color.WHITE);
        this.setTypeface(ResourcesCompat.getFont(getContext(), R.font.inter_bold));
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_button_large));
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.setPadding(0,0,0,0);
    }
}
