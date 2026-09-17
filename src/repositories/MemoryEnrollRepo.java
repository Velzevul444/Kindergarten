package repositories;

import classes.Enrollment;

import java.util.ArrayList;
import java.util.List;

public class MemoryEnrollRepo implements EnrollmentRepo {
    private final List<Enrollment> enrollments = new ArrayList<>();

    @Override
    public void save(Enrollment enrollment) {
        enrollments.add(enrollment);
    }

    @Override
    public List<Enrollment> findAll() {
        return enrollments;
    }

    @Override
    public Enrollment findById(int id) {
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getId() == id) {
                return enrollment;
            }
        }

        return null;
    }

    @Override
    public void update(Enrollment enrollment) {
        Enrollment existingEnrollment = findById(enrollment.getId());

        if (existingEnrollment != null) {
            existingEnrollment.setChildName(enrollment.getChildName());
            existingEnrollment.setParentId(enrollment.getParentId());
            existingEnrollment.setCreatedAt(enrollment.getCreatedAt());
            existingEnrollment.setStatus(enrollment.getStatus());
        }
    }

    @Override
    public void delete(int id) {
        enrollments.removeIf(enrollment -> enrollment.getId() == id);
    }
}