/*
 * Copyright 2016 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stormpath.shiro.samples.springboot.common.dao;

import com.stormpath.shiro.samples.springboot.common.model.Stormtrooper;
import java.util.Collection;

/**
 * Example CRUD DAO interface.
 * @author min
 */
public interface StormtrooperDao {

  /**
   * listStormtroopers
   * @return
   */
  Collection<Stormtrooper> listStormtroopers();

  /**
   * getStormtrooper
   * @param id
   * @return
   */
  Stormtrooper getStormtrooper(String id);

  /**
   * addStormtrooper
   * @param stormtrooper
   * @return
   */
  Stormtrooper addStormtrooper(Stormtrooper stormtrooper);

  /**
   * updateStormtrooper
   * @param id
   * @param stormtrooper
   * @return
   */
  Stormtrooper updateStormtrooper(String id, Stormtrooper stormtrooper);

  /**
   * deleteStormtrooper
   * @param id
   * @return
   */
  boolean deleteStormtrooper(String id);
}
