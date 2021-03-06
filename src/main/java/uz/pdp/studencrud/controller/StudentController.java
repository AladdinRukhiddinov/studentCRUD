package uz.pdp.studencrud.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.studencrud.entity.Address;
import uz.pdp.studencrud.entity.Group;
import uz.pdp.studencrud.entity.Student;
import uz.pdp.studencrud.entity.Subject;
import uz.pdp.studencrud.payload.StudentDto;
import uz.pdp.studencrud.repository.AddressRepository;
import uz.pdp.studencrud.repository.GroupRepository;
import uz.pdp.studencrud.repository.StudentRepository;
import uz.pdp.studencrud.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final AddressRepository addressRepository;
    private final SubjectRepository subjectRepository;

    //1. VAZIRLIK
    @GetMapping("/forMinistry")
    public Page<Student> getStudentListForMinistry(@RequestParam int page) {

        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAll(pageable);
    }

    //2. UNIVERSITY
    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentListForUniversity(@PathVariable Integer universityId,
                                                     @RequestParam int page) {

        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_Faculty_UniversityId(universityId, pageable);
    }

    //3. FACULTY DEKANAT
    @GetMapping("forFaculty/{facultyId}")
    public Page<Student> getStudentListForFaculty(@PathVariable(value = "facultyId") Integer facultyId,
                                                  @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
    }

    //4. GROUP OWNER
    @GetMapping("forGroup/{groupId}")
    public Page<Student> getStudentListForGroup(@PathVariable(value = "groupId") Integer groupId,
                                                @RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return studentRepository.findAllByGroupId(groupId, pageable);
    }

    @PostMapping("/add")
    public String add(@RequestBody StudentDto dto) {

        List<Subject> subjects = new ArrayList<>();

        for (Integer subject : dto.getSubjectsId()) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subject);
            optionalSubject.ifPresent(subjects::add);
        }
        if (subjects.isEmpty()) return "Subject not found!!";

        Optional<Address> optionalAddress = addressRepository.findById(dto.getAddressId());
        if (optionalAddress.isEmpty()) return "Address not found!!";

        Optional<Group> optionalGroup = groupRepository.findById(dto.getGroupId());
        if (optionalGroup.isEmpty()) return "Group not found!!";

        Student student = new Student();
        student.setAddress(optionalAddress.get());
        student.setGroup(optionalGroup.get());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setSubjects(subjects);
        try {
            studentRepository.save(student);
            return "Successfully added!!";
        } catch (Exception e) {
            return "Error!! No added.";
        }
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable(value = "id") Integer id, StudentDto dto) {

        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) return "Student not found!!";

        List<Subject> subjects = new ArrayList<>();

        for (Integer subject : dto.getSubjectsId()) {
            Optional<Subject> optionalSubject = subjectRepository.findById(subject);
            optionalSubject.ifPresent(subjects::add);
        }
        if (subjects.isEmpty()) return "Subject not found!!";

        Optional<Address> optionalAddress = addressRepository.findById(dto.getAddressId());
        if (optionalAddress.isEmpty()) return "Address not found!!";

        Optional<Group> optionalGroup = groupRepository.findById(dto.getGroupId());
        if (optionalGroup.isEmpty()) return "Group not found!!";

        Student student = optionalStudent.get();
        student.setAddress(optionalAddress.get());
        student.setGroup(optionalGroup.get());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setSubjects(subjects);
        try {
            studentRepository.save(student);
            return "Successfully edited!!!";
        } catch (Exception e) {
            return "Error!! No edited.";
        }
    }

    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable(value = "id") Integer id) {
        try {
            studentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
