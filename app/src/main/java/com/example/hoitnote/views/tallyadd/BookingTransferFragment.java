package com.example.hoitnote.views.tallyadd;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.hoitnote.R;
import com.example.hoitnote.models.Account;
import com.example.hoitnote.models.Tally;
import com.example.hoitnote.utils.App;
import com.example.hoitnote.utils.constants.Constants;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.BookingType;
import com.example.hoitnote.utils.helpers.BookingDataHelper;
import com.example.hoitnote.utils.helpers.ConvertHelper;

import java.sql.Time;
import java.util.List;

public class BookingTransferFragment extends BookingBaseFragment {
    private TextView outcomeAccountTextView;
    private TextView incomeAccountTextView;
    private String outcomeAccountString;
    private String incomeAccountString;
    private OptionsPickerView pvOutcomeAccountOptions;
    private OptionsPickerView pvIncomeAccountOptions;
    private Account outcomeAccount;
    private Account incomeAccount;
    private int lockedAccount = 0; //0代表Outcome,1代表Income是用户的账户
    private List<Account> accountList;
    public BookingTransferFragment(BookingType bookingType) {
        super(bookingType);
        accountList = App.dataBaseHelper.getAccounts();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_transfer, container, false);
        clickButtonInit(view);
        return view;
    }

    @Override
    protected void clickButtonInit(View view) {
        super.clickButtonInit(view);
        outcomeAccountButtonInit(view);
        incomeAccountButtonInit(view);
        changeAccountButtonInit(view);
        restoreTempTally();
        showTempTally();
    }

    @Override
    public void onResume() {
        super.onResume();
        initNowAccount();
    }

    private void initNowAccount(){
        outcomeAccount = BookingDataHelper.getNowAccount();
        incomeAccount = accountList.get(0);
        if(outcomeAccount.getAccountCode() == null || outcomeAccount.getAccountCode() == ""){
            outcomeAccountString = outcomeAccount.getAccountName();
        }else{
            outcomeAccountString = outcomeAccount.getAccountName()+" "+ ConvertHelper.cutoffAccountCode(outcomeAccount.getAccountCode());
        }
        outcomeAccountTextView.setText(outcomeAccountString);
        lockedAccount = 0;
    }
    private void changeAccountButtonInit(View view){
        View changeAccountButton = view.findViewById(R.id.change_account_button);
        changeAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = outcomeAccountString;
                outcomeAccountString = incomeAccountString;
                incomeAccountString = temp;
                incomeAccountTextView.setText(incomeAccountString);
                outcomeAccountTextView.setText(outcomeAccountString);
                lockedAccount = lockedAccount ^ 1;
                Account tempAccount = new Account();
                tempAccount = outcomeAccount;
                outcomeAccount = incomeAccount;
                incomeAccount = tempAccount;
            }
        });
    }
    private void outcomeAccountButtonInit(View view){
        final View chooseClassButton = view.findViewById(R.id.hzs_booking_outcome_account);
        final List<String> accountItems = BookingDataHelper.getAccountsForShow();
        outcomeAccountTextView = view.findViewById(R.id.hzs_booking_outcome_account);
        outcomeAccountString = accountItems.get(0);
        pvOutcomeAccountOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                outcomeAccountString = accountItems.get(options1);
                outcomeAccount = accountList.get(options1);
                outcomeAccountTextView.setText(outcomeAccountString);
            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {
                classSelectOption1 = options1;
            }
        }).setLayoutRes(R.layout.hzs_transfer_account_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = v.findViewById(R.id.hzs_transfer_account_pickerview_finish);
                final TextView tvCancel = v.findViewById(R.id.hzs_transfer_account_pickerview_cancel);
                Button addAccount = v.findViewById(R.id.hzs_transfer_pickerview_add_account);
                addAccount.setText("添加支出方");
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvOutcomeAccountOptions.returnData();
                        pvOutcomeAccountOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvOutcomeAccountOptions.dismiss();
                    }
                });

                addAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddOutcomeAccountDialog();
                    }
                });

            }
        }).isDialog(true).build();
        pvOutcomeAccountOptions.setPicker(accountItems);
        chooseClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lockedAccount != 0){
                    pvOutcomeAccountOptions.show();
                }
            }
        });
    }
    private void incomeAccountButtonInit(View view){

        final View chooseClassButton = view.findViewById(R.id.hzs_booking_income_account);
        final List<String> accountItems = BookingDataHelper.getAccountsForShow();
        incomeAccountTextView = view.findViewById(R.id.hzs_booking_income_account);
        incomeAccountString = accountItems.get(0);
        pvIncomeAccountOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                incomeAccountString = accountItems.get(options1);
                incomeAccount = accountList.get(options1);
                incomeAccountTextView.setText(incomeAccountString);
            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {
                classSelectOption2 = options1;
            }
        }).setLayoutRes(R.layout.hzs_transfer_account_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = v.findViewById(R.id.hzs_transfer_account_pickerview_finish);
                final TextView tvCancel = v.findViewById(R.id.hzs_transfer_account_pickerview_cancel);
                Button addAccount = v.findViewById(R.id.hzs_transfer_pickerview_add_account);
                addAccount.setText("添加收款方");
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvIncomeAccountOptions.returnData();
                        pvIncomeAccountOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvIncomeAccountOptions.dismiss();
                    }
                });

                addAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddIncomeAccountDialog();
                    }
                });

            }
        }).isDialog(true).build();
        pvIncomeAccountOptions.setPicker(accountItems);
        chooseClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lockedAccount != 1){
                    pvIncomeAccountOptions.show();
                }
            }
        });
    }
    private void showAddOutcomeAccountDialog(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hzs_add_class_view,null,false);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(view).create();
        TextView cancelButton = view.findViewById(R.id.hzs_add_class_cancel);
        TextView finishButton = view.findViewById(R.id.hzs_add_class_finish);
        final EditText editText = view.findViewById(R.id.hzs_add_class_text);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outcomeAccountString = editText.getText().toString();
                outcomeAccount = new Account();
                outcomeAccount.setAccountName(outcomeAccountString);
                outcomeAccountTextView.setText(outcomeAccountString);
                dialog.dismiss();
                pvOutcomeAccountOptions.dismiss();
            }
        });

        dialog.show();
        if(dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    private void showAddIncomeAccountDialog(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hzs_add_class_view,null,false);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(view).create();
        TextView cancelButton = view.findViewById(R.id.hzs_add_class_cancel);
        TextView finishButton = view.findViewById(R.id.hzs_add_class_finish);
        final EditText editText = view.findViewById(R.id.hzs_add_class_text);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeAccountString = editText.getText().toString();
                incomeAccount = new Account();
                incomeAccount.setAccountName(incomeAccountString);
                incomeAccountTextView.setText(incomeAccountString);
                dialog.dismiss();
                pvIncomeAccountOptions.dismiss();
            }
        });

        dialog.show();
        if(dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void saveTransferTally(){
        java.sql.Date date = new java.sql.Date(inputDate.getTime());
        Time time = new Time(inputDate.getTime());
        Double money = 0.0;
        try {
            money = Double.parseDouble(moneyEditText.getText().toString());
        }catch (Exception e){
            money = 0.0;
        }
        String remark = noteEditText.getText().toString();
        String memeber = personTextView.getText().toString();
        String project = projectTextView.getText().toString();
        String vendor = storeTextView.getText().toString();
        String classificationOutcome = "转账支出";
        String classificationOutcome2 = "转至";
        String classificationIncome = "转账收入";
        String classificationIncome2 = "来自";
        List<String> accountItems = BookingDataHelper.getAccountsForShow();
        String outcomeAccountString = outcomeAccountTextView.getText().toString();
        String incomeAccountString = incomeAccountTextView.getText().toString();

        if(accountItems.contains(outcomeAccountString)){

            Tally tally = new Tally(money,date,time,remark,outcomeAccount, ActionType.OUTCOME,
                    classificationOutcome,classificationOutcome2+incomeAccount.getAccountName(),memeber,project,vendor);
            Toast.makeText(getContext(),tally.getDate().toString(),Toast.LENGTH_SHORT).show();
            App.dataBaseHelper.addTally(tally);
        }
        if(accountItems.contains(incomeAccountString)){

            Tally tally = new Tally(money,date,time,remark,incomeAccount, ActionType.INCOME,
                    classificationIncome,classificationIncome2+outcomeAccount.getAccountName(),memeber,project,vendor);
            Toast.makeText(getContext(),tally.getDate().toString(),Toast.LENGTH_SHORT).show();
            App.dataBaseHelper.addTally(tally);
        }
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("tempTally"+bookingType, Context.MODE_PRIVATE).edit();
        editor.clear().commit();
        hasTemp = false;
    }

    @Override
    protected void saveTempTally(){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("tempTally"+bookingType, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.BookingActivityMoney,moneyEditText.getText().toString());
        editor.putString(Constants.BookingActivityOutcomeAccount,outcomeAccountString);
        editor.putString(Constants.BookingActivityIncomeAccount,incomeAccountString);
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
                outcomeAccountString = pref.getString(Constants.BookingActivityOutcomeAccount,"");
                incomeAccountString = pref.getString(Constants.BookingActivityIncomeAccount,"");
                noteEditText.setText(pref.getString(Constants.BookingActivityRemark,""));
                personString = pref.getString(Constants.BookingActivityPerson,"");
                storeString = pref.getString(Constants.BookingActivityStore,"");
                projectString = pref.getString(Constants.BookingActivityProject,"");
            }
        }

    }
    @Override
    protected void showTempTally(){
        outcomeAccountTextView.setText(outcomeAccountString);
        incomeAccountTextView.setText(incomeAccountString);
        timeTextView.setText(timeString);
        personTextView.setText(personString);
        storeTextView.setText(storeString);
        projectTextView.setText(projectString);
        hasTemp = true;
    }
}