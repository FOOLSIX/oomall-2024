package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.customer.dao.bo.Address;
import cn.edu.xmu.oomall.customer.mapper.jpa.AddressPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j

public class AddressDao {

    private final static Logger logger = LoggerFactory.getLogger(AddressDao.class);

    private final RedisUtil redisUtil;

    private final AddressPoMapper addressPoMapper;

    @Autowired
    public AddressDao(RedisUtil redisUtil, AddressPoMapper addressPoMapper) {
        this.redisUtil = redisUtil;
        this.addressPoMapper = addressPoMapper;
    }

    public Address build(AddressPo po){
        Address ret = CloneFactory.copy(new Address(),po);
        this.build(ret);
        return ret;
    }

    public Address build(Address bo){
        bo.setAddressDao(this);
        return bo;
    }

    public Address findById(Long id){
        logger.debug("findById: id = {}", id);
        assert (!Objects.isNull(id)): "id can not be null";
        Optional<AddressPo> po = addressPoMapper.findById(id);
        return po.map(this::build).orElse(null);
    }

    public List<Address> getAlladdress(Long customer_id,Integer page, Integer pageSize){
        List<Address> ret = new ArrayList<>();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "id");
        List<AddressPo> addressPoPage = this.addressPoMapper.findBycustomer_id(customer_id, pageable);
        ret  = addressPoPage.stream().map(po -> CloneFactory.copy(new Address(),po)).collect(Collectors.toList());

        return ret;
    }

    public void SaveAddress(Address address){
        AddressPo po = CloneFactory.copy(new AddressPo(),address);
        this.addressPoMapper.save(po);
    }

    public void DeleteAddress(Long id){
        this.addressPoMapper.deleteById(id);
    }
    public void setdefault(Long id){
        Optional<AddressPo> addressPo = this.addressPoMapper.findById(id);
        AddressPo po = addressPo.orElse(new AddressPo());
        AddressPo ret = this.addressPoMapper.findBycustomer_id(id,Boolean.TRUE);
        if(ret != null){
            ret.setBe_default(Boolean.FALSE);
            this.addressPoMapper.save(ret);
        }
        po.setBe_default(Boolean.TRUE);
        this.addressPoMapper.save(po);
    }


}
