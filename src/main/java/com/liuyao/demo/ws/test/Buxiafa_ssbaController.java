package com.liuyao.demo.ws.test;

import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.SOAPHeaderElement;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 类名称：Buxiafa 创建人：sl 创建时间：2017-08-07
 */
@Controller
@RequestMapping(value = "/buxiafa_ssba")
public class Buxiafa_ssbaController {

	// 调用webservices接口(带用户和密码验证),返回值为字符串;
	@SuppressWarnings("unchecked")
	public static String callAsmxWebService(String serviceUrl,
											String serviceNamespace, String methodName, String headerName,
											Map<String, String> params, Map<String, String> user)
			throws ServiceException, RemoteException, MalformedURLException {

		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(serviceUrl));
		call.setOperationName(new QName(serviceNamespace, methodName));

		ArrayList<String> paramValues = new ArrayList<String>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			call.addParameter(new QName(serviceNamespace, entry.getKey()),
					XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			paramValues.add(entry.getValue());
		}

		call.setReturnType(XMLType.XSD_STRING);
		call.setUseSOAPAction(true);
		call.setSOAPActionURI(serviceNamespace + methodName);

		SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement(
				serviceNamespace, headerName);
		try {
			for (Map.Entry<String, String> entry : user.entrySet()) {
				soapHeaderElement.addChildElement(entry.getKey()).setValue(
						entry.getValue());
			}
			call.addHeader(soapHeaderElement);
		} catch (SOAPException e) {
			e.printStackTrace();
		}

		Object[] paramvalue = paramValues
				.toArray(new String[paramValues.size()]);

		return (String) call.invoke(paramvalue);
	}

	// 调用webservices接口(带用户和密码验证),返回字节码;
	public byte[] callAsmxWebService64(String serviceUrl,
			String serviceNamespace, String methodName, String headerName,
			Map<String, String> params, Map<String, String> user)
			throws ServiceException, RemoteException, MalformedURLException {

		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(serviceUrl));
		call.setOperationName(new QName(serviceNamespace, methodName));

		ArrayList<String> paramValues = new ArrayList<String>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			call.addParameter(new QName(serviceNamespace, entry.getKey()),
					XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			paramValues.add(entry.getValue());
		}
		call.setReturnType(XMLType.XSD_BASE64);
		call.setUseSOAPAction(true);
		call.setSOAPActionURI(serviceNamespace + methodName);

		SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement(
				serviceNamespace, headerName);
		soapHeaderElement.setNamespaceURI(serviceNamespace);
		try {
			for (Map.Entry<String, String> entry : user.entrySet()) {
				soapHeaderElement.addChildElement(entry.getKey()).setValue(
						entry.getValue());
			}
			call.addHeader(soapHeaderElement);
		} catch (SOAPException e) {
			e.printStackTrace();
		}

		Object[] paramvalue = paramValues
				.toArray(new String[paramValues.size()]);

		return (byte[]) call.invoke(paramvalue);
	}

	public static void main(String[] args) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("end", "2013-12-27");
		params.put("begin", "2013-12-28");

		Map<String, String> user = new HashMap<String, String>();
		user.put("InvokeUserName", "sd_jk");
		user.put("InvokeUserPwd", "sd100100");
		String result = null;
		try {
			result = callAsmxWebService(
					"http://10.255.4.100/YSBAService/YsbaService.asmx",
					"http://tempuri.org/", "GetJsxmByTime", "SecuritySoapHeader",
					params, user);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		System.out.println(result);

	}

}
