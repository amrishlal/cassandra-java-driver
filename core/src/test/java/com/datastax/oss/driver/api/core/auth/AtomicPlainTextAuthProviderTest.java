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
package com.datastax.oss.driver.api.core.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.datastax.oss.driver.api.core.auth.PlainTextAuthProviderBase.Credentials;
import com.datastax.oss.driver.api.core.metadata.EndPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Strict.class)
public class AtomicPlainTextAuthProviderTest {
  @Mock private EndPoint endpoint;

  @Test
  public void should_return_correct_credentials() {
    AtomicPlainTextAuthProvider provider = new AtomicPlainTextAuthProvider("user", "pass");

    Credentials credentials = provider.getCredentials(endpoint, "irrelevant");
    assertThat(credentials.getUsername()).isEqualTo("user".toCharArray());
    assertThat(credentials.getPassword()).isEqualTo("pass".toCharArray());
  }

  @Test
  public void should_change_username() {
    AtomicPlainTextAuthProvider provider = new AtomicPlainTextAuthProvider("user", "pass");

    provider.setCredentials("user2", "pass2");
    Credentials credentials = provider.getCredentials(endpoint, "irrelevant");
    assertThat(credentials.getUsername()).isEqualTo("user2".toCharArray());
    assertThat(credentials.getPassword()).isEqualTo("pass2".toCharArray());
  }
}
