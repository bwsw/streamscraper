/**
 * 
 */
package com.bwsw.streamscraper.system;

import java.util.UUID;

/**
 * @author ivan
 *
 */
public class RecurrentPlatformStream extends PlatformStream {

	/**
	 * 
	 */
    public RecurrentPlatformStream(int backlog) throws ImpossibleStreamException {
        super();
        if (backlog < 0)
            throw new ImpossibleStreamException("Backlog should be 0 or greater.");
        setBacklogLength(backlog);
        setProperty(P_RECURRENT, "true");
    }

	/**
	 * @param id
	 */
    public RecurrentPlatformStream(UUID id, int backlog) throws ImpossibleStreamException {
        super(id);
        if (backlog < 0)
            throw new ImpossibleStreamException("Backlog should be 0 or greater.");
        setBacklogLength(backlog);
        setProperty(P_RECURRENT, "true");
    }

    public int getBacklogLength() {
        return Integer.parseInt(getProperty(P_BACKLOG));
    }

    private void setBacklogLength(int backlog) throws ImpossibleStreamException {
        if (backlog < 0)
            throw new ImpossibleStreamException("Backlog should be 0 or greater.");
        setProperty(P_BACKLOG, new Integer(backlog).toString());
    }

}
