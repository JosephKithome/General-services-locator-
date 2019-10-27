package com.example.sejjoh.gsls.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sejjoh.gsls.Message;
import com.example.sejjoh.gsls.R;
import com.example.sejjoh.gsls.models.ChatModel;
import com.example.sejjoh.gsls.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;
    private List<UserModel> mUsers;
    private  boolean isChat;
    String theLastMessage;
    public UserAdapter(Context mContext, List<UserModel>mUsers,boolean isChat){
        this.mUsers=mUsers;
        this.mContext=mContext;
        this.isChat=isChat;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_row,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserModel userModel=mUsers.get(position);
        holder.username.setText(userModel.getUsername());
        holder.email.setText(userModel.getEmail());
        if (userModel.getImageUrl().equals("default")){
            holder.circleImageView.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(userModel.getImageUrl()).into(holder.circleImageView);
        }
        if (isChat){
            lastMessage(userModel.getId(),holder.last_msg);

        }else {
            holder.last_msg.setVisibility(View.GONE);
        }
        if (isChat){
            if (userModel.getStatus().equals("online")){
                holder.img_On.setVisibility(View.VISIBLE);
                holder.img_Off.setVisibility(View.GONE);
            }else {
                holder.img_On.setVisibility(View.GONE);
                holder.img_Off.setVisibility(View.VISIBLE);

            }
        }else {
            holder.img_On.setVisibility(View.GONE);
            holder.img_Off.setVisibility(View.GONE);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messageintent= new Intent(mContext, Message.class);
                messageintent.putExtra("userid",userModel.getId());
                mContext.startActivity(messageintent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{
        public TextView username;
        public TextView email;
        public CircleImageView circleImageView;
        private TextView img_On;
        private TextView img_Off;
        private  TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username= itemView.findViewById(R.id.username);
            email= itemView.findViewById(R.id.email);
            img_On= itemView.findViewById(R.id.img_on);
            img_Off= itemView.findViewById(R.id.img_off);
            circleImageView= itemView.findViewById(R.id.profile);
            last_msg=itemView.findViewById(R.id.last_msg);
        }
    }
    // here we check last message
    private  void  lastMessage(final String userid, final TextView last_mesg){
        theLastMessage="default";
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatModel chat=snapshot.getValue(ChatModel.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) &&chat.getSender().equals(firebaseUser.getUid())
                        || chat.getReceiver().equals(userid) &&chat.getSender().equals(userid)){
                        theLastMessage=chat.getMessage();

                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_mesg.setText("No message");
                        break;
                        default:
                            last_mesg.setText(theLastMessage);
                            break;
                }
                theLastMessage="default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
