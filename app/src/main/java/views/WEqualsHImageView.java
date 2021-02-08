package views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

//AppCompatImageView
public class WEqualsHImageView extends AppCompatImageView {
    public WEqualsHImageView(Context context) {
        super(context);
    }

    public WEqualsHImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WEqualsHImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //重写onMeasure
    //接受View 宽高变量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //不写东西表示不去控制View的宽高 而是通过系统测量
       // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        //获取当前View的宽度
      // int width=MeasureSpec.getSize(widthMeasureSpec);
       //获取View模式
        //match_parent warp_content 具体的dp
        //int mode=MeasureSpec.getMode(widthMeasureSpec);
    }
}
