package com.bwsw.streamscraper.system.models.adapters;

import org.slf4j.Logger;

/**
 * Created by ivan on 21.12.15.
 */
public interface ICallbackFactory {
    void generate(Object runtime, Object entity, Logger logger);
}
