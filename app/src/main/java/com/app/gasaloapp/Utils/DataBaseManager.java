package com.app.gasaloapp.Utils;

import androidx.annotation.NonNull;

import com.app.gasaloapp.Interfaces.DataBaseResponse;
import com.app.gasaloapp.Model.Entry;
import com.app.gasaloapp.Model.Tank;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBaseManager
{


    private static final String  DATABASE_REF_ENTRY="Entries";
    private static final String  DATABASE_REF_TOTAL_LITRES="TotalLitres";

    public static  void authenticate(String email, String pass, DataBaseResponse dataBaseResponse)
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                dataBaseResponse.onResponse(task.isSuccessful(),task.isSuccessful()?"Success":task.getException().getMessage(),null);
            }
        });
    }
    public static  void readTotalLitres( DataBaseResponse dataBaseResponse)
    {
        final CollectionReference collection=FirebaseFirestore.getInstance().collection(DATABASE_REF_TOTAL_LITRES);

        collection.document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful() && task.getResult().exists())
                {
                    dataBaseResponse.onResponse(task.isSuccessful(),task.isSuccessful()?"Success":task.getException().getMessage(),task.getResult().toObject(Tank.class));
                }
                else
                {
                    dataBaseResponse.onResponse(task.isSuccessful(),task.isSuccessful()?"Success":task.getException().getMessage(),null);
                }
            }
        });
    }
    public static  void updateTotalLitres(Tank tank)
    {
        final CollectionReference collection=FirebaseFirestore.getInstance().collection(DATABASE_REF_TOTAL_LITRES);
        collection.document(FirebaseAuth.getInstance().getUid()).set(tank);
    }
    public static  void insertEntry(Entry entry, DataBaseResponse dataBaseResponse)
    {
        final CollectionReference collection=FirebaseFirestore.getInstance().collection(DATABASE_REF_ENTRY);
        entry.id=collection.document().getId();
        collection.document( entry.id).set(entry).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dataBaseResponse.onResponse(task.isSuccessful(),task.isSuccessful()?"Success":task.getException().getMessage(),null);

            }
        });
    }

    public static  void readEntries(String person_id, DataBaseResponse dataBaseResponse)
    {
        final CollectionReference collection=FirebaseFirestore.getInstance().collection(DATABASE_REF_ENTRY);
        final Query query=collection
                /*.whereEqualTo("person_id", person_id)*/
                .whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .orderBy("person_id", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                List<Entry> entries=new ArrayList<>();

                if(task.isSuccessful())
                {
                    if(!task.getResult().isEmpty() )
                    {
                        for(DocumentSnapshot document:task.getResult().getDocuments())
                        {

                            entries.add(document.toObject(Entry.class));
                        }

                    }
                }
                dataBaseResponse.onResponse(task.isSuccessful(),task.isSuccessful()?"Success":task.getException().getMessage(),entries);

            }
        });
    }

    public static  void removeEntries( DataBaseResponse dataBaseResponse)
    {
        final CollectionReference collection=FirebaseFirestore.getInstance().collection(DATABASE_REF_ENTRY);
        final Query query=collection
                /*.whereEqualTo("person_id", person_id)*/
                .whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                List<Entry> entries=new ArrayList<>();

                if(task.isSuccessful())
                {
                    if(!task.getResult().isEmpty() )
                    {
                        for(DocumentSnapshot document:task.getResult().getDocuments())
                        {

                            collection.document(document.getId()).delete();
                        }

                    }
                }
                dataBaseResponse.onResponse(task.isSuccessful(),task.isSuccessful()?"Success":task.getException().getMessage(),entries);

            }
        });
    }


}
