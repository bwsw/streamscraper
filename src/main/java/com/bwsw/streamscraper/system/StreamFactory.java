package com.bwsw.streamscraper.system;

import java.util.UUID;

public class StreamFactory {
	public static PlatformStream getParallelPlatformStream(UUID id, int bandwidth) throws ImpossibleStreamException {
		return new ParallelPlatformStream(id, bandwidth);
	}
	
	public static PlatformStream getRecurrentPlatformStream(UUID id, int backlog) {
		return (new PlatformStream(id))
				.setProperty(PlatformStream.P_BACKLOG, Integer.toString(backlog))
				.setProperty(PlatformStream.P_RECURRENT, "true");
	} 

	public static void setSolid(VirtualStream vs, boolean ephemeral) {
		if (ephemeral)
			vs.setProperty(PlatformStream.P_EPHEMERAL, "true");
		else
			vs.setProperty(PlatformStream.P_SOLID, "true");
	}
	
	public static VirtualStream getRecurrentVirtualStream(UUID id, int backlog, boolean ephemeral) {
		VirtualStream vs = new VirtualStream(getRecurrentPlatformStream(id, backlog));
		setSolid(vs, ephemeral);
		return vs;
	}

	public static VirtualStream getParallelVirtualStream(UUID id, int bandwidth, boolean ephemeral) throws ImpossibleStreamException {
		VirtualStream vs = new VirtualStream(getParallelPlatformStream(id, bandwidth));
		setSolid(vs, ephemeral);
		return vs;
	} 

}
