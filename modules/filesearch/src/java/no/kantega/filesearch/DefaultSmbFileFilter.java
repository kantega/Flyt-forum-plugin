package no.kantega.filesearch;

import jcifs.smb.SmbFileFilter;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbException;

/**
 * Created by IntelliJ IDEA.
 * User: bjorsnos
 * Date: Oct 9, 2007
 * Time: 12:59:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultSmbFileFilter implements SmbFileFilter {
    private long sizeLimit = 10000000;
    public boolean accept(SmbFile smbFile) throws SmbException {
        if(smbFile.getName().startsWith("~")) {
            return false;
        }
        if(smbFile.getName().startsWith(".")) {
            return false;
        }
        if(sizeLimit > 0 && smbFile.length() > sizeLimit) {
            return false;
        }
        return true;
    }

    public void setSizeLimit(long sizeLimit) {
        this.sizeLimit = sizeLimit*1000;
    }
}
