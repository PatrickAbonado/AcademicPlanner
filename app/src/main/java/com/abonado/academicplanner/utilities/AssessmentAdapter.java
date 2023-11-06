package com.abonado.academicplanner.utilities;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abonado.academicplanner.R;
import com.abonado.academicplanner.UI.AssessmentDetails;
import com.abonado.academicplanner.UI.TermDetails;
import com.abonado.academicplanner.entities.Assessment;
import com.abonado.academicplanner.entities.Term;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    private List<Assessment> mAssessments;
    private Context context;
    private final LayoutInflater mInflater;

    public AssessmentAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class AssessmentViewHolder extends RecyclerView.ViewHolder{

        TextView assessmentId;
        TextView assessmentTitle;
        TextView assessmentStart;
        TextView assessmentEnd;


        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);

            assessmentId = itemView.findViewById(R.id.asmntsLstRcylId);
            assessmentTitle = itemView.findViewById(R.id.asmntsLstRcylTitle);
            assessmentStart = itemView.findViewById(R.id.asmntsLstRcylStrt);
            assessmentEnd = itemView.findViewById(R.id.asmntsLstRcylEnd);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Assessment current = mAssessments.get(position);

                    Intent intent = new Intent(context, AssessmentDetails.class);
                    intent.putExtra("assessment_id",current.getAssessmentId());
                    intent.putExtra("assessment_title", current.getAssessmentTitle());
                    intent.putExtra("assessment_start", current.getAssessmentStart());
                    intent.putExtra("assessment_end", current.getAssessmentEnd());
                    context.startActivity(intent);
                }
            });


        }
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View termView = mInflater.inflate(R.layout.assessments_list_item, parent, false);
        return new AssessmentViewHolder(termView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {

        Assessment assessment = mAssessments.get(position);

        holder.assessmentId.setText(String.valueOf(assessment.getAssessmentId()));
        holder.assessmentTitle.setText(assessment.getAssessmentTitle());
        holder.assessmentStart.setText(assessment.getAssessmentStart());
        holder.assessmentEnd.setText(assessment.getAssessmentEnd());

        holder.itemView.setOnClickListener(v -> {

            HelperToAssessment.assessmentToUpdate = mAssessments.get(position);

            Intent intent = new Intent(context, AssessmentDetails.class);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        if(mAssessments != null) {
            return mAssessments.size();
        }
        else return 0;
    }

    public void setAssessments(List<Assessment> assessments){
        mAssessments = assessments;
        notifyDataSetChanged();
    }


}
