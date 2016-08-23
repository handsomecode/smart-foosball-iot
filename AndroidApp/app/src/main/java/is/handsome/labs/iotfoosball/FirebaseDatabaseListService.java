package is.handsome.labs.iotfoosball;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseListService<T> {
    private Class<T> mModelClass;
    private List<T> mDataList;
    private List<String> mKeyList;
    private List<ActionListener<T>> mListenersList;

    public FirebaseDatabaseListService(DatabaseReference Ref, Class<T> modelClass) {
        this.mModelClass = modelClass;
        mDataList = new ArrayList<>();
        mKeyList = new ArrayList<>();
        ChildEventListener mChildEventListener = Ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                T data = dataSnapshot.getValue(FirebaseDatabaseListService.this.mModelClass);
                String key = dataSnapshot.getKey();
                int index;
                if (previousChildName == null) {
                    mDataList.add(0, data);
                    mKeyList.add(0, key);
                    index = mKeyList.size() - 1;
                } else {
                    int previousIndex = mKeyList.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mDataList.size()) {
                        mDataList.add(data);
                        mKeyList.add(key);
                        index = mKeyList.size() - 1;
                    } else {
                        mDataList.add(nextIndex, data);
                        mKeyList.add(nextIndex, key);
                        index = nextIndex;
                    }
                }
                for (ActionListener<T> actionListener : mListenersList) {
                    actionListener.addingPerformed(key, data, index);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                T data = dataSnapshot.getValue(FirebaseDatabaseListService.this.mModelClass);
                String key = dataSnapshot.getKey();
                int index = mKeyList.indexOf(key);
                mDataList.set(index, data);
                for (ActionListener<T> actionListener : mListenersList) {
                    actionListener.changingPerformed(key, data, index);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                T data = dataSnapshot.getValue(FirebaseDatabaseListService.this.mModelClass);
                int index = mKeyList.indexOf(key);
                mDataList.remove(index);
                mKeyList.remove(index);
                for (ActionListener<T> actionListener : mListenersList) {
                    actionListener.removingPerformed(key, data, index);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void AddListener(ActionListener<T> actionListener) {
        actionListener.initialisation(mKeyList, mDataList);
        mListenersList.add(actionListener);
    }

    public void RemoveListener(ActionListener<T> actionListener) {
        actionListener.listenerRemovingPerformed();
        mListenersList.remove(actionListener);
    }
}
