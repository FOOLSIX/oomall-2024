package cn.edu.xmu.oomall.customer.mapper.jpa;

import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface AddressPoMapper extends JpaRepository<AddressPo, Long> {
    List<AddressPo> findBycustomer_id(Long customerId, Pageable pageable);
    AddressPo findBycustomer_id(Long customerId, Boolean be_default);
}
