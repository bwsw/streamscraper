/**
 * 
 */
package com.bwsw.streamscraper.system;

import java.util.UUID;

/**
 * @author ivan
 *
 */
public class ParallelVirtualStream extends VirtualStream implements EphemeralStream {
	
	public static int DEF_BANDWIDTH = 10;
	public static int DEF_BANDWIDTH_MAX = 100000;

	/**
	 * 
	 */

	private int getBandWidth() {
		String pbw = StreamScraperMgmtService.getProperty("streamscraper.streams.parallel.default_bandwidth");
		Integer ibw;
		if(null == pbw)
			ibw = new Integer(DEF_BANDWIDTH);
		else
		{
			Integer res = Integer.parseInt(pbw);
			if(res < 0 || res > DEF_BANDWIDTH_MAX)
				ibw = new Integer(DEF_BANDWIDTH);
			else
				ibw = res;
		}
		return ibw;
	}
	
	public ParallelVirtualStream() {
		// TODO Auto-generated constructor stub
		super();
		VirtualStream vs = StreamFactory.getParallelVirtualStream(UUID.randomUUID(), getBandWidth() , false);
		this.setID(vs.getID());
		this.properties = vs.properties;
	}

	/**
	 * @param id
	 */
	public ParallelVirtualStream(UUID id) {
		super(id);
		VirtualStream vs = StreamFactory.getParallelVirtualStream(id, getBandWidth() , false);
		this.properties = vs.properties;
		// TODO Auto-generated constructor stub
	}
	
	public int getBacklog() {
		return Integer.parseInt(getProperty(P_BACKLOG));
	}
	
	public ParallelVirtualStream setBacklog(int backlog) {
		setProperty(P_BACKLOG, (new Integer(backlog)).toString());
		return this;
	}
	
	@Override
	public boolean getEphemeral() {
		// TODO Auto-generated method stub
		String ep = getProperty(P_EPHEMERAL);
		return ep == "true" ? true : false;
	}
	
	@Override
	public void setEphemeral(boolean ephemeral) throws ImpossibleStreamException {
		// TODO Auto-generated method stub
		if(null != getAssignedStream()) {
			throw new ImpossibleStreamException("Unable to change stream type because it is " + 
												"bound to pstream `" + getAssignedStream().toString() + "'");
		}
	}
}
