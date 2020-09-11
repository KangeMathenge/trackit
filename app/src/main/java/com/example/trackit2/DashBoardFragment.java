package com.example.trackit2;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackit2.Model.Data;
import com.firebase.client.Firebase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

//floating button
    private FloatingActionButton fab_main_btn;
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;

    //floating button textview

    private TextView fab_income_txt;
    private TextView fab_expense_txt;

    //boolean
    private boolean isOpen = false;

    //object of animation class

    private Animation FadeOpen, FadeClose;

    //firebase

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDataBase;

    public DashBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_dash_board, container, false);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();

        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDataBase =  FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);

        //connect floating button with layout

        fab_main_btn = myView.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn = myView.findViewById(R.id.income_ft_button);
        fab_expense_btn = myView.findViewById(R.id.expense_ft_btn);

        //connect floating text
        fab_income_txt =myView.findViewById(R.id.income_ft_text);
        fab_expense_txt =myView.findViewById(R.id.expense_ft_text);

        //
        FadeOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_open);
        FadeClose = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_close);


        fab_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addData();

                if(isOpen){
                    fab_income_btn.startAnimation(FadeClose);
                    fab_expense_btn.startAnimation(FadeClose);
                    fab_income_btn.setClickable(false);
                    fab_expense_btn.setClickable(false);


                    fab_income_txt.startAnimation(FadeClose);
                    fab_expense_txt.startAnimation(FadeClose);
                    fab_income_txt.setClickable(false);
                    fab_expense_txt.setClickable(false);
                    isOpen = false;
                }else{
                    fab_income_btn.startAnimation(FadeOpen);
                    fab_expense_btn.startAnimation(FadeOpen);
                    fab_income_btn.setClickable(true);
                    fab_expense_btn.setClickable(true);

                    fab_income_txt.startAnimation(FadeOpen);
                    fab_expense_txt.startAnimation(FadeOpen);
                    fab_income_txt.setClickable(true);
                    fab_expense_txt.setClickable(true);
                    isOpen = true;
                }
            }
        });




        return myView;
    }

    //floating button animation

    private void ftAnimation(){
        if(isOpen){
            fab_income_btn.startAnimation(FadeClose);
            fab_expense_btn.startAnimation(FadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);


            fab_income_txt.startAnimation(FadeClose);
            fab_expense_txt.startAnimation(FadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen = false;
        }else{
            fab_income_btn.startAnimation(FadeOpen);
            fab_expense_btn.startAnimation(FadeOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);

            fab_income_txt.startAnimation(FadeOpen);
            fab_expense_txt.startAnimation(FadeOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isOpen = true;
        }
    }

    private void addData(){

        //fab_button income

        fab_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomeDataInsert();

            }
        });

        fab_expense_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseDataInsert();
            }
        });
    }

    public void incomeDataInsert(){

        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myViewm =inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        myDialog.setView(myViewm);
        final AlertDialog dialog = myDialog.create();

        dialog.setCancelable(false);

        final EditText editAmount = myViewm.findViewById(R.id.amount_edt);
        final EditText editType = myViewm.findViewById(R.id.type_edt);
        final EditText editNote = myViewm.findViewById(R.id.note_edt);

        Button btnSave = myViewm.findViewById(R.id.btnSave);
        Button btnCancel = myViewm.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type= editType.getText().toString().trim();
                String amount = editAmount.getText().toString().trim();
                String note = editNote.getText().toString().trim();

                if(TextUtils.isEmpty(type)){
                    editType.setError("Required Field....");
                    return;
                }

                if(TextUtils.isEmpty(amount)){
                    editAmount.setError("Required Field....");
                    return;
                }

                int ouramountint=Integer.parseInt(amount);

                if(TextUtils.isEmpty(note)){
                    editNote.setError("Required Field....");
                    return;
                }

                String id = mIncomeDatabase.push().getKey();
                String mDate = DateFormat.getDateTimeInstance().format(new Date());

                Data data = new Data(ouramountint,type,note,id,mDate);

                mIncomeDatabase.child(id).setValue(data);


                Toast.makeText(getActivity(), "DATA ADDED", Toast.LENGTH_SHORT).show();
                ftAnimation();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void expenseDataInsert(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.custom_layout_for_insertdata, null);
        mydialog.setView(myview);

        final AlertDialog dialog =mydialog.create();

        dialog.setCancelable(false);

        final EditText amount = myview.findViewById(R.id.amount_edt);
        final EditText type = myview.findViewById(R.id.type_edt);
        final EditText note = myview.findViewById(R.id.note_edt);

        Button btnSave = myview.findViewById(R.id.btnSave);
        Button btnCancel = myview.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mAmount = amount.getText().toString().trim();
                String mtype = type.getText().toString().trim();
                String tnote = note.getText().toString().trim();

                if(TextUtils.isEmpty(mAmount)){
                    amount.setError("Required Field....");
                    return;
                }

                int inamount = Integer.parseInt(mAmount);

                if(TextUtils.isEmpty(mtype)){
                    type.setError("Required Field....");
                    return;
                }

                if(TextUtils.isEmpty(tnote)){
                    note.setError("Required Field....");
                    return;
                }
                 String id = mExpenseDataBase.push().getKey();
                String mDate = DateFormat.getDateTimeInstance().format(new Date());

                Data data = new Data(inamount,mtype,tnote,id,mDate);
                mExpenseDataBase.child(id).setValue(data);

                Toast.makeText(getActivity(), "Data Added",Toast.LENGTH_SHORT).show();

                ftAnimation();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftAnimation();
               dialog.dismiss();
            }
        });
        dialog.show();
    }
}
