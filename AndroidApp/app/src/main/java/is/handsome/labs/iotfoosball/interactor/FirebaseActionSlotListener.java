package is.handsome.labs.iotfoosball.interactor;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import is.handsome.labs.iotfoosball.models.FirebaseActionListener;
import is.handsome.labs.iotfoosball.models.Slot;
import timber.log.Timber;


public class FirebaseActionSlotListener extends FirebaseActionListener {

    private List<Slot> slotsList;
    private Comparator<Slot> slotsComparator;
    private Interactor interactor;

    FirebaseActionSlotListener(List slotsList, Interactor interactor) {
        this.slotsList = slotsList;
        this.interactor = interactor;
        slotsComparator = new Comparator<Slot>() {
            @Override
            public int compare(Slot slot1, Slot slot2) {
                return slot1.getFromDate().compareTo(slot2.getFromDate());
            }
        };
    }

    @Override
    public void addingPerformed(String key, Object data, int index) {
        Slot slot = (Slot) data;
        slot.setId(key);
        addSlot(key, slot, index);
    }

    @Override
    public void removingPerformed(String key, Object data, int index) {
        super.removingPerformed(key, data, index);
    }

    @Override
    public void changingPerformed(String key, Object data, int index) {
        Slot slot = (Slot) data;
        slot.setId(key);
        addSlot(key, slot, index);
    }

    @Override
    public void initialisation(List keyList, List dataList) {
        super.initialisation(keyList, dataList);
    }

    @Override
    public void listenerRemovingPerformed() {
        super.listenerRemovingPerformed();
    }

    private boolean addSlot(String key, Slot data, int index) {
        boolean isExist = false;
        synchronized (slotsList) {
            int i = 0;
            for (; i < slotsList.size(); i++) {
                if (slotsList.get(i).getId().equals(key)) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                Timber.d("adding new team " + key);
                slotsList.add(data);
            } else {
                slotsList.set(i, data);
                Timber.d("team already existed " + key);
            }
            Collections.sort(slotsList, slotsComparator);
        }
        return !isExist;
    }

    public Slot getNextSlot() {
        Date now = Calendar.getInstance().getTime();
        int i = 0;
        for (; i < slotsList.size(); i++) {
            if (slotsList.get(i).getFromDate().compareTo(now) > 0) {
                break;
            }
            if (i == slotsList.size()) {
                return null;
            }
        }
        return slotsList.get(i);
    }
}
