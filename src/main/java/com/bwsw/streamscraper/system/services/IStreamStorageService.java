package com.bwsw.streamscraper.system.services;

import com.bwsw.streamscraper.system.exceptions.BadPlatformStreamException;
import com.bwsw.streamscraper.system.exceptions.IncompatibleStreamException;
import com.bwsw.streamscraper.system.models.PlatformStream;
import com.bwsw.streamscraper.system.models.VirtualStream;

/**
 * Created by ivan on 17.12.15.
 */
public interface IStreamStorageService {
    void save(PlatformStream s)
            throws IncompatibleStreamException, BadPlatformStreamException;

    void save(VirtualStream s)
            throws IncompatibleStreamException, BadPlatformStreamException;
}
