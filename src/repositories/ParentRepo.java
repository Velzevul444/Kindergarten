package repositories;

import classes.Parents;

import java.util.List;

public interface ParentRepo {
    List<Parents> findAll();

    boolean existsById(int id);
}