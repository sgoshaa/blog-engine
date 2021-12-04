package edu.spirinigor.blogengine.model;

import lombok.Data;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;

@Entity
@Table(name = "global_settings")
@Data
public class GlobalSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String code;

    @Column
    private String name;

    @Column
    private String value;
}
