<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:property value="username"/>

<s:iterator value="years" var="year">
<s:property value="#year"/>
</s:iterator>

<br>

<a href="http://test.dev.com:8080/struts-study/example/HelloWorld"> cross domian link</a>