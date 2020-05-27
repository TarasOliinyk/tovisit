package com.lits.tovisitapp.unit.sevice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lits.tovisitapp.dto.UserDTO;
import com.lits.tovisitapp.exceptions.user.UserNotFoundException;
import com.lits.tovisitapp.model.User;
import com.lits.tovisitapp.repository.UserRepository;
import com.lits.tovisitapp.service.UserService;
import com.lits.tovisitapp.service.impl.UserServiceImpl;
import com.lits.tovisitapp.utils.ParseDataUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImpTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    public void init() {
        userService = new UserServiceImpl(userRepository, new BCryptPasswordEncoder(), new ModelMapper());
    }

 /*   @Test
    public void createUser_CreateNewUser_ReturnUser() throws IOException {
        init();
        User user = ParseDataUtils.prepareData("unit/service/user/create_positive_user/" +
                "user.json", new TypeReference<>() {});
        UserDTO expected = ParseDataUtils.prepareData("unit/service/user/create_positive_user/" +
                "user.json", new TypeReference<>() {});

        Mockito.when(userRepository.save(eq(user))).thenReturn(user);

        UserDTO actualUserDTO = userService.createUser(user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword());

        Assertions.assertThat(actualUserDTO).isEqualTo(expected);
    }*/

    @Test
    public void findAll_RetrieveAllUsers_ReturnAllUsers() throws IOException {
        // Arrange
        init();

        List<User> users = ParseDataUtils.prepareData("unit/service/user/find_all_positive/" +
                "user.json", new TypeReference<>() {});
        List<UserDTO> expected = ParseDataUtils.prepareData("unit/service/user/find_all_positive/" +
                "user.json", new TypeReference<>() {});

        Mockito.when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserDTO> actual = userService.getAllUsers();

        // Assert
        Assertions.assertThat(actual).isEqualTo(expected);
    }

/*    @Test
    public void findOneById_RetrieveUser_ReturnUser() throws IOException {
        // Arrange
        init();
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
        init();
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

    @Test
    public void deleteUser_RemoveSpecificUser() {
        init();
        userService.deleteUser(1L);
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(Mockito.eq(1L));
    }


    @Test(expected = UserNotFoundException.class)
    public void findOne_RetrieveUser_ThrowException() {
        // Arrange
        init();

        // Act
        userService.getUserById(1L);
    }
}
