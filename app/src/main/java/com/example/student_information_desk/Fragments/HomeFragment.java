package com.example.student_information_desk.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.student_information_desk.BookingActivity;
import com.example.student_information_desk.R;


public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize CardView
        CardView cardViewBooking = view.findViewById(R.id.card_view_booking);

        // Set onClickListener for the CardView (Alternatively, you can set this in XML)
        cardViewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event, e.g., start a new activity
                openBookingActivity();
            }
        });

        return view;
    }

    // Method to open the BookingActivity
    private void openBookingActivity() {
        Intent intent = new Intent(requireActivity(), BookingActivity.class);
        startActivity(intent);
    }
}