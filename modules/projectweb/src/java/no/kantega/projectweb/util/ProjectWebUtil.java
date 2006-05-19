package no.kantega.projectweb.util;

import no.kantega.projectweb.model.Participant;
import no.kantega.projectweb.control.participant.dto.ParticipantDto;
import no.kantega.projectweb.user.UserProfileManager;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: May 19, 2006
 * Time: 1:34:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectWebUtil {

     public static List getUserProfileDtos(UserProfileManager manager, List participants) {
        List dtos = new ArrayList();
        for (int i = 0; i < participants.size(); i++) {
            Participant participant = (Participant) participants.get(i);
            ParticipantDto dto = new ParticipantDto();
            dto.setParticipant(participant);
            dto.setProfile(manager.getUserProfile(participant.getUser()));
            dtos.add(dto);
        }
        return dtos;
    }
}
