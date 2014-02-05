package br.com.thejcs.stahp.api.entity;

import java.util.List;

public class EntryEntity {

    private Integer score;

    private PlayerEntity player;

    private List<String> words;

    public Integer getScore() {
        return score;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public List<String> getWords() {
        return words;
    }

}
