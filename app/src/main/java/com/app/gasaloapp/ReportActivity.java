package com.app.gasaloapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.app.gasaloapp.Adapters.EntryAdapter;
import com.app.gasaloapp.Interfaces.DataBaseResponse;
import com.app.gasaloapp.Model.Entry;
import com.app.gasaloapp.Utils.DataBaseManager;
import com.app.gasaloapp.databinding.ActivityReportBinding;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {


    ActivityReportBinding binding;
    EntryAdapter entryAdapter;
    List<Entry> entries;
    SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPref=new SharedPref(this);
        entries=new ArrayList<>();
        entryAdapter=new EntryAdapter(entries,this);
        binding.recyclerView.setAdapter(entryAdapter);
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog progressDialog=new ProgressDialog(ReportActivity.this);
                progressDialog.show();
                progressDialog.setMessage("Saving Excel File");
                XlsSheetManager sheetManager=new XlsSheetManager(ReportActivity.this);
                sheetManager.addHeader();
                for(Entry entry:entries)
                {
                    sheetManager.addRow(entry);
                }


                DataBaseManager.removeEntries(new DataBaseResponse() {
                    @Override
                    public void onResponse(boolean isSuccess, String message, Object Data) {
                        progressDialog.dismiss();

                        if(isSuccess)
                        {
                            entries.clear();
                            entryAdapter.notifyDataSetChanged();
                            String path=sheetManager.save();
                            Util.showCustomToast(ReportActivity.this,"Saved At : "+path,false);

                        }
                        else
                        {
                            Util.showCustomToast(ReportActivity.this,message,true);
                        }

                    }
                });



            }
        });

        loadData();
    }

    private void loadData() {

        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DataBaseManager.readEntries(sharedPref.getQrCode(), new DataBaseResponse() {
            @Override
            public void onResponse(boolean isSuccess, String message, Object Data) {

                progressDialog.dismiss();
                Util.showCustomToast(ReportActivity.this,message,isSuccess);
                if(isSuccess)
                {
                    entries.clear();
                    entries.addAll((List<Entry>) Data);
                    entryAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}