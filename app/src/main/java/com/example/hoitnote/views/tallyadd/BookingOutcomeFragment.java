package com.example.hoitnote.views.tallyadd;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.solver.state.State;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.hoitnote.R;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.BookingType;
import com.example.hoitnote.utils.helpers.BookingDataHelper;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class BookingOutcomeFragment extends BookingBaseFragment {



    public BookingOutcomeFragment(BookingType bookingType) {
        super(bookingType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_outcome, container, false);
        allItemsLinearLayout = (LinearLayout)view.findViewById(R.id.hzs_booking_all_items);
        clickButtonInit(view);
        return view;
    }


    @Override
    protected void clickButtonInit(View view) {
        super.clickButtonInit(view);
        classifyButtonInit(view);
        accountButtonInit(view);

        restoreTempTally();
        showTempTally();
    }




    public Tally getOutcomeTally(){
        java.sql.Date date = new java.sql.Date(inputDate.getTime());
        Time time = new Time(inputDate.getTime());
        Double money = 0.0;
        try {
            money = Double.parseDouble(moneyEditText.getText().toString());
        }catch (Exception e){
            money = 0.0;
        }
        String classification1 = firstClassString;
        String classification2 = secondClassString;
        String remark = noteEditText.getText().toString();

        Account account = new Account();
        String [] arr = accountString.split("\\s+");
        account.setAccountName(arr[0]);
        if(arr.length > 1){
            account.setAccountCode(arr[1]);
        }
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
        editor.putString(Constants.BookingActivityAccount,accountString);
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
                accountString = pref.getString(Constants.BookingActivityAccount,"");
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
        accountTextView.setText(accountString);
        timeTextView.setText(timeString);
        personTextView.setText(personString);
        storeTextView.setText(storeString);
        projectTextView.setText(projectString);
        hasTemp = true;
    }
}