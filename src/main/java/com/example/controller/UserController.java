package com.example.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Rol;
import com.example.entity.User;
import com.example.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // MANAGER + ANGAJAT: pot vedea formularul
    @PreAuthorize("hasAnyRole('MANAGER','ANGAJAT')")
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }

    // MANAGER + ANGAJAT: pot crea user
    // DAR: ANGAJAT -> doar CLIENT (fortat si in backend)
    @PreAuthorize("hasAnyRole('MANAGER','ANGAJAT')")
    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, RedirectAttributes ra) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isEmployee = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ANGAJAT"));

            if (isEmployee) {
                user.setRol(Rol.CLIENT);
            }

            userService.addUser(user);
            ra.addFlashAttribute("success", "User adăugat cu succes!");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/users/add";
    }

    // doar MANAGER
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/employees")
    public String employees(Model model) {
        List<User> employees = userService.findEmployees();
        model.addAttribute("employees", employees);
        return "employees";
    }

    // doar MANAGER
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String username, Model model) {
        if (username != null && !username.isBlank()) {
            model.addAttribute("results", userService.findUser(username));
        }
        return "user-search";
    }

    // doar MANAGER
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/delete")
    public String delete(@RequestParam String cnp, RedirectAttributes ra) {
        try {
            userService.deleteUserByCnp(cnp);
            ra.addFlashAttribute("success", "User șters!");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/users/employees";
    }

    // doar MANAGER
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/password")
    public String showChangePasswordForm() {
        return "change-password";
    }

    // doar MANAGER - schimbare parola dupa EMAIL
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/password")
    public String changePassword(@RequestParam String email,
                                 @RequestParam String password,
                                 RedirectAttributes ra) {
        try {
            userService.changePasswordByEmail(email, password);
            ra.addFlashAttribute("success", "Parola a fost schimbată!");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/users/password";
    }
}
