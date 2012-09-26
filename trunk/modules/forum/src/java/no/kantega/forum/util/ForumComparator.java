package no.kantega.forum.util;

import no.kantega.forum.model.Forum;
import no.kantega.publishing.common.Aksess;

import java.util.Comparator;
import java.util.Locale;
import java.text.Collator;

/**
 * User: Anders Skar, Kantega AS
 * Date: Jun 15, 2007
 * Time: 10:19:01 AM
 */
public class ForumComparator implements Comparator {
    Collator collator = null;


    public ForumComparator() {
        collator = Collator.getInstance(new Locale("no", "NO"));
        collator.setStrength(Collator.PRIMARY);
    }

    public int compare(Object o1, Object o2) {
        Forum f1 = (Forum) o1;
        Forum f2 = (Forum) o2;

        if (f1 != null && f2 != null) {
            return collator.compare(f1.getName(), f2.getName());
        }

        return 0;
    }
}
