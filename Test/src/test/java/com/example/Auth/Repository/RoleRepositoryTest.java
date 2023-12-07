package com.example.Auth.Repository;

import com.example.Common.Entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

//@AllArgsConstructor
//@ExtendWith(MockitoExtension.class)
//@DataJpaTest
public class RoleRepositoryTest {

//    @Autowired
//    private EntityManager entityManager;
//
//    private final RoleRepository roleRepository;
//
//    @Test
//    void testFindRolesByEmail() {
//        // Tạo danh sách vai trò giả định
//        List<Role> mockRoles = new ArrayList<>();
//        Role role1 = new Role();
//        role1.setId(1L);
//        role1.setRoleName("ROLE_USER");
//        mockRoles.add(role1);
//
//        // Giả đinh khi gọi các phương thức Entity manager
//        TypedQuery<Role> query = Mockito.mock(TypedQuery.class);
//        when(entityManager.createQuery(anyString(), eq(Role.class))).thenReturn(query);
//        when(query.setParameter(eq("accountId"), anyLong())).thenReturn(query);
//        when(query.getResultList()).thenReturn(mockRoles);
//
//        // Gọi phương thức cần kiểm tra
//        List<Role> roles = roleRepository.findRolesByEmail(1L);
//    }
}
