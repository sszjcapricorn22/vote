package com.example.jayvote.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jayvote.R;
import com.example.jayvote.model.Deal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class VotesActivity extends AppCompatActivity {
    DatabaseReference ref;
    ArrayList<Deal> list;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    String key, VoterCasted;
    int Totalvoter;
    String percent;
    float Votess;
    String totalVotesHere;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votes);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        ref = FirebaseDatabase.getInstance().getReference().child("Item_0");

        setVotersHere();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (ref != null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        list = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            list.add(ds.getValue(Deal.class));

                        }
                        AdapterClassss adapterClassss = new AdapterClassss(list);
                        recyclerView.setAdapter(adapterClassss);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void setVotersHere() {
        DatabaseReference mDatabase;
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //change "Item_0_Auth"
        mDatabase.child("Item_0_Auth").child(Objects.requireNonNull(auth.getUid()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            Deal voter = dataSnapshot.getValue(Deal.class);

                            VoterCasted = voter.getVoteCast();
                            if (VoterCasted != null) {

                                Log.d("vote123", VoterCasted.toString());
                            } else {

                                Toast.makeText(VotesActivity.this, "yes", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class AdapterClassss extends RecyclerView.Adapter<AdapterClassss.MyViewHolder> {
        Context context;
        ArrayList<Deal> list;

        public AdapterClassss(ArrayList<Deal> list) {
            this.list = list;
        }

        @NonNull

        @Override
        public AdapterClassss.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardholder, viewGroup, false);
            return new AdapterClassss.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterClassss.MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {

            myViewHolder.votesPercentage.setText(list.get(i).getPercentage());
            myViewHolder.number.setText(list.get(i).getvotes());
            myViewHolder.name.setText(list.get(i).getTitle());

            int percentage = 0;
            try {
                percentage = Integer.parseInt(list.get(i).getPercentage());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            myViewHolder.hpb.setProgress(percentage);

            int VotesGained = Integer.parseInt(list.get(i).getvotes());
            if (VotesGained != 100) {
                myViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {

                        auth = FirebaseAuth.getInstance();
                        if (VoterCasted == null) {
                            String votes = list.get(i).getvotes();
                            int VoteInt = Integer.parseInt(votes);
                            Totalvoter = VoteInt + 1;


                            totalVotesHere = String.valueOf(Totalvoter);
                            Votess = Float.valueOf(Totalvoter);
                            myViewHolder.hpb.getProgress();

                            float percentageConversion = (Votess / 100) * 100;

                            myViewHolder.votesPercentage.setText(Math.round(percentageConversion) + "%");

                            percent = String.valueOf(Math.round(percentageConversion));


                            key = list.get(i).setKey(list.get(i).getKey());
                            //change Item_0
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Item_0").child(key);
                            HashMap<String, Object> hashref = new HashMap();
                            hashref.put("votes", totalVotesHere);
                            hashref.put("Percentage", percent);
                            ref.updateChildren(hashref).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                }
                            });
                            DatabaseReference refVoters = FirebaseDatabase.getInstance().getReference("Item_0_Auth").child(auth.getUid());

                            HashMap<String, Object> hashrefVoters = new HashMap();
                            hashrefVoters.put("VoteCast", auth.getUid());

                            refVoters.setValue(hashrefVoters).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                }
                            });

                        } else if (VoterCasted.equals(auth.getUid())) {
                            Toast.makeText(VotesActivity.this, "Already Voted", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            } else {

//                Toast.makeText(VotesActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name, votesPercentage, number;
            ProgressBar hpb;
            Button btn;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                votesPercentage = itemView.findViewById(R.id.votesPercentage);
                hpb = itemView.findViewById(R.id.hpb);
                btn = itemView.findViewById(R.id.setBtn);
                name = itemView.findViewById(R.id.name);
                number = itemView.findViewById(R.id.number);
            }
        }
    }

}