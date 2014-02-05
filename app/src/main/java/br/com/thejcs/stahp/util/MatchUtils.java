package br.com.thejcs.stahp.util;

import br.com.thejcs.stahp.api.entity.MatchEntity;
import br.com.thejcs.stahp.api.entity.PlayerEntity;

public class MatchUtils {

    public static boolean isCreator(String playerId, MatchEntity match) {
        return (match.getCreator().getId().equals(playerId));
    }

    public enum ConsolidatedStatus {
        READY_TO_START,
        WAITING_START,
        OPEN_TO_JOIN,
        PLAYER_TURN,
        STARTED,
        WAITING_FINISH,
        FINISHED
    };

    public static ConsolidatedStatus getConsolidatedStatus(String playerId, MatchEntity match) {
        boolean isCreator = isCreator(playerId, match);
        boolean hasPlayerJoined = match.hasJoined();
        boolean hasPlayerResponded = match.hasResponded();

        switch(match.getStatus()) {
            case FINISHED:
                return ConsolidatedStatus.FINISHED;
            case CREATED:
                if(isCreator) {
                    return ConsolidatedStatus.READY_TO_START;
                }
                else if(hasPlayerJoined) {
                    return ConsolidatedStatus.WAITING_START;
                }
                else {
                    return ConsolidatedStatus.OPEN_TO_JOIN;
                }
            case STARTED:
                if(!hasPlayerJoined) {
                    return ConsolidatedStatus.STARTED;
                }
                else if(hasPlayerResponded) {
                    return ConsolidatedStatus.WAITING_FINISH;
                }
                else {
                    return ConsolidatedStatus.PLAYER_TURN;
                }
        }

        return ConsolidatedStatus.FINISHED;
    }

}
