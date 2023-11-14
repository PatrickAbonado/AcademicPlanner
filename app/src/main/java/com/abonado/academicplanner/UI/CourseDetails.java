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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abonado.academicplanner.R;
import com.abonado.academicplanner.database.AssessmentRepository;
import com.abonado.academicplanner.database.CourseRepository;
import com.abonado.academicplanner.database.TermRepository;
import com.abonado.academicplanner.entities.Assessment;
import com.abonado.academicplanner.entities.Course;
import com.abonado.academicplanner.entities.Term;
import com.abonado.academicplanner.utilities.AssessmentAdapter;
import com.abonado.academicplanner.utilities.MyReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CourseDetails extends AppCompatActivity {


    CourseRepository courseRepository;
    TermRepository termRepository;
    AssessmentRepository assessmentRepository;
    TextView mCourseId;
    EditText mCourseTitle;
    EditText mCourseStart;
    EditText mCourseEnd;
    EditText mCourseNotes;
    EditText mCourseInstrName;
    EditText mCourseInstrPhone;
    EditText mCourseInstrEmail;
    String courseStatusSelection;
    Spinner mCourseSpinner;
    Spinner mCrsTrmIdSpin;
    List<Course> mAllCourses;
    boolean isCourseUpdate = false;
    int courseToUpdateId = 0;
    String selectedCrseTrmId;
    FloatingActionButton coursePlusButtonMenuFab;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        mCourseSpinner = findViewById(R.id.courseStatusSpinner);
        mCrsTrmIdSpin = findViewById(R.id.courseTermIdSpinner);

        ArrayAdapter<CharSequence> courseStatusSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.courseStatusSpinnerArray, android.R.layout.simple_spinner_item);
        courseStatusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mCourseSpinner.setAdapter(courseStatusSpinnerAdapter);
        mCourseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                courseStatusSelection = getResources().getStringArray(
                        R.array.courseStatusSpinnerArray)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                courseStatusSelection = getResources().getStringArray(R.array.courseStatusSpinnerArray)[0];
            }
        });


        termRepository = new TermRepository(getApplication());
        ArrayList<Term> allTerms = new ArrayList<>(termRepository.getAllTerms());
        ArrayList<String> allTermIds = new ArrayList<>();
        allTermIds.add("Select Term ID");
        for(Term term : allTerms){
            allTermIds.add(String.valueOf(term.getTermId()));
        }

        ArrayAdapter<String> courseTermIdSpnAdptr = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, allTermIds);
        courseTermIdSpnAdptr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCrsTrmIdSpin.setAdapter(courseTermIdSpnAdptr);
        mCrsTrmIdSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedCrseTrmId =
                        mCrsTrmIdSpin.getSelectedItem().equals(allTermIds.get(0)) ?  "0" : allTermIds.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                selectedCrseTrmId = "0";
            }
        });

        courseRepository = new CourseRepository(getApplication());
        mAllCourses = courseRepository.getAllCourses();

        setElementIds();

        Intent courseListIntent = getIntent();

        courseToUpdateId = courseListIntent.getIntExtra("courseId",0);

        if(courseToUpdateId == 0){

            mCourseId.setText("New ID");
        }
        else {

            isCourseUpdate = true;

            mCourseId.setText(String.valueOf(courseToUpdateId));

            for(Course course : mAllCourses){

                if(course.getCourseId() == courseToUpdateId){

                    mCourseTitle.setText(course.getCourseTitle());
                    mCourseStart.setText(course.getCourseStart());
                    mCourseEnd.setText(course.getCourseEnd());
                    mCourseNotes.setText(course.getCourseNotes());
                    mCourseInstrName.setText(course.getCourseInstrName());
                    mCourseInstrPhone.setText(course.getCourseInstrPhone());
                    mCourseInstrEmail.setText(course.getCourseInstrEmail());

                    String statusSelection  = course.getCourseStatus();
                    String[] spinnerOptions = getResources().getStringArray(
                            R.array.courseStatusSpinnerArray);
                    int positionCounter = 0;
                    for(String option : spinnerOptions){
                        if(option.equals(statusSelection)){
                            break;
                        }
                        ++positionCounter;
                    }
                    mCourseSpinner.setSelection(positionCounter);

                    int termIdSelection  = course.getCourseTermId();
                    positionCounter = 0;
                    for(String option : allTermIds){
                        if(option.equals(String.valueOf(termIdSelection))){
                            break;
                        }
                        ++positionCounter;
                    }
                    mCrsTrmIdSpin.setSelection(positionCounter);

                }

            }
        }

        assessmentRepository = new AssessmentRepository(getApplication());
        List<Assessment> asscAsmnts = assessmentRepository.getCourseAsscAsmnts(courseToUpdateId);
        RecyclerView recyclerView = findViewById(R.id.courseDtlsLstRcyle);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setAssessments(asscAsmnts);

        Toolbar myToolbar = findViewById(R.id.course_details_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        coursePlusButtonMenuFab = findViewById(R.id.coursePlusButFab);
        coursePlusButtonMenuFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(CourseDetails.this,coursePlusButtonMenuFab);
                popupMenu.getMenuInflater().inflate(R.menu.course_plus_button_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if(item.getItemId() == R.id.courseAllCoursesOpt){

                            Intent intent = new Intent(CourseDetails.this, CoursesList.class);
                            startActivity(intent);

                            return true;

                        }

                        if(item.getItemId() == R.id.courseNotifyOpt){

                            boolean isValidNotifyData = true;

                            setElementIds();

                            if(isCourseUpdate) {

                                String courseId = mCourseId.getText().toString();
                                String courseTermId = selectedCrseTrmId;
                                String courseTitle = mCourseTitle.getText().toString();
                                String courseStart = mCourseStart.getText().toString();
                                String courseEnd = mCourseEnd.getText().toString();
                                String courseStatus = courseStatusSelection;
                                String courseNotes = mCourseNotes.getText().toString();
                                String courseInstrNm = mCourseInstrName.getText().toString();
                                String courseInstrPhn = mCourseInstrPhone.getText().toString();
                                String courseInstrEml = mCourseInstrEmail.getText().toString();

                                courseRepository = new CourseRepository(getApplication());
                                List<Course> courseList = courseRepository.getAllCourses();
                                int counter = courseList.size();

                                for(Course course : courseList){
                                    if(course.getCourseId() == Integer.parseInt(courseId) &&
                                            course.getCourseTermId() == Integer.parseInt(courseTermId) &&
                                            course.getCourseTitle().equals(courseTitle) &&
                                            course.getCourseStart().equals(courseStart) &&
                                            course.getCourseEnd().equals(courseEnd) &&
                                            course.getCourseStatus().equals(courseStatus) &&
                                            course.getCourseNotes().equals(courseNotes) &&
                                            course.getCourseInstrName().equals(courseInstrNm) &&
                                            course.getCourseInstrPhone().equals(courseInstrPhn) &&
                                            course.getCourseInstrEmail().equals(courseInstrEml)){

                                        --counter;
                                    }
                                }

                                if(counter != courseList.size()-1){

                                    isValidNotifyData = false;
                                }


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                Date startDate = null;
                                Date endDate = null;
                                try{

                                    LocalDate start = LocalDate.parse(courseStart);
                                    LocalDate end = LocalDate.parse(courseEnd);

                                    if(!start.isBefore(end) || !end.isAfter(start)){

                                        isValidNotifyData = false;
                                    }

                                    startDate = sdf.parse(courseStart);
                                    endDate = sdf.parse(courseEnd);

                                }
                                catch (Exception e){

                                    isValidNotifyData = false;
                                    e.fillInStackTrace();
                                }




                                if (isValidNotifyData) {

                                    Long startTrigger = startDate.getTime();
                                    Intent startIntent = new Intent(CourseDetails.this,
                                            MyReceiver.class);
                                    startIntent.setAction("courseStartDateNotify");
                                    startIntent.putExtra("startCourseKey", "Course ID: " + courseId
                                            + "\nCourse Title: " + courseTitle +"\nSTARTS: " + courseStart);
                                    PendingIntent courseStartSender = PendingIntent.getBroadcast(CourseDetails.this,
                                            ++Home.courseStartAlertNum, startIntent, PendingIntent.FLAG_IMMUTABLE);
                                    AlarmManager asmntStartAlarmManager =
                                            (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                    asmntStartAlarmManager.set(AlarmManager.RTC_WAKEUP, startTrigger, courseStartSender);

                                    Long endTrigger = endDate.getTime();
                                    Intent endIntent = new Intent(CourseDetails.this, MyReceiver.class);
                                    endIntent.setAction("courseEndDateNotify");
                                    endIntent.putExtra("courseEndKey", "Course ID: " + courseId
                                            + "\tCourse Title: " + courseTitle + "\n ENDS: " + courseEnd);
                                    PendingIntent courseEndSender = PendingIntent.getBroadcast(CourseDetails.this,
                                            ++Home.courseEndAlertNum, endIntent, PendingIntent.FLAG_IMMUTABLE);
                                    AlarmManager asmntEndAlarmManager =
                                            (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                    asmntEndAlarmManager.set(AlarmManager.RTC_WAKEUP, endTrigger, courseEndSender);


                                    Toast.makeText(getApplicationContext(), "Notify COURSE" +
                                                    "\nID: " + courseId + "\tTitle: " + courseTitle + "\nStart Date: " + courseStart
                                                    + "\nEnd Date: " + courseEnd,
                                            Toast.LENGTH_LONG).show();

                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Invalid Selection." +
                                            "\nData changes must be saved before setting a notification." +
                                            "\nStart date must be before end date." +
                                            "\nDate Format: YYYY-MM-DD", Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Course must be saved to database" +
                                        " before notifications can be set.", Toast.LENGTH_LONG).show();


                            return true;
                        }

                        if(item.getItemId() == R.id.courseShareNoteOpt){

                            mCourseNotes = findViewById(R.id.courseNotesTxt);
                            String notes = mCourseNotes.getText().toString();

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, notes);
                            sendIntent.putExtra(Intent.EXTRA_TITLE, "Course Notes");
                            sendIntent.setType("text/plain");
                            Intent shareIntent = Intent.createChooser(sendIntent, null);
                            startActivity(shareIntent);

                            return true;
                        }

                        if(item.getItemId() == R.id.courseDeleteOpt){

                            if(isCourseUpdate){

                                getCourseDeleteConfirmation();

                                Intent intent = new Intent(CourseDetails.this, CoursesList.class);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Select a course " +
                                        "from the all courses list for deletion.", Toast.LENGTH_LONG).show();

                            return  true;
                        }

                        if(item.getItemId() == R.id.courseSaveOpt){

                            courseRepository = new CourseRepository(getApplication());
                            termRepository = new TermRepository(getApplication());

                            Course course = createCourse();

                            Term checkTerm = termRepository.getTerm(Integer.parseInt(selectedCrseTrmId));

                            if(checkTerm != null){

                                boolean isValidCourseTimeEntries = true;

                                if (isCourseUpdate) {

                                    try{
                                        LocalDate checkStart = LocalDate.parse(String.valueOf(course.getCourseStart()));
                                        LocalDate checkEnd = LocalDate.parse(String.valueOf(course.getCourseEnd()));

                                        if(!checkStart.isBefore(checkEnd) || !checkEnd.isAfter(checkStart)){

                                            isValidCourseTimeEntries = false;

                                        }
                                    }
                                    catch (Exception e){

                                        isValidCourseTimeEntries = false;
                                    }

                                    if(isValidCourseTimeEntries) {
                                        courseRepository.update(course);

                                        isCourseUpdate = false;
                                        Toast.makeText(getApplicationContext(), "COURSE UPDATED",
                                                Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(CourseDetails.this, CoursesList.class);
                                        startActivity(intent);

                                        return true;
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(), "Invalid DATE entry." +
                                                "\nStart date must be before end date." +
                                                "\nFormat: YYYY-MM-DD", Toast.LENGTH_LONG).show();

                                }
                                else
                                {

                                    try{
                                        LocalDate checkStart = LocalDate.parse(String.valueOf(course.getCourseStart()));
                                        LocalDate checkEnd = LocalDate.parse(String.valueOf(course.getCourseEnd()));

                                        if(!checkStart.isBefore(checkEnd) || !checkEnd.isAfter(checkStart)){

                                            isValidCourseTimeEntries = false;
                                        }
                                    }
                                    catch (Exception e){

                                        isValidCourseTimeEntries = false;
                                    }

                                    if (isValidCourseTimeEntries) {
                                        courseRepository.insert(course);

                                        Toast.makeText(getApplicationContext(), "COURSE SAVED",
                                                Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(CourseDetails.this, CoursesList.class);
                                        startActivity(intent);

                                        return true;
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(), "Invalid DATE entry." +
                                                "\nStart date must be before end date." +
                                                "\nFormat: YYYY-MM-DD", Toast.LENGTH_LONG).show();
                                }

                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Select a TERM ID", Toast.LENGTH_LONG).show();
                            }




                        }



                        return false;
                    }
                });

                popupMenu.show();

            }
        });

    }

    public void getCourseDeleteConfirmation(){

        mCourseId = findViewById(R.id.courseIdTxt);
        int courseToDeleteId = Integer.parseInt(String.valueOf(mCourseId.getText()));

        courseRepository = new CourseRepository(getApplication());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CONFIRMATION");
        builder.setMessage("Delete COURSE ID #" + courseToDeleteId + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean isAsmntsFound = isAsmntCrsIdFound(courseToDeleteId);

                if(isAsmntsFound){

                    AlertDialog.Builder asmntsBuilder = new AlertDialog.Builder(CourseDetails.this);
                    asmntsBuilder.setTitle("CONFIRMATION");
                    asmntsBuilder.setMessage("Delete all ASSOCIATED ASSESSMENTS to course ID #" + courseToDeleteId + "?");
                    asmntsBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            assessmentRepository = new AssessmentRepository(getApplication());
                            List<Assessment> assessmentList = assessmentRepository.getAllAssessments();

                            for (Assessment assessment : assessmentList){
                                if(assessment.getAsmntCourseId() == courseToDeleteId){
                                    assessmentRepository.delete(assessment);
                                }
                            }

                            Course courseToDelete = courseRepository.getCourse(courseToDeleteId);
                            courseRepository.delete(courseToDelete);
                            Toast.makeText(getApplicationContext(), "Course Deleted",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(CourseDetails.this, CoursesList.class);
                            startActivity(intent);

                            dialog.dismiss();
                        }
                    });
                    asmntsBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog asmntsAlertDialog = asmntsBuilder.create();
                    asmntsAlertDialog.show();

                }
                else {

                    courseRepository.delete(courseRepository.getCourse(courseToDeleteId));
                    Toast.makeText(getApplicationContext(), "Course Deleted",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    public boolean isAsmntCrsIdFound(int courseId){

        AssessmentRepository assessmentRepository;

        assessmentRepository = new AssessmentRepository(getApplication());
        List<Assessment> allAssessments = assessmentRepository.getAllAssessments();

        boolean isFound = false;
        for (Assessment assessment : allAssessments){

            if(assessment.getAsmntCourseId() == courseId){

                return true;

            }
        }

        return isFound;
    }

    public Course createCourse(){

        setElementIds();

        String courseTitle = mCourseTitle.getText().toString();
        String courseStart = mCourseStart.getText().toString();
        String courseEnd = mCourseEnd.getText().toString();
        String courseNotes = mCourseNotes.getText().toString();
        String courseInstrName = mCourseInstrName.getText().toString();
        String courseInstrPhone = mCourseInstrPhone.getText().toString();
        String courseInstrEmail = mCourseInstrEmail.getText().toString();

        int termId = Integer.parseInt(selectedCrseTrmId);

        return new Course(courseToUpdateId, termId, courseTitle, courseStart, courseEnd,
                courseStatusSelection, courseNotes, courseInstrName, courseInstrPhone, courseInstrEmail);

    }

    public void setElementIds(){
        mCourseId = findViewById(R.id.courseIdTxt);
        mCourseTitle = findViewById(R.id.courseTitleEdTxt);
        mCourseStart = findViewById(R.id.courseStrtTxt);
        mCourseEnd = findViewById(R.id.courseEndTxt);
        mCourseInstrName = findViewById(R.id.courseInstrNameTxt);
        mCourseInstrPhone = findViewById(R.id.courseInstrPhoneTxt);
        mCourseInstrEmail = findViewById(R.id.courseInstrEmailTxt);
        mCourseNotes = findViewById(R.id.courseNotesTxt);
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

            Intent intent = new Intent(this, Home.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.termLstMenuPop ) {

            Intent intent = new Intent(this, TermsList.class);
            startActivity(intent);

            return true;
        }
        if (id ==R.id.courseLstMenuPop ) {

            Intent intent = new Intent(this, CoursesList.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.asmntLstMenuPop ) {

            Intent intent = new Intent(this, AssessmentsList.class);
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