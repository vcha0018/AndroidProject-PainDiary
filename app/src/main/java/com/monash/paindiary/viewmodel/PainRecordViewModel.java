package com.monash.paindiary.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.monash.paindiary.entity.PainRecord;
import com.monash.paindiary.repository.PainRecordRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PainRecordViewModel extends AndroidViewModel {
    private PainRecordRepository repository;
    private LiveData<List<PainRecord>> allPainRecords;

    public PainRecordViewModel(@NonNull Application application) {
        super(application);
        repository = new PainRecordRepository(application);
        allPainRecords = repository.getAllPainRecords();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findRecordByID(final int uid) {
        return repository.findByID(uid);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findRecordByTimestamp(final double timestamp) {
        return repository.findByTimestamp(timestamp);
    }

    public LiveData<List<PainRecord>> getAllPainRecords() {
        return allPainRecords;
    }

    public void insert(PainRecord painRecord) {
        repository.insert(painRecord);
    }

    public void delete(PainRecord painRecord) {
        repository.delete(painRecord);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void update(PainRecord painRecord) {
        repository.updatePainRecord(painRecord);
    }
}
