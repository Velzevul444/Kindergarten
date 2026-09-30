package services;

import classes.Enrollment;
import exceptions.BusinessException;
import exceptions.EntityNotFound;
import repositories.EnrollmentRepo;
import repositories.ParentRepo;

import java.util.List;

public class EnrollService {
    private final EnrollmentRepo repository;
    private final ParentRepo parentRepository;

    public EnrollService(EnrollmentRepo repository, ParentRepo parentRepository) {
        this.repository = repository;
        this.parentRepository = parentRepository;
    }

    public void create(Enrollment enrollment) {
        if (enrollment.getChildName() == null
                || enrollment.getChildName().isBlank()) {
            throw new BusinessException(
                    "Child name cannot be empty."
            );
        }

        if (repository.findById(enrollment.getId()) != null) {
            throw new BusinessException(
                    "Enrollment with this ID already exists."
            );
        }

        if (!parentRepository.existsById(enrollment.getParentId())) {
            throw new BusinessException(
                "Parent with this ID does not exist."
            );
        }

        repository.save(enrollment);
    }

    public List<Enrollment> findAll() {
        return repository.findAll();
    }

    public Enrollment findById(int id) {
        Enrollment enrollment = repository.findById(id);

        if (enrollment == null) {
            throw new EntityNotFound(
                    "Enrollment with this ID was not found."
            );
        }

        return enrollment;
    }

    public void update(Enrollment enrollment) {
        findById(enrollment.getId());

        if (enrollment.getChildName() == null
                || enrollment.getChildName().isBlank()) {
            throw new BusinessException(
                    "Child name cannot be empty."
            );
        }

        repository.update(enrollment);
    }

    public void delete(int id) {
        findById(id);
        repository.delete(id);
    }
}