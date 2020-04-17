package com.study.mapper;

import com.study.model.Role;
import com.study.util.MyMapper;
import java.util.List;

/**
 * @author min
 */
public interface RoleMapper extends MyMapper<Role> {

  /**
   * queryRoleListWithSelected
   * @param id
   * @return
   */
  List<Role> queryRoleListWithSelected(Integer id);
}