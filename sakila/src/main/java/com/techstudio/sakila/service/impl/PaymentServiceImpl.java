package com.techstudio.sakila.service.impl;

import com.techstudio.sakila.domain.Payment;
import com.techstudio.sakila.dao.mapper.PaymentMapper;
import com.techstudio.sakila.service.IPaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lj
 * @since 2020-03-23
 */
@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements IPaymentService {

}
