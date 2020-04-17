package com.study.mapper;

import com.study.model.Resources;
import com.study.util.MyMapper;
import java.util.List;
import java.util.Map;

/**
 * @author min
 */
public interface ResourcesMapper extends MyMapper<Resources> {

  /**
   * queryAll
   * @return
   */
  public List<Resources> queryAll();

  /**
   * loadUserResources
   * @param map
   * @return
   */
  public List<Resources> loadUserResources(Map<String, Object> map);

  /**
   * queryResourcesListWithSelected
   * @param rid
   * @return
   */
  public List<Resources> queryResourcesListWithSelected(Integer rid);
}