package com.example.sejjoh.gsls;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.sejjoh.gsls.Interface.ItemClickListener;
import com.example.sejjoh.gsls.models.MechanicModel;
import com.example.sejjoh.gsls.viewHolder.ViewHolderService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class FragmentMechanics extends Fragment {
    private RecyclerView recyclerView;

    FirebaseRecyclerAdapter adapter;
    int color;

    @SuppressLint("ValidFragment")
    public FragmentMechanics(int color) {
        this.color = color;
    }
    public FragmentMechanics() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.layout_viewpager, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerservices);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Mechanic");

        FirebaseRecyclerOptions<MechanicModel> options = new FirebaseRecyclerOptions.Builder<MechanicModel>()
                .setQuery(query, MechanicModel.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<MechanicModel, ViewHolderService>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderService holder, int position, @NonNull MechanicModel model) {

                holder.post_name.setText(model.getMechanicTitle());

                Glide.with(getActivity())
                        .load(model.getImage())
                        .into(holder.imageView);



                holder.setItemClickListener(new ItemClickListener() {

                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent MechanicIntent=new Intent(getActivity(), MechanicDetails.class);
                        MechanicIntent.putExtra("mechanicId",adapter.getRef(position).getKey()); //send hotel Id to new activity
                        startActivity(MechanicIntent);

                    }
                });
            }

            @Override
            public ViewHolderService onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_service, parent, false);

                return new ViewHolderService(view);
            }


        };

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
//        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.startListening();
    }

}

