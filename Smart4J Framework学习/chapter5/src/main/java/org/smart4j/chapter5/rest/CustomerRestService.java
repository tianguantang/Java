package org.smart4j.chapter5.rest;

import org.smart4j.chapter5.model.Customer;
import org.smart4j.chapter5.service.CustomerService;
import org.smart4j.framework.annotation.Service;
import org.smart4j.plugin.rest.Rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * 客户 REST 服务
 * @author wangjianchun
 * @create 2018/1/26
 */
@Rest
@Service
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerRestService {

//    @Inject
    private CustomerService customerService = new CustomerService();

    @GET
    @Path("/customer/{id}")
    public Customer getCustomer(@PathParam("id") long customerId){
        return customerService.getCustomer(customerId);
    }

}
