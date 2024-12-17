package com.gmail.sergeymars8888.crm_system.specification;

import com.gmail.sergeymars8888.crm_system.model.Client;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecification {


    public static Specification<Client> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Client> hasIndustry(String industry) {
        return (root, query, criteriaBuilder) -> {
            if (industry == null || industry.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("industry")), "%" + industry.toLowerCase() + "%");
        };
    }

    public static Specification<Client> hasAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if (address == null || address.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + address.toLowerCase() + "%");
        };
    }
}
