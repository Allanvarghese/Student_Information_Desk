package com.example.student_information_desk.Model;

public class TimeSlot {
    private Long slot;
    // Constructor that takes a Long directly
    public TimeSlot(Long slot) {
        this.slot = slot;
    }



//    public TimeSlot(){
//
//    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }
}
