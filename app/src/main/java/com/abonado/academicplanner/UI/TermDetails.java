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
import com.abonado.academicplanner.database.TermRepository;
import com.abonado.academicplanner.entities.Term;
import com.abonado.academicplanner.utilities.HelperToTerm;
import com.abonado.academicplanner.utilities.TermAdapter;

import java.util.ArrayList;
import java.util.List;

public class TermDetails extends AppCompatActivity {

    EditText editId;
    EditText editName;
    EditText editStart;
    EditText editEnd;
    String termName;
    String termStart;
    String termEnd;
    TermRepository termRepository;
    Term currentTerm;
    int numTerms;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        RecyclerView recyclerView = findViewById(R.id.trmsLstRcyle);
        termRepository = new TermRepository(getApplication());
        List<Term> allTerms = termRepository.getAllTerms();
        final TermAdapter termAdapter = new TermAdapter(this);
        recyclerView.setAdapter(termAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdapter.setTerms(allTerms);


        editName = findViewById(R.id.trmNmEdTxt);
        editStart = findViewById(R.id.trmStrtTxt);
        editEnd = findViewById(R.id.trmEndTxt);

        editName.setText("");
        editStart.setText("");
        editEnd.setText("");


        if(HelperToTerm.termToUpdate != null){

            Term termToPopulate = HelperToTerm.termToUpdate;

            editName.setText(termToPopulate.getTermName());
            editStart.setText(termToPopulate.getTermStart());
            editEnd.setText(termToPopulate.getTermEnd());
        }


        Toolbar myToolbar = findViewById(R.id.term_details_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Button termsSaveButton = findViewById(R.id.saveTermDetails);
        termsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                termRepository = new TermRepository(getApplication());

                editName = findViewById(R.id.trmNmEdTxt);
                editStart = findViewById(R.id.trmStrtTxt);
                editEnd = findViewById(R.id.trmEndTxt);
                termName = editName.getText().toString();
                termStart = editStart.getText().toString();
                termEnd = editEnd.getText().toString();

                Term term = new Term(termName,termStart,termEnd);

                termRepository.insert(term);

                Intent intent = new Intent(TermDetails.this, TermDetails.class);
                startActivity(intent);
            }
        });

        Button delete = findViewById(R.id.deleteTrmTrmDtls);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Term termToDelete = HelperToTerm.termToUpdate;

                termRepository = new TermRepository(getApplication());

                if(allTerms != null){
                    for(Term term : allTerms){
                        if(editName.getText().toString().equals(term.getTermName())){
                            termRepository.delete(term);
                            Toast.makeText(getApplicationContext(), "ID: " + termToDelete.getTermId()
                                            + "-- Name: " + termToDelete.getTermName() + " was deleted",
                                    Toast.LENGTH_SHORT).show();

                            HelperToTerm.termToUpdate = null;

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