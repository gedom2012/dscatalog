package com.gedom.dsctalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gedom.dsctalog.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
