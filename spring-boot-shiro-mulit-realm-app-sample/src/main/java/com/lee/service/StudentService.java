package com.lee.service;

import com.lee.bean.Student;

public interface StudentService {

  Student getStudentById(int id);

  Student getStudentByAccount(String account);
}
