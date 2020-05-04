package web.command.http.post;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import db.exception.AppException;
import model.User;
import service.LoginService;
import utils.HashUtil;
import web.Path;
import web.command.CommandResult;

@RunWith(MockitoJUnitRunner.class)
public class LoginCommandTest {
	
	private static final String RIGHT_LOGIN="Vasya123";
	private static final String RIGHT_PASSWORD="123123";
	private static final String WRONG_PASSWORD="321321";
	
	@InjectMocks
	private LoginCommand command;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private LoginService loginService;
	
	@Mock
	private HttpSession httpSession;
	
	private User user;

	@Before
	public void init() throws NoSuchAlgorithmException {
		command = new LoginCommand(loginService);
		Mockito.when(request.getSession(true)).thenReturn(httpSession);
		user=new User();
		user.setLogin(RIGHT_LOGIN);
		user.setPassword(new String(HashUtil.getSHA(RIGHT_PASSWORD)));
		user.setRoleId(2);
	}

	@Test
	public void executeRightParameters() throws AppException {
		Mockito.when(request.getParameter("loginUser")).thenReturn(RIGHT_LOGIN);
		Mockito.when(request.getParameter("passwordUser")).thenReturn(RIGHT_PASSWORD);
		
		Mockito.when(loginService.findUserByLogin(RIGHT_LOGIN)).thenReturn(user);
		
		CommandResult commandResult = command.execute(request, response);
		Mockito.verify(httpSession, Mockito.times(1)).setAttribute("user", user);
		
		assertEquals(commandResult.getResult(), Path.PAGE_MAIN);
	}
	
	@Test(expected = AppException.class)
	public void executeWrongParameters() throws AppException {
		Mockito.when(request.getParameter("loginUser")).thenReturn(RIGHT_LOGIN);
		Mockito.when(request.getParameter("passwordUser")).thenReturn(WRONG_PASSWORD);
		
		Mockito.when(loginService.findUserByLogin(RIGHT_LOGIN)).thenReturn(user);
		
		command.execute(request, response);
		Mockito.verify(httpSession, Mockito.times(0)).setAttribute("user", user);
	}
}
