package com.lits.tovisitapp.integration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lits.tovisitapp.controller.UserController;
import com.lits.tovisitapp.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Mock
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void createUser_CreateNewUser_ReturnCreatedUser() throws Exception {
    }

    @Test
    public void findOneById_RetrieveUser_ReturnUser() throws IOException {

    }

    @Test
    public void findOneByName_RetrieveUser_ReturnUser() throws IOException {

    }

    @Test
    public void findAll_RetrieveUsers_ReturnUsers() throws IOException {

    }

    @Test
    public void deleteUser_RemoveSpecificUser() throws Exception {

    }

}
