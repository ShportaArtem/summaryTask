package service;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import db.DBManager;
import db.exception.AppException;
import db.exception.DBException;
import db.repository.UserRep;
import model.User;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {
	
	private static final String LOGIN ="Vasya123";
	private static final String WRONG_LOGIN ="Vasya123";
	private static final Integer ID =2;
	private static final Integer WRONG_ID =22131231;
	
	@InjectMocks
	private LoginService loginService;
	
	@Mock
	private DBManager dbManager;
	
	@Mock
	private UserRep userRep;
	
	@Mock
	private Connection con;
	
	private User user;
	
	@Before
	public void init() {
		loginService = new LoginService(dbManager, userRep);
		user = new User();
		user.setId(ID);
		user.setLogin(LOGIN);
	}
	
	@Test
	public void findUserByLoginRightParameters() throws AppException, SQLException {
		Mockito.when(dbManager.getConnection()).thenReturn(con);
		Mockito.when(userRep.findUserByLogin(con, LOGIN)).thenReturn(user);
		User userByService = loginService.findUserByLogin(LOGIN);
		assertEquals(userByService, user);
	}
	
	@Test(expected = DBException.class)
	public void findUserByLoginWrongParameters() throws AppException, SQLException {
		Mockito.when(dbManager.getConnection()).thenReturn(con);
		Mockito.when(userRep.findUserByLogin(con, WRONG_LOGIN)).thenThrow(new SQLException());
		loginService.findUserByLogin(WRONG_LOGIN);
	}
	
	@Test
	public void findUserByIdRightParameters() throws AppException, SQLException {
		Mockito.when(dbManager.getConnection()).thenReturn(con);
		Mockito.when(userRep.findUserById(con, ID)).thenReturn(user);
		User userByService = loginService.findUserById(ID);
		assertEquals(userByService, user);
	}
	
	@Test(expected = DBException.class)
	public void findUserByIdWrongParameters() throws AppException, SQLException {
		Mockito.when(dbManager.getConnection()).thenReturn(con);
		Mockito.when(userRep.findUserById(con, WRONG_ID)).thenThrow(new SQLException());
		loginService.findUserById(WRONG_ID);
	}
}
