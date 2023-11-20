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
import com.abonado.academicplanner.entities.Assessment;
import com.abonado.academicplanner.entities.Course;
import com.abonado.academicplanner.utilities.CourseAdapter;
import com.abonado.academicplanner.utilities.Receiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssessmentDetails extends AppCompatActivity {

    FloatingActionButton asmntPlusMenuFab;
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
    String selectedAsmntCourseId;
    String mAsmntTypeSelction;
    ArrayList<String> allCourseIds = new ArrayList<>();
    Course associatedCourse;
    List<Course> courseList;
    RecyclerView recyclerView;
    CourseAdapter courseAdapter;
    int asmntCrsIdSlct;





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
        allCourseIds.add("ID Not Selected");
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

                selectedAsmntCourseId =
                        mAsmntCrsIdSpin.getSelectedItem().equals(allCourseIds.get(0)) ?  "0" : allCourseIds.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                selectedAsmntCourseId = "0";

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


                    asmntCrsIdSlct = assessment.getAsmntCourseId();
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

            courseList = new ArrayList<>();
            courseList.add(associatedCourse);
            recyclerView = findViewById(R.id.asmntDtlsLstRcyle);
            courseAdapter = new CourseAdapter(this);
            recyclerView.setAdapter(courseAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            courseAdapter.setCourses(courseList);
        }


        asmntPlusMenuFab = findViewById(R.id.asmntPlusMenuFab);
        asmntPlusMenuFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(AssessmentDetails.this,asmntPlusMenuFab);
                popupMenu.getMenuInflater().inflate(R.menu.assessment_plus_button_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.asmntStartNotifyOpt){

                            boolean isValidNotifyData = true;

                            if (isAsmntUpdate) {

                                setElementIds();

                                String startDateEntry = mAsmntStart.getText().toString();
                                String endDateEntry = mAsmntEnd.getText().toString();
                                String asmntTitle = mAsmntTitle.getText().toString();
                                String asmntId = mAsmntId.getText().toString();

                                Assessment assessmentToCheck =
                                        assessmentRepository.getAsmntByAsmntId(Integer.parseInt(asmntId));
                                if(!assessmentToCheck.getAssessmentStart().equals(startDateEntry)){
                                    isValidNotifyData = false;
                                }
                                if(!assessmentToCheck.getAssessmentEnd().equals(endDateEntry)){
                                    isValidNotifyData = false;
                                }
                                if(!assessmentToCheck.getAssessmentTitle().equals(asmntTitle)){
                                    isValidNotifyData = false;
                                }
                                if(!(assessmentToCheck.getAsmntCourseId() == Integer.parseInt(selectedAsmntCourseId))){
                                    isValidNotifyData = false;
                                }
                                if(!assessmentToCheck.getAssessmentType().equals(mAsmntTypeSelction)){
                                    isValidNotifyData = false;
                                }


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                Date startDate = null;
                                try {

                                    startDate = sdf.parse(startDateEntry);

                                } catch (Exception e) {

                                    e.fillInStackTrace();
                                    isValidNotifyData = false;
                                }

                                if (isValidNotifyData) {

                                    Long startTrigger = startDate.getTime();
                                    Intent startIntent = new Intent(AssessmentDetails.this,
                                            Receiver.class);
                                    startIntent.setAction("asmntStartDateNotify");
                                    startIntent.putExtra("startAsmntKey", "Assessment ID: " + asmntId
                                            + "\nAssessment Title: " + asmntTitle +"\nSTARTS: " + startDateEntry);
                                    PendingIntent asmntStartSender = PendingIntent.getBroadcast(AssessmentDetails.this,
                                            ++Home.asmntStartAlertNum, startIntent, PendingIntent.FLAG_IMMUTABLE);
                                    AlarmManager asmntStartAlarmManager =
                                            (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                    try {

                                        asmntStartAlarmManager.setExactAndAllowWhileIdle (AlarmManager.RTC_WAKEUP, startTrigger, asmntStartSender);

                                    }catch (SecurityException e){

                                        Toast.makeText(getApplicationContext(),"There is a problem with " +
                                                "setting the ASSESSMENT START alarm.", Toast.LENGTH_LONG).show();
                                        e.fillInStackTrace();
                                    }

                                    Toast.makeText(getApplicationContext(), "NOTIFY ASSESSMENT START" +
                                                    "\nID: " + asmntId + "\tTitle: " + asmntTitle + "\nStart Date: " + startDateEntry,
                                            Toast.LENGTH_LONG).show();

                                    return true;

                                } else
                                    Toast.makeText(getApplicationContext(), "Invalid Selection." +
                                            "\nData changes must be saved before setting a notification." +
                                            "\nStart date must be before end date." +
                                            "\nDate Format: YYYY-MM-DD", Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Assessment must be saved to database" +
                                        " before notifications can be set.", Toast.LENGTH_LONG).show();


                        }

                        if(item.getItemId() == R.id.asmntEndNotifyOpt){

                            boolean isValidNotifyData = true;

                            if (isAsmntUpdate) {

                                setElementIds();
                                String startDateEntry = mAsmntStart.getText().toString();
                                String endDateEntry = mAsmntEnd.getText().toString();
                                String asmntTitle = mAsmntTitle.getText().toString();
                                String asmntId = mAsmntId.getText().toString();


                                Assessment assessmentToCheck =
                                        assessmentRepository.getAsmntByAsmntId(Integer.parseInt(asmntId));
                                if(!assessmentToCheck.getAssessmentStart().equals(startDateEntry)){
                                    isValidNotifyData = false;
                                }
                                if(!assessmentToCheck.getAssessmentEnd().equals(endDateEntry)){
                                    isValidNotifyData = false;
                                }
                                if(!assessmentToCheck.getAssessmentTitle().equals(asmntTitle)){
                                    isValidNotifyData = false;
                                }
                                if(!(assessmentToCheck.getAsmntCourseId() == Integer.parseInt(selectedAsmntCourseId))){
                                    isValidNotifyData = false;
                                }
                                if(!assessmentToCheck.getAssessmentType().equals(mAsmntTypeSelction)){
                                    isValidNotifyData = false;
                                }


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                Date endDate = null;
                                try {

                                    endDate = sdf.parse(endDateEntry);

                                } catch (Exception e) {

                                    e.fillInStackTrace();
                                    isValidNotifyData = false;
                                }

                                if (isValidNotifyData) {


                                    Long endTrigger = endDate.getTime();
                                    Intent endIntent = new Intent(AssessmentDetails.this, Receiver.class);
                                    endIntent.setAction("asmntEndDateNotify");
                                    endIntent.putExtra("endAsmntKey", "Assessment ID: " + asmntId
                                            + "\tAssessment Title: " + asmntTitle + "\n ENDS: " + endDateEntry);
                                    PendingIntent asmntEndSender = PendingIntent.getBroadcast(AssessmentDetails.this,
                                            ++Home.asmntEndAlertNum, endIntent, PendingIntent.FLAG_IMMUTABLE);
                                    AlarmManager asmntEndAlarmManager =
                                            (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                                    try{
                                        asmntEndAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTrigger, asmntEndSender);


                                    }
                                    catch (SecurityException e){

                                        Toast.makeText(getApplicationContext(),"There is a problem with " +
                                                "setting the ASSESSMENT END alarm.", Toast.LENGTH_LONG).show();
                                        e.fillInStackTrace();
                                    }


                                    Toast.makeText(getApplicationContext(), "NOTIFY ASSESSMENT END" +
                                                    "\nID: " + asmntId + "\tTitle: " + asmntTitle
                                                    + "\nEnd Date: " + endDateEntry,
                                            Toast.LENGTH_LONG).show();

                                    return true;


                                } else
                                    Toast.makeText(getApplicationContext(), "Invalid Selection." +
                                            "\nData changes must be saved before setting a notification." +
                                            "\nStart date must be before end date." +
                                            "\nDate Format: YYYY-MM-DD", Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Assessment must be saved to database" +
                                        " before notifications can be set.", Toast.LENGTH_LONG).show();
                        }

                        if(item.getItemId() == R.id.asmntDeleteOpt){

                            if(isAsmntUpdate){

                                getDeleteConfirmation();

                                return true;
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Select an assessment " +
                                        "from the all assessments list for deletion.", Toast.LENGTH_LONG).show();

                        }

                        if(item.getItemId() == R.id.asmntSaveOpt){

                            assessmentRepository = new AssessmentRepository(getApplication());
                            courseRepository = new CourseRepository(getApplication());

                            Assessment createdAssessment = createAssessment();
                            Course checkCourse = courseRepository.getCourse(Integer.parseInt(selectedAsmntCourseId));

                            if (checkCourse != null) {

                                boolean isValidAsmntDate = true;

                                if (isAsmntUpdate) {

                                    try{
                                        LocalDate checkStart = LocalDate.parse(String.valueOf(createdAssessment.getAssessmentStart()));
                                        LocalDate checkEnd = LocalDate.parse(String.valueOf(createdAssessment.getAssessmentEnd()));

                                        if(checkEnd.isBefore(checkStart)){

                                            isValidAsmntDate = false;

                                        }
                                    }
                                    catch (Exception e){

                                        isValidAsmntDate = false;
                                    }

                                    if(isValidAsmntDate){

                                        assessmentRepository.update(createdAssessment);

                                        isAsmntUpdate = false;

                                        Toast.makeText(getApplicationContext(), "ASSESSMENT UPDATED",
                                                Toast.LENGTH_LONG).show();

                                        finish();
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(), "Invalid DATE entry." +
                                                "\nStart date must be before end date." +
                                                "\nFormat: YYYY-MM-DD", Toast.LENGTH_LONG).show();
                                }
                                else
                                {

                                    try{
                                        LocalDate checkStart = LocalDate.parse(String.valueOf(createdAssessment.getAssessmentStart()));
                                        LocalDate checkEnd = LocalDate.parse(String.valueOf(createdAssessment.getAssessmentEnd()));

                                        if(checkEnd.isBefore(checkStart)){

                                            isValidAsmntDate = false;

                                        }
                                    }
                                    catch (Exception e){

                                        isValidAsmntDate = false;
                                    }

                                    if(isValidAsmntDate){
                                        assessmentRepository.insert(createdAssessment);

                                        Toast.makeText(getApplicationContext(), " ASSESSMENT SAVED",
                                                Toast.LENGTH_LONG).show();

                                        finish();
                                    }
                                    else
                                        Toast.makeText(getApplicationContext(), "Invalid DATE entry." +
                                                "\nStart date must be before end date." +
                                                "\nFormat: YYYY-MM-DD", Toast.LENGTH_LONG).show();
                                }

                            }
                            else
                                Toast.makeText(getApplicationContext(), "Select a valid COURSE ID",
                                        Toast.LENGTH_LONG).show();
                        }

                        if(item.getItemId() == R.id.asmntAllAsmntsOpt){

                            Intent intent = new Intent(AssessmentDetails.this, AssessmentsList.class);
                            startActivity(intent);
                            return true;

                        }

                        return  false;
                    }
                });

                popupMenu.show();
            }
        });

    }

    @Override
    protected void onResume(){

        super.onResume();

        if(isAsmntUpdate){

            associatedCourse = courseRepository.getCourse(asmntCrsIdSlct);
            courseList.clear();
            courseList.add(associatedCourse);
            courseAdapter.setCourses(courseList);

        }

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

                finish();

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
        int asmntCrsId = Integer.parseInt(selectedAsmntCourseId);
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