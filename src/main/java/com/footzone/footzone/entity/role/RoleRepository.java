package com.footzone.footzone.entity.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true,
            value = "CREATE OR REPLACE FUNCTION calculate_distance(\n" +
                    "    lat1 float,\n" +
                    "    lon1 float,\n" +
                    "    lat2 float,\n" +
                    "    lon2 float)\n" +
                    "    RETURNS float AS\n" +
                    "$dist$\n" +
                    "DECLARE\n" +
                    "    dist     float = 0;\n" +
                    "    radlat1  float;\n" +
                    "    radlat2  float;\n" +
                    "    theta    float;\n" +
                    "    radtheta float;\n" +
                    "BEGIN\n" +
                    "    IF lat1 = lat2 OR lon1 = lon2\n" +
                    "    THEN\n" +
                    "        RETURN dist;\n" +
                    "    ELSE\n" +
                    "        radlat1 = pi() * lat1 / 180;\n" +
                    "        radlat2 = pi() * lat2 / 180;\n" +
                    "        theta = lon1 - lon2;\n" +
                    "        radtheta = pi() * theta / 180;\n" +
                    "        dist = sin(radlat1) * sin(radlat2) + cos(radlat1) * cos(radlat2) * cos(radtheta);\n" +
                    "\n" +
                    "        IF dist > 1 THEN dist = 1; END IF;\n" +
                    "\n" +
                    "        dist = acos(dist);\n" +
                    "        dist = dist * 180 / pi();\n" +
                    "        dist = dist * 60 * 1.1515;\n" +
                    "        dist = dist * 1.609344 * 1000;\n" +
                    "\n" +
                    "        RETURN dist;\n" +
                    "    END IF;\n" +
                    "END;\n" +
                    "$dist$ LANGUAGE plpgsql")
    void createFunction();
}
