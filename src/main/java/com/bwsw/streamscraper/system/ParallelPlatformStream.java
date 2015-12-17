/**
 * 
 */
package com.bwsw.streamscraper.system;

import java.util.UUID;

/**
 * @author ivan
 *
 */
public class ParallelPlatformStream extends PlatformStream implements IParallelStream {

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

    public int getBandwidth() {
        return Integer.parseInt(getProperty(P_BANDWIDTH));
    }

    private void setBandwidth(int bandwidth) throws ImpossibleStreamException {
        if (bandwidth < 2)
            throw new ImpossibleStreamException("Minimal bandwidth is 2.");
        setProperty(P_BANDWIDTH, new Integer(bandwidth).toString());
    }

    @Override
    public PlatformStream addVirtualStream(VirtualStream s)
            throws IncompatibleStreamException, DuplicateVstreamException, ImpossibleStreamException {
        if (!s.getClass().equals(ParallelVirtualStream.class))
            throw new IncompatibleStreamException("Pstream & Vstream should be both parallel.");
        super.addVirtualStream(s);
        return this;
    }

}
