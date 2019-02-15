package com.lee;

import com.lee.bean.Student;
import com.lee.service.impl.StudentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SprringbootShiroMulitRealmApplicationTests {

  @Autowired
  StudentServiceImpl StudentServiceImpl;

  @Test
  public void contextLoads() {
  }

  @Test
  public void testStudent() {
    Student student = StudentServiceImpl.getStudentById(1);
    System.out.println(student);
  }

}
