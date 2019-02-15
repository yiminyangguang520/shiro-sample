package com.lee.service.impl;

import com.lee.bean.Teacher;
import com.lee.bean.TeacherExample;
import com.lee.dao.TeacherMapper;
import com.lee.service.TeacherService;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

  @Autowired
  TeacherMapper teacherMapper;

  @Override
  public Teacher getTeacherByAccount(String account) {
    TeacherExample teacherExample = new TeacherExample();
    teacherExample.createCriteria().andAccountEqualTo(account);
    List<Teacher> teacherList = teacherMapper.selectByExample(teacherExample);
    if (CollectionUtils.isEmpty(teacherList)) {
      return null;
    }
    return teacherList.get(0);
  }

}
