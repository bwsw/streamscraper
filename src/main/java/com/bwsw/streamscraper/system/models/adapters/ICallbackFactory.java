package com.bwsw.streamscraper.system.models.adapters;

import com.bwsw.streamscraper.system.models.BasicHandler;

/**
 * Created by ivan on 21.12.15.
 */
public interface ICallbackFactory {
    void generate(BasicHandler h);
}
