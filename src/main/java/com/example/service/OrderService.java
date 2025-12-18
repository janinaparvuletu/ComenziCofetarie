package com.example.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.Order;
import com.example.entity.Status;
import com.example.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        if (order == null)
            throw new IllegalArgumentException("Comanda e null!");

        if (order.getDeadline() == null || order.getDeadline().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Data deadline este invalidă!");

        if (order.getBlat() == null || order.getCrema() == null || order.getGlazura() == null)
            throw new IllegalArgumentException("Ai lăsat câmpuri necompletate!");

        if (order.getMesajTort() != null && order.getMesajTort().length() > 50)
            throw new IllegalArgumentException("Mesajul este prea lung (max 50 caractere)!");

        order.setStatus(Status.PROCESARE);
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUserEmail(String email) {
        return orderRepository.findByUserEmail(email);
    }

    public List<Order> findOrdersByUser(String username, String nrTelefon) {
        return orderRepository.findByUserUsernameAndUserNrTelefon(username, nrTelefon);
    }

    public Order updateStatus(Integer id, Status status) {
        if (status == null)
            throw new IllegalArgumentException("Status null!");

        Order ord = orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Nu există o comandă cu acest ID!"));

        if (ord.getStatus().equals(Status.LIVRATA) || ord.getStatus().equals(Status.ANULATA))
            throw new IllegalArgumentException("Nu poți modifica statusul la LIVRATA/ANULATA!");

        ord.setStatus(status);
        return orderRepository.save(ord);
    }

    public Order getById(Integer id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Nu există o comandă cu acest ID!"));
    }
}
