/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.cluster.datastore.shardstrategy;

import com.google.common.base.Preconditions;
import org.opendaylight.yangtools.yang.data.api.InstanceIdentifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShardStrategyFactory {
  private static final Map<String, ShardStrategy> moduleNameToStrategyMap = new ConcurrentHashMap();

  private static final String UNKNOWN_MODULE_NAME = "unknown";

  public static ShardStrategy getStrategy(InstanceIdentifier path){
    Preconditions.checkNotNull(path, "path should not be null");

    String moduleName = getModuleName(path);
    ShardStrategy shardStrategy = moduleNameToStrategyMap.get(moduleName);
    if(shardStrategy == null){
      return new DefaultShardStrategy();
    }

    return shardStrategy;
  }


  private static String getModuleName(InstanceIdentifier path){
    return UNKNOWN_MODULE_NAME;
  }

  /**
   * This is to be used in the future to register a custom shard strategy
   *
   * @param moduleName
   * @param shardStrategy
   */
  public static void registerShardStrategy(String moduleName, ShardStrategy shardStrategy){
    throw new UnsupportedOperationException("registering a custom shard strategy not supported yet");
  }
}