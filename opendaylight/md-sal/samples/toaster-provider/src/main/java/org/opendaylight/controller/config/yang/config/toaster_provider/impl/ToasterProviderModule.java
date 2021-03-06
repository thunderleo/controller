/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

/**
* Generated file

* Generated from: yang module name: toaster-provider-impl  yang module local name: toaster-provider-impl
* Generated by: org.opendaylight.controller.config.yangjmxgenerator.plugin.JMXGenerator
* Generated at: Wed Feb 05 11:05:32 CET 2014
*
* Do not modify this file unless it is present under src/main directory
*/
package org.opendaylight.controller.config.yang.config.toaster_provider.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataTreeIdentifier;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker;
import org.opendaylight.controller.sample.toaster.provider.OpendaylightToaster;
import org.opendaylight.yang.gen.v1.http.netconfcentral.org.ns.toaster.rev091120.Toaster;
import org.opendaylight.yang.gen.v1.http.netconfcentral.org.ns.toaster.rev091120.ToasterService;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*
*/
public final class ToasterProviderModule extends
        org.opendaylight.controller.config.yang.config.toaster_provider.impl.AbstractToasterProviderModule {
    private static final Logger log = LoggerFactory.getLogger(ToasterProviderModule.class);

    public ToasterProviderModule(final org.opendaylight.controller.config.api.ModuleIdentifier identifier,
            final org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public ToasterProviderModule(final org.opendaylight.controller.config.api.ModuleIdentifier identifier,
            final org.opendaylight.controller.config.api.DependencyResolver dependencyResolver,
            final ToasterProviderModule oldModule, final java.lang.AutoCloseable oldInstance) {

        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    protected void customValidation() {
        // No need to validate dependencies, since all dependencies have
        // mandatory true flag in yang
        // config-subsystem will perform the validation for dependencies
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        final OpendaylightToaster opendaylightToaster = new OpendaylightToaster();

        // Register to md-sal
        opendaylightToaster.setNotificationProvider(getNotificationServiceDependency());

        DataBroker dataBrokerService = getDataBrokerDependency();
        opendaylightToaster.setDataProvider(dataBrokerService);

        final ListenerRegistration<OpendaylightToaster> dataTreeChangeListenerRegistration = dataBrokerService
                .registerDataTreeChangeListener(new DataTreeIdentifier<Toaster>(LogicalDatastoreType.CONFIGURATION,
                        OpendaylightToaster.TOASTER_IID), opendaylightToaster);

        final BindingAwareBroker.RpcRegistration<ToasterService> rpcRegistration = getRpcRegistryDependency()
                .addRpcImplementation(ToasterService.class, opendaylightToaster);

        // Register runtimeBean for toaster statistics via JMX
        final ToasterProviderRuntimeRegistration runtimeReg = getRootRuntimeBeanRegistratorWrapper().register(
                opendaylightToaster);

        // Wrap toaster as AutoCloseable and close registrations to md-sal at
        // close()
        final class AutoCloseableToaster implements AutoCloseable {

            @Override
            public void close() throws Exception {
                dataTreeChangeListenerRegistration.close();
                rpcRegistration.close();
                runtimeReg.close();
                closeQuietly(opendaylightToaster);
                log.info("Toaster provider (instance {}) torn down.", this);
            }

            private void closeQuietly(final AutoCloseable resource) {
                try {
                    resource.close();
                } catch (final Exception e) {
                    log.debug("Ignoring exception while closing {}", resource, e);
                }
            }
        }

        AutoCloseable ret = new AutoCloseableToaster();
        log.info("Toaster provider (instance {}) initialized.", ret);
        return ret;
    }
}
