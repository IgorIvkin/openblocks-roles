package ru.openblocks.roles.persistence.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.openblocks.roles.persistence.entity.UserRoleEntity;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    @Query(nativeQuery = true,
            value = "select exists(select 1 from user_role ur " +
                    "where ur.user_name = :userName and ur.role_code = :roleCode)")
    boolean userHasRole(@Param("userName") String userName,
                        @Param("roleCode") String roleCode);

    List<UserRoleEntity> findByUserName(@NotNull @Size(max = 255) String userName);

    List<UserRoleEntity> findByGrantBy(@NotNull @Size(max = 255) String grantBy);
}
