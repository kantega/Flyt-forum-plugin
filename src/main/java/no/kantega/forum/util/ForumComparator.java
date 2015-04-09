package no.kantega.forum.util;

import no.kantega.forum.model.Forum;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * User: Anders Skar, Kantega AS
 * Date: Jun 15, 2007
 * Time: 10:19:01 AM
 */
public class ForumComparator implements Comparator<Forum> {
    Collator collator = null;


    public ForumComparator() {
        collator = Collator.getInstance(new Locale("no", "NO"));
        collator.setStrength(Collator.PRIMARY);
    }

    public int compare(Forum f1, Forum f2) {
        if (f1 != null && f2 != null) {
            return collator.compare(f1.getName(), f2.getName());
        }

        return 0;
    }
}
