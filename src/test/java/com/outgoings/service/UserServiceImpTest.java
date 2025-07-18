package com.outgoings.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.outgoings.entity.Account;
import com.outgoings.repository.AccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

  @Mock private AccountRepository accountRepository;

  @InjectMocks private UserServiceImp userService;

  @Test
  void findByTokenShouldReturnUser() {

    Account account = new Account();
    account.setToken("ABC123");
    account.setUsername("testName");
    account.setPassword("pass");

    when(accountRepository.findByToken("ABC123")).thenReturn(Optional.of(account));

    Optional<User> result = userService.findByToken("ABC123");
    assertTrue(result.isPresent());

    User user = result.get();
    assertEquals(account.getUsername(), user.getUsername());
    verify(accountRepository).findByToken("ABC123");
    verifyNoMoreInteractions(accountRepository);
  }

  @Test
  void findByTokenShouldReturnEmptyWhenNotFound() {
    when(accountRepository.findByToken("ABC123")).thenReturn(Optional.empty());

    Optional result = userService.findByToken("ABC123");

    assertEquals(result, Optional.empty());
  }

  @Test
  void loginShouldReturnTokenAndSaveAccount() {

    Account account = new Account();
    account.setUsername("User");
    account.setPassword("Password");

    InOrder order = inOrder(accountRepository);
    when(accountRepository.login(anyString(), anyString())).thenReturn(Optional.of(account));

    String token = userService.login("User", "password");
    assertNotEquals(token, "");

    ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
    verify(accountRepository).save(captor.capture());

    Account captured = captor.getValue();
    assertEquals(token, captured.getToken());
    order.verify(accountRepository).login(anyString(), anyString());
    order.verify(accountRepository).save(any(Account.class));
    order.verifyNoMoreInteractions();
  }

  @Test
  void login_shouldReturnEmptyStringWhenUserNotFound() {
    when(accountRepository.login("bad", "creds")).thenReturn(Optional.empty());

    String token = userService.login("bad", "creds");

    assertEquals("", token);
    verify(accountRepository, never()).save(any());
  }

  @Test
  void findByIdShouldThrowException() {
    when(accountRepository.findById(1)).thenThrow(new RuntimeException("DB Error"));

    assertThrows(RuntimeException.class, () -> userService.findById(1));
  }
}
