package no.kantega.projectweb.control.participant.dto;

import no.kantega.projectweb.model.Participant;
import no.kantega.projectweb.user.UserProfile;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: 24.sep.2005
 * Time: 16:36:25
 * To change this template use File | Settings | File Templates.
 */
public class ParticipantDto {
    private Participant participant;
    private UserProfile profile;

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}
