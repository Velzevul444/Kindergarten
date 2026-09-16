package classes;

import Enumes.Status;
import java.time.LocalDate;

public class Enrollment {
    private int id;
    private String childName;
    private int parentId;
    private LocalDate createdAt;
    private Status status;

    public Enrollment(int id, String childName, int parentId, LocalDate createdAt, Status status) {
        this.id = id;
        this.childName = childName;
        this.parentId = parentId;
        this.createdAt = createdAt;
        this.status = status;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
}
    public String getChildName() {
        return childName;
    }
    public void setChildName(String childName) {
        this.childName = childName;
    }
    public int getParentId() {
        return parentId;
    }
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}
