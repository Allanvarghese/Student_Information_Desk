import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class AppointmentAdapter extends ArrayAdapter<String> {

    private ArrayList<String> timeSlots;

    public AppointmentAdapter(Context context) {
        super(context, R.layout.grid_item_time_slot);
        timeSlots = new ArrayList<>();
    }

    public void setTimeSlots(ArrayList<String> timeSlots) {
        this.timeSlots = timeSlots;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return timeSlots.size();
    }

    @Override
    public String getItem(int position) {
        return timeSlots.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            button = (Button) inflater.inflate(R.layout.grid_item_time_slot, parent, false);
        } else {
            button = (Button) convertView;
        }

        String timeSlot = getItem(position);
        button.setText(timeSlot);

        // Add onClickListener for booking logic

        return button;
    }
}
