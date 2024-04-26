package com.example.student_information_desk.Interface;

import java.util.List;

public interface IAllServiceTypeLoadListener {
    void ServiceTypeLoadSuccess(List<String> serviceTypeList);
    void ServiceTypeLoadFaild(String message);
}
