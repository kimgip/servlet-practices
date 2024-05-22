<%@page import="guestbook.dao.GuestbookDao"%>
<%@page import="guestbook.vo.GuestbookVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	
	Long no = new Long(request.getParameter("no"));
	String password = request.getParameter("password");
	
	new GuestbookDao().deleteByNoAndPassword(no, password);
	
	response.sendRedirect("/guestbook1");
%>