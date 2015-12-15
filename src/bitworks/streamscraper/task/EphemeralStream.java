package bitworks.streamscraper.task;

import com.bwsw.streamscraper.system.ImpossibleStreamException;

public interface EphemeralStream {
	public boolean getEphemeral();
	public void setEphemeral(boolean ephemeral) throws ImpossibleStreamException;
}
