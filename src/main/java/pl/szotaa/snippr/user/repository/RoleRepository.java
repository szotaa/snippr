package pl.szotaa.snippr.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.szotaa.snippr.user.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
