package edu.spirinigor.blogengine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {

    private String name;

    private Double weight;

}
//{
//        "tags":
//        [
//        {"name":"Java", "weight":1},
//        {"name":"Spring", "weight":0.56},
//        {"name":"Hibernate", "weight":0.22},
//        {"name":"Hadoop", "weight":0.17},
//        ]
//        }

