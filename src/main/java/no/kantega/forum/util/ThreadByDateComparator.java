package no.kantega.forum.util;

import no.kantega.forum.model.ForumThread;

import java.util.Comparator;

public class ThreadByDateComparator implements Comparator<ForumThread> {


    public int compare(ForumThread p1, ForumThread p2) {
        if (p1 != null && p2 != null) {
            return p2.getCreatedDate().compareTo(p1.getCreatedDate());
        }

        return 0;
    }
}