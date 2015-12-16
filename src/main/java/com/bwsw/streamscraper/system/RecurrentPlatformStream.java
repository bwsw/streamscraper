/**
 * 
 */
package com.bwsw.streamscraper.system;

import java.util.UUID;

/**
 * @author ivan
 *
 */
public class RecurrentPlatformStream extends PlatformStream {

	/**
	 * 
	 */
	public RecurrentPlatformStream() {
        super();
    }

	/**
	 * @param id
	 */
	public RecurrentPlatformStream(UUID id) {
		super(id);
	}

    public int getBacklog() throws ImpossibleStreamException {
        return Integer.parseInt(getProperty(P_BANDWIDTH));
    }


}
