package views;

        import android.graphics.Rect;
        import android.view.View;
        import android.widget.LinearLayout;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private  int mSpace;
    public GridSpaceItemDecoration(int space,RecyclerView parent){
        mSpace=space;
        getRecyclerViewOffsets(parent);
    }

    /**
     *
     * @param outRect Item的矩形边界
     * @param view  ItemView
     * @param parent  RecyclerView
     * @param state RecyclerView的状态
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//使偏移
        outRect.left=mSpace;
        //判断是不是每一行第一个Item 是的话不偏移 光这样和后面的高度不一致
//        if(parent.getChildLayoutPosition(view)%3==0){
////            outRect.left=0;
////        }

    }
    private  void getRecyclerViewOffsets(RecyclerView parent){
        // View margin,
        //margin为正 则View距离边界产生一个距离
        //反之 超出边界一个距离
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) parent.getLayoutParams();
        layoutParams.leftMargin=-mSpace;
        parent.setLayoutParams(layoutParams);
    }

}
