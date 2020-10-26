package com.example.hoitnote.views.tallyadd;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.hoitnote.R;
import com.example.hoitnote.utils.enums.ActionType;
import com.example.hoitnote.utils.enums.BookingType;
import com.example.hoitnote.utils.enums.IconType;
import com.example.hoitnote.utils.helpers.BookingDataHelper;
import com.example.hoitnote.views.flow.ManageClassActivity;
import com.example.hoitnote.views.flow.ManageOptionActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class BookingBaseFragment extends Fragment {
    protected BookingType bookingType;
    private ActionType actionType;
    protected boolean hasTemp;
    protected String timeString;
    protected String personString;
    protected String storeString;
    protected String projectString;
    protected EditText noteEditText;
    protected EditText moneyEditText;
    protected int classSelectOption1 = 0;
    protected int classSelectOption2 = 0;
    protected Date inputDate = new Date();
    protected OptionsPickerView pvPersonOptions;
    protected OptionsPickerView pvStoreOptions;
    protected OptionsPickerView pvProjectOptions;
    protected TextView timeTextView;
    protected TextView personTextView;
    protected TextView storeTextView;
    protected TextView projectTextView;

    protected String firstClassString;
    protected String secondClassString;
    protected OptionsPickerView pvClassificationOptions;
    protected TextView firstClassTextView;
    protected TextView secondClassTextView;
    protected List<String> classifications1;
    protected List<List<String>> classifications2;
    protected List<String> personItems;
    protected List<String> projectItems;
    protected List<String> storeItems;
    protected Typeface tf;
    public BookingBaseFragment(BookingType bookingType){
        this.bookingType = bookingType;
        switch (bookingType){
            case OUTCOME:
                actionType = ActionType.OUTCOME;
                break;
            default:
                actionType = ActionType.INCOME;
                break;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void clickButtonInit(View view){
        tf = ResourcesCompat.getFont(getContext(), R.font.fontawesome5solid);
        editTextInit(view);
        timeButtonInit(view);
        personButtonInit(view);
        storeButtonInit(view);
        projectButtonInit(view);
    }
    protected void showAddFirstClassDialog(){
        Intent intent = new Intent(getContext(), ManageClassActivity.class);
        intent.putExtra("booking_type",bookingType.ordinal());
        intent.putExtra("classification1",classSelectOption1);
        startActivity(intent);
        /*View view = LayoutInflater.from(getContext()).inflate(R.layout.hzs_add_class_view,null,false);
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
                String newClassName = editText.getText().toString();
                BookingDataHelper.addClassification1(newClassName, actionType);
                dialog.dismiss();
            }
        });

        dialog.show();*/
    }
    protected void showAddSecondClassDialog(){
        Intent intent = new Intent(getContext(), ManageClassActivity.class);
        intent.putExtra("booking_type",bookingType.ordinal());
        intent.putExtra("classification1",classSelectOption1);
        startActivity(intent);
        /*View view = LayoutInflater.from(getContext()).inflate(R.layout.hzs_add_class_view,null,false);
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
                String newClassName = editText.getText().toString();
                String classification1 = BookingDataHelper.getClassifications1(bookingType).get(classSelectOption1);
                BookingDataHelper.addClassification2(classification1,newClassName,actionType);
                dialog.dismiss();
            }
        });

        dialog.show();*/
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPickerView();
    }
    protected void refreshPickerView(){
        if(pvClassificationOptions != null){
            classifications1.clear();
            classifications1.addAll(BookingDataHelper.getClassifications1WithIcons(bookingType));
            classifications2.clear();
            classifications2.addAll(BookingDataHelper.getClassifications2WithIcons(bookingType));
            pvClassificationOptions.setPicker(classifications1,classifications2);
            firstClassString = classifications1.get(0);
            secondClassString = classifications2.get(0).get(0);
            firstClassTextView.setText(firstClassString);
            secondClassTextView.setText(secondClassString);
        }
        if(pvPersonOptions != null){
            personItems.clear();
            personItems.addAll(BookingDataHelper.getPersons());
            pvPersonOptions.setPicker(personItems);
            personString = personItems.get(0);
            personTextView.setText(personString);
        }
        if(pvProjectOptions != null){
            projectItems.clear();
            projectItems.addAll(BookingDataHelper.getProjects());
            pvProjectOptions.setPicker(projectItems);
            projectString = projectItems.get(0);
            projectTextView.setText(projectString);
        }
        if(pvStoreOptions != null){
            storeItems.clear();
            storeItems.addAll(BookingDataHelper.getStores());
            pvStoreOptions.setPicker(storeItems);
            storeString = storeItems.get(0);
            storeTextView.setText(storeString);
        }

    }
    protected void classifyButtonInit(View view){
        final View chooseClassButton = view.findViewById(R.id.hzs_booking_classify_button);
        firstClassTextView = view.findViewById(R.id.hzs_booking_first_class);
        secondClassTextView = view.findViewById(R.id.hzs_booking_second_class);
        classifications1 = BookingDataHelper.getClassifications1WithIcons(bookingType);
        classifications2 = BookingDataHelper.getClassifications2WithIcons(bookingType);
        firstClassString = classifications1.get(0);
        secondClassString = classifications2.get(0).get(0);
        pvClassificationOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                firstClassString = classifications1.get(options1);
                secondClassString = classifications2.get(options1).get(options2);
                firstClassTextView.setText(firstClassString);
                secondClassTextView.setText(secondClassString);
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
                final TextView tvSubmit = v.findViewById(R.id.hzs_class_pickerview_finish);
                final TextView tvCancel = v.findViewById(R.id.hzs_class_pickerview_cancel);
                Button addFirstClass = v.findViewById(R.id.hzs_add_first_class_button);
                Button addSecondClass = v.findViewById(R.id.hzs_add_second_class_button);
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
                        pvClassificationOptions.setPicker(BookingDataHelper.getClassifications1WithIcons(bookingType),
                                BookingDataHelper.getClassifications2WithIcons(bookingType));
                    }
                });
                addSecondClass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddSecondClassDialog();
                        pvClassificationOptions.setPicker(BookingDataHelper.getClassifications1WithIcons(bookingType),
                                BookingDataHelper.getClassifications2WithIcons(bookingType));
                    }
                });

            }
        }).isDialog(true)
                .setTypeface(tf)
                .build();
        pvClassificationOptions.setPicker(BookingDataHelper.getClassifications1WithIcons(bookingType),
                BookingDataHelper.getClassifications2WithIcons(bookingType));
        chooseClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvClassificationOptions.show();
            }
        });
    }
    /*protected void accountButtonInit(View view){
        final View chooseAccountButton = view.findViewById(R.id.hzs_booking_account_button);
        final List<String> accountItems = BookingDataHelper.getAccounts();
        accountTextView = view.findViewById(R.id.hzs_booking_account_button);
        accountString = accountItems.get(0);
        final OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                accountString = accountItems.get(options1);
                accountTextView.setText(accountString);
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
    }*/
    protected void timeButtonInit(View view){
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        final View chooseTimeButton = view.findViewById(R.id.hzs_booking_time_button);
        timeTextView = view.findViewById(R.id.hzs_booking_time_button);
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(1970,0,1);
        endDate.set(2020,11,31);
        final DateFormat df2 = DateFormat.getDateTimeInstance();
        timeString = df2.format(new Date());
        //时间选择器
        final TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
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
    protected void personButtonInit(View view){
        final View chooseAccountButton = view.findViewById(R.id.hzs_booking_person_button);
        personItems = BookingDataHelper.getPersons();
        personTextView = view.findViewById(R.id.hzs_booking_person_button);
        if(!personItems.isEmpty()){
            personString = personItems.get(0);
        }else{
            personString = "无";
        }

        pvPersonOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                personString = personItems.get(options1);
                personTextView.setText(personString);
            }
        }).setLayoutRes(R.layout.hzs_thirdparty_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = v.findViewById(R.id.hzs_pickerview_finish);
                final TextView tvCancel = v.findViewById(R.id.hzs_pickerview_cancel);
                Button addFirstClass = v.findViewById(R.id.hzs_add_button);
                addFirstClass.setText("+ 增加成员");
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
    protected void showAddPersonDialog(){
        Intent intent = new Intent(getContext(), ManageOptionActivity.class);
        intent.putExtra("icon_type", IconType.MEMBER.ordinal());
        startActivity(intent);
        /*View view = LayoutInflater.from(getContext()).inflate(R.layout.hzs_add_class_view,null,false);
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
                String newPersonName = editText.getText().toString();

                BookingDataHelper.addPerson(newPersonName);
                dialog.dismiss();
            }
        });

        dialog.show();*/
    }
    protected void showAddStoreDialog(){
        Intent intent = new Intent(getContext(), ManageOptionActivity.class);
        intent.putExtra("icon_type", IconType.VENDOR.ordinal());
        startActivity(intent);
        /*View view = LayoutInflater.from(getContext()).inflate(R.layout.hzs_add_class_view,null,false);
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
                String newStoreName = editText.getText().toString();

                BookingDataHelper.addStore(newStoreName);
                dialog.dismiss();
            }
        });

        dialog.show();*/
    }

    protected void editTextInit(View view){
        noteEditText = view.findViewById(R.id.hzs_booking_note_button);
        moneyEditText = view.findViewById(R.id.hzs_booking_money_button);
    }
    protected void storeButtonInit(View view){
        final View chooseStoreButton = view.findViewById(R.id.hzs_booking_store_button);
        storeItems = BookingDataHelper.getStores();
        storeTextView = view.findViewById(R.id.hzs_booking_store_button);
        if(!storeItems.isEmpty()){
            storeString = storeItems.get(0);
        }else{
            storeString = "无";
        }
        pvStoreOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                storeString = storeItems.get(options1);
                storeTextView.setText(storeString);
            }
        }).setLayoutRes(R.layout.hzs_thirdparty_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = v.findViewById(R.id.hzs_pickerview_finish);
                final TextView tvCancel = v.findViewById(R.id.hzs_pickerview_cancel);
                Button addFirstClass = (Button)v.findViewById(R.id.hzs_add_button);
                addFirstClass.setText("+ 增加商家");
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
    protected void projectButtonInit(View view){
        final View chooseProjectButton = view.findViewById(R.id.hzs_booking_project_button);
        projectItems = BookingDataHelper.getProjects();
        projectTextView = view.findViewById(R.id.hzs_booking_project_button);
        if(!projectItems.isEmpty()){
            projectString = projectItems.get(0);
        }else{
            projectString = "无";
        }
        pvProjectOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                projectString = projectItems.get(options1);
                projectTextView.setText(projectString);
            }
        }).setLayoutRes(R.layout.hzs_thirdparty_pickerview, new CustomListener() {
            @Override
            public void customLayout(View v) {
                //自定义布局中的控件初始化及事件处理
                final TextView tvSubmit = v.findViewById(R.id.hzs_pickerview_finish);
                final TextView tvCancel = v.findViewById(R.id.hzs_pickerview_cancel);
                Button addFirstClass = v.findViewById(R.id.hzs_add_button);
                addFirstClass.setText("+ 增加项目");
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
    protected void showAddProjectDialog(){
        Intent intent = new Intent(getContext(), ManageOptionActivity.class);
        intent.putExtra("icon_type", IconType.PROJECT.ordinal());
        startActivity(intent);
        /*View view = LayoutInflater.from(getContext()).inflate(R.layout.hzs_add_class_view,null,false);
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
                String newProjectName = editText.getText().toString();

                BookingDataHelper.addProject(newProjectName);
                dialog.dismiss();
            }
        });
        dialog.show();*/
    }

    @Override
    public void onPause() {
        if(hasTemp){
            saveTempTally();
        }
        super.onPause();
    }

    protected void saveTempTally(){

    }

    protected void restoreTempTally(){

    }

    protected void showTempTally(){

    }

}
