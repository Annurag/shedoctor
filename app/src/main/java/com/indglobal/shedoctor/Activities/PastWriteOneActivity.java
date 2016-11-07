package com.indglobal.shedoctor.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Adapters.LabTestWriteAdapter;
import com.indglobal.shedoctor.Adapters.PaymentAdapter;
import com.indglobal.shedoctor.Beans.LabTestItem;
import com.indglobal.shedoctor.Beans.PaymentItem;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.Fragments.NotificationFragment;
import com.indglobal.shedoctor.Fragments.Past_Write_Preview;
import com.indglobal.shedoctor.Fragments.Past_Write_Three;
import com.indglobal.shedoctor.Fragments.Past_Write_Two;
import com.indglobal.shedoctor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Android on 9/12/16.
 */
public class PastWriteOneActivity extends AppCompatActivity implements View.OnClickListener,RippleView.OnRippleCompleteListener,FragmentManager.OnBackStackChangedListener {

    Toolbar mtoolbar;
    FragmentManager manager;

    RippleView rplPastWrtOneNext;
    TextView tvToolbarDiagnosis,tvToolbarLabtest,tvToolbarMedicn,tvToolbarFinish;
    int count;

    EditText etDiagnosisReport;
    RelativeLayout rlNextVisit;
    TextView tvNextvisit;
    public static String DiagnosisReport,NextVisit,OthersLab;

