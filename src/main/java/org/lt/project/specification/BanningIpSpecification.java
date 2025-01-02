package org.lt.project.specification;

import org.lt.project.model.BanningIp;
import org.springframework.data.jpa.domain.Specification;

public class BanningIpSpecification {
    public static Specification<BanningIp> hasIp(String ip){
        return ((root, query, criteriaBuilder) ->
                ip==null ?
                        criteriaBuilder.conjunction(): criteriaBuilder.like(root.get("ip"),ip));
    }
    public static Specification<BanningIp> hasIpType(BanningIp.BanningIpType ipType){
        return ((root, query, criteriaBuilder) ->
                ipType==null ?
                        criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("ipType"),ipType));
    }
}
