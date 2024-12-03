package com.example.announcement_app.controller;

@Controller
public class MainController {
    @Autowired
    private StudentRepository studentRepo;
    @Autowired
    private TeacherRepository teacherRepo;
    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private AnnouncementRepository announcementRepo;

    @GetMapping("/")
    public String home() {
        return "login"; // Redirect to login page
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        // Check admin
        Optional<Admin> admin = adminRepo.findByUsernameAndPassword(username, password);
        if (admin.isPresent()) {
            session.setAttribute("adminUser", username);
            return "redirect:/adminDashboard";
        }

        // Check student
        Optional<Student> student = studentRepo.findByRollNoAndPassword(username, password);
        if (student.isPresent()) {
            session.setAttribute("user", username);
            return "redirect:/studentDashboard";
        }

        // Check teacher
        Optional<Teacher> teacher = teacherRepo.findByUsernameAndPassword(username, password);
        if (teacher.isPresent()) {
            session.setAttribute("user", username);
            return "redirect:/teacherDashboard";
        }

        model.addAttribute("error", "Invalid credentials!");
        return "login";
    }

    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("announcements", announcementRepo.findAll());
        return "admin_dashboard";
    }

    @PostMapping("/addAnnouncement")
    public String addAnnouncement(
            @RequestParam String category,
            @RequestParam String announcement,
            @RequestParam String teacherName,
            @RequestParam String section,
            @RequestParam LocalDate date) {
        Announcement newAnnouncement = new Announcement();
        newAnnouncement.setCategory(category);
        newAnnouncement.setAnnouncement(announcement);
        newAnnouncement.setTeacherName(teacherName);
        newAnnouncement.setSection(section);
        newAnnouncement.setDate(date);
        announcementRepo.save(newAnnouncement);

        return "redirect:/adminDashboard";
    }
}
