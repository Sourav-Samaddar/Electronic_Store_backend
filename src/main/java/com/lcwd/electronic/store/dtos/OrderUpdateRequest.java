package com.lcwd.electronic.store.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUpdateRequest {
	@NotBlank(message = "orderStatus is required")
	private String orderStatus;
	
	@NotBlank(message = "paymentStatus is required")
    private String paymentStatus;

	@NotBlank(message = "billingName is required")
    private String billingName;

	@NotBlank(message = "billingPhone is required")
    private String billingPhone;

	@NotBlank(message = "billingAddress is required")
    private String billingAddress;

	@JsonFormat(pattern = "dd/MM/yyyy")
    private Date deliveredDate;
}
