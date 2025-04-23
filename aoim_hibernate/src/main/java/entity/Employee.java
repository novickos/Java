package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@NamedQuery(name="Employee.byClass", query = "select e from Employee e where e.classEmployee.groupName = ?1")
@NamedQuery(name = "Employee.findAll", query = "select e from Employee e")
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "surname", nullable = false, length = 100)
    private String surname;

    @Lob
    @Column(name = "cond", nullable = false)
    private String cond;

    @Column(name = "birthYear", nullable = false)
    private Integer birthYear;

    @Column(name = "salary", nullable = false, precision = 10)
    private Double salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "classEmployee_id")
    private Classemployee classEmployee;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCond() {
        return cond;
    }

    public void setCond(String cond) {
        this.cond = cond;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Classemployee getClassEmployee() {
        return classEmployee;
    }

    public void setClassEmployee(Classemployee classEmployee) {
        this.classEmployee = classEmployee;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", cond='" + cond + '\'' +
                ", birthYear=" + birthYear +
                ", salary=" + salary +
                ", classEmployee=" + classEmployee +
                '}';
    }
}