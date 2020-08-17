package com.adit.carnage.activities;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.adit.carnage.R;
import com.adit.carnage.fragments.HostFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.annotations.NonNull;

public class Activity2 extends BaseActivity {

    HostFragment fragment;

    public static void startActivity(BaseActivity source){
        Intent i = new Intent(source, Activity2.class);
        source.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Toast.makeText(getBaseContext(), "Ini activity dua", Toast.LENGTH_LONG);

        Button btnShowDialog = findViewById(R.id.btnShowDialog);
        Button btnKembali = findViewById(R.id.btnKembali);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("TES", "ASU");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.videoCall:
                showDialog("Info", "VideoCall dipilih");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void validatePeriodDate(){
        boolean isValid = true;

        Integer monthCounter = 0;
//        Integer tanggalDebet = Integer.valueOf(sTanggalDebet.getSelectedItem().toString());
//        Integer tanggalPeriod = Integer.valueOf(sTimePeriod.getSelectedItem().toString());
        Integer progressMax = 0;
        Integer progressNow = 0;
//        String regDate = detailRspData.getCreatedDate();

        Date today = new Date();
        Date debetDate = new Date();
        Date periodDate = new Date();
        Date registrationDate = new Date();

        SimpleDateFormat regDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        SimpleDateFormat periodDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String regDateFormatted, todayFormatted, debetDateFormatted, periodDateFormatted = "";

//        try {
//            registrationDate = regDateFormat.parse(regDate);
//        }catch(ParseException e){
//            e.printStackTrace();
////            showDialog("ParseException", e.getMessage(), false);
//        }

        regDateFormatted = periodDateFormat.format(registrationDate);
        periodDateFormatted = periodDateFormat.format(periodDate);
        debetDateFormatted = periodDateFormat.format(debetDate);
        todayFormatted = periodDateFormat.format(today);

        String[] debetDateSplit = debetDateFormatted.split("/");
        String[] todayDateSplit = todayFormatted.split("/");
        String[] regDateSplit = regDateFormatted.split("/");

        Calendar installmentDateCalendar = Calendar.getInstance();
//        Integer dateInstallment = tanggalDebet;
        Integer monthInstallment = Integer.valueOf(debetDateSplit[1]);
        Integer yearInstallment = Integer.valueOf(debetDateSplit[2]);
//        installmentDateCalendar.set(yearInstallment, monthInstallment, dateInstallment);

        Calendar todayDateCalendar = Calendar.getInstance();
        Integer dateToday = Integer.valueOf(todayDateSplit[0]);
        Integer monthToday = Integer.valueOf(todayDateSplit[1]);
        Integer yearToday = Integer.valueOf(todayDateSplit[2]);
        todayDateCalendar.set(yearToday, monthToday, dateToday);

        Calendar regDateCalendar = Calendar.getInstance();
        Integer dateReg = Integer.valueOf(regDateSplit[0]);
        Integer monthReg = Integer.valueOf(regDateSplit[1]);
        Integer yearReg = Integer.valueOf(regDateSplit[2]);
        regDateCalendar.set(yearReg, monthReg, dateReg);

        Integer nextMonthInstallment = monthInstallment;
        if(installmentDateCalendar.before(todayDateCalendar)){
            if(nextMonthInstallment == 12){
                nextMonthInstallment = 1;
            }else{
                nextMonthInstallment += 1;
            }
            installmentDateCalendar.set(Calendar.MONTH, nextMonthInstallment);
        }else if(installmentDateCalendar.equals(todayDateCalendar) || installmentDateCalendar.after(todayDateCalendar)){
            progressNow +=1;
        }

        while(todayDateCalendar.after(installmentDateCalendar)){
            progressNow += 1;
        }

//        showDialog("Tanggal", "Installment Date : " + dateInstallment + "-" + monthInstallment  +"-" + yearInstallment + "\n" +
//                "Created Date : " + dateReg + "-" + monthReg  +"-" + yearReg + "\n"+
//                "Today Date : " + dateToday+ "-" + monthToday +"-" + yearToday + "\n"+
//                "Next Installment Date : " + dateInstallment+ "-" + nextMonthInstallment +"-" + yearInstallment + "\n"+
//                "Progress : " + progressNow + " of " + tanggalPeriod, false);
    }



}
