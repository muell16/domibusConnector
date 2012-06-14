<%@ page import="java.util.*,java.io.*,com.oreilly.servlet.*,com.oreilly.servlet.multipart.*,org.holodeck.ebms3.web.EbManager" %> 

<%
   String pmodeDir = request.getParameter("pmodeDir");
   String path = //application.getRealPath("/WEB-INF") + File.separator + 
                //"Submitted_Messages" +
                EbManager.getSubmitFolder() + 
                File.separator + pmodeDir + File.separator;
   
   new File(path).mkdirs();
   MultipartRequest parts = new MultipartRequest(request, path);
%>

