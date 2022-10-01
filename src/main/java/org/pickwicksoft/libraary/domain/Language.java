package org.pickwicksoft.libraary.domain;

import javax.persistence.*;

@Entity
@Table(name = "language")
public class Language {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "flag", nullable = false)
    private String flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Language{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
