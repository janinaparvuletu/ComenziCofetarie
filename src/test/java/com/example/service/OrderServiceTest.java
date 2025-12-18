package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.entity.Blat;
import com.example.entity.Crema;
import com.example.entity.Glazura;
import com.example.entity.Order;
import com.example.entity.Rol;
import com.example.entity.Status;
import com.example.entity.User;
import com.example.repository.OrderRepository;

@ExtendWith(MockitoExtension.class) // activeaza Mockito in JUnit 5
public class OrderServiceTest {
	
	@Mock//creeaza o copie falsa a repository ului
    private OrderRepository orderRepository; // repository fals (nu atinge DB)

	@InjectMocks
    private OrderService orderService; // service real, dar cu repository mock-uititory-ului
	
	@Test
	void createOrder_deadlineInPast_throwsIllegalArgumentException() {
	    // GIVEN: construiesc un user si o comanda cu deadline in trecut
	    User user = new User("ana", "ana@test.com", "1234567890123", "0712345678", Rol.CLIENT);

	    Order order = new Order(
	        user,
	        LocalDate.now().minusDays(1),  // deadline invalid
	        Blat.ALB,
	        Crema.CIOCOLATA,
	        Glazura.MARTIPAN,
	        null
	    );

	    // WHEN + THEN: verific ca metoda arunca IllegalArgumentException
	    assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(order));
	}
	
	@Test
	void createOrder_validOrder_setsStatusProcesare_andSaves() {
	    // GIVEN: comanda valida
	    User user = new User("ana", "ana@test.com", "1234567890123", "0712345678", Rol.CLIENT);

	    Order order = new Order(
	        user,
	        LocalDate.now().plusDays(2),
	        Blat.ALB,
	        Crema.CIOCOLATA,
	        Glazura.FRISCA,
	        null
	    );

	    // Mock behavior: cand se cheama save(any(Order)), returneaza obiectul primit
	    // thenAnswer -> "da inapoi argumentul", util ca sa nu inventam id-uri etc.
	    when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

	    // WHEN: apelez service-ul
	    Order saved = orderService.createOrder(order);

	    // THEN: statusul trebuie sa fie PROCESARE
	    assertEquals(Status.PROCESARE, saved.getStatus());

	    // Si verific ca repository.save a fost apelat exact o data
	    verify(orderRepository, times(1)).save(any(Order.class));
	}

}
