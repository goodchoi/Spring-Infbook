package infbook.infbook.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Column(nullable = false, columnDefinition = "varchar(6)")
    private String zipcode;
    @Column(nullable = false, columnDefinition = "varchar(15)")
    private String street;
    @Column( columnDefinition = "varchar(100)")
    private String city;
    @Column( columnDefinition = "varchar(100)")
    private String detailedAddress;

    @Builder
    private Address(String zipcode, String street, String city, String detailedAddress) {
        this.zipcode = zipcode;
        this.street = street;
        this.city = city;
        this.detailedAddress = detailedAddress;
    }
}
