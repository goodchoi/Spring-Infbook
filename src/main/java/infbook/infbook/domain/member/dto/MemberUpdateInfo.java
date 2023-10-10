package infbook.infbook.domain.member.dto;

import infbook.infbook.domain.model.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static infbook.infbook.global.util.RegExUtil.*;

@Data
public class MemberUpdateInfo {
    @NotBlank(message = "{}")
    @Pattern(regexp = NAME_PATTERN)
    @Length(max = 17)
    private String name;


    @NotNull
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate birthDate;

    @NotEmpty
    private String zipcode;
    private String street;
    private String city;
    private String detailedAddress;

    @NotBlank
    @Pattern(regexp = TELEPHONE_PATTERN)
    private String telephone;

    @Builder
    public MemberUpdateInfo(String name, LocalDate birthDate, String zipcode, String street, String city, String detailedAddress, String telephone) {
        this.name = name;
        this.birthDate = birthDate;
        this.zipcode = zipcode;
        this.street = street;
        this.city = city;
        this.detailedAddress = detailedAddress;
        this.telephone = telephone;
    }

    public Address getAddress() {
        return Address.builder()
                .zipcode(this.zipcode)
                .city(this.city)
                .detailedAddress(this.detailedAddress)
                .street(this.street)
                .build();
    }
}
