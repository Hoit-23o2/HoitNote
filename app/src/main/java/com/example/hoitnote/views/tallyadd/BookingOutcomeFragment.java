package com.example.hoitnote.views.tallyadd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoitnote.R;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.BookingType;
import com.example.hoitnote.utils.helpers.BookingDataHelper;

import java.sql.Time;


public class BookingOutcomeFragment extends BookingBaseFragment {



    public BookingOutcomeFragment(BookingType bookingType) {
        super(bookingType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        clickButtonInit(view);
        return view;
    }


    @Override
    protected void clickButtonInit(View view) {
        super.clickButtonInit(view);
        classifyButtonInit(view);
        restoreTempTally();
        showTempTally();
    }


    /*public void tempClick(View view){
        view.findViewById(R.id.OK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), RecycleBinActivity.class);
                startActivity(intent);
            }
        });
    }*/

    public Tally getOutcomeTally(){
        java.sql.Date date = new java.sql.Date(inputDate.getTime());
        Time time = new Time(inputDate.getTime());
        Double money = 0.0;
        try {
            money = Double.parseDouble(moneyEditText.getText().toString());
        }catch (Exception e){
            money = 0.0;
        }
        String[] strs1 = firstClassString.split("\\s+");
        String classification1 = strs1[1];
        String[] strs2 = secondClassString.split("\\s+");
        String classification2 = strs2[1];
        String remark = noteEditText.getText().toString();

        Account account = BookingDataHelper.getNowAccount();
        String memeber = personString;
        String project = projectString;
        String vendor = storeString;
        Tally tally = new Tally(money,date,time,remark,account, ActionType.OUTCOME,
                classification1,classification2,memeber,project,vendor);
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("tempTally"+bookingType, Context.MODE_PRIVATE).edit();
        editor.clear().commit();
        hasTemp = false;
        return tally;

    }

    @Override
    protected void saveTempTally(){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("tempTally"+bookingType, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.BookingActivityMoney,moneyEditText.getText().toString());
        editor.putString(Constants.BookingActivityClassification1,firstClassString);
        editor.putString(Constants.BookingActivityClassification2,secondClassString);
        editor.putString(Constants.BookingActivityRemark,noteEditText.getText().toString());
        editor.putString(Constants.BookingActivityPerson,personString);
        editor.putString(Constants.BookingActivityStore,storeString);
        editor.putString(Constants.BookingActivityProject,projectString);
        editor.putBoolean(Constants.BookingActivityHasTemp,true);
        editor.apply();
    }
    @Override
    protected void restoreTempTally(){
        SharedPreferences pref = getActivity().getSharedPreferences("tempTally"+bookingType,Context.MODE_PRIVATE);
        if(pref != null){
            hasTemp = pref.getBoolean(Constants.BookingActivityHasTemp,false);
            if(hasTemp){
                moneyEditText.setText(pref.getString(Constants.BookingActivityMoney,""));
                firstClassString = pref.getString(Constants.BookingActivityClassification1,"");
                secondClassString = pref.getString(Constants.BookingActivityClassification2,"");
                noteEditText.setText(pref.getString(Constants.BookingActivityRemark,""));
                personString = pref.getString(Constants.BookingActivityPerson,"");
                storeString = pref.getString(Constants.BookingActivityStore,"");
                projectString = pref.getString(Constants.BookingActivityProject,"");
            }
        }

    }
    @Override
    protected void showTempTally(){
        firstClassTextView.setText(firstClassString);
        secondClassTextView.setText(secondClassString);
        timeTextView.setText(timeString);
        personTextView.setText(personString);
        storeTextView.setText(storeString);
        projectTextView.setText(projectString);
        hasTemp = true;
    }
}