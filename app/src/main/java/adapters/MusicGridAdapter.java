package adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.imoocmusicdemo.activitys.AlbumListActivity;

import java.util.List;

import models.AlbumModel;

public class MusicGridAdapter extends RecyclerView.Adapter<MusicGridAdapter.ViewHolder> {
    private Context mContext;
    private List<AlbumModel> mDataSource;
    //创建一个构造方法
    public MusicGridAdapter(Context context,List<AlbumModel> dataSource) {
        mContext=context;
        this.mDataSource=dataSource;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_grid_music,viewGroup,false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AlbumModel albumModel=mDataSource.get(position);
        Glide.with(mContext)
               // .load("https://c-ssl.duitang.com/uploads/item/201509/28/20150928210202_hUFBZ.thumb.700_0.jpeg")
                .load(albumModel.getPoster())
                .into(holder.ivIcon);
        holder.mTvPlayNum.setText(albumModel.getPlayNum());
        holder.mTvName.setText(albumModel.getName());
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
          public void onClick(View v) {
               Intent intent=new Intent(mContext, AlbumListActivity.class);
               intent.putExtra(AlbumListActivity.ALBUM_ID,albumModel.getAlbumId());
               mContext.startActivity(intent);
           }
       });
    }
    @Override
    public int getItemCount() {
       // return 6;
        return mDataSource.size();
    }
//创建一个ViewHolder
    class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView ivIcon;
        View itemView;
        TextView mTvPlayNum,mTvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView=itemView;
            ivIcon=itemView.findViewById(R.id.Icon2);
            mTvPlayNum=itemView.findViewById(R.id.tv_play_num);
            mTvName=itemView.findViewById(R.id.tv_name);
        }
    }
}
