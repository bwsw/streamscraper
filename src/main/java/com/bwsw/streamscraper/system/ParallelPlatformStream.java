/**
 * 
 */
package com.bwsw.streamscraper.system;

import java.util.UUID;

/**
 * @author ivan
 *
 */
public class ParallelPlatformStream extends PlatformStream {

	/**
	 * 
	 */
    public ParallelPlatformStream(int bandwidth) throws ImpossibleStreamException {
        super();
        if (bandwidth < 2)
            throw new ImpossibleStreamException("Minimal bandwidth is 2.");
        setBandwidth(bandwidth);
        setProperty(P_PARALLEL, "true");
    }

	/**
	 * @param id
	 */
    public ParallelPlatformStream(UUID id, int bandwidth) throws ImpossibleStreamException {
        super(id);
        if (bandwidth < 2)
            throw new ImpossibleStreamException("Minimal bandwidth is 2.");
        setProperty(P_PARALLEL, "true");
        setBandwidth(bandwidth);
    }

    public int getBandwidth() throws ImpossibleStreamException {
        return Integer.parseInt(getProperty(P_BANDWIDTH));
    }

    private void setBandwidth(int bw) {
        setProperty(P_BANDWIDTH, new Integer(bw).toString());
    }

}
