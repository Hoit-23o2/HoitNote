package com.example.hoitnote.views.tallyadd;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
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
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.helpers.BookingDataHelper;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookingIncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookingIncomeFragment extends Fragment {
    private LinearLayout allItemsLinearLayout;
    private TextView firstClassTextView;
    private TextView secondClassTextView;
    private TextView accountTextView;
    private TextView timeTextView;
    private TextView personTextView;
    private TextView storeTextView;
    private TextView projectTextView;
    private EditText noteEditText;
    private EditText moneyEditText;
    private Date inputDate = new Date();
    private int classSelectOption1 = 0;
    private int classSelectOption2 = 0;
    private OptionsPickerView pvClassificationOptions;
    private OptionsPickerView pvPersonOptions;
    private OptionsPickerView pvStoreOptions;
    private OptionsPickerView pvProjectOptions;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters

    public BookingIncomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookingIncomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookingIncomeFragment newInstance(String param1, String param2) {
        BookingIncomeFragment fragment = new BookingIncomeFragment();
        Bundle args = new Bundle();
        /*
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_income, container, false);
        allItemsLinearLayout = (LinearLayout)view.findViewById(R.id.hzs_booking_income_all_items);
        clickButtonInit(view);

        return view;
    }

    private void clickButtonInit(View view){
        editTextInit(view);
        classifyButtonInit(view);
        accountButtonInit(view);
        timeButtonInit(view);
        personButtonInit(view);
        storeButtonInit(view);
        projectButtonInit(view);
        /*classifyButton1 = (Button)view.findViewById(R.id.hzs_booking_first_classify);
        classifyButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstClassifyPopupWindow.showAtLocation(firstClassifyContentView, Gravity.BOTTOM,0,0);
            }
        });

        classifyButton2 = (Button)view.findViewById(R.id.hzs_booking_second_classify);
        classifyButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondClassifyPopupWindow.showAtLocation(secondClassifyContentView, Gravity.BOTTOM,0,0);
            }
        });*/
    }
    private void classifyButtonInit(View view){

        final View chooseClassButton = (View) view.findViewById(R.id.hzs_booking_income_classify_button);
        final List<String> classification1Items = BookingDataHelper.getClassifications1();
        final List<List<String>> classification2Items = BookingDataHelper.getClassifications2();
        firstClassTextView = (TextView)view.findViewById(R.id.hzs_booking_income_first_class);
        secondClassTextView = (TextView)view.findViewById(R.id.hzs_booking_income_second_class);
        pvClassificationOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String chosenClass1 = classification1Items.get(options1);
                String chosenClass2 = classification2Items.get(options1).get(options2);
                firstClassTextView.setText(chosenClass1);
                secondClassTextView.setText(chosenClass2);
            }
        }).setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {
                classSelectOption1 = options1;
                classSelectOption2 = options2;
            }
        }).setLayoutRes(R.layout.hzs_class_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = (TextView) v.findViewById(R.id.hzs_class_pickerview_finish);
                final TextView tvCancel = (TextView) v.findViewById(R.id.hzs_class_pickerview_cancel);
                Button addFirstClass = (Button)v.findViewById(R.id.hzs_add_first_class_button);
                Button addSecondClass = (Button)v.findViewById(R.id.hzs_add_second_class_button);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvClassificationOptions.returnData();
                        pvClassificationOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvClassificationOptions.dismiss();
                    }
                });

                addFirstClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddFirstClassDialog();
                        Toast.makeText(getContext(),"HHH",Toast.LENGTH_SHORT).show();
                        pvClassificationOptions.setPicker(BookingDataHelper.getClassifications1(),BookingDataHelper.getClassifications2());
                    }
                });
                addSecondClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddSecondClassDialog();
                        pvClassificationOptions.setPicker(BookingDataHelper.getClassifications1(),BookingDataHelper.getClassifications2());
                    }
                });

            }
        }).isDialog(true).build();
        pvClassificationOptions.setPicker(classification1Items,classification2Items);
        chooseClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvClassificationOptions.show();
            }
        });
    }
    private void showAddFirstClassDialog(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hzs_add_class_view,null,false);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(view).create();
        TextView cancelButton = (TextView)view.findViewById(R.id.hzs_add_class_cancel);
        TextView finishButton = (TextView)view.findViewById(R.id.hzs_add_class_finish);
        final EditText editText = (EditText) view.findViewById(R.id.hzs_add_class_text);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newClassName = editText.getText().toString();
                BookingDataHelper.addClassification1(newClassName);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void showAddSecondClassDialog(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hzs_add_class_view,null,false);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(view).create();
        TextView cancelButton = (TextView)view.findViewById(R.id.hzs_add_class_cancel);
        TextView finishButton = (TextView)view.findViewById(R.id.hzs_add_class_finish);
        final EditText editText = (EditText) view.findViewById(R.id.hzs_add_class_text);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newClassName = editText.getText().toString();
                String classification1 = BookingDataHelper.getClassifications1().get(classSelectOption1);
                BookingDataHelper.addClassification2(classification1,newClassName);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void accountButtonInit(View view){
        final View chooseAccountButton = (View) view.findViewById(R.id.hzs_booking_income_account_button);
        final List<String> accountItems = BookingDataHelper.getAccounts();
        accountTextView = (TextView)view.findViewById(R.id.hzs_booking_income_account_button);
        final OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String chosenAccount = accountItems.get(options1);
                accountTextView.setText(chosenAccount);
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("选择账户")
                .build();
        pvOptions.setPicker(accountItems);
        chooseAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });
    }
    private void timeButtonInit(View view){
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        final View chooseTimeButton = (View) view.findViewById(R.id.hzs_booking_income_time_button);
        timeTextView = (TextView)view.findViewById(R.id.hzs_booking_income_time_button);

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(1970,0,1);
        endDate.set(2020,11,31);
        final DateFormat df2 = DateFormat.getDateTimeInstance();
        timeTextView.setText(df2.format(new Date()));
        //时间选择器
        final TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date, View v) {
                inputDate = date;
                timeTextView.setText(df2.format(date));
            }
        }).setCancelText("取消")
                .setType(new boolean[]{true, true, true, true, true, true})
                .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .setSubmitText("确认")
                .build();
        chooseTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvTime.show();
            }
        });
    }
    private void personButtonInit(View view){
        final View chooseAccountButton = (View) view.findViewById(R.id.hzs_booking_income_person_button);
        final List<String> personItems = BookingDataHelper.getPersons();
        personTextView = (TextView)view.findViewById(R.id.hzs_booking_income_person_button);
        pvPersonOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String chosenAccount = personItems.get(options1);
                personTextView.setText(chosenAccount);
            }
        }).setLayoutRes(R.layout.hzs_person_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = (TextView) v.findViewById(R.id.hzs_person_pickerview_finish);
                final TextView tvCancel = (TextView) v.findViewById(R.id.hzs_person_pickerview_cancel);
                Button addFirstClass = (Button)v.findViewById(R.id.hzs_add_person_button);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvPersonOptions.returnData();
                        pvPersonOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvPersonOptions.dismiss();
                    }
                });

                addFirstClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddPersonDialog();
                        pvPersonOptions.setPicker(BookingDataHelper.getPersons());
                    }
                });

            }
        }).isDialog(true)
                .build();
        pvPersonOptions.setPicker(personItems);
        chooseAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPersonOptions.show();
            }
        });
    }
    private void showAddPersonDialog(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hzs_add_class_view,null,false);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(view).create();
        TextView cancelButton = (TextView)view.findViewById(R.id.hzs_add_class_cancel);
        TextView finishButton = (TextView)view.findViewById(R.id.hzs_add_class_finish);
        final EditText editText = (EditText) view.findViewById(R.id.hzs_add_class_text);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPersonName = editText.getText().toString();

                BookingDataHelper.addPerson(newPersonName);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void showAddStoreDialog(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hzs_add_class_view,null,false);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(view).create();
        TextView cancelButton = (TextView)view.findViewById(R.id.hzs_add_class_cancel);
        TextView finishButton = (TextView)view.findViewById(R.id.hzs_add_class_finish);
        final EditText editText = (EditText) view.findViewById(R.id.hzs_add_class_text);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newStoreName = editText.getText().toString();

                BookingDataHelper.addStore(newStoreName);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void editTextInit(View view){
        noteEditText = (EditText)view.findViewById(R.id.hzs_booking_income_note_button);
        moneyEditText = (EditText)view.findViewById(R.id.hzs_booking_income_money_button);
    }
    private void storeButtonInit(View view){
        final View chooseStoreButton = (View) view.findViewById(R.id.hzs_booking_income_store_button);
        final List<String> storeItems = BookingDataHelper.getStores();
        storeTextView = (TextView)view.findViewById(R.id.hzs_booking_income_store_button);
        pvStoreOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String chosenAccount = storeItems.get(options1);
                storeTextView.setText(chosenAccount);
            }
        }).setLayoutRes(R.layout.hzs_store_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = (TextView) v.findViewById(R.id.hzs_store_pickerview_finish);
                final TextView tvCancel = (TextView) v.findViewById(R.id.hzs_store_pickerview_cancel);
                Button addFirstClass = (Button)v.findViewById(R.id.hzs_add_store_button);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvStoreOptions.returnData();
                        pvStoreOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvStoreOptions.dismiss();
                    }
                });

                addFirstClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddStoreDialog();
                        pvStoreOptions.setPicker(BookingDataHelper.getStores());
                    }
                });

            }
        }).isDialog(true).build();
        pvStoreOptions.setPicker(storeItems);
        chooseStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvStoreOptions.show();
            }
        });
    }
    private void projectButtonInit(View view){
        final View chooseProjectButton = (View) view.findViewById(R.id.hzs_booking_income_project_button);
        final List<String> projectItems = BookingDataHelper.getProjects();
        projectTextView = (TextView)view.findViewById(R.id.hzs_booking_income_project_button);
        pvProjectOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String chosenAccount = projectItems.get(options1);
                projectTextView.setText(chosenAccount);
            }
        }).setLayoutRes(R.layout.hzs_project_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = (TextView) v.findViewById(R.id.hzs_project_pickerview_finish);
                final TextView tvCancel = (TextView) v.findViewById(R.id.hzs_project_pickerview_cancel);
                Button addFirstClass = (Button)v.findViewById(R.id.hzs_add_project_button);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvProjectOptions.returnData();
                        pvProjectOptions.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvProjectOptions.dismiss();
                    }
                });

                addFirstClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddProjectDialog();
                        pvProjectOptions.setPicker(BookingDataHelper.getProjects());
                    }
                });

            }
        }).isDialog(true).build();
        pvProjectOptions.setPicker(projectItems);
        chooseProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvProjectOptions.show();
            }
        });
    }
    private void showAddProjectDialog(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hzs_add_class_view,null,false);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(view).create();
        TextView cancelButton = (TextView)view.findViewById(R.id.hzs_add_class_cancel);
        TextView finishButton = (TextView)view.findViewById(R.id.hzs_add_class_finish);
        final EditText editText = (EditText) view.findViewById(R.id.hzs_add_class_text);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newProjectName = editText.getText().toString();

                BookingDataHelper.addProject(newProjectName);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public Tally getIncomeTally(){
        java.sql.Date date = new java.sql.Date(inputDate.getTime());
        Time time = new Time(inputDate.getTime());
        Double money = 0.0;
        try {
            money = Double.parseDouble(moneyEditText.getText().toString());
        }catch (Exception e){
            money = 0.0;
        }
        String classification1 = firstClassTextView.getText().toString();
        String classification2 = secondClassTextView.getText().toString();
        String remark = noteEditText.getText().toString();

        Account account = new Account();
        String [] arr = accountTextView.getText().toString().split("\\s+");
        account.setAccountName(arr[0]);
        if(arr.length > 1){
            account.setAccountCode(arr[1]);
        }
        String memeber = personTextView.getText().toString();
        String project = projectTextView.getText().toString();
        String vendor = storeTextView.getText().toString();
        Tally tally = new Tally(money,date,time,remark,account, ActionType.INCOME,
                classification1,classification2,memeber,project,vendor);
        return tally;

    }

    /*private void PopupWindowInit(View view){
        //一级分类选项菜单
        firstClassifyContentView = LayoutInflater.from(view.getContext()).inflate(R.layout.popupwindow_booking_classify,null);
        HzsListViewAdapter adapter1 = new HzsListViewAdapter(
                firstClassifyContentView.getContext(), R.layout.hzs_simple_item, Arrays.asList(FIRST_DATA));
        ListView listView1 = (ListView) firstClassifyContentView.findViewById(R.id.hzs_booking_classify_first_list_view);
        listView1.setAdapter(adapter1);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String firstClass = FIRST_DATA[i];
                classifyButton1.setText(firstClass);
                firstClassifyPopupWindow.dismiss();
            }
        });
        firstClassifyPopupWindow = new PopupWindow(firstClassifyContentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        firstClassifyPopupWindow.setFocusable(true);
        firstClassifyPopupWindow.setOutsideTouchable(true);
        firstClassifyPopupWindow.setAnimationStyle(R.style.hzs_window_anim_style);

        //二级分类选项菜单
        secondClassifyContentView = LayoutInflater.from(view.getContext()).inflate(R.layout.popupwindow_booking_classify,null);
        HzsListViewAdapter adapter2 = new HzsListViewAdapter(
                secondClassifyContentView.getContext(), R.layout.hzs_simple_item, Arrays.asList(SECOND_DATA));
        ListView listView2 = (ListView) secondClassifyContentView.findViewById(R.id.hzs_booking_classify_first_list_view);
        listView2.setAdapter(adapter2);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String secondClass = SECOND_DATA[i];
                classifyButton2.setText(secondClass);
                secondClassifyPopupWindow.dismiss();
            }
        });
        secondClassifyPopupWindow = new PopupWindow(secondClassifyContentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        secondClassifyPopupWindow.setFocusable(true);
        secondClassifyPopupWindow.setOutsideTouchable(true);
        secondClassifyPopupWindow.setAnimationStyle(R.style.hzs_window_anim_style);
    }*/
}