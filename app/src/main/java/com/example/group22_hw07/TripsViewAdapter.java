package com.example.group22_hw07;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TripsViewAdapter extends FirestoreRecyclerAdapter<TripData, TripsViewAdapter.TripHolder> {

    Context context;
    String userId;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See
     * {@link FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TripsViewAdapter(FirestoreRecyclerOptions<TripData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(TripHolder tripHolder, int i, TripData tripData) {
        TextView TripName = tripHolder.tv_TripName;
        TextView TripDescription = tripHolder.tv_TripDescription;
        TextView TripCreatedBy = tripHolder.tv_TripCreatedBy;
        ImageView TripPhoto = tripHolder.iv_TripPhoto;

        TripName.setText(tripData.TripName);
        TripDescription.setText(tripData.TripDescription);
        TripCreatedBy.setText(tripData.CreatedBy);
        //TripPhoto.set
    }

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_recycler_view, parent, false);
        TripHolder tripHolder = new TripHolder(view);
        return tripHolder;
    }

    public class TripHolder extends RecyclerView.ViewHolder {
        TextView tv_TripName;
        TextView tv_TripDescription;
        TextView tv_TripCreatedBy;
        ImageView iv_TripPhoto;

        public TripHolder(View itemView) {
            super(itemView);
            tv_TripName = itemView.findViewById(R.id.tv_TripName);
            tv_TripDescription = itemView.findViewById(R.id.tv_TripDescription);
            tv_TripCreatedBy = itemView.findViewById(R.id.tv_TripCreatedBy);
            iv_TripPhoto = itemView.findViewById(R.id.iv_TripPhoto);
        }
    }
}
