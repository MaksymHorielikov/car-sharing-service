package com.example.carsharingservice.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.example.carsharingservice.model.Car;
import com.example.carsharingservice.service.CarService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser(username="admin", roles={"USER","MANAGER"})
    public void testCreateCar() {
        Car car = new Car("Toyota", "Camry", Car.Type.SEDAN, 10, new BigDecimal("50.00"));

        Mockito.when(carService.createCar(car)).thenReturn(new Car(1L, "Toyota", "Camry", Car.Type.SEDAN, 10, new BigDecimal("50.00")));
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body(car)
                .when()
                .post("/cars")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("brand", equalTo("Toyota"))
                .body("model", equalTo("Camry"))
                .body("type", equalTo("SEDAN"))
                .body("inventory", equalTo(10))
                .body("dailyFee", equalTo(50.00F));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER","MANAGER"})
    public void testGetAllCars() {
        List<Car> mockCars = List.of(
                new Car(1L,"Toyota", "Camry", Car.Type.SEDAN, 10, new BigDecimal("50.00")),
                new Car(2L, "Honda", "Accord", Car.Type.SEDAN, 5, new BigDecimal("60.00"))
        );
        Mockito.when(carService.findAll()).thenReturn(mockCars);

        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/cars")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(1))
                .body("[0].brand", equalTo("Toyota"))
                .body("[0].model", equalTo("Camry"))
                .body("[0].type", equalTo("SEDAN"))
                .body("[0].inventory", equalTo(10))
                .body("[0].dailyFee", equalTo(50.00F))
                .body("[1].id", equalTo(2))
                .body("[1].brand", equalTo("Honda"))
                .body("[1].model", equalTo("Accord"))
                .body("[1].type", equalTo("SEDAN"))
                .body("[1].inventory", equalTo(5))
                .body("[1].dailyFee", equalTo(60.00F));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER","MANAGER"})
    public void testGetCarById() {
        Long carId = 1L;
        Car car = new Car("Toyota", "Camry", Car.Type.SEDAN, 10, new BigDecimal("50.00"));
        car.setId(carId);
        Mockito.when(carService.findById(carId)).thenReturn(car);

        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/cars/{id}", carId)
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("brand", equalTo("Toyota"))
                .body("model", equalTo("Camry"))
                .body("type", equalTo("SEDAN"))
                .body("inventory", equalTo(10))
                .body("dailyFee", equalTo(50.00F));
    }

    @Test
    @WithMockUser(username="admin", roles={"USER","MANAGER"})
    public void testDeleteCar() throws Exception {
        Long carId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/cars/{id}", carId))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(carService, Mockito.times(1)).deleteById(carId);
    }
}