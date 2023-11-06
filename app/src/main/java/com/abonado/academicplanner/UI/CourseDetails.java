package com.abonado.academicplanner.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.abonado.academicplanner.R;
import com.abonado.academicplanner.database.CourseRepository;
import com.abonado.academicplanner.entities.Course;
import com.abonado.academicplanner.utilities.CourseAdapter;
import com.abonado.academicplanner.utilities.HelperToCourse;
import com.abonado.academicplanner.utilities.HelperToTerm;


import java.util.List;

public class CourseDetails extends AppCompatActivity {


    CourseRepository courseRepository;
    EditText mCourseTitle;
    EditText mCourseStart;
    EditText mCourseEnd;
    EditText mCourseNotes;
    EditText mCourseInstrName;
    EditText mCourseInstrPhone;
    EditText mCourseInstrEmail;
    EditText mCourseTermId;
    String mStatusSelection;
    Spinner mCourseSpinner;
    List<Course> mAllCourses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        mCourseSpinner = findViewById(R.id.courseStatusSpinner);

        ArrayAdapter<CharSequence> courseSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.courseStatusSpinnerArray, android.R.layout.simple_spinner_item);
        courseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mCourseSpinner.setAdapter(courseSpinnerAdapter);
        mCourseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mStatusSelection = getResources().getStringArray(
                        R.array.courseStatusSpinnerArray)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                mStatusSelection = "Status Not Selected";
            }
        });


        RecyclerView recyclerView = findViewById(R.id.courseDtlsLstRcyle);
        courseRepository = new CourseRepository(getApplication());
        List<Course> allCourses = courseRepository.getAllCourses();
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setCourses(allCourses);


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

                mCourseTitle = findViewById(R.id.courseTitleEdTxt);
                mCourseStart = findViewById(R.id.courseStrtTxt);
                mCourseEnd = findViewById(R.id.courseEndTxt);
                mCourseTermId = findViewById(R.id.courseTermIdTxt);
                mCourseInstrName = findViewById(R.id.courseInstrNameTxt);
                mCourseInstrPhone = findViewById(R.id.courseInstrPhoneTxt);
                mCourseInstrEmail = findViewById(R.id.courseInstrEmailTxt);
                mCourseNotes = findViewById(R.id.courseNotesTxt);

                String courseTitle = mCourseTitle.getText().toString();
                String courseStart = mCourseStart.getText().toString();
                String courseEnd = mCourseEnd.getText().toString();
                String courseNotes = mCourseNotes.getText().toString();
                String courseInstrName = mCourseInstrName.getText().toString();
                String courseInstrPhone = mCourseInstrPhone.getText().toString();
                String courseInstrEmail = mCourseInstrEmail.getText().toString();
                int courseTermId = Integer.parseInt(mCourseTermId.getText().toString());

                Course course = new Course(courseTermId, courseTitle, courseStart, courseEnd,
                        mStatusSelection, courseNotes, courseInstrName, courseInstrPhone, courseInstrEmail);


                courseRepository = new CourseRepository(getApplication());

                courseRepository.insert(course);

                Toast.makeText(getApplicationContext(), "ID: " + course.getCourseId()
                                + "-- Title: " + course.getCourseTitle() + " was saved",
                        Toast.LENGTH_SHORT).show();

                HelperToCourse.courseToUpdate = null;

                Intent intent = new Intent(CourseDetails.this, CourseDetails.class);
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

                for(Course course : mAllCourses){
                    courseRepository.delete(course);
                }

                if(mAllCourses != null){
                    for(Course course : mAllCourses){
                        if(mCourseTitle.getText().toString().equals(course.getCourseTitle())){
                            courseRepository.delete(course);
                            Toast.makeText(getApplicationContext(), "ID: " + courseToDelete.getCourseId()
                                            + "-- Name: " + courseToDelete.getCourseTitle() + " was deleted",
                                    Toast.LENGTH_SHORT).show();

                            HelperToTerm.termToUpdate = null;

                            Intent intent = new Intent(CourseDetails.this, CourseDetails.class);
                            startActivity(intent);

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Deletion Selection Not Valid",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });



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