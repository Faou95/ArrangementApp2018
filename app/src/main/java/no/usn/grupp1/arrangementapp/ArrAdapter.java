package no.usn.grupp1.arrangementapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by finge on 16.02.2018.
 */

public class ArrAdapter extends RecyclerView.Adapter<ArrAdapter.ViewHolder> {


    //Member variables
    private ArrayList<Arrangement> mArrData;
    private Context mContext;


    public ArrAdapter(Context mContext, ArrayList<Arrangement>  mArrData) {
        this.mArrData = mArrData;
        this.mContext = mContext;
    }


    @Override
    public ArrAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_arr, parent, false));
    }

    @Override
    public void onBindViewHolder(ArrAdapter.ViewHolder holder, int position) {
        //Get current sport
        Arrangement currentArr = mArrData.get(position);
        //Populate the textviews with data
        holder.bindTo(currentArr);

        Glide.with(mContext)
                .load(currentArr.getImageResource())
                .override(600,400)
                .centerCrop()
                .into(holder.mArrImage);
    }

    @Override
    public int getItemCount() {
        return mArrData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mInfoText;
        private ImageView mArrImage;

        /**
         * Constructor for the ViewHolder, used in onCreateViewHolder().
         * @param itemView The rootview of the list_item.xml layout file
         */
        ViewHolder(View itemView) {
            super(itemView);

            //Initialize the views
            mTitleText = itemView.findViewById(R.id.title);
            mInfoText = itemView.findViewById(R.id.subTitle);
            mArrImage = itemView.findViewById(R.id.arrImage);
            itemView.setOnClickListener(this);
        }

        void bindTo(Arrangement currentArr){
            //Populate the textviews with data
            mTitleText.setText(currentArr.getTittel());
            mInfoText.setText(currentArr.getDescription());

        }

        @Override
        public void onClick(View view) {
            Arrangement currentArr = mArrData.get(getAdapterPosition());

            Intent detailIntent = new Intent(mContext, DetailArrActivity.class);
            detailIntent.putExtra("title", currentArr.getTittel());
            detailIntent.putExtra("image_resource", currentArr.getImageResource());
            mContext.startActivity(detailIntent);
        }
    }
}
