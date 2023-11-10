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
import com.abonado.academicplanner.entities.Assessment;
import com.abonado.academicplanner.entities.Course;
import com.abonado.academicplanner.utilities.CourseAdapter;

import java.util.ArrayList;
import java.util.List;

public class AssessmentDetails extends AppCompatActivity {

    Button asmntSave;
    Button asmntDelete;
    TextView mAsmntId;
    EditText mAsmntTitle;
    EditText mAsmntStart;
    EditText mAsmntEnd;
    CourseRepository courseRepository;
    AssessmentRepository assessmentRepository;
    Spinner mAsmntCrsIdSpin;
    Spinner mAsmntTypeSpin;
    int asmntToUpdateId = 0;
    List<Assessment> mAllAsmnts;
    boolean isAsmntUpdate = false;
    String xAssessmentId;
    String xAsmntCourseId;
    String mAsmntTypeSelction;
    ArrayList<String> allCourseIds = new ArrayList<>();
    Course associatedCourse;
    boolean isAsmntToDelete = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        Toolbar myToolbar = findViewById(R.id.ass_details_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        courseRepository = new CourseRepository(getApplication());
        List<Course> mAllCourses = new ArrayList<>(courseRepository.getAllCourses());
        allCourseIds.add("Select Course ID");
        for (Course course : mAllCourses){
            allCourseIds.add(String.valueOf(course.getCourseId()));
        }
        mAsmntCrsIdSpin = findViewById(R.id.asmntCrsIdSpinner);
        ArrayAdapter<String> asmntsStatusSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, allCourseIds);
        asmntsStatusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAsmntCrsIdSpin.setAdapter(asmntsStatusSpinnerAdapter);
        mAsmntCrsIdSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                xAsmntCourseId =
                        mAsmntCrsIdSpin.getSelectedItem().equals(allCourseIds.get(0)) ?  "0" : allCourseIds.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                xAsmntCourseId = "0";

            }
        });

        mAsmntTypeSpin = findViewById(R.id.asmntTestTypeSpinner);
        ArrayAdapter<CharSequence> asmntTypeSpinAdptr = ArrayAdapter.createFromResource(this,
                R.array.asmntTypeSpinArr, android.R.layout.simple_spinner_item);
        asmntTypeSpinAdptr.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mAsmntTypeSpin.setAdapter(asmntTypeSpinAdptr);
        mAsmntTypeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mAsmntTypeSelction = getResources().getStringArray(R.array.asmntTypeSpinArr)[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                mAsmntTypeSelction = getResources().getStringArray(R.array.asmntTypeSpinArr)[0];

            }
        });

        assessmentRepository = new AssessmentRepository(getApplication());
        mAllAsmnts = assessmentRepository.getAllAssessments();

        setElementIds();

        Intent asmntListIntent = getIntent();
        asmntToUpdateId = asmntListIntent.getIntExtra("assessment_id", 0);
        if(asmntToUpdateId == 0){

            mAsmntId.setText("New ID");

            xAssessmentId = "0";


        }
        else {

            isAsmntUpdate = true;

            xAssessmentId = String.valueOf(asmntToUpdateId);

            mAsmntId.setText(xAssessmentId);

            for(Assessment assessment : mAllAsmnts){

                if(assessment.getAssessmentId() == asmntToUpdateId){

                    mAsmntTitle.setText(assessment.getAssessmentTitle());
                    mAsmntStart.setText(assessment.getAssessmentStart());
                    mAsmntEnd.setText(assessment.getAssessmentEnd());

                    int positionCounter = 0;


                    int asmntCrsIdSlct = assessment.getAsmntCourseId();
                    for(String id : allCourseIds){
                        if(id.equals(String.valueOf(asmntCrsIdSlct))){
                            break;
                        }
                        ++positionCounter;
                    }
                    mAsmntCrsIdSpin.setSelection(positionCounter);

                    positionCounter = 0;
                    String asmntTstTypSlct = assessment.getAssessmentType();
                    String[] types = getResources().getStringArray(R.array.asmntTypeSpinArr);
                    for (String type : types){
                        if(type.equals(asmntTstTypSlct)){
                            break;
                        }
                        positionCounter++;
                    }
                    mAsmntTypeSpin.setSelection(positionCounter);

                    courseRepository = new CourseRepository(getApplication());

                    associatedCourse = courseRepository.getCourse(assessment.getAsmntCourseId());

                }

            }
        }


        if(isAsmntUpdate){

            List<Course> courseList = new ArrayList<>();
            courseList.add(associatedCourse);
            RecyclerView recyclerView = findViewById(R.id.asmntDtlsLstRcyle);
            final CourseAdapter courseAdapter = new CourseAdapter(this);
            recyclerView.setAdapter(courseAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            courseAdapter.setCourses(courseList);
        }


        asmntSave = findViewById(R.id.saveAsmntBut);
        asmntSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assessmentRepository = new AssessmentRepository(getApplication());

                Assessment assessment = createAssessment();

                if(isAsmntUpdate){

                    assessmentRepository.update(assessment);

                    isAsmntUpdate = false;

                    Toast.makeText(getApplicationContext(), "UPDATED",
                            Toast.LENGTH_LONG).show();
                }
                else {

                    assessmentRepository.insert(assessment);

                    Toast.makeText(getApplicationContext(), "SAVED",
                            Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(AssessmentDetails.this, AssessmentsList.class);
                startActivity(intent);
            }
        });

        asmntDelete = findViewById(R.id.deleteAsmntBut);
        asmntDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               getDeleteConfirmation();

            }
        });


    }

    public void getDeleteConfirmation(){

        mAsmntId = findViewById(R.id.asmntIdTxt);
        int asmntToDeleteId = Integer.parseInt(String.valueOf(mAsmntId.getText()));

        assessmentRepository = new AssessmentRepository(getApplication());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("CONFIRMATION");
        builder.setMessage("Delete Assessment ID #" + asmntToDeleteId + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                assessmentRepository = new AssessmentRepository(getApplication());

                //List<Assessment> allAssessments = assessmentRepository.getAllAssessments();

                mAsmntId = findViewById(R.id.asmntIdTxt);
                int asmntToDeleteId = Integer.parseInt(String.valueOf(mAsmntId.getText()));

                assessmentRepository.delete(assessmentRepository.getAsmntByAsmntId(asmntToDeleteId));

                Intent intent = new Intent(AssessmentDetails.this, AssessmentsList.class);
                startActivity(intent);

                dialog.dismiss();

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

    public Assessment createAssessment(){

        setElementIds();

        int asmntId = Integer.parseInt(xAssessmentId);
        int asmntCrsId = Integer.parseInt(xAsmntCourseId);
        String asmntTitle = mAsmntTitle.getText().toString();
        String asmntType = mAsmntTypeSelction;
        String asmntStart = mAsmntStart.getText().toString();
        String asmntEnd = mAsmntEnd.getText().toString();

        return new Assessment(asmntId, asmntCrsId, asmntTitle, asmntType, asmntStart, asmntEnd);

    }

    public void setElementIds(){
        mAsmntId = findViewById(R.id.asmntIdTxt);
        mAsmntTitle = findViewById(R.id.asmntTitleEdTxt);
        mAsmntStart = findViewById(R.id.asmntStrtTxt);
        mAsmntEnd = findViewById(R.id.asmntEndTxt);

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