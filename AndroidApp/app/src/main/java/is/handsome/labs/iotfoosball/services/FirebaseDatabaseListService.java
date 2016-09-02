package is.handsome.labs.iotfoosball.services;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import is.handsome.labs.iotfoosball.models.ActionListener;
import timber.log.Timber;

public class FirebaseDatabaseListService<T> {
    private Class<T> modelClass;
    private List<T> dataList;
    private List<String> keyList;
    private List<ActionListener<T>> listenersList;

    public FirebaseDatabaseListService(DatabaseReference Ref, Class<T> modelClass) {
        this.modelClass = modelClass;
        this.listenersList = new ArrayList<>();
        this.dataList = new ArrayList<>();
        this.keyList = new ArrayList<>();
        Timber.d("listenerlist = " + this.listenersList.toString());
        ChildEventListener mChildEventListener = Ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                T data = dataSnapshot.getValue(FirebaseDatabaseListService.this.modelClass);
                String key = dataSnapshot.getKey();
                int index;
                if (previousChildName == null) {
                    dataList.add(0, data);
                    keyList.add(0, key);
                    index = keyList.size() - 1;
                } else {
                    int previousIndex = keyList.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == dataList.size()) {
                        dataList.add(data);
                        keyList.add(key);
                        index = keyList.size() - 1;
                    } else {
                        dataList.add(nextIndex, data);
                        keyList.add(nextIndex, key);
                        index = nextIndex;
                    }
                }
                for (ActionListener<T> actionListener : listenersList) {
                    actionListener.addingPerformed(key, data, index);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                T data = dataSnapshot.getValue(FirebaseDatabaseListService.this.modelClass);
                String key = dataSnapshot.getKey();
                int index = keyList.indexOf(key);
                dataList.set(index, data);
                for (ActionListener<T> actionListener : listenersList) {
                    actionListener.changingPerformed(key, data, index);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                T data = dataSnapshot.getValue(FirebaseDatabaseListService.this.modelClass);
                int index = keyList.indexOf(key);
                dataList.remove(index);
                keyList.remove(index);
                for (ActionListener<T> actionListener : listenersList) {
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

    public void addListener(ActionListener<T> actionListener) {
        actionListener.initialisation(keyList, dataList);
        listenersList.add(actionListener);
    }

    public void removeListener(ActionListener<T> actionListener) {
        actionListener.listenerRemovingPerformed();
        listenersList.remove(actionListener);
    }
}
