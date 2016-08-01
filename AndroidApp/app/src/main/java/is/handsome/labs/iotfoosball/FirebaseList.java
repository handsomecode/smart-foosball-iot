package is.handsome.labs.iotfoosball;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class FirebaseList<T> {

    protected DatabaseReference mRef;
    protected Class<T> mModelClass;
    protected List<T> mDataList;
    protected List<String> mKeyList;
    protected ChildEventListener childEventListener;

    public FirebaseList(DatabaseReference Ref, Class<T> modelClass) {
        this.mRef = Ref;
        this.mModelClass = modelClass;
        mDataList = new ArrayList<>();
        mKeyList = new ArrayList<>();
        childEventListener = this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                T data = dataSnapshot.getValue(FirebaseList.this.mModelClass);
                String key = dataSnapshot.getKey();
                if (previousChildName == null) {
                    mDataList.add(0, data);
                    mKeyList.add(0, key);
                    afterAdded(mKeyList.size()-1);
                } else {
                    int previousIndex = mKeyList.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mDataList.size()) {
                        mDataList.add(data);
                        mKeyList.add(key);
                        afterAdded(mKeyList.size()-1);
                    } else {
                        mDataList.add(nextIndex, data);
                        mKeyList.add(nextIndex, key);
                        afterAdded(nextIndex);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                T data = dataSnapshot.getValue(FirebaseList.this.mModelClass);
                String key = dataSnapshot.getKey();
                int index = mKeyList.indexOf(key);
                mDataList.set(index, data);
                afterChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = mKeyList.indexOf(key);
                mDataList.remove(index);
                mKeyList.remove(index);
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
        return mDataList;
    }

    protected List<String> getKeyList(){
        return mKeyList;
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
