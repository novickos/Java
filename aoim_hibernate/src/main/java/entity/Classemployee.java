package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@NamedQuery(name = "ClassEmployee.findByGroupName", query = "select c from Classemployee c where c.groupName = ?1")
@NamedQuery(name = "ClassEmployee.findAll", query = "select c from Classemployee c")
@Table(name = "classemployee")
public class Classemployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "groupName", nullable = false)
    private String groupName;

    @ColumnDefault("10")
    @Column(name = "maxNumber")
    private Integer maxNumber;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }

    @Override
    public String toString() {
        return "Classemployee{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", maxNumber=" + maxNumber +
                '}';
    }
}