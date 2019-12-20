package service;

import entity.Student;
import mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RestController
public class StudentService {

    @Autowired
    StudentMapper sm;

    @RequestMapping(value="/insert",produces = "application/json;charset=UTF-8")
    public int insert(Student student){
        student = new Student("花花2",91,"女");
        return sm.insert(student);
    }



    public Student getInfo() {
       // return sm.getInfo();//就孩不用数据库没什么意义
        return  new Student("张三2",22,"男2");

    }
}
