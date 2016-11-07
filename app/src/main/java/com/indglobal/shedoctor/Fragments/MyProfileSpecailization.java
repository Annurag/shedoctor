package com.indglobal.shedoctor.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.indglobal.shedoctor.Activities.MyProfileActivity;
import com.indglobal.shedoctor.Adapters.LanguageAdapter;
import com.indglobal.shedoctor.Adapters.WriteServicesAdapter;
import com.indglobal.shedoctor.Adapters.WriteSpecializationAdapter;
import com.indglobal.shedoctor.Commans.Comman;
import com.indglobal.shedoctor.Commans.ExpandableHeightGridView;
import com.indglobal.shedoctor.Commans.RippleView;
import com.indglobal.shedoctor.R;

/**
 * Created by SONY on 19/09/2016.
 */
public class MyProfileSpecailization extends Fragment implements RippleView.OnRippleCompleteListener{

    LayoutInflater inflater;

    public static ExpandableHeightGridView grdEditPrflSpeclizations,grdEditPrflServcs;
    RippleView rplAddEditPrflSpeclizations,rplAddEditPrflServcs;

    WriteSpecializationAdapter writeSpecializationAdapter;
    WriteServicesAdapter writeServicesAdapter;

    Dialog dialog,dialogServc;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myprofile_specialization, container, false);

        grdEditPrflSpeclizations = (ExpandableHeightGridView)view.findViewById(R.id.grdEditPrflSpeclizations);
        grdEditPrflServcs = (ExpandableHeightGridView)view.findViewById(R.id.grdEditPrflServcs);
        rplAddEditPrflSpeclizations = (RippleView)view.findViewById(R.id.rplAddEditPrflSpeclizations);
        rplAddEditPrflServcs = (RippleView)view.findViewById(R.id.rplAddEditPrflServcs);

        writeSpecializationAdapter = new WriteSpecializationAdapter(getActivity(), MyProfileActivity.specializationArrayList);
        grdEditPrflSpeclizations.setAdapter(writeSpecializationAdapter);
        grdEditPrflSpeclizations.setExpanded(true);

        writeServicesAdapter = new WriteServicesAdapter(getActivity(),MyProfileActivity.servicesArrayList);
        grdEditPrflServcs.setAdapter(writeServicesAdapter);
        grdEditPrflServcs.setExpanded(true);

        rplAddEditPrflSpeclizations.setOnRippleCompleteListener(this);
        rplAddEditPrflServcs.setOnRippleCompleteListener(this);

        return view;

    }

    @Override
    public void onComplete(RippleView rippleView) {
        int id = rippleView.getId();

        switch (id){
            case R.id.rplAddEditPrflSpeclizations:
                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_specialization_dialog);
                dialog.show();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                TextView tvDialogSpeclznCancl = (TextView)dialog.findViewById(R.id.tvDialogSpeclznCancl);
                TextView tvDialogSpeclznPst = (TextView)dialog.findViewById(R.id.tvDialogSpeclznPst);
                final EditText etDialogSpeclzn = (EditText)dialog.findViewById(R.id.etDialogSpeclzn);


                tvDialogSpeclznCancl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tvDialogSpeclznPst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String specialzn = etDialogSpeclzn.getText().toString();
                        if (specialzn.equalsIgnoreCase("")){
                            Toast.makeText(getActivity(),"Please provide a specialization!",Toast.LENGTH_SHORT).show();
                        }else {

                            MyProfileActivity.specializationArrayList.add(specialzn);
                            writeSpecializationAdapter.notifyDataSetChanged();
                            grdEditPrflSpeclizations.setExpanded(true);
                            dialog.dismiss();

                        }
                    }
                });

                break;

            case R.id.rplAddEditPrflServcs:
                dialogServc = new Dialog(getActivity());
                dialogServc.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogServc.setContentView(R.layout.add_services_dialog);
                dialogServc.show();
                dialogServc.setCancelable(false);
                dialogServc.setCanceledOnTouchOutside(false);

                TextView tvDialogServcesCancl = (TextView)dialogServc.findViewById(R.id.tvDialogServcesCancl);
                TextView tvDialogServcesPst = (TextView)dialogServc.findViewById(R.id.tvDialogServcesPst);
                final EditText etDialogServces = (EditText)dialogServc.findViewById(R.id.etDialogServces);


                tvDialogServcesCancl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogServc.dismiss();
                    }
                });

                tvDialogServcesPst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String servc = etDialogServces.getText().toString();
                        if (servc.equalsIgnoreCase("")){
                            Toast.makeText(getActivity(),"Please provide a service!",Toast.LENGTH_SHORT).show();
                        }else {

                            MyProfileActivity.servicesArrayList.add(servc);
                            writeServicesAdapter.notifyDataSetChanged();
                            grdEditPrflServcs.setExpanded(true);
                            dialogServc.dismiss();

                        }
                    }
                });

                break;
        }
    }
}
