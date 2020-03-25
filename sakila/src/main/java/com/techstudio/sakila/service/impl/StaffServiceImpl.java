package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.Staff;
import com.techstudio.sakila.dao.mapper.StaffMapper;
import com.techstudio.sakila.service.IStaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements IStaffService {

}
