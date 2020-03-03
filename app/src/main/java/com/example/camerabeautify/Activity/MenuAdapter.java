package com.example.camerabeautify.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.camerabeautify.R;

import java.util.ArrayList;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

    private Context mContext;
    public ArrayList<MenuBean> data;
    public int checkPos=0;

    public MenuAdapter(Context context, ArrayList<MenuBean> data){
        this.mContext=context;
        this.data=data;
    }

    @Override
    public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuHolder(LayoutInflater.from(mContext).inflate(R.layout.item_small_menu,parent,false));
    }

    @Override
    public void onBindViewHolder(MenuHolder holder, int position) {
        holder.setData(data.get(position),position);

    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }

    private View.OnClickListener mListener;
    public void setOnClickListener(View.OnClickListener listener){
        this.mListener=listener;
    }

    public class MenuHolder extends RecyclerView.ViewHolder{

        private ImageView iv;

        public MenuHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.mMenu);
            ClickUtils.addClickTo(iv,mListener, R.id.mMenu);
        }

        public void setData(MenuBean bean,int pos){

            iv.setImageResource(bean.image);
            iv.setSelected(pos==checkPos);
            ClickUtils.setPos(iv,pos);
        }

        public void select(boolean isSelect){
            iv.setSelected(isSelect);
        }
    }

}
