package com.example.student_information_desk.Interface;

import com.example.student_information_desk.Model.Counter;

import java.util.List;

public interface ICounterLoadListener {
    void CounterLoadSuccess(List<Counter> counterNumberList);
    void CounterLoadFailed(String message);
}
