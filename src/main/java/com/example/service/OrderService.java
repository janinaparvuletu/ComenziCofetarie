package com.example.service;


import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.entity.Order;
import com.example.entity.Status;
import com.example.repository.OrderRepository;
import java.util.List;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	 
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	 
	public  Order createOrder( Order order) {
		if(order==null)
			throw new IllegalArgumentException("comanda are campuri cu valori null, mai inceaarca!");
		if (order.getDeadline() == null || order.getDeadline().isBefore(LocalDate.now())) 
		    throw new IllegalArgumentException("data asta e nerealista!");
		if(order.getBlat()==null ||order.getCrema()==null||order.getGlazura()==null)
			throw new IllegalArgumentException("ati lasat camp necompletat!");
		if(order.getMesajTort() != null && order.getMesajTort().length()>50)
			throw new IllegalArgumentException("lungimea textului e prea mare");
		order.setStatus(Status.PROCESARE);
		
		return orderRepository.save(order);
	}
	
	public Order cancelOrder(Integer id)
	{
		Order ord = orderRepository.findById(id)
			    .orElseThrow(() -> new IllegalArgumentException("nu exista o comanda cu acest Id"));

		if(!ord.getStatus().equals(Status.PROCESARE))
			throw new IllegalArgumentException("Stadiul comenzii e prea avansat sa mai poataa fi anulata!");
		
		ord.setStatus(Status.ANULATA);
		return orderRepository.save(ord);
	}
	
	public List<Order> findOrdersByUser(String username, String nrTelefon)
	{
		
		return orderRepository.findByUserUsernameAndUserNrTelefon(username,nrTelefon);
	}
	
	public Order updateStatus(Integer id, Status status)
	{
		if(status==null)
			throw new IllegalArgumentException("nu poti lasa null la status!");
		Order ord = orderRepository.findById(id)
			    .orElseThrow(() -> new IllegalArgumentException("nu exista o comanda cu acest Id"));
		if(ord.getStatus().equals(Status.LIVRATA)||ord.getStatus().equals(Status.ANULATA))
				throw new IllegalArgumentException("Nu poti modifica statusul comenzilor deja LIVRATE sau ANULATE!");
		
		ord.setStatus(status);
		return orderRepository.save(ord);
	}
	
	public List<Order> getAllOrders()
	{
		return orderRepository.findAll();
	}
	
}
