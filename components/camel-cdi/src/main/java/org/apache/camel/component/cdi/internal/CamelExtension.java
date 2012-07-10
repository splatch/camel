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
package org.apache.camel.component.cdi.internal;

import java.lang.reflect.Method;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;

/**
 * Set of camel specific hooks for CDI.
 */
public class CamelExtension implements Extension {

    /**
     * Process camel context aware bean definitions.
     * 
     * @param process Annotated type.
     * @throws Exception In case of exceptions.
     */
    protected void contextAwareness(@Observes ProcessAnnotatedType<CamelContextAware> process) throws Exception {
        AnnotatedType<CamelContextAware> annotatedType = process.getAnnotatedType();
        Class<CamelContextAware> javaClass = annotatedType.getJavaClass();
        if (CamelContextAware.class.isAssignableFrom(javaClass)) {
            Method method = javaClass.getMethod("setCamelContext", CamelContext.class);
            AnnotatedTypeBuilder<CamelContextAware> builder = new AnnotatedTypeBuilder<CamelContextAware>()
                .readFromType(javaClass)
                .addToMethod(method, new InjectLiteral());
            process.setAnnotatedType(builder.create());
        }
    }

//    protected void multiply(@Observes AfterBeanDiscovery abd, BeanManager manager) {
//        abd.addBean(new CamelContextBean(manager.createInjectionTarget(manager.createAnnotatedType(CdiCamelContext.class))));
//    }

}
