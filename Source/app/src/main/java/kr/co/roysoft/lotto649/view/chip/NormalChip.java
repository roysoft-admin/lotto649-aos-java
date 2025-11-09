package kr.co.roysoft.lotto649.view.chip;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import kr.co.roysoft.lotto649.R;

public class NormalChip extends androidx.appcompat.widget.AppCompatTextView {
    public NormalChip(Context context) {
        super(context);

        init();
    }

    public NormalChip(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public NormalChip(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }
    private void init(){
        this.setGravity(Gravity.CENTER);
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_chip));
        this.setTypeface(ResourcesCompat.getFont(getContext(), R.font.inter));
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }

    public void setSelected(boolean selected){
        if(selected){
            this.setBackground(getResources().getDrawable(R.drawable.back_round_8_main_color, null));
            this.setTextColor(getResources().getColor(R.color.white, null));
        }
        else{
            this.setBackground(getResources().getDrawable(R.drawable.back_round_8_sub_gray, null));
            this.setTextColor(getResources().getColor(R.color.main_gray, null));
        }
    }
}
