package kr.co.roysoft.lotto649.view.button;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import kr.co.roysoft.lotto649.R;

public class IconButton extends androidx.appcompat.widget.AppCompatImageButton {
    public IconButton(Context context) {
        super(context);

        init();
    }

    public IconButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public IconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        this.setBackground(getResources().getDrawable(R.drawable.back_round_border_8_white, null));
        this.setPadding(10,10,10,10);
    }
}
