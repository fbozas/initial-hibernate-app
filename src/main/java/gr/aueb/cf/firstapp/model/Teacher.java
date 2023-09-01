package gr.aueb.cf.firstapp.model;

import javax.persistence.*;

@Entity
@Table(name="TEACHERS", indexes = {
        @Index(name = "lastname_idx", columnList = "LASTNAME")
})
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="FIRSTNAME", length = 50, nullable = true, unique = true)
    private String firstname;
    @Column(name="LASTNAME", length = 50, nullable = true, unique = true)
    private String lastname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }
}
