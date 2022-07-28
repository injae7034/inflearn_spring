package com.example.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(value = {"password", "ssn"})
@JsonFilter("UserInfo")
public class User {
    private Integer id;

    @Size(min = 2, message = "이름은 2글자 이상 입력해주세요.")
    private String name;
    @Past(message = "날짜는 과거 날짜만 입력해주세요.")
    private Date joinDate;

    //    @JsonIgnore
    private String password;
    //    @JsonIgnore
    private String ssn;
}
