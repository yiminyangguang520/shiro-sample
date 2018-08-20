/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class ApplicationTests {

  @Autowired
  WebApplicationContext context;

  @Autowired
  AbstractShiroFilter shiroFilter;

  MockMvc mockMvc;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .addFilters(shiroFilter)
        .alwaysDo(print())
        .build();
  }

  @Test
  public void apiNeedsAuthentication() throws Exception {
    mockMvc
        .perform(get("/api/health"))
        .andExpect(status().is3xxRedirection());
  }

  @Test
  public void loginSuccess() throws Exception {
    MockHttpServletRequestBuilder loginRequest = post("/login")
        .param("username", "admin")
        .param("password", "adminjdbc");
    mockMvc
        .perform(loginRequest)
        .andExpect(status().is3xxRedirection());
  }

  @Test
  public void invalidUsernamePassword() throws Exception {
    MockHttpServletRequestBuilder loginRequest = post("/login")
        .param("username", "admin")
        .param("password", "INVALID");
    mockMvc
        .perform(loginRequest)
        .andExpect(status().isOk());
  }
}
