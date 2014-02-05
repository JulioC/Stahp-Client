package br.com.thejcs.stahp.api.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class MatchEntity implements Serializable {

    public enum Status {
        CREATED,
        STARTED,
        FINISHED
    }

    private String id;

    private Status status;

    private List<EntryEntity> entries;

    private PlayerEntity creator;

    private Integer timeLimit;

    private boolean joined;

    private boolean responded;

    public String getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public List<EntryEntity> getEntries() {
        return entries;
    }

    public PlayerEntity getCreator() {
        return creator;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public boolean hasJoined() {
        return joined;
    }

    public boolean hasResponded() {
        return responded;
    }
}
