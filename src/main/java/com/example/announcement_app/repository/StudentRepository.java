package com.example.announcement_app.repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRollNoAndPassword(String rollNo, String password);
}
