package com.liuyao.demo.ws.server;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.rmi.Remote;

@WebService
public interface WbSvc extends Remote {

    String findAreaCount(@WebParam(name = "startDate") String startDate,
                         @WebParam(name = "endDate") String endDate,
                         @WebParam(name = "areaCode") String areaCode);

}
