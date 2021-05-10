package com.monash.paindiary.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.monash.paindiary.R;
import com.monash.paindiary.databinding.LayoutPainRecordBinding;
import com.monash.paindiary.entity.PainRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<PainRecord> painRecordList;

    public RecyclerViewAdapter(List<PainRecord> painRecordList) {
        this.painRecordList = painRecordList;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutPainRecordBinding binding = LayoutPainRecordBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        final PainRecord painRecord = painRecordList.get(position);
        viewHolder.binding.textHiddenRecordId.setText(String.valueOf(painRecord.getUid()));
        viewHolder.binding.textEntryDate.setText((new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss")).format(new Date(painRecord.getDateTime())));
        viewHolder.binding.painIntensityValue.setText(String.valueOf(painRecord.getPainIntensityLevel()));
        viewHolder.binding.painAreaValue.setText(painRecord.getPainArea());
        viewHolder.binding.dailyStepValue.setText(String.valueOf(painRecord.getStepCount()));
        viewHolder.binding.moodValue.setText(painRecord.getMood());

        viewHolder.binding.temperatureValue.setText(String.valueOf(painRecord.getTemperature()));
        viewHolder.binding.humidityValue.setText(String.valueOf(painRecord.getHumidity()));
        viewHolder.binding.pressureValue.setText(String.valueOf(painRecord.getPressure()));

        viewHolder.itemView.setOnClickListener(v -> {
            int uid = Integer.parseInt(((TextView) v.findViewById(R.id.text_hidden_record_id)).getText().toString());
            Bundle bundle = new Bundle();
            bundle.putInt("uid", uid);
            Navigation.findNavController(v).navigate(
                    R.id.nav_pain_data_entry_fragment,
                    bundle,
                    new NavOptions.Builder()
                            .setEnterAnim(R.anim.slide_up)
                            .setExitAnim(R.anim.fade_out)
                            .setPopExitAnim(R.anim.slide_down)
                            .build()
            );
        });
    }

    @Override
    public int getItemCount() {
        return painRecordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutPainRecordBinding binding;
        public ViewHolder(LayoutPainRecordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}