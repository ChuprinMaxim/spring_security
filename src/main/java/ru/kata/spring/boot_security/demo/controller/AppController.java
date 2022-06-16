package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

@Controller
public class AppController {

    private final UserServiceImp userService;
    @Autowired
    public AppController(UserServiceImp userService) {
        this.userService = userService;
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/admin")
    public String showAllUsers() {
        return "redirect:/admin/all-users";
    }

    @GetMapping("/admin/all-users")
    public String showAllUsers(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "all-users";
    }

    @GetMapping("/admin/addNewForm")
    public String addNewForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("setRoles", userService.getRoles());

        return "info-user";
    }

    @PostMapping("/admin/saveUser")
    public String saveUser(@ModelAttribute("user") User user) {
        user.setPassword((bCryptPasswordEncoder.encode(user.getPassword())));
        userService.saveUser(user);

        return "redirect:/admin/all-users";
    }

    @GetMapping("/admin/updateUser")
    public String updateUser(@RequestParam("updateUser") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("setRoles", userService.getRoles());

        return "info-user";
    }

    @GetMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam("deleteUser") long id) {
        userService.deleteUser(id);

        return "redirect:/admin/all-users";
    }

    @GetMapping("/user/{name}")
    public String userPage(@PathVariable("name") String name, Model model) {
        model.addAttribute("user", userService.loadUserByUsername(name));
        return "user";
    }
}
