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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abonado.academicplanner.R;
import com.abonado.academicplanner.database.CourseRepository;
import com.abonado.academicplanner.database.TermRepository;
import com.abonado.academicplanner.entities.Course;
import com.abonado.academicplanner.entities.Term;
import com.abonado.academicplanner.utilities.CourseAdapter;

import java.util.List;

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

                if(isUpdate){

                    nonEditId = findViewById(R.id.trmIdTxt);
                    editName = findViewById(R.id.trmNmEdTxt);
                    editStart = findViewById(R.id.trmStrtTxt);
                    editEnd = findViewById(R.id.trmEndTxt);
                    mTermId = termIdToUpdate;
                    mTermName = editName.getText().toString();
                    mTermStart = editStart.getText().toString();
                    mTermEnd = editEnd.getText().toString();

                    Term term = new Term(mTermId, mTermName, mTermStart, mTermEnd);

                    termRepository.update(term);

                    isUpdate = false;

                    Intent intent = new Intent(TermDetails.this, TermsList.class);
                    startActivity(intent);


                }
                else {

                    editName = findViewById(R.id.trmNmEdTxt);
                    editStart = findViewById(R.id.trmStrtTxt);
                    editEnd = findViewById(R.id.trmEndTxt);
                    mTermName = editName.getText().toString();
                    mTermStart = editStart.getText().toString();
                    mTermEnd = editEnd.getText().toString();

                    Term term = new Term(mTermId, mTermName, mTermStart, mTermEnd);

                    termRepository.insert(term);

                    Intent intent = new Intent(TermDetails.this, TermsList.class);
                    startActivity(intent);
                }
            }
        });

        Button delete = findViewById(R.id.deleteTrmTrmDtls);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                termRepository = new TermRepository(getApplication());

                List<Term> allTerms = termRepository.getAllTerms();

                if(allTerms != null){
                    for(Term term : allTerms){
                        if(editName.getText().toString().equals(term.getTermName())){
                            termRepository.delete(term);
                            Toast.makeText(getApplicationContext(), "ID: " + term.getTermId()
                                            + "-- Name: " + term.getTermName() + " was deleted",
                                    Toast.LENGTH_SHORT).show();
                            
                            Intent intent = new Intent(TermDetails.this, TermDetails.class);
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