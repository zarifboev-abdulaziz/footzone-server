package com.footzone.footzone.entity.role;

import com.footzone.footzone.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;


    public ApiResponse addRole(RoleDTO roleDTO) {
        if (roleRepository.existsByName(roleDTO.getName()))
            return new ApiResponse("Role with this name already exists", false);

        Role role = new Role(
                roleDTO.getName(),
                roleDTO.getDescription()
        );

        roleRepository.save(role);

        return new ApiResponse("Role has been created successfully !!!", true);
    }

    public ApiResponse editRole(Long id, RoleDTO roleDTO) {

        return new ApiResponse("", true);
    }
}
