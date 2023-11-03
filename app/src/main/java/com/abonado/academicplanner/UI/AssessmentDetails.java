package com.abonado.academicplanner.UI;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.abonado.academicplanner.R;

public class AssessmentDetails extends AppCompatActivity {

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