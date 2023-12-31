package com.abonado.academicplanner.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.abonado.academicplanner.R;
import com.abonado.academicplanner.database.AssessmentRepository;
import com.abonado.academicplanner.database.CourseRepository;
import com.abonado.academicplanner.database.TermRepository;
import com.abonado.academicplanner.entities.Assessment;
import com.abonado.academicplanner.entities.Course;
import com.abonado.academicplanner.entities.Term;
import com.abonado.academicplanner.utilities.CourseAdapter;
import com.abonado.academicplanner.utilities.Receiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TermDetails extends AppCompatActivity {

    TextView nonEditId;
    EditText editName;
    EditText editStart;
    EditText editEnd;
    String mTermStart;
    String mTermEnd;
    String mTermName;
    int mTermId = 0;
    TermRepository termRepository;
    CourseRepository courseRepository;
    boolean isTermUpdate = false;
    int termIdToUpdate = 0;
    FloatingActionButton termFloatButtonMenu;
    List<Course> associatedCourses;
    CourseAdapter courseAdapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        Toolbar myToolbar = findViewById(R.id.term_details_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent intent = getIntent();

        nonEditId = findViewById(R.id.trmIdTxt);
        editName = findViewById(R.id.trmNmEdTxt);
        editStart = findViewById(R.id.trmStrtTxt);
        editEnd = findViewById(R.id.trmEndTxt);


        termIdToUpdate = intent.getIntExtra("term_id",0);

        recyclerView = findViewById(R.id.coursesLstTermDtlsRcyle);
        courseRepository = new CourseRepository(getApplication());
        associatedCourses = courseRepository.getAllAsscCourses(termIdToUpdate);
        courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(associatedCourses);

        if(termIdToUpdate == 0){

            nonEditId.setText("New ID");

        }
        else {

            nonEditId.setText(String.valueOf(termIdToUpdate));
            isTermUpdate = true;
        }

        if(isTermUpdate){
            String xTermName = intent.getStringExtra("term_name");
            String xTermStart = intent.getStringExtra("term_start");
            String xTermEnd =  intent.getStringExtra("term_end");

            editName.setText(xTermName);
            editStart.setText(xTermStart);
            editEnd.setText(xTermEnd);

        }


        termFloatButtonMenu = findViewById(R.id.termFloatButMenu);
        termFloatButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(TermDetails.this,termFloatButtonMenu);
                popupMenu.getMenuInflater().inflate(R.menu.term_plus_button_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        termRepository = new TermRepository(getApplication());

                        if(item.getItemId() == R.id.termStartNotifyOpt){

                            boolean isValidNotifyData = true;

                            if(isTermUpdate){

                                editName = findViewById(R.id.trmNmEdTxt);
                                editStart = findViewById(R.id.trmStrtTxt);
                                editEnd = findViewById(R.id.trmEndTxt);
                                nonEditId = findViewById(R.id.trmIdTxt);
                                String termId = nonEditId.getText().toString();
                                String termName = editName.getText().toString();
                                String termStart = editStart.getText().toString();
                                String termEnd = editEnd.getText().toString();


                                Term termToCheck = termRepository.getTerm(Integer.parseInt(termId));
                                if(!termToCheck.getTermName().equals(termName)){
                                    isValidNotifyData = false;
                                }
                                if(!termToCheck.getTermStart().equals(termStart)){
                                    isValidNotifyData = false;
                                }
                                if(!termToCheck.getTermEnd().equals(termEnd)){
                                    isValidNotifyData = false;
                                }

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                Date startDate = null;

                                try {

                                    startDate = sdf.parse(termStart);

                                }
                                catch (Exception e){

                                    isValidNotifyData = false;
                                    e.fillInStackTrace();
                                }

                                if(isValidNotifyData){

                                    Long startTrigger = startDate.getTime();
                                    Intent startIntent = new Intent(TermDetails.this,
                                            Receiver.class);
                                    startIntent.setAction("termStartDateNotify");
                                    startIntent.putExtra("startTermKey", "Term ID: " + termId
                                            + "\nTerm Name: " + termName +"\nSTARTS: " + termStart);
                                    PendingIntent termStartSender = PendingIntent.getBroadcast(TermDetails.this,
                                            ++Home.termStartAlertNum, startIntent, PendingIntent.FLAG_IMMUTABLE);
                                    AlarmManager asmntStartAlarmManager =
                                            (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                                    try{
                                        asmntStartAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startTrigger, termStartSender);

                                    }
                                    catch (SecurityException e){

                                        Toast.makeText(getApplicationContext(),"There is a problem with " +
                                                "setting the TERM START alarm.", Toast.LENGTH_LONG).show();
                                        e.fillInStackTrace();
                                    }


                                    Toast.makeText(getApplicationContext(), "NOTIFY TERM START" +
                                                    "\nID: " + termId + "\tName: " + termName +
                                                    "\nStart Date: " + termStart,
                                            Toast.LENGTH_LONG).show();

                                    return true;

                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Invalid Selection." +
                                            "\nData changes must be saved before setting a notification." +
                                            "\nStart date must be before end date." +
                                            "\nDate Format: YYYY-MM-DD", Toast.LENGTH_LONG).show();

                            }
                            else
                                Toast.makeText(getApplicationContext(), "Term must be saved to database" +
                                        " before notifications can be set.", Toast.LENGTH_LONG).show();
                        }

                        if(item.getItemId() == R.id.termAllTermsOpt){

                            Intent intent = new Intent(TermDetails.this, TermsList.class);
                            startActivity(intent);

                        }


                        if(item.getItemId() == R.id.termEndNotifyOpt){

                            boolean isValidNotifyData = true;

                            if(isTermUpdate){

                                editName = findViewById(R.id.trmNmEdTxt);
                                editStart = findViewById(R.id.trmStrtTxt);
                                editEnd = findViewById(R.id.trmEndTxt);
                                nonEditId = findViewById(R.id.trmIdTxt);
                                String termId = nonEditId.getText().toString();
                                String termName = editName.getText().toString();
                                String termStart = editStart.getText().toString();
                                String termEnd = editEnd.getText().toString();

                                Term termToCheck = termRepository.getTerm(Integer.parseInt(termId));
                                if(!termToCheck.getTermName().equals(termName)){
                                    isValidNotifyData = false;
                                }
                                if(!termToCheck.getTermStart().equals(termStart)){
                                    isValidNotifyData = false;
                                }
                                if(!termToCheck.getTermEnd().equals(termEnd)){
                                    isValidNotifyData = false;
                                }


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                Date endDate = null;
                                try {
                                    endDate = sdf.parse(termEnd);
                                }
                                catch (Exception e){

                                    isValidNotifyData = false;
                                    e.fillInStackTrace();
                                }

                                if(isValidNotifyData){


                                    Long endTrigger = endDate.getTime();
                                    Intent endIntent = new Intent(TermDetails.this, Receiver.class);
                                    endIntent.setAction("termEndDateNotify");
                                    endIntent.putExtra("termEndKey", "Term ID: " + termId
                                            + "\tTerm Name: " + termName + "\n ENDS: " + termEnd);
                                    PendingIntent termEndSender = PendingIntent.getBroadcast(TermDetails.this,
                                            ++Home.termEndAlertNum, endIntent, PendingIntent.FLAG_IMMUTABLE);
                                    AlarmManager asmntEndAlarmManager =
                                            (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                                    try{

                                        asmntEndAlarmManager.setExactAndAllowWhileIdle (AlarmManager.RTC_WAKEUP, endTrigger, termEndSender);

                                    }
                                    catch (SecurityException e){

                                        Toast.makeText(getApplicationContext(),"There is a problem with " +
                                                "setting the TERM END alarm.", Toast.LENGTH_LONG).show();
                                        e.fillInStackTrace();
                                    }

                                    Toast.makeText(getApplicationContext(), "NOTIFY TERM END" +
                                                    "\nID: " + termId + "\tName: " + termName +
                                                    "\nEnd Date: " + termEnd,
                                            Toast.LENGTH_LONG).show();

                                    return true;
                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Invalid Selection." +
                                            "\nData changes must be saved before setting a notification." +
                                            "\nStart date must be before end date." +
                                            "\nDate Format: YYYY-MM-DD", Toast.LENGTH_LONG).show();

                            }
                            else
                                Toast.makeText(getApplicationContext(), "Term must be saved to database" +
                                        " before notifications can be set.", Toast.LENGTH_LONG).show();

                        }

                        if(item.getItemId() == R.id.termDeleteOpt){

                            if(isTermUpdate){

                                getTermDeleteConfirmation();

                                return true;

                            }
                            else
                                Toast.makeText(getApplicationContext(),"Select a term " +
                                        "from the all terms list for deletion.", Toast.LENGTH_LONG).show();
                        }

                        if(item.getItemId() == R.id.termSaveOpt){

                            termRepository = new TermRepository(getApplication());
                            boolean isTermTimeValid = true;

                            if(isTermUpdate){

                                nonEditId = findViewById(R.id.trmIdTxt);
                                editName = findViewById(R.id.trmNmEdTxt);
                                editStart = findViewById(R.id.trmStrtTxt);
                                editEnd = findViewById(R.id.trmEndTxt);
                                mTermId = termIdToUpdate;
                                mTermName = editName.getText().toString();

                                mTermStart = editStart.getText().toString();
                                mTermEnd = editEnd.getText().toString();


                                try{
                                    LocalDate checkStart = LocalDate.parse(mTermStart);
                                    LocalDate checkEnd = LocalDate.parse(mTermEnd);

                                    if(checkEnd.isBefore(checkStart)){
                                        isTermTimeValid = false;
                                    }
                                }
                                catch (Exception e){
                                    isTermTimeValid = false;
                                }


                                if(isTermTimeValid){

                                    Term term = new Term(mTermId, mTermName, mTermStart, mTermEnd);

                                    termRepository.update(term);

                                    isTermUpdate = false;

                                    Toast.makeText(getApplicationContext(), "TERM SAVED",
                                            Toast.LENGTH_LONG).show();

                                    finish();

                                    return true;
                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Invalid DATE entry." +
                                            "\nStart date must be before end date." +
                                            "\nFormat: YYYY-MM-DD", Toast.LENGTH_LONG).show();

                            }
                            else {

                                editName = findViewById(R.id.trmNmEdTxt);
                                editStart = findViewById(R.id.trmStrtTxt);
                                editEnd = findViewById(R.id.trmEndTxt);
                                mTermName = editName.getText().toString();

                                mTermStart = editStart.getText().toString();
                                mTermEnd = editEnd.getText().toString();


                                try{

                                    LocalDate checkStart = LocalDate.parse(mTermStart);
                                    LocalDate checkEnd = LocalDate.parse(mTermEnd);

                                    if(checkEnd.isBefore(checkStart)){
                                        isTermTimeValid = false;
                                    }
                                }
                                catch (Exception e){
                                    isTermTimeValid = false;
                                }


                                if(isTermTimeValid){

                                    Term term = new Term(mTermId, mTermName, mTermStart, mTermEnd);

                                    termRepository.insert(term);

                                    Toast.makeText(getApplicationContext(), "TERM SAVED",
                                            Toast.LENGTH_LONG).show();

                                    finish();

                                    return true;
                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Invalid DATE entry." +
                                            "\nStart date must be before end date." +
                                            "\nFormat: YYYY-MM-DD", Toast.LENGTH_LONG).show();

                            }
                        }


                        return false;
                    }
                });

                popupMenu.show();
            }
        });

    }

    @Override
    protected void onResume(){

        super.onResume();
        associatedCourses = courseRepository.getAllAsscCourses(termIdToUpdate);
        courseAdapter.setCourses(associatedCourses);

    }



    public void getTermDeleteConfirmation(){

        AssessmentRepository assessmentRepository = new AssessmentRepository(getApplication());
        termRepository = new TermRepository(getApplication());
        courseRepository = new CourseRepository(getApplication());

        AlertDialog.Builder termConfirmBuilder = new AlertDialog.Builder(this);
        termConfirmBuilder.setTitle("CONFIRMATION");
        termConfirmBuilder.setMessage("Delete TERM ID #" + termIdToUpdate + "?");
        termConfirmBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                List<Course> coursesAsscToTrmList = courseRepository.getAllAsscCourses(termIdToUpdate);

                if(!coursesAsscToTrmList.isEmpty()){

                    AlertDialog.Builder termAsscCrsesConfirmBuilder = new AlertDialog.Builder(TermDetails.this);
                    termAsscCrsesConfirmBuilder.setTitle("CONFIRMATION");
                    termAsscCrsesConfirmBuilder.setMessage("Delete all ASSOCIATED COURSES to term ID #"
                            + termIdToUpdate + "?");
                    termAsscCrsesConfirmBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            for(Course course : coursesAsscToTrmList){

                                List<Assessment> allAsscAsmnts = assessmentRepository.getCourseAsscAsmnts(course.getCourseId());

                                if(!allAsscAsmnts.isEmpty()){

                                    for (Assessment assessment : allAsscAsmnts){

                                        assessmentRepository.delete(assessment);
                                    }
                                }

                                courseRepository.delete(course);


                            }

                            Term term = termRepository.getTerm(termIdToUpdate);
                            termRepository.delete(term);
                            Toast.makeText(getApplicationContext(), "TERM ID #" + termIdToUpdate + " DELETED",
                                    Toast.LENGTH_LONG).show();

                            finish();

                            dialog.dismiss();
                        }
                    });



                    termAsscCrsesConfirmBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = termAsscCrsesConfirmBuilder.create();
                    alertDialog.show();

                }
                else {

                    termRepository.delete(termRepository.getTerm(termIdToUpdate));
                    Toast.makeText(getApplicationContext(), "TERM ID #" + termIdToUpdate + " Deleted",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(TermDetails.this, TermsList.class);
                    startActivity(intent);
                    dialog.dismiss();
                }

            }
        });
        termConfirmBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        AlertDialog alertDialog = termConfirmBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Button menuChoice;

        if(id == R.id.homeMenuPop){

            Intent intent = new Intent(TermDetails.this, Home.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.termLstMenuPop ) {

            Intent intent = new Intent(TermDetails.this, TermsList.class);
            startActivity(intent);

            return true;
        }
        if (id ==R.id.courseLstMenuPop ) {

            Intent intent = new Intent(TermDetails.this, CoursesList.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.asmntLstMenuPop ) {

            Intent intent = new Intent(TermDetails.this, AssessmentsList.class);
            startActivity(intent);
            return true;
        }
        if ( id == R.id.exitMenuPop){

            finishAffinity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}