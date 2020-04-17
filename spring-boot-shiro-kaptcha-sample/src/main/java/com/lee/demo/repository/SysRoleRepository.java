package com.lee.demo.repository;

import com.lee.demo.entity.SysRole;
import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by archerlj on 2017/7/4.
 */
public interface SysRoleRepository extends CrudRepository<SysRole, Long> {

  List<SysRole> findAllByDescriptionIn(Collection<String> roleNames);
}
