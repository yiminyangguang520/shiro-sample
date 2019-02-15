package com.lee.service.impl;

import com.lee.bean.Student;
import com.lee.bean.StudentExample;
import com.lee.dao.StudentMapper;
import com.lee.service.StudentService;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {


  @Autowired
  StudentMapper studentMapper;

  @Override
  public Student getStudentById(int id) {
    Student student = studentMapper.selectByPrimaryKey(id);
    return student;
  }

  @Override
  public Student getStudentByAccount(String account) {
    StudentExample studentExample = new StudentExample();
    studentExample.createCriteria().andAccountEqualTo(account);
    List<Student> studentList = studentMapper.selectByExample(studentExample);
    if (CollectionUtils.isEmpty(studentList)) {
      return null;
    }
    return studentList.get(0);
  }

}
