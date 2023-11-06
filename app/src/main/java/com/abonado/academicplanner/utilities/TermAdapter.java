package com.abonado.academicplanner.utilities;

import static androidx.core.content.ContextCompat.startActivity;

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
import com.abonado.academicplanner.UI.TermDetails;
import com.abonado.academicplanner.UI.TermsList;
import com.abonado.academicplanner.entities.Term;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    private List<Term> mTerms;
    private Context context;
    private final LayoutInflater mInflater;

    public TermAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termId;
        private final TextView termName;
        private final TextView termStart;
        private final TextView termEnd;

        public TermViewHolder(@NonNull View itemView){

            super(itemView);

            termId = itemView.findViewById(R.id.trmsLstRcylTrmId);
            termName = itemView.findViewById(R.id.trmsLstRcylTrmNm);
            termStart = itemView.findViewById(R.id.trmsLstRcylTrmStrt);
            termEnd = itemView.findViewById(R.id.trmsLstRcylTrmEnd);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Term current = mTerms.get(position);

                    Intent intent = new Intent(context, TermDetails.class);
                    intent.putExtra("term_id",current.getTermId());
                    intent.putExtra("term_name", current.getTermName());
                    intent.putExtra("term_start", current.getTermStart());
                    intent.putExtra("term_end", current.getTermEnd());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View termView = mInflater.inflate(R.layout.terms_list_item, parent, false);
        return new TermViewHolder(termView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {

        if(mTerms != null){

        Term currTerm = mTerms.get(position);

        /*int currTermId = currTerm.getTermId();
        String currTermName = currTerm.getTermName();
        String currTermStart = currTerm.getTermStart();
        String currTermEnd = currTerm.getTermEnd();*/

        holder.termId.setText(String.valueOf(currTerm.getTermId()));
        holder.termName.setText(currTerm.getTermName());
        holder.termStart.setText(currTerm.getTermStart());
        holder.termEnd.setText(currTerm.getTermEnd());

        }
        else {
            System.out.println("FUCK");
        }


    }

    @Override
    public int getItemCount() {
        if(mTerms != null) {
            return mTerms.size();
        }
        else return 0;
    }
    public void setTerms(List<Term> terms){
        mTerms = terms;
        notifyDataSetChanged();
    }

}