    public static ArrayList<String> checkLabsNameList;
    public static JSONArray medicinjsonArray;
    JSONObject medicinjsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.past_write_one);

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        manager = getSupportFragmentManager();

        rplPastWrtOneNext = (RippleView)findViewById(R.id.rplPastWrtOneNext);
        tvToolbarDiagnosis = (TextView)findViewById(R.id.tvToolbarDiagnosis);
        tvToolbarLabtest = (TextView)findViewById(R.id.tvToolbarLabtest);
        tvToolbarMedicn = (TextView)findViewById(R.id.tvToolbarMedicn);
        tvToolbarFinish = (TextView)findViewById(R.id.tvToolbarFinish);

        etDiagnosisReport = (EditText)findViewById(R.id.etDiagnosisReport);
        rlNextVisit = (RelativeLayout)findViewById(R.id.rlNextVisit);
        tvNextvisit = (TextView)findViewById(R.id.tvNextvisit);

        rplPastWrtOneNext.setOnRippleCompleteListener(this);

        manager.addOnBackStackChangedListener(this);

        rlNextVisit.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    public void onBackStackChanged() {
        count = manager.getBackStackEntryCount();

        if(count==0){
            tvToolbarDiagnosis.setBackgroundResource(R.drawable.arrow_shape);
            tvToolbarLabtest.setBackgroundColor(getResources().getColor(R.color.lightGray));
            tvToolbarMedicn.setBackgroundColor(getResources().getColor(R.color.lightGray));
            tvToolbarFinish.setBackgroundColor(getResources().getColor(R.color.lightGray));
        }

        for(int i = count-1;i>=0;i--){

            FragmentManager.BackStackEntry backStackEntry = manager.getBackStackEntryAt(i);
            String name = backStackEntry.getName();
            if(name.equalsIgnoreCase("Two")) {
                tvToolbarDiagnosis.setBackgroundColor(getResources().getColor(R.color.gray));
                tvToolbarLabtest.setBackgroundResource(R.drawable.arrow_shape);
                tvToolbarMedicn.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvToolbarFinish.setBackgroundColor(getResources().getColor(R.color.lightGray));
                return;

            }else if(name.equalsIgnoreCase("Three")){
                tvToolbarDiagnosis.setBackgroundColor(getResources().getColor(R.color.gray));
                tvToolbarLabtest.setBackgroundColor(getResources().getColor(R.color.gray));
                tvToolbarMedicn.setBackgroundResource(R.drawable.arrow_shape);
                tvToolbarFinish.setBackgroundColor(getResources().getColor(R.color.lightGray));
                return;

            }else if(name.equalsIgnoreCase("Four")){
                tvToolbarDiagnosis.setBackgroundColor(getResources().getColor(R.color.gray));
                tvToolbarLabtest.setBackgroundColor(getResources().getColor(R.color.gray));
                tvToolbarMedicn.setBackgroundResource(R.drawable.arrow_shape);
                tvToolbarFinish.setBackgroundColor(getResources().getColor(R.color.lightGray));
                return;

            }else {
                tvToolbarDiagnosis.setBackgroundResource(R.drawable.arrow_shape);
                tvToolbarLabtest.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvToolbarMedicn.setBackgroundColor(getResources().getColor(R.color.lightGray));
                tvToolbarFinish.setBackgroundColor(getResources().getColor(R.color.lightGray));
                return;
            }
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.rlNextVisit:
                openCalander();
                break;
        }
    }

    @Override
    public void onComplete(RippleView rippleView) {

        int id = rippleView.getId();

        switch (id){

            case R.id.rplPastWrtOneNext:
                if(count==0){

                    DiagnosisReport = etDiagnosisReport.getText().toString();
                    NextVisit = tvNextvisit.getText().toString();

                    if (!Comman.isConnectionAvailable(PastWriteOneActivity.this)){
                        Toast.makeText(PastWriteOneActivity.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                    }else if (DiagnosisReport.equalsIgnoreCase("")){
                        Toast.makeText(PastWriteOneActivity.this,"Please provide Diagnosis Report!",Toast.LENGTH_SHORT).show();
                    }else {
                        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.enter_from_right, R.anim.exit_to_right, R.anim.exit_to_right);
                        transaction.add(R.id.fragment_past_write_one, new Past_Write_Two(),"Two");
                        transaction.addToBackStack("Two");
                        transaction.commit();
                    }


                }else if (count==1){
                    checkLabsNameList = new ArrayList<>();
                    for (LabTestItem f : LabTestWriteAdapter.getBox()) {
                        if (f.labSelected) {
                            checkLabsNameList.add(f.getLabName());
                        }
                    }

                    OthersLab = Past_Write_Two.etOthers.getText().toString();

                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.trans_in_left, R.anim.trans_in_left, R.anim.trans_right_out, R.anim.trans_right_out);
                    transaction.add(R.id.fragment_past_write_one, new Past_Write_Three(),"Three");
                    transaction.addToBackStack("Three");
                    transaction.commit();


                }else if (count==2){

                    medicinjsonArray = new JSONArray();

                    for (int i = 0; i < Past_Write_Three.rvAddMedicins.getAdapter().getItemCount(); i++) {

                        View medicibView = Past_Write_Three.rvAddMedicins.getChildAt(i);
                        if (medicibView.findViewById(R.id.etmedicineName) != null) {

                            Spinner spinMorning = (Spinner)medicibView.findViewById(R.id.spinMorning);
                            Spinner spinAftrnon = (Spinner)medicibView.findViewById(R.id.spinAftrnon);
                            Spinner spinEvng = (Spinner)medicibView.findViewById(R.id.spinEvng);
                            Spinner spinNght = (Spinner)medicibView.findViewById(R.id.spinNght);
                            Spinner spinMedicineType = (Spinner)medicibView.findViewById(R.id.spinMedicineType);
                            EditText etAddNote = (EditText)medicibView.findViewById(R.id.etAddNote);
                            EditText etmedicineName = (EditText)medicibView.findViewById(R.id.etmedicineName);

                            String medcnName = etmedicineName.getText().toString();
                            String medcnNote = etAddNote.getText().toString();

                            if (Past_Write_Three.addMedicineItemArrayList.size()>1){

                                if (medcnName.length()<5){
                                    Toast.makeText(PastWriteOneActivity.this,"Please provide medicine name!",Toast.LENGTH_SHORT).show();
                                    Past_Write_Three.rvAddMedicins.scrollToPosition(i);
                                }else {

                                    medicinjsonObject = new JSONObject();
                                    try {
                                        medicinjsonObject.put("count",i);
                                        medicinjsonObject.put("type",spinMedicineType.getSelectedItem());
                                        medicinjsonObject.put("morning",spinMorning.getSelectedItem());
                                        medicinjsonObject.put("afternoon",spinAftrnon.getSelectedItem());
                                        medicinjsonObject.put("evening",spinEvng.getSelectedItem());
                                        medicinjsonObject.put("night",spinNght.getSelectedItem());
                                        medicinjsonObject.put("name",medcnName);
                                        if (!medcnNote.equalsIgnoreCase("")){
                                            medicinjsonObject.put("addnote",true);
                                            medicinjsonObject.put("note",medcnNote);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    medicinjsonArray.put(medicinjsonObject);

                                }
                            }else {

                                if(!medcnName.equalsIgnoreCase("")){
                                    medicinjsonObject = new JSONObject();
                                    try {
                                        medicinjsonObject.put("count",i);
                                        medicinjsonObject.put("type",spinMedicineType.getSelectedItem());
                                        medicinjsonObject.put("morning",spinMorning.getSelectedItem());
                                        medicinjsonObject.put("afternoon",spinAftrnon.getSelectedItem());
                                        medicinjsonObject.put("evening",spinEvng.getSelectedItem());
                                        medicinjsonObject.put("night",spinNght.getSelectedItem());
                                        medicinjsonObject.put("name",medcnName);
                                        if (!medcnNote.equalsIgnoreCase("")){
                                            medicinjsonObject.put("addnote",true);
                                            medicinjsonObject.put("note",medcnNote);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    medicinjsonArray.put(medicinjsonObject);

                                }

                            }
                        }
                    }

                    android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.trans_in_left, R.anim.trans_in_left, R.anim.trans_right_out, R.anim.trans_right_out);
                    transaction.add(R.id.fragment_past_write_preview, new Past_Write_Preview(),"Four");
                    transaction.addToBackStack("Four");
                    transaction.commit();

                }
                break;
        }
    }


    private void openCalander() {
        final Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                PastWriteOneActivity.this,  new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mcurrentDate.set(Calendar.YEAR, year);
                mcurrentDate.set(Calendar.MONTH, monthOfYear);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd-MMMM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String selectDate = sdf.format(mcurrentDate.getTime());


                tvNextvisit.setText(selectDate);

            }
        }, mYear, mMonth, mDay);


        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        count = manager.getBackStackEntryCount();
        if (count==0){
            Toast.makeText(PastWriteOneActivity.this,"Please write prescription first!",Toast.LENGTH_SHORT).show();
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        }

    }
}