package io.yorkecao.jwtauth.repository;

import io.yorkecao.jwtauth.entity.UserRole;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Yorke
 */
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

  List<UserRole> findByUsername(String username);
}
