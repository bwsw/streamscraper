package com.bwsw.streamscraper.system;

import java.util.UUID;

public class VirtualStream extends PlatformStream {
	
	public static void addPolicyCheck(VirtualStream vs, PlatformStream ps) throws IncompatibleStreamException {
		String excMessage = "";
		while(true) {
			if (vs.getProperty(PlatformStream.P_BANDWIDTH) != ps.getProperty(PlatformStream.P_BANDWIDTH)) {
				excMessage = "Vstream & Pstream P_BANDWIDTH mismatch.";
				break;
			}
			
			if (vs.getProperty(PlatformStream.P_BACKLOG) != ps.getProperty(PlatformStream.P_BACKLOG)) {
				excMessage = "Vstream & Pstream P_BACKLOG mismatch.";
				break;				
			}
			
			if (null != ps.getProperty(PlatformStream.P_SOLID)) {
					excMessage = "Pstream P_SOLID is set. Shouldn't be set.";
					break;				
			}
					
			if (vs.getProperty(PlatformStream.P_EPHEMERAL) != ps.getProperty(PlatformStream.P_EPHEMERAL)) {
				excMessage = "Vstream & Pstream P_EPHEMERAL mismatch.";
				break;								
			}		

			if (vs.getProperty(P_PARALLEL) != ps.getProperty(P_PARALLEL)) {
				excMessage = "Vstream & Pstream P_PARALLEL mismatch.";
				break;								
			}
					
			if (vs.getProperty(P_RECURRENT) != ps.getProperty(P_RECURRENT)) {
				excMessage = "Vstream & Pstream P_RECURRENT mismatch.";
				break;								
			}		

			if (null == vs.getProperty(P_RECURRENT) && null == vs.getProperty(P_PARALLEL)) {
				excMessage = "Vstream P_RECURRENT & P_PARALLEL are both unset.";
				break;								
			}		

			if (null != vs.getProperty(P_RECURRENT) && null != vs.getProperty(P_PARALLEL)) {
				excMessage = "Vstream P_RECURRENT & P_PARALLEL are both set.";
				break;								
			}		
			
			if (null != vs.getProperty(P_RECURRENT) && "true" != vs.getProperty(P_RECURRENT)) {
				excMessage = "Vstream P_RECURRENT is set, but neq true.";
				break;								
			}		

			if (null != vs.getProperty(P_PARALLEL) && "true" != vs.getProperty(P_PARALLEL)) {
				excMessage = "Vstream P_PARALLEL is set, but neq true.";
				break;								
			}		
			
			if (null != ps.getProperty(P_RECURRENT) && "true" != ps.getProperty(P_RECURRENT)) {
				excMessage = "Pstream P_RECURRENT is set, but neq true.";
				break;								
			}		

			if (null != ps.getProperty(P_PARALLEL) && "true" != ps.getProperty(P_PARALLEL)) {
				excMessage = "Pstream P_PARALLEL is set, but neq true.";
				break;								
			}		

			if (vs.getHasChanges()) {
				excMessage = "Vstream has unsaved changes. Will not save. Save it first.";
				break;												
			}

			if (ps.getHasChanges()) {
				excMessage = "Pstream has unsaved changes. Will not save. Save it first.";
				break;												
			}
			
			return;
		}
		
		throw new IncompatibleStreamException("Vstream `" + vs.getID() + 
													"' is incompatible with " +
													" pstream `" + ps.getID() + "': " + excMessage);
	}

	public static void updatePolicy(VirtualStream vs, PlatformStream ps) throws ImpossibleStreamException {
		String solid = vs.getProperty(PlatformStream.P_SOLID);
		if (null != solid)
			ps.setProperty(PlatformStream.P_SOLID, solid);

		String ephemeral = vs.getProperty(PlatformStream.P_EPHEMERAL);
		if (null != ephemeral)
			ps.setProperty(PlatformStream.P_EPHEMERAL, ephemeral);

		ps.setHasChanges(true);
		
		if (null != ephemeral && null != solid)
			throw new ImpossibleStreamException("Both P_EPHEMERAL && P_SOLID are set. Nonsense! Use addPolicyCheck first.");
	}
	
	
	public VirtualStream() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public VirtualStream(UUID id) {
		super(id);
	}
	
	public VirtualStream(PlatformStream pcopy) {
		super(pcopy.getID());
		this.vstreams = pcopy.vstreams;
		this.properties = pcopy.properties;
	}

}
