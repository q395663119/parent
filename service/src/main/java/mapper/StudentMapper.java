package mapper;

import entity.Student;

public interface StudentMapper {
    Student getInfo();

    int insert(Student record);

    int insertSelective(Student record);
}