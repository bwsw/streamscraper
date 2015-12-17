package com.bwsw.streamscraper.system;

import java.util.HashMap;
import java.util.UUID;

public class VirtualStream extends PlatformStream {
	
	PlatformStream assignedStream;
	// defines weight of virtual vstream.
	// used to balance vstreams across pstreams.
	int weight;
	String name;

	public VirtualStream(String name, int weight) {
		// TODO Auto-generated constructor stub
		super();
		assignedStream = null;
		this.weight = weight;
		this.name = name;
		setHasChanges(true);
	}

	public VirtualStream(UUID id, String name, int weight) {
		super(id);
		this.assignedStream = null;
		this.weight = weight;
		this.name = name;
		setHasChanges(true);
	}


	public static void addPolicyCheck(VirtualStream vs, PlatformStream ps) throws IncompatibleStreamException {
		try {
			if (vs.getProperty(PlatformStream.P_BANDWIDTH) != ps.getProperty(PlatformStream.P_BANDWIDTH))
				throw new Exception("Vstream & Pstream P_BANDWIDTH mismatch.");


			if (vs.getProperty(PlatformStream.P_BACKLOG) != ps.getProperty(PlatformStream.P_BACKLOG))
				throw new Exception("Vstream & Pstream P_BACKLOG mismatch.");

			if (null != ps.getProperty(PlatformStream.P_SOLID))
				throw new Exception("Pstream P_SOLID is set. Shouldn't be set.");


			if (vs.getProperty(PlatformStream.P_EPHEMERAL) != ps.getProperty(PlatformStream.P_EPHEMERAL))
				throw new Exception("Vstream & Pstream P_EPHEMERAL mismatch.");

			if (vs.getProperty(P_PARALLEL) != ps.getProperty(P_PARALLEL))
				throw new Exception("Vstream & Pstream P_PARALLEL mismatch.");

			if (vs.getProperty(P_RECURRENT) != ps.getProperty(P_RECURRENT))
				throw new Exception("Vstream & Pstream P_RECURRENT mismatch.");


			if (null == vs.getProperty(P_RECURRENT) && null == vs.getProperty(P_PARALLEL))
				throw new Exception("Vstream P_RECURRENT & P_PARALLEL are both unset.");

			if (null != vs.getProperty(P_RECURRENT) && null != vs.getProperty(P_PARALLEL))
				throw new Exception("Vstream P_RECURRENT & P_PARALLEL are both set.");

			if (null != vs.getProperty(P_RECURRENT) && "true" != vs.getProperty(P_RECURRENT))
				throw new Exception("Vstream P_RECURRENT is set, but neq true.");

			if (null != vs.getProperty(P_PARALLEL) && "true" != vs.getProperty(P_PARALLEL))
				throw new Exception("Vstream P_PARALLEL is set, but neq true.");

			if (null != ps.getProperty(P_RECURRENT) && "true" != ps.getProperty(P_RECURRENT))
				throw new Exception("Pstream P_RECURRENT is set, but neq true.");

			if (null != ps.getProperty(P_PARALLEL) && "true" != ps.getProperty(P_PARALLEL))
				throw new Exception("Pstream P_PARALLEL is set, but neq true.");

			if (null != vs.getAssignedStream())
				throw new Exception("Vstream has assigned Pstream (already bound).");

			if (vs.getHasChanges())
				throw new Exception("Vstream has unsaved changes. Will not save. Save it first.");

			if (ps.getHasChanges())
				throw new Exception("Pstream has unsaved changes. Will not save. Save it first.");

		} catch (Exception e) {

			throw new IncompatibleStreamException("Vstream `" + vs.getID() +
					"' is incompatible with " +
					" pstream `" + ps.getID() + "': " + e.getMessage());
		}
	}

	public static void updatePolicy(VirtualStream vs, PlatformStream ps) throws ImpossibleStreamException {
		String solid = vs.getProperty(PlatformStream.P_SOLID);
		if (null != solid)
			ps.setProperty(PlatformStream.P_SOLID, solid);

		String ephemeral = vs.getProperty(PlatformStream.P_EPHEMERAL);
		if (null != ephemeral)
			ps.setProperty(PlatformStream.P_EPHEMERAL, ephemeral);

		ps.setHasChanges(true);
		vs.assignStream(ps);

		if (null != ephemeral && null != solid)
			throw new ImpossibleStreamException("Both P_EPHEMERAL && P_SOLID are set. Nonsense! Use addPolicyCheck first.");
	}
	
	protected void assignStream(PlatformStream ps) {
		assignedStream = ps;
		setHasChanges(true);
	}

	public int getWeight() {
		return weight;
	}

	private void setWeight(int weight) throws ImpossibleStreamException {
		if (weight <= 0)
			throw new ImpossibleStreamException("Vstream weight is set to negative value. Should be greater than 0.");
		this.weight = weight;
	}

	public PlatformStream getAssignedStream() {
		return assignedStream;
	}
	
	@Override
	public PlatformStream addVirtualStream(VirtualStream s) throws ImpossibleStreamException {
		throw new ImpossibleStreamException("Vstreams unable to bind to another Vstreams.");
	}
	
	@Override
	public VirtualStream getVirtualStream(UUID id) {
		return null;
	}
	
	@Override
	public HashMap<UUID, VirtualStream> getVirtualStreams() {
		return null;
	}

	public String getName() {
		return name;
	}
}
