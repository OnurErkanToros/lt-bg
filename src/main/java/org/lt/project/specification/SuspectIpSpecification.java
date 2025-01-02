package org.lt.project.specification;

import org.lt.project.model.BanningIp;
import org.lt.project.model.SuspectIP;
import org.springframework.data.jpa.domain.Specification;

public class SuspectIpSpecification {
    public static Specification<SuspectIP> hasIp(String ip){
        return ((root, query, criteriaBuilder) ->
                ip==null ?
                        criteriaBuilder.conjunction(): criteriaBuilder.like(root.get("ipAddress"),ip));
    }
    public static Specification<SuspectIP> hasStatus(SuspectIP.IpStatus status){
        return ((root, query, criteriaBuilder) ->
                status==null ?
                        criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("status"),status));
    }
    public static Specification<SuspectIP> hasHost(String host){
        return ((root, query, criteriaBuilder) ->
                host==null ?
                        criteriaBuilder.conjunction(): criteriaBuilder.equal(root.get("host"),host));
    }

}
