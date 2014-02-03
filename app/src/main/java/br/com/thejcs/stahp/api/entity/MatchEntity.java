package br.com.thejcs.stahp.api.entity;

import java.util.Set;

public class MatchEntity {

    public enum Status {
        CREATED,
        STARTED,
        FINISHED
    }

    private String id;

    private Status status;

    private Set<PlayerEntity> players;

    private PlayerEntity creator;

    public String getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Set<PlayerEntity> getPlayers() {
        return players;
    }

    public PlayerEntity getCreator() {
        return creator;
    }

    // TODO: this shouldn't be here...
    public boolean isCreator(String playerId) {
        return (creator.getId().equals(playerId));
    }

    // TODO: this shouldn't be here...
    public boolean isPlayerIn(String playerId) {
        for(PlayerEntity player: players) {
            if(player.getId().equals(playerId)) {
                return true;
            }
        }

        return false;
    }
}
