package com.bwsw.streamscraper.system;

import java.util.HashMap;
import java.util.UUID;

public class VirtualStream extends PlatformStream {
	
	PlatformStream assignedStream;
	// defines weight of virtual vstream.
	// used to balance vstreams across pstreams.
	int weight;

	public VirtualStream() {
		// TODO Auto-generated constructor stub
		super();
		assignedStream = null;
	}

	public VirtualStream(UUID id) {
		super(id);
		assignedStream = null;
		weight = 1;
		setHasChanges(true);
	}


	public VirtualStream(PlatformStream pcopy) {
		super(pcopy.getID());
		assignedStream = null;
		vstreams = pcopy.vstreams;
		properties = pcopy.properties;
		weight = 1;
		setHasChanges(true);
	}

	public static void addPolicyCheck(VirtualStream vs, PlatformStream ps) throws IncompatibleStreamException {
		String excMessage;
		while (true) {
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

			if (null != vs.getAssignedStream()) {
				excMessage = "Vstream has assigned Pstream (already bound).";
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

	public VirtualStream setWeight(int new_weight) throws ImpossibleStreamException {
		if (weight <= 0)
			throw new ImpossibleStreamException("Vstream weight is set to negative value. Should be greater than 0.");
		weight = new_weight;
		return this;
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
}
