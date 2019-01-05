package com.tejas.root.finalapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tejas.root.finalapp.databases.DatabaseHandler;
import com.tejas.root.finalapp.entities.CoinsStack;
import com.tejas.root.finalapp.entities.PaperInformation;


import java.util.List;

/**
 * Created by root on 24/12/18.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<PaperInformation> list;
    Context context;
    public CustomRecyclerAdapter(Context context, List<PaperInformation> listdata){

        this.context = context;

        this.list = listdata;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == R.layout.single_test_view){
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.single_test_view,parent,false);

            return new MyItemViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.single_test_view,parent,false);

            return new MyItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder.getItemViewType() == R.layout.single_test_view){
            MyItemViewHolder itemViewHolder = (MyItemViewHolder) holder;
            itemViewHolder.list_name.setText(list.get(position).getTest_name());
            itemViewHolder.list_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkWhetherLock(list.get(position),position,view);
                }
            });
            int price = list.get(position).getPrice();
            String priceString;
            if(price==0){
                priceString="Free";
            }else{
                priceString = String.valueOf(price);
            }
            itemViewHolder.price.setText(priceString);
            itemViewHolder.chilar.setVisibility((price==0)?View.GONE:View.VISIBLE);

            String attempt = list.get(position).getVisited();
            itemViewHolder.attempt.setText((attempt.equals("N"))?"Not-Attempted":"Attempted");
            itemViewHolder.attempt.setTextColor((attempt.equals("N"))?Color.parseColor("#ff5050"):
                    Color.parseColor("#3d3d3d"));

            String lock = list.get(position).getLock();
            itemViewHolder.lock_img.setImageResource((lock.equals("Y"))?R.drawable.lock:R.drawable.unlock);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.single_test_view;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyItemViewHolder extends RecyclerView.ViewHolder{

        TextView list_name,price,attempt,questions;
        ImageView list_start,lock_img,chilar;


        public MyItemViewHolder(View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.item_name);
            list_start = itemView.findViewById(R.id.item_start);
            price = itemView.findViewById(R.id.price_text);
            attempt = itemView.findViewById(R.id.attempted_text);
            lock_img = itemView.findViewById(R.id.lock_img);
            questions = itemView.findViewById(R.id.noOfQuestions);
            chilar = itemView.findViewById(R.id.chilar);
        }
    }

    public void updateRecord(PaperInformation information){
        DatabaseHandler.getInstance(context).getAppDatabase().getTestDao().updatePaperInformation(information);
    }

    public String checkVisited(PaperInformation information,int position){
        if(information.getVisited().equals("N")){
            information.setVisited("Y");
            list.get(position).setVisited("Y");
            updateRecord(information);
            return "N";
        }else{
            return "Y";
        }
    }

    private void checkWhetherLock(PaperInformation information,int position,View view){
        if(information.getLock().equals("Y")){
            createDialogMethod(information.getPrice(),information,position);
            notifyItemChanged(position);
        }else{
            String temp1 = checkVisited(list.get(position),position);
            notifyItemChanged(position);
            Intent intent = new Intent(view.getContext(), TestActivity.class);
            intent.putExtra("address",list.get(position).getFilename());
            intent.putExtra("type","input");
            intent.putExtra("visited",temp1);
            intent.putExtra("name",list.get(position).getTest_name());
            view.getContext().startActivity(intent);
        }
    }

    public void createDialogMethod(final int price, final PaperInformation information, final int position){
        final Dialog dialog = new Dialog(context);
        Button closeButton,unlockButton;
        TextView closeImage;
        TextView singlePrice;
        final List<CoinsStack> stacks = DatabaseHandler.getInstance(context).getAppDatabase().getTestDao()
                .getCoins();

        dialog.setContentView(R.layout.unlock_single_test);

        //closeButton = dialog.findViewById(R.id.cancelSingle);
        unlockButton = dialog.findViewById(R.id.unlockTest);
        closeImage = dialog.findViewById(R.id.closeSingle);
        singlePrice = dialog.findViewById(R.id.singlePrice);

        singlePrice.setText(String.valueOf(price));
        /*closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
*/
        unlockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int total = stacks.get(0).getCoins();
                if(total<price){
                    Toast.makeText(view.getContext(),"You don't have enough coins",Toast.LENGTH_SHORT).show();

                }else{
                    total = total-price;
                    CoinsStack stack = new CoinsStack();
                    stack = stacks.get(0);
                    stack.setCoins(total);
                    DatabaseHandler.getInstance(view.getContext()).getAppDatabase().getTestDao()
                            .updateCoins(stack);
                    changeListData(information);
                    dialog.cancel();

                    notifyItemChanged(position);
                }

            }
        });

        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void changeListData(PaperInformation information) {
        information.setLock("N");
        updateRecord(information);
    }


}
