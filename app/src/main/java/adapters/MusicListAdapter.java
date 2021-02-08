package adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imoocmusicdemo.R;
import com.example.imoocmusicdemo.activitys.PlayMusicActivity;

import java.util.List;

import models.MusicModel;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private Context mContext;
    private View mItemView;
    private  RecyclerView mRv;
    private boolean isCalcaulationRyHeight;
    private List<MusicModel> mDataSource;

    public MusicListAdapter(Context context,RecyclerView recyclerView,List<MusicModel> dataSource){
    mContext=context;
    mRv=recyclerView;
    this.mDataSource=dataSource;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mItemView=LayoutInflater.from(mContext).inflate(R.layout.item_list_music,parent,false);
        return new ViewHolder(mItemView);
}

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        setRecyclerViewHeight();
        final MusicModel musicModel=mDataSource.get(position);

        Glide.with(mItemView)
                //.load("https://c-ssl.duitang.com/uploads/item/201807/16/20180716130754_KaPPe.thumb.700_0.jpeg")
                .load(musicModel.getPoster())
                .into(holder.ivIcon);
        holder.tvName.setText(musicModel.getName());
        holder.tvAuthor.setText(musicModel.getAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, PlayMusicActivity.class);
                intent.putExtra(PlayMusicActivity.MUSIC_ID,musicModel.getMusicId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //return 8;
        return  mDataSource.size();
    }
    /**
     * 1、获取ItemView的高度
     * 2  itemView的数量
     * 3 使用 itemViewHeight * itemViewNum =RecyclerView的高度
     */
    private  void setRecyclerViewHeight(){
        if(isCalcaulationRyHeight || mRv==null) return;
        isCalcaulationRyHeight=true;
        //  获取ItemView的高度
       RecyclerView.LayoutParams itemViewLp= (RecyclerView.LayoutParams) mItemView.getLayoutParams();
       //获取ItemVIew的数量
       int itemCount=getItemCount();
       // 使用 itemViewHeight * itemViewNum =RecyclerView的高度
       int recyclerViewHeight= itemViewLp.height*itemCount;
       //设置recyclerViewHeight的高度
        LinearLayout.LayoutParams rvLp= (LinearLayout.LayoutParams) mRv.getLayoutParams();
        rvLp.height=recyclerViewHeight;
        mRv.setLayoutParams(rvLp);
    }
    class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView ivIcon;
        View itemView;
        TextView tvName,tvAuthor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            ivIcon=itemView.findViewById(R.id.iv_icon);
            tvName=itemView.findViewById(R.id.tv_name);
            tvAuthor=itemView.findViewById(R.id.tv_author);
        }
    }
}
