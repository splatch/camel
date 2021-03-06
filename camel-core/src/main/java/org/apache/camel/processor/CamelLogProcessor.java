/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.spi.ExchangeFormatter;
import org.apache.camel.util.CamelLogger;

/**
 * A {@link Processor} which just logs to a {@link CamelLogger} object which can be used
 * as an exception handler instead of using a dead letter queue.
 * <p/>
 * The name <tt>CamelLogger</tt> has been chosen to avoid any name clash with log kits
 * which has a <tt>Logger</tt> class.
 *
 * @version 
 */
public class CamelLogProcessor implements Processor {
    private CamelLogger log;
    private ExchangeFormatter formatter;

    public CamelLogProcessor() {
        this(new CamelLogger(CamelLogProcessor.class.getName()));
    }
    
    public CamelLogProcessor(CamelLogger log) {
        this.formatter = new DefaultExchangeFormatter();
        this.log = log;
    }

    public CamelLogProcessor(CamelLogger log, ExchangeFormatter formatter) {
        this(log);
        this.formatter = formatter;
    }

    @Override
    public String toString() {
        return "Logger[" + log + "]";
    }

    public void process(Exchange exchange) {
        if (log.shouldLog()) {
            log.log(formatter.format(exchange));
        }
    }
    
    public void process(Exchange exchange, Throwable exception) {
        if (log.shouldLog()) {
            log.log(formatter.format(exchange), exception);
        }
    }

    public void process(Exchange exchange, String message) {
        if (log.shouldLog()) {
            log.log(formatter.format(exchange) + message);
        }
    }

    public CamelLogger getLogger() {
        return log;
    }
    
    public void setLogName(String logName) {
        log.setLogName(logName);
    }
    
    public void setLevel(LoggingLevel level) {
        log.setLevel(level);
    }

    public void setMarker(String marker) {
        log.setMarker(marker);
    }

    static class DefaultExchangeFormatter implements ExchangeFormatter {
        public String format(Exchange exchange) {
            return exchange.toString();
        }
    }
}
