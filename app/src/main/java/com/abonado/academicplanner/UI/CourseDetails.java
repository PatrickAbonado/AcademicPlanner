package com.abonado.academicplanner.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
import com.abonado.academicplanner.utilities.HelperToCourse;

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
    boolean isFoundTermId = false;
    String xTermId;
    List<Assessment> associatedAsmnts = new ArrayList<>();



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
        List<Course> allCourses = courseRepository.getAllCourses();

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

                    assessmentRepository = new AssessmentRepository(getApplication());
                    List<Assessment> allAssessments = assessmentRepository.getAllAssessments();
                    for(Assessment assessment : allAssessments){

                        if(assessment.getAsmntCourseId() == course.getCourseId()){

                            associatedAsmnts.add(assessment);
                        }
                    }

                }

            }
        }


        RecyclerView recyclerView = findViewById(R.id.courseDtlsLstRcyle);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setAssessments(associatedAsmnts);


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

                Course courseToDelete = HelperToCourse.courseToUpdate;

                courseRepository = new CourseRepository(getApplication());

                mAllCourses = courseRepository.getAllCourses();

                if(mAllCourses != null){
                    for(Course course : mAllCourses){
                        if(mCourseTitle.getText().toString().equals(course.getCourseTitle())){
                            courseRepository.delete(course);
                            Toast.makeText(getApplicationContext(), "ID: " + courseToDelete.getCourseId()
                                            + "-- Name: " + courseToDelete.getCourseTitle() + " was deleted",
                                    Toast.LENGTH_LONG).show();



                            Intent intent = new Intent(CourseDetails.this, CourseDetails.class);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Deletion Selection Not Valid",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });

    }

    public Course createCourse(){

        setElementIds();

        TermRepository termRepository = new TermRepository(getApplication());
        List<Term> allTerms = termRepository.getAllTerms();

        String courseTitle = mCourseTitle.getText().toString();
        String courseStart = mCourseStart.getText().toString();
        String courseEnd = mCourseEnd.getText().toString();
        String courseNotes = mCourseNotes.getText().toString();
        String courseInstrName = mCourseInstrName.getText().toString();
        String courseInstrPhone = mCourseInstrPhone.getText().toString();
        String courseInstrEmail = mCourseInstrEmail.getText().toString();


        int termId = 0;

        try {
            termId = Integer.parseInt(xTermId);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "VALID TERM ID NOT SELECTED",
                    Toast.LENGTH_LONG).show();
            e.fillInStackTrace();

            Intent intent = new Intent(CourseDetails.this, CoursesList.class);
            startActivity(intent);
        }


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