package infbook.infbook.domain.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private int zipcode;
    private String street;
    private String city;
    private String detailedAddress;

    @Builder
    private Address(int zipcode, String street, String city, String detailedAddress) {
        this.zipcode = zipcode;
        this.street = street;
        this.city = city;
        this.detailedAddress = detailedAddress;
    }
}
