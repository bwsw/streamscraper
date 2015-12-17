/**
 * 
 */
package com.bwsw.streamscraper.system;

import java.util.UUID;

/**
 * @author ivan
 *
 */
public class ParallelVirtualStream extends VirtualStream implements IEphemeralStream, IParallelStream {

    public ParallelVirtualStream(String name, int weight, int bandwidth, boolean ephemeral) throws ImpossibleStreamException {
        super(name, weight);
        if (bandwidth < 2)
            throw new ImpossibleStreamException("Parallel Vstream bandwidth should be greater than 1");
        setBandwidth(bandwidth);
        setEphemeral(ephemeral);
    }

	/**
	 * @param id
	 */
    public ParallelVirtualStream(UUID id, String name, int weight, int bandwidth, boolean ephemeral) throws ImpossibleStreamException {
        super(id, name, weight);
        if (bandwidth < 2)
            throw new ImpossibleStreamException("Parallel Vstream bandwidth should be greater than 1");
        setBandwidth(bandwidth);
        setEphemeral(ephemeral);
    }

    public int getBandwidth() {
        return Integer.parseInt(getProperty(P_BANDWIDTH));
    }

    private void setBandwidth(int bandwidth) throws ImpossibleStreamException {
        if (bandwidth < 2)
            throw new ImpossibleStreamException("Parallel Virtual Stream Minimal bandwidth is 2.");
        setProperty(P_BANDWIDTH, new Integer(bandwidth).toString());
    }


    @Override
	public boolean getEphemeral() {
		// TODO Auto-generated method stub
		String ep = getProperty(P_EPHEMERAL);
		return ep == "true" ? true : false;
	}

    private void setEphemeral(boolean ephemeral) {
        // TODO Auto-generated method stub
        setProperty(P_EPHEMERAL, "true");
    }


}
