package io.yorkecao.jwtauth.repository;

import io.yorkecao.jwtauth.entity.RolePermission;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Yorke
 */
public interface RolePermissionRepository extends CrudRepository<RolePermission, Long> {

  List<RolePermission> findByRole(String role);
}
