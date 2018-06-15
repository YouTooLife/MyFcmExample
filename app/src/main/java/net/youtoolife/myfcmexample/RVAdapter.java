package net.youtoolife.myfcmexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by youtoolife on 6/8/18.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MessageViewHolder>{

    List<Message> msgs;

    public Map<String, Bitmap>  bitmaps;

    RVAdapter(List<Message> messages){
        this.msgs = messages;

        bitmaps  = new HashMap<>();
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }



    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView date;
        TextView body;
        ImageView img;
        MessageViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            date = (TextView)itemView.findViewById(R.id.msg_date);
            body = (TextView)itemView.findViewById(R.id.msg_body);
            img = (ImageView)itemView.findViewById(R.id.msg_img);
        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        MessageViewHolder pvh = new MessageViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder messageViewHolder, int i) {
        messageViewHolder.date.setText(msgs.get(i).date);
        messageViewHolder.body.setText(msgs.get(i).body);

        String hash = msgs.get(i).hash;

        if (bitmaps.containsKey(hash)) {
            messageViewHolder.img.setImageBitmap(bitmaps.get(hash));
        }
        /*
        byte[] data = msgs.get(i).blob;
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            messageViewHolder.img.setImageBitmap(bitmap);
        }
        */
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }




}