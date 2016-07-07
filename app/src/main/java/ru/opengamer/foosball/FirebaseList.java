package ru.opengamer.foosball;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Opengamer on 30.06.2016.
 */
public class FirebaseList<T> {

    protected DatabaseReference mRef;
    protected Class<T> modelClass;
    protected List<T> dataList;
    protected List<String> keyList;
    protected ChildEventListener childEventListener;

    public FirebaseList(DatabaseReference mRef, Class<T> modelClass) {
        this.mRef = mRef;
        this.modelClass = modelClass;
        dataList = new ArrayList<>();
        keyList = new ArrayList<>();
        childEventListener = this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                T data = dataSnapshot.getValue(FirebaseList.this.modelClass);
                String key = dataSnapshot.getKey();
                if (previousChildName == null) {
                    dataList.add(0, data);
                    keyList.add(0, key);
                    afterAdded(keyList.size()-1);
                } else {
                    int previousIndex = keyList.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == dataList.size()) {
                        dataList.add(data);
                        keyList.add(key);
                        afterAdded(keyList.size()-1);
                    } else {
                        dataList.add(nextIndex, data);
                        keyList.add(nextIndex, key);
                        afterAdded(nextIndex);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                T data = dataSnapshot.getValue(FirebaseList.this.modelClass);
                String key = dataSnapshot.getKey();
                int index = keyList.indexOf(key);
                dataList.set(index, data);
                afterChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = keyList.indexOf(key);
                dataList.remove(index);
                keyList.remove(index);
                afterRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                afterCancelled();
            }
        });
    }

    protected List<T> getDataList(){
        return dataList;
    }

    protected List<String> getKeyList(){
        return keyList;
    }

    protected void afterAdded(int index){
    };

    protected void afterChanged(int index){
    };

    protected void afterRemoved(int index){
    };

    protected void afterMoved(int index){
    };

    protected void afterCancelled(){
    };

}
