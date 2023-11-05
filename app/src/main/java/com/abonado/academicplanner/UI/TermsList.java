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

import com.abonado.academicplanner.R;
import com.abonado.academicplanner.database.TermRepository;
import com.abonado.academicplanner.entities.Term;
import com.abonado.academicplanner.utilities.HelperToTerm;
import com.abonado.academicplanner.utilities.TermAdapter;

import java.util.List;

public class TermsList extends AppCompatActivity {

    TermRepository termRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);


        Toolbar myToolbar = findViewById(R.id.terms_list_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Button addTerms = findViewById(R.id.addTermsBut);
        addTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperToTerm.termToUpdate = null;

                Intent intent = new Intent(TermsList.this, TermDetails.class);
                startActivity(intent);
            }
        });

        Button addCourses = findViewById(R.id.addCoursesTrmDtls);
        addCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsList.this, CourseDetails.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.trmsLstRcyle);
        termRepository = new TermRepository(getApplication());
        List<Term> allTerms = termRepository.getAllTerms();
        final TermAdapter termAdapter = new TermAdapter(this);
        recyclerView.setAdapter(termAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdapter.setTerms(allTerms);

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



        if(id == R.id.homeMenuPop){

            Intent intent = new Intent(this, Home.class);
            startActivity(intent);

            //Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.termLstMenuPop ) {

            Intent intent = new Intent(this, TermsList.class);
            startActivity(intent);

            //Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id ==R.id.courseLstMenuPop ) {

            Intent intent = new Intent(this, CoursesList.class);
            startActivity(intent);

            //Toast.makeText(this, "Item 3 selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.asmntLstMenuPop ) {

            Intent intent = new Intent(this, AssessmentsList.class);
            startActivity(intent);
            //Toast.makeText(this, "Item 4 selected", Toast.LENGTH_SHORT).show();
            return true;
        }
        if ( id == R.id.exitMenuPop){

            finishAffinity();
            //Toast.makeText(this, "Item 5 selected", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}