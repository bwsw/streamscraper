package com.bwsw.streamscraper.system;

import java.util.UUID;

public class RecurrentVirtualStream
		extends VirtualStream
		implements IEphemeralStream, IRecurrentStream {

	public RecurrentVirtualStream(String name, int weight, int backlog, boolean ephemeral) throws ImpossibleStreamException {
		super(name, weight);
		if (backlog < 0)
			throw new ImpossibleStreamException("Recurrent Vstream backlog should be 0 or greater");
		setBacklogLength(backlog);
		setEphemeral(ephemeral);
		setProperty(P_RECURRENT, "true");
	}

	/**
	 * @param id
	 */
	public RecurrentVirtualStream(UUID id, String name, int weight, int backlog, boolean ephemeral) throws ImpossibleStreamException {
		super(id, name, weight);
		if (backlog < 0)
			throw new ImpossibleStreamException("Recurrent Vstream backlog should be 0 or greater");
		setBacklogLength(backlog);
		setEphemeral(ephemeral);
		setProperty(P_RECURRENT, "true");
	}

	public int getBacklogLength() {
		return Integer.parseInt(getProperty(P_BACKLOG));
	}

	private void setBacklogLength(int backlog) throws ImpossibleStreamException {
		if (backlog < 0)
			throw new ImpossibleStreamException("Recurrent Vstream backlog should be 0 or greater.");
		setProperty(P_BACKLOG, new Integer(backlog).toString());
	}

	@Override
	public boolean isEphemeral() {
		String ep = getProperty(P_EPHEMERAL);
		return ep == "true" ? true : false;
	}

	private void setEphemeral(boolean ephemeral) {
		setProperty(P_EPHEMERAL, "true");
	}

}
