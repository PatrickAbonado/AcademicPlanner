package com.abonado.academicplanner.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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

import java.util.ArrayList;
import java.util.List;

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
    String mStatusSelection;
    Spinner mCourseSpinner;
    Spinner mCrsTrmIdSpin;
    List<Course> mAllCourses;
    boolean isCourseUpdate = false;
    int courseToUpdateId = 0;
    String xTermId;
    Button courseDeleteButton;




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

                mStatusSelection = getResources().getStringArray(
                        R.array.courseStatusSpinnerArray)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                mStatusSelection = getResources().getStringArray(R.array.courseStatusSpinnerArray)[0];
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

                xTermId =
                        mCrsTrmIdSpin.getSelectedItem().equals(allTermIds.get(0)) ?  "0" : allTermIds.get(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                xTermId = "0";
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
        List<Assessment> asscAsmnts = assessmentRepository.getAsscAsmnts(courseToUpdateId);
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

        Button courseDtlsSaveBut = findViewById(R.id.saveCourseDetails);
        courseDtlsSaveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                courseRepository = new CourseRepository(getApplication());

                Course course = createCourse();

                if(isCourseUpdate)
                {
                    courseRepository.update(course);

                    isCourseUpdate = false;
                    Toast.makeText(getApplicationContext(), "SAVED",
                            Toast.LENGTH_LONG).show();
                }
                else
                {

                    courseRepository.insert(course);

                    Toast.makeText(getApplicationContext(), "SAVED",
                            Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(CourseDetails.this, CoursesList.class);
                startActivity(intent);

            }
        });

        Button delete = findViewById(R.id.deleteCourseDtls);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCourseDeleteConfirmation();

            }
        });

    }

    public void getCourseDeleteConfirmation(){

        mCourseId = findViewById(R.id.courseIdTxt);
        int courseToDeleteId = Integer.parseInt(String.valueOf(mCourseId.getText()));

        courseRepository = new CourseRepository(getApplication());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CONFIRMATION");
        builder.setMessage("Delete Course ID #" + courseToDeleteId + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean isAsmntsFound = isAsmntCrsIdFound(courseToDeleteId);

                if(isAsmntsFound){

                    AlertDialog.Builder asmntsBuilder = new AlertDialog.Builder(CourseDetails.this);
                    asmntsBuilder.setTitle("CONFIRMATION");
                    asmntsBuilder.setMessage("Delete all associated assessments to course ID #" + courseToDeleteId + "?");
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

        int termId = Integer.parseInt(xTermId);

        return new Course(courseToUpdateId, termId, courseTitle, courseStart, courseEnd,
                mStatusSelection, courseNotes, courseInstrName, courseInstrPhone, courseInstrEmail);

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