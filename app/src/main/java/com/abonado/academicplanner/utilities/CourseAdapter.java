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
import com.abonado.academicplanner.UI.CourseDetails;
import com.abonado.academicplanner.entities.Course;


import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> mCourses;
    private Context context;
    private final LayoutInflater mInflater;

    public CourseAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {


        TextView courseId;
        TextView courseTitle;
        TextView courseStart;
        TextView courseEnd;


        public CourseViewHolder(@NonNull View itemView){

            super(itemView);

            courseId = itemView.findViewById(R.id.coursesLstRcylId);
            courseTitle = itemView.findViewById(R.id.coursesLstRcylNm);
            courseStart = itemView.findViewById(R.id.coursesLstRcylStrt);
            courseEnd = itemView.findViewById(R.id.coursesLstRcylEnd);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Course current = mCourses.get(position);

                    Intent intent = new Intent(context, CourseDetails.class);
                    intent.putExtra("courseId",current.getCourseId());
                    intent.putExtra("courseTitle", current.getCourseTitle());
                    intent.putExtra("courseStart", current.getCourseStart());
                    intent.putExtra("courseEnd", current.getCourseEnd());
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View termView = mInflater.inflate(R.layout.courses_list_item, parent, false);
        return new CourseViewHolder(termView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {

        if(mCourses != null){
            Course course = mCourses.get(position);

            holder.courseId.setText(String.valueOf(course.getCourseId()));
            holder.courseTitle.setText(course.getCourseTitle());
            holder.courseStart.setText(course.getCourseStart());
            holder.courseEnd.setText(course.getCourseEnd());
        }
        else {
            System.out.println("blank");
        }

    }

    @Override
    public int getItemCount() {
        if(mCourses != null) {
            return mCourses.size();
        }
        else return 0;
    }

    public void setCourses(List<Course> coursesList){
         mCourses = coursesList;
        notifyDataSetChanged();
    }


}
