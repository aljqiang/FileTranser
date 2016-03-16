<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title></title>
</head>
<body>

</body>
<form name="form1" action="ReciveOrderServlet" method="post">
    <input type="text" id="orderType" name="orderType"/>
    <input type="text" id="dataFilePath" name="dataFilePath"/>
    <input type="submit" value="启动发送任务"/>
</form>
<br/>
<form name="form2" action="TestReciveOrderServlet" method="post">
    <input type="text" id="orderTypeTest" name="orderTypeTest"/>
    <input type="text" id="dataFilePathTest" name="dataFilePathTest"/>
    <input type="submit" value="启动发送任务(11个)"/>
</form>
<br/>
<form name="form3" action="ROSS" method="post">
    <input type="text" id="date" name="date"/>
    <input type="text" id="time" name="time"/>
    <input type="submit" value="发起调度任务"/>
</form>
</html>