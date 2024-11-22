/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.oss.driver.internal.core.channel;

import com.datastax.oss.driver.api.core.metadata.Node;
import java.util.Objects;
import net.jcip.annotations.Immutable;

/** Events relating to driver channels. */
@Immutable
public class ChannelRecycleEvent {
  public enum Type {
    RECYCLE_TIME,
    RECYCLE_COUNT
  }

  /* Connection has reached its time limit. */
  public static ChannelRecycleEvent recycleTimeExpired(Node node) {
    return new ChannelRecycleEvent(Type.RECYCLE_TIME, node);
  }

  /* Connection has reached its write count limit. */
  public static ChannelRecycleEvent recycleCountExpired(Node node) {
    return new ChannelRecycleEvent(Type.RECYCLE_COUNT, node);
  }

  public final Type type;
  public final Node node;

  public ChannelRecycleEvent(Type type, Node node) {
    this.type = type;
    this.node = node;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    } else if (other instanceof ChannelRecycleEvent) {
      ChannelRecycleEvent that = (ChannelRecycleEvent) other;
      return this.type == that.type && Objects.equals(this.node, that.node);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, node);
  }

  @Override
  public String toString() {
    return "ChannelRecycleEvent(" + type + ", " + node + ")";
  }
}
