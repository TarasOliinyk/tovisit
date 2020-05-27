package com.lits.tovisitapp.integration.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lits.tovisitapp.dto.UserDTO;
import com.lits.tovisitapp.model.User;
import com.lits.tovisitapp.repository.UserRepository;
import com.lits.tovisitapp.service.UserService;
import com.lits.tovisitapp.utils.ParseDataUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void createUser_CreateNewUser_ReturnUser() throws IOException {

    }

  /*  @Test
    public void findOneById_RetrieveUser_ReturnUser() throws IOException {
        // Arrange
        User user = ParseDataUtils.prepareData("unit/service/user/find_one_positive/" +
                "user.json", new TypeReference<>() {
        });
        UserDTO expected = ParseDataUtils.prepareData("unit/service/user/find_one_positive/" +
                "user.json", new TypeReference<>() {
        });
        Mockito.when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));

        // Act
        UserDTO result = userService.getUserById(1L);

        // Assert
        Assertions.assertThat(result).isEqualTo(expected);
    }*/

    @Test
    public void findOneByName_RetrieveUser_ReturnUser() throws IOException {
        // Arrange
        User user = ParseDataUtils.prepareData("unit/service/user/find_one_positive/" +
                "user.json", new TypeReference<>() {
        });
        UserDTO expected = ParseDataUtils.prepareData("unit/service/user/find_one_positive/" +
                "user.json", new TypeReference<>() {
        });
        Mockito.when(userRepository.findOneByUsername(eq("Hulk"))).thenReturn(Optional.of(user));

        // Act
        UserDTO result = userService.getUserByUsername("Hulk");

        // Assert
        Assertions.assertThat(result).isEqualTo(expected);
    }

/*    @Test
    public void findAll_RetrieveUsers_ReturnUsers() throws IOException {
        // Arrange
        List<User> users = ParseDataUtils.prepareData("integration/service/user/find_all_positive/" +
                "user.json", new TypeReference<>() {});
        List<UserDTO> expected = ParseDataUtils.prepareData("integration/service/user/find_all_positive/" +
                "user.json", new TypeReference<>() {});
        Mockito.when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserDTO> actual = userService.getAllUsers();

        // Assert
        Assertions.assertThat(actual).isEqualTo(expected);
    }*/

    @Test
    public void deleteUser_RemoveSpecificUser() {
        userService.deleteUser(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(Mockito.eq(1L));
    }
}
