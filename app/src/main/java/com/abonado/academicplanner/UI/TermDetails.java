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
import com.abonado.academicplanner.utilities.MyReceiver;


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
    boolean isUpdate = false;
    int termIdToUpdate = 0;



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

        RecyclerView recyclerView = findViewById(R.id.coursesLstTermDtlsRcyle);
        courseRepository = new CourseRepository(getApplication());
        List<Course> associatedCourses = courseRepository.getAllAsscCourses(termIdToUpdate);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(associatedCourses);

        if(termIdToUpdate == 0){

            nonEditId.setText("New ID");

        }
        else {

            nonEditId.setText(String.valueOf(termIdToUpdate));
            isUpdate = true;
        }

        if(isUpdate){
            String xTermName = intent.getStringExtra("term_name");
            String xTermStart = intent.getStringExtra("term_start");
            String xTermEnd =  intent.getStringExtra("term_end");

            editName.setText(xTermName);
            editStart.setText(xTermStart);
            editEnd.setText(xTermEnd);

        }


        Button termsSaveButton = findViewById(R.id.saveTermDetails);
        termsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                termRepository = new TermRepository(getApplication());
                boolean isTermTimeValid = true;

                if(isUpdate){

                    nonEditId = findViewById(R.id.trmIdTxt);
                    editName = findViewById(R.id.trmNmEdTxt);
                    editStart = findViewById(R.id.trmStrtTxt);
                    editEnd = findViewById(R.id.trmEndTxt);
                    mTermId = termIdToUpdate;
                    mTermName = editName.getText().toString();

                    mTermStart = editStart.getText().toString();
                    mTermEnd = editEnd.getText().toString();

                    DateTimeFormatter dateFormatPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    try{
                        LocalDate checkStart = LocalDate.parse(mTermStart);
                        LocalDate checkEnd = LocalDate.parse(mTermEnd);

                        if(!checkStart.isBefore(checkEnd) || !checkEnd.isAfter(checkStart)){
                            isTermTimeValid = false;
                        }
                    }
                    catch (Exception e){
                        isTermTimeValid = false;
                    }


                    if(isTermTimeValid){

                        Term term = new Term(mTermId, mTermName, mTermStart, mTermEnd);

                        termRepository.update(term);

                        isUpdate = false;

                        Toast.makeText(getApplicationContext(), "TERM saved",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(TermDetails.this, TermsList.class);
                        startActivity(intent);
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

                        if(!checkStart.isBefore(checkEnd) || !checkEnd.isAfter(checkStart)){
                            isTermTimeValid = false;
                        }
                    }
                    catch (Exception e){
                        isTermTimeValid = false;
                    }


                    if(isTermTimeValid){

                        Term term = new Term(mTermId, mTermName, mTermStart, mTermEnd);

                        termRepository.insert(term);

                        Toast.makeText(getApplicationContext(), "TERM saved",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(TermDetails.this, TermsList.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Invalid DATE entry." +
                                "\nStart date must be before end date." +
                                "\nFormat: YYYY-MM-DD", Toast.LENGTH_LONG).show();

                }
            }
        });

        Button delete = findViewById(R.id.deleteTrmTrmDtls);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTermDeleteConfirmation();

            }
        });


        Button termNotifyButton = findViewById(R.id.termsNotifyBut);
        termNotifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValidNotifyData = true;

                if(isUpdate){

                    editName = findViewById(R.id.trmNmEdTxt);
                    editStart = findViewById(R.id.trmStrtTxt);
                    editEnd = findViewById(R.id.trmEndTxt);
                    nonEditId = findViewById(R.id.trmIdTxt);
                    String termId = nonEditId.getText().toString();
                    String termName = editName.getText().toString();
                    String termStart = editStart.getText().toString();
                    String termEnd = editEnd.getText().toString();

                    termRepository = new TermRepository(getApplication());
                    List<Term> termList = termRepository.getAllTerms();
                    int counter = termList.size();

                    for(Term term : termList){
                        if(term.getTermId() == Integer.parseInt(termId) &&
                        term.getTermName().equals(termName) &&
                        term.getTermStart().equals(termStart) &&
                        term.getTermEnd().equals(termEnd)){

                            --counter;
                        }
                    }

                    if(counter != termList.size()-1){

                        isValidNotifyData = false;
                    }


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    Date startDate = null;
                    Date endDate = null;
                    try {
                        LocalDate start = LocalDate.parse(termStart);
                        LocalDate end = LocalDate.parse(termEnd);

                        if(!start.isBefore(end) || !end.isAfter(start)){

                            isValidNotifyData = false;
                        }

                        startDate = sdf.parse(termStart);
                        endDate = sdf.parse(termEnd);
                    }
                    catch (Exception e){

                        isValidNotifyData = false;
                        e.fillInStackTrace();
                    }


                    if(isValidNotifyData){

                        Long startTrigger = startDate.getTime();
                        Intent startIntent = new Intent(TermDetails.this,
                                MyReceiver.class);
                        startIntent.setAction("termStartDateNotify");
                        startIntent.putExtra("startTermKey", "Term ID: " + termId
                                + "\nTerm Name: " + termName +"\nSTARTS: " + termStart);
                        PendingIntent termStartSender = PendingIntent.getBroadcast(TermDetails.this,
                                ++Home.termStartAlertNum, startIntent, PendingIntent.FLAG_IMMUTABLE);
                        AlarmManager asmntStartAlarmManager =
                                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        asmntStartAlarmManager.set(AlarmManager.RTC_WAKEUP, startTrigger, termStartSender);


                        Long endTrigger = endDate.getTime();
                        Intent endIntent = new Intent(TermDetails.this, MyReceiver.class);
                        endIntent.setAction("termEndDateNotify");
                        endIntent.putExtra("termEndKey", "Term ID: " + termId
                                + "\tTerm Name: " + termName + "\n ENDS: " + termEnd);
                        PendingIntent termEndSender = PendingIntent.getBroadcast(TermDetails.this,
                                ++Home.termEndAlertNum, endIntent, PendingIntent.FLAG_IMMUTABLE);
                        AlarmManager asmntEndAlarmManager =
                                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        asmntEndAlarmManager.set(AlarmManager.RTC_WAKEUP, endTrigger, termEndSender);


                        Toast.makeText(getApplicationContext(), "Notify COURSE" +
                                        "\nID: " + termId + "\tName: " + termName + "\nStart Date: " + termStart
                                        + "\nEnd Date: " + termEnd,
                                Toast.LENGTH_LONG).show();

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
        });



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
                            Toast.makeText(getApplicationContext(), "TERM ID #" + termIdToUpdate + " Deleted",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(TermDetails.this, TermsList.class);
                            startActivity(intent);

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