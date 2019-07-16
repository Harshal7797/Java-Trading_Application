package ca.jrvs.apps.model.domain;

public interface Entity<ID> {
    ID getId();
    void setID(ID id);
}