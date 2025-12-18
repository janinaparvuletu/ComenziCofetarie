package com.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Order;
import com.example.entity.Status;
import com.example.repository.UserRepository;
import com.example.service.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    // MENIU ORDERS: acces tuturor rolurilor (dar afiseaza butoane diferite)
    @GetMapping
    public String menu() {
        return "orders/menu";
    }

    // CLIENT: pagina de creare
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("order", new Order());
        return "orders/new";
    }

    // CLIENT: submit creare
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/new")
    public String create(@ModelAttribute Order order, Principal principal, RedirectAttributes ra) {
        try {
            // legam comanda de userul logat (email)
            String email = principal.getName();
            var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User logat inexistent in DB!"));

            order.setUser(user);
            orderService.createOrder(order);

            ra.addFlashAttribute("success", "Comandă plasată!");
            return "redirect:/orders/my";
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/orders/new";
        }
    }

    // CLIENT: doar comenzile lui
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/my")
    public String myOrders(Model model, Principal principal) {
        List<Order> orders = orderService.getOrdersByUserEmail(principal.getName());
        model.addAttribute("orders", orders);
        model.addAttribute("mode", "MY");
        return "orders/list";
    }

    // ANGAJAT + MANAGER: toate comenzile
    @PreAuthorize("hasAnyRole('ANGAJAT','MANAGER')")
    @GetMapping("/all")
    public String allOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("mode", "ALL");
        return "orders/list";
    }

    // ANGAJAT + MANAGER: detalii comanda
    @PreAuthorize("hasAnyRole('ANGAJAT','MANAGER')")
    @GetMapping("/{id}")
    public String details(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            Order ord = orderService.getById(id);
            model.addAttribute("order", ord);
            model.addAttribute("statuses", Status.values());
            return "orders/details";
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/orders/all";
        }
    }

    // ANGAJAT + MANAGER: update status
    @PreAuthorize("hasAnyRole('ANGAJAT','MANAGER')")
    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Integer id, @RequestParam Status status, RedirectAttributes ra) {
        try {
            orderService.updateStatus(id, status);
            ra.addFlashAttribute("success", "Status actualizat!");
            return "redirect:/orders/" + id;
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/orders/" + id;
        }
    }

    // ANGAJAT + MANAGER: cautare comenzi dupa user (username+telefon)
    @PreAuthorize("hasAnyRole('ANGAJAT','MANAGER')")
    @GetMapping("/search-user")
    public String searchUserForm() {
        return "orders/search-user";
    }

    @PreAuthorize("hasAnyRole('ANGAJAT','MANAGER')")
    @GetMapping("/by-user")
    public String byUser(@RequestParam String username, @RequestParam String nrTelefon, Model model) {
        List<Order> orders = orderService.findOrdersByUser(username, nrTelefon);
        model.addAttribute("orders", orders);
        model.addAttribute("info", orders.isEmpty() ? "Nu există comenzi pentru acest user." : null);
        model.addAttribute("mode", "ALL");
        return "orders/list";
    }
}
