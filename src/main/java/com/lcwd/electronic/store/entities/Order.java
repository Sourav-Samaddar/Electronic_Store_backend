package com.lcwd.electronic.store.entities;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
public class Order {
	
	@Id
	private String orderId;
	
	//Pending || Dispatched || Delivered
	private String orderStatus;
	
	//Paid || Not Paid
	private String paymentStatus;
	
	private double billingAmount;
	
	@Column(length = 1000)
	private String billingAddress;
	
	private String billingPhone;
	
	private String billingName;
	
	private Date orderdDate;
	
	private Date deliveredDate;
	
	private String paymentId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<OrderItem> orderItems = new LinkedHashSet<>();
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;
}
