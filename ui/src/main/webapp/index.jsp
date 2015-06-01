<%@ page contentType="text/html; charset=UTF-8" %> 
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:form action="register">
	<s:textfield name="username" label="username"/>
	<s:password name="password" label="password"/>ß
	<s:submit value="submit"/>
</s:form>