package repositories;

import classes.Enrollment;

import java.util.List;

public interface EnrollmentRepo {
    void save(Enrollment enrollment);

    List<Enrollment> findAll();

    Enrollment findById(int id);

    void update(Enrollment enrollment);

    void delete(int id);
}