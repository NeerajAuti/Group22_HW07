package com.example.group22_hw07;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.StorageReference;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message,MessageAdapter.MessageHolder> {
    Context context;
    String userId;
    StorageReference storageReference;
//    private RequestOptions requestOptions = new RequestOptions();
    private final int MESSAGE_IN_VIEW_TYPE  = 1;
    private final int MESSAGE_OUT_VIEW_TYPE = 2;

    public MessageAdapter(@NonNull Context context, Query query, String userID) {
        /*
        Configure recycler adapter options:
        query defines the request made to Firestore
        Message.class instructs the adapter to convert each DocumentSnapshot to a Message object
        */
        super(new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build());
        this.context=context;
        this.userId=userID;
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position).getMessage_userid().equals(userId)){
            return MESSAGE_OUT_VIEW_TYPE;
        }
        return MESSAGE_IN_VIEW_TYPE;    }

    @Override
    protected void onBindViewHolder(@NonNull MessageHolder holder, final int position, @NonNull Message model) {
        Log.d("Data", "onBindViewHolder: "+model.toString());
        TextView mText=holder.mText;
        TextView mUsername=holder.mUsername;
        TextView mTime=holder.mTime;
        ImageButton btn_delete=holder.btn_delete;

        mText.setText(model.message_text);
        mTime.setText(DateFormat.format("dd MMM  (h:mm a)", model.message_time));
        mUsername.setText(model.message_user);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Clicked"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType==MESSAGE_IN_VIEW_TYPE){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_in_view_layout, parent, false);
        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_out_view_layout, parent, false);
        }
        Log.d("Recycler", "onBindViewHolder: "+view.toString());
        return new MessageHolder(view);    }

    public class MessageHolder extends RecyclerView.ViewHolder {
        TextView mText;
        TextView mUsername;
        TextView mTime;
        ImageButton btn_delete;
        View view;

        public MessageHolder(View view) {
            super(view);
            this.mText = view.findViewById(R.id.tv_Chat_message);
            this.mUsername = view.findViewById(R.id.tv_Chat_UserName);
            this.mTime = view.findViewById(R.id.tv_Chat_time);
            this.btn_delete = view.findViewById(R.id.btn_Chat_Delete);
            this.view = view;
        }
    }
}
