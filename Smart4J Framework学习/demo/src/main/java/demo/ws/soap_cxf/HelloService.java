package demo.ws.soap_cxf;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author wangjianchun
 * @create 2018/1/15
 */
@WebService
public interface HelloService {

    String say(@WebParam String name);
}
